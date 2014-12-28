/*
                    ,i::,
               :;;;;;;;
              ;:,,::;.
            1ft1;::;1tL
              t1;::;1,
               :;::;               _____        __  ___              __
          fCLff ;:: tfLLC         / ___/      /  |/  /____ _ _____ / /_
         CLft11 :,, i1tffLi       \__ \ ____ / /|_/ // __ `// ___// __ \
         1t1i   .;;   .1tf       ___/ //___// /  / // /_/ // /__ / / / /
       CLt1i    :,:    .1tfL.   /____/     /_/  /_/ \__,_/ \___//_/ /_/
       Lft1,:;:       , 1tfL:
       ;it1i ,,,:::;;;::1tti      s_mach.codetools
         .t1i .,::;;; ;1tt        Copyright (c) 2014 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.codetools.impl

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import s_mach.codetools.{Result, BlackboxHelper}
import s_mach.codetools.reflectPrint._

class ReflectPrintMacroBuilderImpl(val c: blackbox.Context) extends
  ReflectPrintMacroBuilder with
  BlackboxHelper {
  import c.universe._

  def build[A: c.WeakTypeTag]() : c.Expr[ReflectPrint[A]] = {
    val aType = c.weakTypeOf[A]
    val isTuple = isTupleType(aType)

    val aTypeName = {
      if(isTuple) {
        ""
      } else {
        aType.typeSymbol.name.toString
      }
    }

    val printableTypeConstructor = typeOf[ReflectPrint[_]].typeConstructor

    val productType : ProductType = abortIfFailure(ProductType(aType))

    val lcs = ('a' to 'z').map(_.toString)

    val oomField =
      productType.oomField.zip(lcs).map { case (field,lc) =>
        (TermName(s"_${lc}ReflectPrint"), field)
      }

    val fields = oomField.map { case (rpFieldName,field) =>
      val innerReflectPrintType = appliedType(printableTypeConstructor, List(field._type))
      val innerReflectPrint =
        abortIfFailure(inferImplicit(innerReflectPrintType))

      q"val $rpFieldName = $innerReflectPrint"
    }

    def mkBody(aTypeName: String,showSymbols:Boolean) : c.Tree = {
      def appendFieldName(fieldName:String) : c.Tree = {
        if(showSymbols) {
            q"""
if(fmt.namedParams) {
  builder.append($fieldName)
  builder.append(fmt.space)
  builder.append('=')
  builder.append(fmt.space)
}
"""
        } else {
          q""
        }
      }

      if (oomField.size == 1) {
        val (rpFieldName, field) = oomField.head
        q"""
val v = ${aType.typeSymbol.companion}.unapply(a).get
val builder = new StringBuilder
${ if(aTypeName.nonEmpty) {
          q"""
builder.append($aTypeName)
builder.append('(')
           """
        } else {
          q""
        }
}
val innerFmt = fmt.copy(indent = fmt.indent + fmt.indentString)
builder.append(innerFmt.newLine)
${appendFieldName(field.fieldName)}
builder.append($rpFieldName.printApply(v)(innerFmt))
builder.append(fmt.newLine)
${if(aTypeName.nonEmpty) q"builder.append(')')" else q""}
builder.result()
        """
      } else {
        val values =
          oomField
            .zipWithIndex
            .map { case ((rpFieldName, field), i) =>
            q"""
{
  val innerFmt = fmt.copy(indent = fmt.indent + fmt.indentString)
  builder.append(innerFmt.newLine)
  ${appendFieldName(field.fieldName)}
  builder.append($rpFieldName.printApply(tuple.${TermName("_" + (i + 1))})(innerFmt))
  ${if (i != oomField.indices.last) q"builder.append(',')" else q""}
}
              """
          }

        q"""{
val tuple = ${aType.typeSymbol.companion}.unapply(a).get
val builder = new StringBuilder
${if(aTypeName.nonEmpty) q"builder.append($aTypeName)" else q""}
builder.append('(')
..$values
builder.append(fmt.newLine)
builder.append(')')
builder.result()
        }"""
      }
  }

    val result = c.Expr[ReflectPrint[A]] {
      q"""
new ReflectPrint[$aType] {
  ..$fields
  def printApply(a: $aType)(implicit fmt:ReflectPrintFormat) = ${mkBody(aTypeName,isTuple == false)}
  def printUnapply(a: $aType)(implicit fmt:ReflectPrintFormat) = ${mkBody("",false)}
}
      """
    }
//    println(result)
    result
  }
}

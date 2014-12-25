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
package s_mach.codetools.reflectPrint.impl

import s_mach.codetools.ReflectToolbox
import s_mach.codetools.reflectPrint.{ReflectPrintMacroBuilder, ReflectPrint}

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

class ReflectPrintMacroBuilderImpl(val c: blackbox.Context) extends
  ReflectPrintMacroBuilder with
  ReflectToolbox {
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

    val structType = calcStructType(aType)

    val lcs = ('a' to 'z').map(_.toString)

    val oomMember =
      structType.oomMember.zip(lcs).map { case ((optSymbol, _type),lc) =>
        (TermName(s"${lc}ReflectPrint"), optSymbol, _type)
      }

    val fields = oomMember.map { case (fieldName,optSymbol,_type) =>
      val innerReflectPrintType = appliedType(printableTypeConstructor, List(_type))
      val innerReflectPrint = inferImplicitOrDie(innerReflectPrintType)
      q"val $fieldName = $innerReflectPrint"
    }

    def mkBody(aTypeName: String,showSymbols:Boolean) : c.Tree = {
      def appendSymbol(optSymbol:Option[c.Symbol]) : c.Tree = {
        if(showSymbols) {
          optSymbol.fold(q"") { symbol =>
            q"""
if(fmt.namedParams) {
  builder.append(${symbol.name.toString})
  builder.append(fmt.space)
  builder.append('=')
  builder.append(fmt.space)
}
"""
          }
        } else {
          q""
        }
      }

      if (oomMember.size == 1) {
        val (fieldName, optSymbol, _) = oomMember.head
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
${appendSymbol(optSymbol)}
builder.append($fieldName.printApply(v)(innerFmt))
builder.append(fmt.newLine)
${if(aTypeName.nonEmpty) q"builder.append(')')" else q""}
builder.result()
        """
      } else {
        val values =
          oomMember
            .zipWithIndex
            .map { case ((fieldName, optSymbol, _), i) =>
            q"""
{
  val innerFmt = fmt.copy(indent = fmt.indent + fmt.indentString)
  builder.append(innerFmt.newLine)
  ${appendSymbol(optSymbol)}
  builder.append($fieldName.printApply(tuple.${TermName("_" + (i + 1))})(innerFmt))
  ${if (i != oomMember.indices.last) q"builder.append(',')" else q""}
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
    println(result)
    result
  }
}

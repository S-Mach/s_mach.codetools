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

class ReflectPrintMacroBuilderImpl(
  val c: blackbox.Context,
  override val showDebug: Boolean
) extends
  ReflectPrintMacroBuilder with
  BlackboxHelper {
  import c.universe._

  def build[A: c.WeakTypeTag]() : c.Expr[ReflectPrint[A]] = getOrAbort {
    for {
      productType <- calcProductType(c.weakTypeTag[A].tpe)
      result <- build(productType)
    } yield {
      result
    }
  }

  def build[A: c.WeakTypeTag](
    productType: ProductType
  ) : Result[c.Expr[ReflectPrint[A]]] = {
    val aType = productType._type

    val isTuple = isTupleType(aType)

    val aTypeName = {
      if(isTuple) {
        ""
      } else {
        aType.typeSymbol.name.toString
      }
    }

    val (oomTypeClassValName, oomTypeClassVal) =
      productType.oomField.map { field =>
        val valTermName = TermName(field.name + "ReflectPrint")
        (valTermName, q"val $valTermName = implicitly[ReflectPrint[${field._type}]]")
      }.unzip

    val oomField = productType.oomField.zip(oomTypeClassValName)

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
        val (field, typeClassValName) = oomField.head
        q"""
..${productType.mkFieldValsTree(q"a")}
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
${appendFieldName(field.name)}
builder.append($typeClassValName.printApply(${TermName(field.name)})(innerFmt))
builder.append(fmt.newLine)
${if(aTypeName.nonEmpty) q"builder.append(')')" else q""}
builder.result()
        """
      } else {
        val values =
          oomField
            .zipWithIndex
            .map { case ((field, typeClassValName), i) =>
            q"""
{
  val innerFmt = fmt.copy(indent = fmt.indent + fmt.indentString)
  builder.append(innerFmt.newLine)
  ${appendFieldName(field.name)}
  builder.append($typeClassValName.printApply(${TermName(field.name)})(innerFmt))
  ${if (i != oomField.indices.last) q"builder.append(',')" else q""}
}
              """
          }

        q"""{
..${productType.mkFieldValsTree(q"a")}
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
  ..$oomTypeClassVal
  def printApply(a: $aType)(implicit fmt:ReflectPrintFormat) = ${mkBody(aTypeName,isTuple == false)}
  def printUnapply(a: $aType)(implicit fmt:ReflectPrintFormat) = ${mkBody("",false)}
}
      """
    }
    Result(result,Result.Debug(result.toString()))
  }
}

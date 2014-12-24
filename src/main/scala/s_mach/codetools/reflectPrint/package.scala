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
package s_mach.codetools

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

package object reflectPrint extends
  ReflectPrintValueTypeImplicits with
  ReflectPrintCollectionImplicits {

  implicit class PimpEverything[A](val self: A) extends AnyVal {
    def printApply(implicit
      p:ReflectPrint[A],
      fmt:ReflectPrintFormat = ReflectPrintFormat.std
    ) : String = p.printApply(self)
//    def printApplyFmt(
//      multiLine: Boolean = false,
//      namedParams: Boolean = true,
//      spacing: Boolean = false,
//      indentString: String = ""
//    )(implicit p:ReflectPrint[A]) : String = p.printApply(self)(
//      ReflectPrintFormat(
//        multiLine = multiLine,
//        namedParams = namedParams,
//        spacing = spacing,
//        indentString = indentString
//      )
//    )
    def printUnapply(implicit
      p:ReflectPrint[A],
      fmt:ReflectPrintFormat = ReflectPrintFormat.std
    ) : String = p.printUnapply(self)
  }

  def mkReflectPrint[A] : ReflectPrint[A] = macro mkReflectPrintMacro[A]

  def mkReflectPrintMacro[A:c.WeakTypeTag](c: blackbox.Context) : c.Expr[ReflectPrint[A]] = {
    val builder = ReflectPrintMacroBuilder(c)
    // TODO: why is this cast necessary?
    builder.build[A]().asInstanceOf[c.Expr[ReflectPrint[A]]]
  }
}
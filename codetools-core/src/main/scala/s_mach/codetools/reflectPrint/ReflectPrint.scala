/*
                    ,i::,
               :;;;;;;;
              ;:,,::;.
            1ft1;::;1tL
              t1;::;1,
               :;::;               _____       __  ___              __
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
package s_mach.codetools.reflectPrint

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

/**
 * A type-class for printing Scala code from an instance
 */
trait ReflectPrint[A] {
  /** @return Scala code for creating an instance that matches the value of a */
  def printApply(a: A)(implicit cfg:ReflectPrintFormat) : String
  /** @return Scala code for creating the unapplied value of a */
  def printUnapply(a: A)(implicit cfg:ReflectPrintFormat) : String
}

object ReflectPrint {
  def forProductType[A <: Product] : ReflectPrint[A] =
    macro macroForProductType[A]

  def macroForProductType[A<:Product:c.WeakTypeTag](c: blackbox.Context) : c.Expr[ReflectPrint[A]] = {
    val builder = ReflectPrintMacroBuilder(c)
    // Note: cast is necessary since compiler can't infer that builder.c is same
    // instance as c above
    builder.build[A]().asInstanceOf[c.Expr[ReflectPrint[A]]]
  }
}
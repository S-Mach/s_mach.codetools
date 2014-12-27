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
package s_mach.codetools.reflectPrint

import s_mach.codetools.impl.ReflectPrintMacroBuilderImpl

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

trait ReflectPrintMacroBuilder {
  val c:blackbox.Context
  def build[A: c.WeakTypeTag]() : c.Expr[ReflectPrint[A]]
}

object ReflectPrintMacroBuilder {
  def apply(c:blackbox.Context) : ReflectPrintMacroBuilder =
    new ReflectPrintMacroBuilderImpl(c)
}


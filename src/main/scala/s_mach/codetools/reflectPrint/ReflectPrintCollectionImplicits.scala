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
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.codetools.reflectPrint

import scala.language.higherKinds
import scala.reflect.ClassTag
import s_mach.codetools.impl.{ReflectPrintTraversable, ReflectPrintOption}

trait ReflectPrintCollectionImplicits {
  implicit def mkReflectPrint_Option[A](implicit pA:ReflectPrint[A]) =
    new ReflectPrintOption[A]

  implicit def mkReflectPrint_Traversable[A,M[AA] <: Traversable[AA]](implicit
    pA:ReflectPrint[A],
    mClassTag:ClassTag[M[_]]
  ) =  new ReflectPrintTraversable[A,M]
}

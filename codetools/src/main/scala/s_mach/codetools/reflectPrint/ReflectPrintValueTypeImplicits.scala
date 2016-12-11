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

import s_mach.codetools.impl._

trait ReflectPrintValueTypeImplicits {
  implicit object ReflectPrint_Boolean extends SimpleReflectPrintImpl[Boolean] {
    override def print(a: Boolean)(implicit cfg: ReflectPrintFormat): String = {
      a.toString
    }
  }
  // TODO: removed these for now since doesn't appear to be possible to create byte of short literals in vanilla Scala as of 2.11
//  implicit object ReflectPrint_Byte extends ValueTypeReflectPrintImpl[Byte]
//  implicit object ReflectPrint_Short extends ValueTypeReflectPrintImpl[Short]
  implicit object ReflectPrint_Int extends ValueTypeReflectPrintImpl[Int]
  implicit object ReflectPrint_Long extends SimpleReflectPrintImpl[Long] {
    override def print(a: Long)(implicit cfg: ReflectPrintFormat): String = {
      s"${a}l"
    }
  }
  implicit object ReflectPrint_Float extends SimpleReflectPrintImpl[Float] {
    override def print(a: Float)(implicit cfg: ReflectPrintFormat): String = {
      s"${a}f"
    }
  }
  implicit object ReflectPrint_Double extends ValueTypeReflectPrintImpl[Double]
  implicit object ReflectPrint_BigInt extends ReflectPrint[BigInt] {
    override def printApply(
      a: BigInt
    )(implicit 
      cfg: ReflectPrintFormat
    ): String = {
      s"""BigInt("$a")"""
    }
    override def printUnapply(
      a: BigInt
    )(implicit 
      cfg: ReflectPrintFormat
    ): String = s""""$a""""
  }
  implicit object ReflectPrint_BigDecimal extends ReflectPrint[BigDecimal] {
    override def printApply(
      a: BigDecimal
    )(implicit 
      cfg: ReflectPrintFormat
    ): String = {
      s"""BigDecimal("$a")"""
    }
    override def printUnapply(
      a: BigDecimal
    )(implicit 
      cfg: ReflectPrintFormat
    ): String = s""""$a""""
  }
  implicit object ReflectPrint_Char extends SimpleReflectPrintImpl[Char] {
    override def print(a: Char)(implicit cfg: ReflectPrintFormat): String = {
      s"'$a'"
    }
  }
  implicit object ReflectPrint_String extends SimpleReflectPrintImpl[String] {
    override def print(a: String)(implicit cfg: ReflectPrintFormat): String = {
      import scala.reflect.runtime.universe._
      // Note: this takes care of escaping strings: http://stackoverflow.com/questions/9913971/scala-how-can-i-get-an-escaped-representation-of-a-string
      Literal(Constant(a)).toString()
    }
  }
}

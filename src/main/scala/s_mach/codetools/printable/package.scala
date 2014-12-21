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

package object printable {
  implicit object Printable_Int extends SimplePrintable[Int]
  implicit object Printable_Double extends SimplePrintable[Double]
  implicit val Printable_String = new Printable[String] {
    override def print(a: String): String = s""""$a""""
  }
  implicit def mkPrintable_Option[A](implicit pA:Printable[A]) = new Printable[Option[A]] {
    override def print(oa: Option[A]): String = oa match {
      case Some(a) => s"Some(${pA.print(a)})"
      case None => "None"
    }
  }
  implicit def mkPrintable_List[A](implicit pA:Printable[A]) = new Printable[List[A]] {
    override def print(oa: List[A]): String = oa match {
      case Nil => "Nil"
      case nonEmptyList => s"List(${nonEmptyList.map(a => pA.print(a)).mkString(",")})"
    }
  }
  implicit class PimpEverything[A](val self: A) extends AnyVal {
    def print(implicit p:Printable[A]) : String = p.print(self)
  }

  def mkPrintable[A] : Printable[A] = macro mkPrintableMacro[A]

  def mkPrintableMacro[A:c.WeakTypeTag](c: blackbox.Context) : c.Expr[Printable[A]] = {
    val builder = PrintableMacroBuilder(c)
    // TODO: why is this cast necessary?
    builder.build[A]().asInstanceOf[c.Expr[Printable[A]]]
  }}
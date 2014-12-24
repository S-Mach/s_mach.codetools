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

/**
 * A case class for
 * @param multiLine FALSE to format strings as a single-line of text
 * @param namedParams TRUE to show named arguments in apply method
 * @param spacing TRUE to use spacing between words
 * @param indentString The string used to increase indentation
 * @param indent The string used to indent at the current level of indentation
 */
case class ReflectPrintFormat(
  multiLine: Boolean = false,
  namedParams: Boolean = true,
  spacing: Boolean = false,
  indentString: String = "",
  indent: String = ""
) {
  val space: String = if(spacing) " " else ""
  val newLine: String = if(multiLine) s"\n$indent"  else ""
  def newSection(f: ReflectPrintFormat => String) : String = {
    val innerFmt = copy(indent = indent + indentString)
    s"${innerFmt.newLine}${f(innerFmt)}$newLine"
  }
}

object ReflectPrintFormat {
  val std = ReflectPrintFormat()
  val verbose = ReflectPrintFormat(
    multiLine = true,
    spacing = true,
    indentString = " "
  )
}

/**
 * A type-class for printing Scala code from an instance
 */
trait ReflectPrint[A] {
  /** @return Scala code for creating an instance that matches the value of a */
  def printApply(a: A)(implicit cfg:ReflectPrintFormat) : String
  /** @return Scala code for creating the unapplied value of a */
  def printUnapply(a: A)(implicit cfg:ReflectPrintFormat) : String
}

trait SimpleReflectPrint[A] extends ReflectPrint[A] {
  def print(a: A)(implicit cfg: ReflectPrintFormat): String

  override def printApply(a: A)(implicit cfg: ReflectPrintFormat): String = {
    print(a)
  }

  override def printUnapply(a: A)(implicit cfg: ReflectPrintFormat): String = {
    print(a)
  }
}

class ValueTypeReflectPrint[A] extends SimpleReflectPrint[A] {
  override def print(a: A)(implicit cfg: ReflectPrintFormat): String =
    a.toString
}
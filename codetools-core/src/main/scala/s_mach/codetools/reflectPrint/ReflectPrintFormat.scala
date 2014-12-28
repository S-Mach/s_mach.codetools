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
 * A case class for formatting the code printed by ReflectPrint implementations
 *
 * @param multiLine FALSE to format code as a single-line of text
 * @param namedParams TRUE to show named arguments
 * @param spacing TRUE to show spacing between terms
 * @param indentString The string used to increase indentation (used only in
 *                     multi-line mode)
 * @param indent The string for the current level of indentation (used only in
 *               multi-line mode)
 */
case class ReflectPrintFormat(
  multiLine: Boolean = false,
  namedParams: Boolean = true,
  spacing: Boolean = false,
  indentString: String = "",
  indent: String = ""
) {
  /** A string that is a single space if spacing is enabled otherwise empty
    * string */
  val space: String = if(spacing) " " else ""
  /** A string that is the newline with indentation if multi-line is enabled
    * otherwise empty string */
  val newLine: String = if(multiLine) s"\n$indent"  else ""
  /**
   * Start a new section with a leading new line and indentation. This format
   * is copied with the indentation is increased one level and passed to the
   * inner function. The output of the inner function is returned with a
   * trailing new line with indentation. If multi-line mode is disabled,
   * new lines and indentations are omitted.
   *
   * @param f function that accepts the inner indented format and returns a
   *          string
   * @return a string with leading newline and indentation, inner section and
   * a trailing new line and indentation. If multi-line mode is disabled,
   * new lines and indentations are omitted.
   * */
  def newSection(f: ReflectPrintFormat => String) : String = {
    val innerFmt = copy(indent = indent + indentString)
    s"${innerFmt.newLine}${f(innerFmt)}$newLine"
  }
}

object ReflectPrintFormat {
  object Implicits {
    implicit val std = ReflectPrintFormat()
    implicit val verbose = ReflectPrintFormat(
      multiLine = true,
      spacing = true,
      indentString = "  "
    )
  }
}


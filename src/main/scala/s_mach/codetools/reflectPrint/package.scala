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

package object reflectPrint extends
  ReflectPrintValueTypeImplicits with
  ReflectPrintCollectionImplicits with
  ReflectPrintTupleImplicits {

  implicit class PimpEverything[A](val self: A) extends AnyVal {
    /** @return a string containing the Scala code necessary to create an
      *         instance of self with the same value */
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
    /** @return a string containing the Scala code necessary to create either
      *         the value of self or the tuple value containing the fields of
      *         self */
    def printUnapply(implicit
      p:ReflectPrint[A],
      fmt:ReflectPrintFormat = ReflectPrintFormat.std
    ) : String = p.printUnapply(self)
  }

}
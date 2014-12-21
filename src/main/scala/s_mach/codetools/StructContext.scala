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

import scala.reflect.macros.blackbox

trait StructContext {
  val c:blackbox.Context

  /**
   * A case class that represents the struct type for some type. A struct type
   * contains a list of the (Symbol,Type) pairs that represent the data types
   * (or struct types) necessary to both create an instance of the type
   * (apply) and capture the data returned when an instance of the type is
   * decomposed (unapply).
   * @param oomMember one or more (Symbol,Type) pairs
   */
  case class StructType(
    oomMember: List[(Option[c.Symbol], c.Type)]
  ) {
    require(oomMember.nonEmpty)

    def print : String = {
      oomMember.map { case (optSymbol, _type) =>
        optSymbol.fold("_:")(symbol => s"$symbol:") + _type.typeSymbol.fullName
      }.mkString(",")
    }

    /**
     * Test if two struct match. Two struct types match if their member types
     * correspond exactly.
     * @param other other struct type to test
     * @return TRUE if struct types match
     */
    def matches(other: StructType) : Boolean = {
      oomMember.corresponds(other.oomMember)(_._2 == _._2)
    }
  }
}


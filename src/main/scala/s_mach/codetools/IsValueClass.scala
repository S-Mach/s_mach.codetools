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
       ;it1i ,,,:::;;;::1tti      s_mach.data
         .t1i .,::;;; ;1tt        Copyright (c) 2015 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.codetools

/**
 * A base trait for a user-defined value-class that standardizes the name of
 * the value-class val to "underlying" and toString to underlying toString. This
 * allows creating an implicit conversion from an instance of any value-class to its
 * underlying representation.
 *
 * For details on value-class see:
 * http://docs.scala-lang.org/overviews/core/value-classes.html
 *
 * Example value-class:
 * implicit class Name(underlying: String) extends AnyVal with IsValueType[String]
 *
 * Note1: When using creating a value-class for String, it is necessary to create an
 * implicit view to StringOps to use the Scala extended String methods on instances
 * of the value-class.
 * Example for Name above:
 * object Name {
 *   import scala.collection.immutable.StringOps
 *   implicit def stringOps_Name(n: Name) = new StringOps(n.underlying)
 * }
 *
 * Note2: while almost every method on underlying can be used on the value-class without
 * special code, the equals method is a special case that still requires wrapping the
 * right-hand type in the value-class to match the other side. Ex:
 *
 * Name("Hal") == "Hal" // always returns false + compiler warning
 * Name("Hal") == Name("Hal") // works correctly
 *
 * @tparam A type of underlying value class (Note: this parameter does not require
 *           inheritance from AnyVal since this would prevent using the trait with
 *           java.lang.String which does not inherit AnyVal)
 */
trait IsValueClass[A] extends Any {
  def underlying: A

  override def toString = underlying.toString
}

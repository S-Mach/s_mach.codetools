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
         .t1i .,::;;; ;1tt        Copyright (c) 2015 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.codetools

import scala.language.higherKinds

/**
 * A base trait for manually building type-classes for a product type
 * @tparam T type-class type
 * @tparam A type for type-class
 */
trait ProductBuilder[T[_],A] {
  /**
   * Add a field to the builder
   * @param name name of the field
   * @param unapply a function to extract the field value from an instance of A
   * @param f a function to modify the implicit type-class T[B] for the field
   * @param vb implicit type-class for B
   * @tparam B field type
   * @return a copy of the product builder with the field appended
   */
  def field[B](
    name: String,
    unapply: A => B
  )(
    f: T[B] => T[B] = { vb:T[B] => vb }
  )(implicit
    vb: T[B]
  ) : ProductBuilder[T,A]

  /**
   * Build an instance of the type-class for A composed of all the appended fields
   * @return an instance of the type-class of A
   */
  def build() : T[A]
}

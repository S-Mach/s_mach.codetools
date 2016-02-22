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
package s_mach

import scala.language.implicitConversions
import scala.language.higherKinds

package object codetools {

  /**
   * Implicitly convert a value class to its underlying type
   * @param v a value-class derived from IsValueClass
   * @tparam A the underlying type
   * @return the wrapped value
   */
  @inline implicit def valueClassToA[A](v: IsValueClass[A]) : A =
    v.underlying

  /**
   * Implicitly convert any generic M[A] to M[V]
   * where V is a distinct type alias of A
   * @param ma generic instance
   * @tparam M generic type
   * @tparam V DTA type
   * @tparam A underlying type of DTA
   * @return instance cast to M[V]
   */
  @inline implicit def distinctTypeAlias_mVtoMa[
    M[_],
    V <: IsDistinctTypeAlias[A],
    A
  ](
    ma: M[A]
  ) : M[V] =
    ma.asInstanceOf[M[V]]

  /**
   * Implicitly convert any generic M[A,B] to a M[V,B]
   * where V is a distinct type alias of A
   * @param ma generic instance
   * @tparam M2L type of generic instance
   * @tparam V DTA type
   * @tparam A underlying type of DTA
   * @tparam B some type
   * @return instance cast to M[V,B]
   */
  @inline implicit def distinctTypeAlias_m2lVtoM2la[
    M2L[_,_],
    A,
    V <: IsDistinctTypeAlias[A],
    B
  ](
    ma: M2L[A,B]
  ) : M2L[V,B] =
    ma.asInstanceOf[M2L[V,B]]

  /**
   * Implicitly convert any generic M[A,B] to a M[A,V]
   * where V is a distinct type alias of B
   * @param ma generic instance
   * @tparam M2R type of generic instance
   * @tparam V DTA type
   * @tparam A some type
   * @tparam B underlying type of DTA
   * @return instance cast to M[A,V]
   */
  @inline implicit def distinctTypeAlias_m2rVtoM2ra[
    M2R[_,_],
    A,
    V <: IsDistinctTypeAlias[A],
    B
  ](
    ma: M2R[B,A]
  ) : M2R[B,V] =
    ma.asInstanceOf[M2R[B,V]]

  // TODO: write a generator for more DTA conversions

}

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
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.codetools

/**
 * Marker trait used to mark a type alias as being a distinct type alias (DTA).
 * A DTA is an alternative to the Scala value-class
 * (http://docs.scala-lang.org/overviews/core/value-classes.html) that
 * never needs to box (or unbox) since type aliases are eliminated in byte code.
 *
 * A DTA is declared with a unique empty trait "tag" to make the type alias distinct
 * in the type system. Since the DTA is a distinct type from its alias, implicit
 * generics (e.g. type-classes) of the DTA can be resolved separately from the aliased
 * type. Implicits resolving a normal type-alias will always resolve to an instance for
 * the aliased type.
 *
 * Example:
 * {{{
 * type Age1 = Int
 * implicitly[Format[Age1] ] // resolves to Format[Int] b/c Age1 isn't distinct
 * trait AgeTag // empty "tag" trait
 * type Age = Int with AgeTag
 * implicitly[Format[Age] ] // resolves to Format[Age] b/c Age is distinct
 * }}}
 *
 * Unlike value-classes, which are elided by the compiler under most circumstances, DTAs
 * are always elided at runtime. Value-classes used in any kind of generic, such as List
 * or Option, cannot be elided by the compiler and boxing/unboxing penalties are incurred.
 *
 * This means if a conversion from a generic of the value-class type to the underlying type
 * is required (or vice-versa), a map operation must be used to box or unbox the value-class.
 * This incurs a O(n) penalty simply to cast between the value-class and the underlying type.
 * Because DTAs are type aliases for their underlying type the compiler treats a
 * generic of a DTA as a generic of the underlying type without any impact on runtime.
 *
 * While covariant generics will automatically cast from a generic of the DTA to its underlying
 * type, casting from the underlying type to the DTA is not supported directly. Instead an implicit
 * generic converter must be imported:
 *
 * Example generic converter:
 * {{{
 * implicit def ma_to_mv[M[_],V < : IsDistinctTypeAlias[A],A](
 *   ma: M[A]
 * ) : M[V] = ma.asInstanceOf[M[V] ]
 * }}}
 *
 * Note: this converter is declared in codetools package.scala
 *
 * To declare a DTA, first create an empty trait tag. Next, declare a type alias for
 * the aliased type but that also extends the tag and the marker trait
 * IsDistinctTypeAlias. Finally, declare an implicit def constructor method that
 * mimics the apply method of a companion object.
 *
 * Example:
 * {{{
 * trait AgeTag
 * type Age = Int with AgeTag with IsDistinctTypeAlias[Int]
 * implicit def Age(i: Int) = i.asInstanceOf[Age]
 * }}}
 *
 * Note1: The IsDistinctTypeAlias marker trait self-documents the DTA and
 * is used by the implicit def converters to prevent ambiguous implicit resolution errors.
 * Note2: While it is possible to declare an object for the DTA, it will not be treated
 * as a companion object for the DTA by the compiler (implicit generics in the object
 * will not resolve implicitly like normal companion objects)
 *
 * @tparam A type aliased by the DTA
 */
trait IsDistinctTypeAlias[A] { self: A =>

}

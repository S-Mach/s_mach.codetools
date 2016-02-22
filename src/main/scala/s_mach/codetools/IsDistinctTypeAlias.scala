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
 *
 * A DTA is always declared with a unique trait tag to make the type alias distinct
 * in the type system. Since the DTA is a distinct type, type-classes of the
 * DTA can be implicitly resolved. In Scala, a normal type alias would always resolve
 * to its underlying type.
 *
 * Tagging a type alias example:
 * type Age1 = Int // implicitly[Format[Age] ] resolves to Format[Int]
 * trait AgeTag
 * type Age = Int with AgeTag // implicitly[Format[Age] ] is distinct from Format[Int]
 *
 *
 * Value-classes are elided by the compiler under many circumstances, but
 * when used in any kind of generic, such as List or Option, they are emitted.
 * To convert from a generic of the value-class type to the underlying type
 * requires a no-op runtime call in byte code that may or may not be eliminated
 * by downstream optimization. For collections such as List, this means a O(n)
 * call to map simply to cast from the value-class to the underlying type. Because
 * DTAs are simply type aliases for their underlying type the compiler can simply
 * treat a generic of a DTA as a generic of the underlying type without any impact
 * on runtime.
 *
 * Converting the other direction for value-classes suffers from the same problem as
 * stated above. Because DTAs require a distinct tag, the compiler can not automatically
 * perform the downcast. Instead, implicit defs are created to support the operation
 * as a casting operation that should be eliminated in otput byte code.
 *
 * Example generic converter:
 * implicit def ma_to_mv[M[_],V < : IsDistinctTypeAlias[A],A](
 *   ma: M[A]
 * ) : M[V] = ma.asInstanceOf[M[V] ]
 *
 * A DTA is declared by first creating a trait tag that is used to make the DTA
 * distinct. Next, the type alias is declared as a mix of the underlying type,
 * the tag and the marker trait IsDistinctTypeAlias. The IsDistinctTypeAlias marker
 * trait is used to in implicit def conversions that convert generics (such as
 * collections) without overhead.
 *
 * Full example:
 * trait AgeTag
 * type Age = Int with AgeTag with IsDistinctTypeAlias[Int]
 * implicit def Age(i: Int) = i.asInstanceOf[Age]
 *
 * @tparam A
 */
trait IsDistinctTypeAlias[A] { self: A =>

}

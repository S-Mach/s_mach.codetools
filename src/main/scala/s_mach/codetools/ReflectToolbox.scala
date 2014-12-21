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
package net.s_mach.codetools

import net.s_mach.codetools.impl.ReflectToolboxImpl

trait ReflectToolbox extends StructContext with ReflectToolboxImpl {
  /**
   * Infer an implicit value for a type. Aborts if the implicit value does not
   * exist.
   * @param aType type to infer implicit value for
   * @return tree of implicit value
   */
  @inline def inferImplicitOrDie(aType:c.Type) : c.Tree =
    impl.inferImplicitOrDie(aType)

  /**
   * Get a symbol to a method in a type's companion object within a
   * context. Aborts if method doesn't exist.
   * @param aType type whose companion object should be searched for method
   * @return the matching method symbol
   */
  @inline def getCompanionMethodOrDie(
    aType: c.Type,
    methodName: String
  ) : c.universe.MethodSymbol =
    impl.getCompanionMethodOrDie(aType,methodName)

  @inline def isTupleType(aType:c.Type) : Boolean =
      impl.isTupleType(aType)

  /**
   * Compute the struct type for a type
   *
   * For case classes, the struct type is the list of (field name, field type)
   * pairs of the case class.
   * For tuples, the struct type is the (incremental tuple field name ("_1",
   * "_2", etc), field type) pairs of the tuple.
   * For other types, the struct type equals the first unapply/apply method
   * pair  in the type's companion object with matching struct types.
   *
   * @param aType type whose companion object should be searched for apply and
   *              unapply methods
   * @return the struct type that contains a non-empty member list of
   *         (Symbol,Type) pairs
   */
  @inline def calcStructType(aType: c.Type): StructType =
    impl.calcStructType(aType)

}


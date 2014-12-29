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

import s_mach.codetools.impl.BlackboxHelperImpl

import scala.reflect.macros.blackbox

trait BlackboxHelper extends BlackboxHelperImpl { self =>
  val c:blackbox.Context

  /** Log all issues to the Context */
  @inline def logIssues(zomIssue: List[Result.Issue]) : Unit =
    Impl.logIssues(zomIssue)

  /** @return If Result is success the value of the Result. If Result is
    *         failure, c.abort is invoked ending the macro. All issues are
    *         logged to the Context */
  @inline def getOrAbort[A,X](r:Result[A]): A =
    Impl.getOrAbort(r)

  /**
   * @return if the method exists, the method symbol otherwise an error message
   */
  @inline def getCompanionMethod(
      aType: c.Type,
      methodName: String
    ) : Result[c.universe.MethodSymbol] =
      Impl.getCompanionMethod(aType, methodName)

  /**
   * @return if inferred, a Tree representing the implicit otherwise an error
   *         message
   */
  @inline def inferImplicit(aType:c.Type) : Result[c.Tree] =
    Impl.inferImplicit(aType)

  /**
   * @return TRUE if type is a tuple type (e.g. Tuple2[T1,T2], etc)
   */
  @inline def isTupleType(aType:c.Type) : Boolean =
    Impl.isTupleType(aType)

  /** @inheritdoc */
  @inline def calcProductType(
     aType: c.Type
  ): Result[ProductType] =
    Impl.calcProductType(aType)


}
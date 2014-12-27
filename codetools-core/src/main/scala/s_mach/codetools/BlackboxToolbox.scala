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

import s_mach.codetools.impl.BlackboxToolboxImpl

import scala.reflect.macros.blackbox

trait BlackboxToolbox extends BlackboxToolboxImpl { self =>
  val c:blackbox.Context

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

  /**
   * Try to compute the product type fields for a type.
   *
   * The product type fields are computed by finding the first unapply/apply
   * method pair in the type's companion object with matching type signatures.
   * The type signature of apply methods is equal to the sequence of the types
   * of its arguments. Unapply methods may have one or two type signatures based
   * on its return type. First, the inner type parameter of the Option return
   * type is extracted. If the inner type parameter is a tuple type, then both
   * the tuple type and the list of tuple type parameters form possible type
   * signatures. Otherwise, if the inner type parameter is not a tuple type then
   * the type signature is equal to the single type parameter. Once an
   * apply/unapply match is made, the symbols of the apply method's argument
   * list are returned as  the product type fields for the type. For tuple types
   * and case classes, this will be the list of it's fields.
   *
   * Example1:
   * class A(...) { ... }
   * object A {
   *   def apply(i: Int, s: String) : A = ???
   *   def apply(i: Int, s: String, f: Float) : A = ???
   *   def unapply(a: A) : Option[(Int,String)] = ???
   * }
   * The first apply method's type signature = List(Int,String)
   * The unapply method's type signature = List(List((Int,String)),Int,String)
   * Product type fields = List(i:Int,s:String)
   *
   * Example2:
   * class B(...) { ... }
   * object B {
   *   def apply(tuple: (String,Int)) : A = ???
   *   def apply(i: Int, s: String) : A = ???
   *   def unapply(b: B) : Option[(Int,String)] = ???
   * }
   * The first apply method's type signature = List((String,Int))
   * The unapply method's type signature = List(List((String,Int)),
   * List(String,Int))
   * Product type fields = List(tuple: (String, Int))
   *
   * Example3:
   * class Enum(...) { ... }
   * object Enum {
   *   def apply(value: String) : A = ???
   *   def unapply(e: Enum) : Option[String] = ???
   * }
   * The first apply method's type signature = List(String)
   * The unapply method's type signature = List(List(String))
   * Product type fields = List(value: String)
   *
   * Example4:
   * case class CaseClass(i: Int, s: String)
   * The first apply method's type signature = List(Int,String)
   * The unapply method's type signature = List(List((Int,String)),
   * List(Int,String))
   * Product type fields = List(i:Int,s:String)
   *
   * Example5:
   * class Tuple2[T1,T2](val _1: T1,val _2 : T2)
   * The first apply method's type signature = List(T1,T2)
   * The unapply method's type signature = List(T1,T2)
   * Product type fields = List(_1:T1,_2:T2)
   *
   * If no product type can be computed, None is returned and an error message
   * is logged to logger
   *
   * @param aType type whose companion object should be searched for apply and
   *              unapply methods
   * @return if successful, Some(product type) otherwise None
   */
  @inline def calcProductTypeFields(aType: c.Type): Result[List[(String, c.Type)]] =
    Impl.calcProductTypeFields(aType)

  /**
   * A case class that represents the fields for a product type.
   * @param oomField one or more (Symbol,Type) pairs
   */
  case class ProductType(
    productType: c.Type,
    oomField: List[ProductType.Field]
  ) {
    require(oomField.nonEmpty)

    def print : String = {
      oomField.map { field =>
        import field._
        s"$fieldName:${_type.typeSymbol.fullName}"
      }.mkString(",")
    }
  }

  object ProductType {
    case class Field(fieldName: String, _type: c.Type)

    def apply(aType: c.Type) : Result[ProductType] = {
      for {
        oomField <- calcProductTypeFields(aType)
      } yield {
        ProductType(
          productType = aType,
          oomField = oomField.map { case (fieldName, _type) =>
            Field(
              fieldName = fieldName,
              _type = _type
            )
          }
        )
      }
    }
  }
}
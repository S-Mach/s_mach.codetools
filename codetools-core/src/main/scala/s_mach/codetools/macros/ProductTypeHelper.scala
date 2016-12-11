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
package s_mach.codetools.macros

import scala.reflect.macros.blackbox

/**
 * Helper trait that adds ProductType support to a blackbox context 
 */
trait ProductTypeHelper { self =>
  val c:blackbox.Context
  import c.universe._

  // TODO: figure out why c.info with force set to false doesn't seem to suppress debug messages (even when verbose is off)
  /** Override to show debug messages */
  val showDebug: Boolean = false

  /**
   * Try to compute the product type fields for a type by finding the first
   * unapply/apply method pair in the type's companion object with matching type
   * signatures. If no product type can be computed, Failure is returned with
   * an error message.
   *
   * @param aType type whose companion object should be searched for apply and
   *              unapply methods
   * @return if successful, Some(product type) otherwise None
   */
   def calcProductType(
    aType: c.Type
  ): Result[ProductType]


  /**
   * A case class that represents the fields for a product type.
   * @param _type the product type
   * @param oomField the fields of the product type (computed from the arguments 
   *                 in the apply method)
   * @param applyMethod the matched apply method
   * @param unapplyMethod the matched unapply method
   * @param allFieldsAreMembers TRUE if all fields correspond to a public no arg
   *                            member method with the same name and type in the
   *                            product type (e.g. this is true for all case
   *                            classes and tuple types)
   */
  case class ProductType(
    _type: c.Type,
    oomField: List[ProductType.Field],
    applyMethod: c.universe.MethodSymbol,
    unapplyMethod: c.universe.MethodSymbol,
    allFieldsAreMembers: Boolean
  ) {
    require(oomField.nonEmpty)

    /** The Tree necessary for the type's companion object */
    val companion : c.Tree = {
      Ident(TermName(_type.typeSymbol.name.toString))
    }
    
    def print : String = {
      _type.typeSymbol.name.toString + "(" +
      oomField.map { field =>
        s"${field.name}:${_type.typeSymbol.fullName}"
      }.mkString(",") +
      ")"
    }

    /**
     * Make the Tree necessary to decompose a value of a product type into the
     * individual fields. If all product type fields are members of the type
     * then the fields are accessed directly, otherwise the unapply method is
     * used.
     * Example1:
     * case class Person1(name: String, age: Int)
     * q"""
     * val name = value.name
     * val age = value.int
     * """
     * Example2:
     * class Person2 { ... }
     * object Person2 {
     *   def apply(name: String, age: Int) : Person2 = ???
     *   def unapply(p:Person2) : Option[(String,Int)] = ???
     * }
     * q"""
     * val (name,age) = Person2.unapply(value).get
     * """
     * @param value the tree for the value
     * @param mkValName a function that accepts the field name and returns the 
     *                  name of the val
     * @param tupleName the val name of the tuple (used only when
     *                  allFieldsAreMembers is false)
     * @return a Tree with a val for each field decomposed from the value
     */
    def mkFieldValsTree(
      value: c.Tree,
      mkValName: String => String = { s => s },
      tupleName: => String = "__decomposedValue$1"
    ) : Seq[c.Tree] = {

      if(allFieldsAreMembers) {
        oomField.map { case field =>
          val valTermName = TermName(mkValName(field.name))
          q"val $valTermName = $value.${field.termName}"
        }
      } else {
        if (oomField.size == 1) {
          val valTermName = TermName(mkValName(oomField.head.name))
          Seq(q"val $valTermName = unapply($value).get")
        } else {
          val tupleTermName = TermName(tupleName)
          Seq(q"val $tupleTermName = unapply($value).get") ++
          oomField.zipWithIndex.map { case (field,i) =>
            val valTermName = TermName(mkValName(field.name))
            q"val $valTermName = $tupleTermName.${TermName(s"_${i+1}")}"
          }
        }
      }
    }

  }

  object ProductType {

    /**
     * Case class for a field of a product type
     * @param index index (ordinal) of field
     * @param name name of the field
     * @param _type type of the field
     */
    case class Field(index: Int, name: String, _type: c.Type) {
      val termName = TermName(name)
    }
  }
}

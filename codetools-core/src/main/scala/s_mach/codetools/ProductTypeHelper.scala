package s_mach.codetools

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

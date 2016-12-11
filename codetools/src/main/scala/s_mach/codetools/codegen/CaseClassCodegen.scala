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
package s_mach.codetools.codegen

import s_mach.codetools.impl

object CaseClassCodegen {

  /**
    * Field definition in a case class
    * @param name name of field
    * @param _type type of field
    * @param optDefault default value of field
    * @param optComment comment for field
    */
  case class Field(
    name: String,
    _type: String,
    optDefault: Option[String] = None,
    optComment: Option[String] = None
  )

  /**
   * Generates Scala code for a case class. If the case class has less than 22
   * fields then a normal case class is generated. If the case class has more
   * than 22 fields a big case class is generated. Generated case  class chunks
   * fields into groups of 22 and creates a child case class for  each chunk.
   * Each generated child case class is named using base class name  with an
   * incremented sequence number appended. Generated code includes  everything
   * that would normally be implemented by compiler for a case class, including:
   * 1. copy method
   * 2. Product methods (productElement, productArity & productIterator)
   * 3. Companion object with apply method (and child case classes)
   *
   * @param name the name of the case class
   * @param fields one or more case class fields
   * @return
   */
  def genCaseClass(name: String, fields: Vector[Field]) : String =
    impl.CaseClassPrinter.printCaseClass(name,fields)

  /**
   * Generates Scala code for a big (> 22 field) case class. Generated case
   * class chunks fields into groups of 22 and creates a child case class for
   * each chunk. Each generated child case class is named using base class name
   * with an incremented sequence number appended. Generated code includes
   * everything that would normally be implemented by compiler for a
   * case class, including:
   * 1. copy method
   * 2. Product methods (productElement, productArity & productIterator)
   * 3. Companion object with apply method (and child case classes)
   *
   * @param name the name of the case class
   * @param fields one or more case class fields
   * @return
   */
  def genBigCaseClass(name: String, fields: Vector[Field]) : String =
    impl.BigCaseClassPrinter.printBigCaseClass(name,fields)
}
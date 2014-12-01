package net.s_mach.codegen.impl

import net.s_mach.codegen.CaseClassField

object CodegenOps {
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
   * @param caseClassName the name of the case class
   * @param oomCaseClassField one or more case class fields
   * @return
   */
  def genCaseClass(caseClassName: String, oomCaseClassField: Seq[CaseClassField]) : String = ???
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
   * @param caseClassName
   * @param oomCaseClassField
   * @return
   */
  def genBigCaseClass(caseClassName: String, oomCaseClassField: Seq[CaseClassField]) : String = ???
}
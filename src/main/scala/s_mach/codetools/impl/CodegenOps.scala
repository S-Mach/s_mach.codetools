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
package s_mach.codetools.impl

import s_mach.codetools.CaseClassField

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
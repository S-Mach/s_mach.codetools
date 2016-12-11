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
package s_mach.codetools

import org.scalatest.{Matchers, FlatSpec}
import testdata._
import s_mach.codetools.reflectPrint._

class ReflectPrintTest extends FlatSpec with Matchers {
  "Macro.printApply" must "correctly print the code necessary to create the same instance for a case class" in {
    val t1 = TestData(value="asdf")
    t1.printApply should equal(
      """TestData(value="asdf")"""
    )
    // TODO: eval the code
  }
  "Macro.printApply" must "correctly print the code necessary to create the result of unapply on a case class" in {
    TestData2(value1=1,value2="2",value3=Some(3.0),value4=TestData(value="asdf")).printUnapply should equal(
      """(1,"2",Some(3.0),TestData(value="asdf"))"""
    )
    // TODO: eval the code
  }
  "Macro.printApply" must "correctly print the code necessary to create the same instance for a case class with a nested case class value" in {
    TestData2(value1=1,value2="2",value3=Some(3.0),value4=TestData(value="asdf")).printApply should equal(
      """TestData2(value1=1,value2="2",value3=Some(3.0),value4=TestData(value="asdf"))"""
    )
  }
  "Macro.printApply" must "correctly implement a case class with a single tuple value" in {
    TestData3(value1=(1,"2",3.0)).printApply should equal(
      """TestData3(value1=(1,"2",3.0))"""
    )
  }
  "Macro.printApply" must "correctly print the code necessary to create the same instance for a case class with a collection" in {
    TestData4(value1=2,value2=TestData2(value1=1,value2="2",value3=Some(10.2),value4=TestData(value="asdf")),value3=List(1.1,2.2,3.3),value4=TestData3(value1=(2,"3",4.0))).printApply should equal(
      """TestData4(value1=2,value2=TestData2(value1=1,value2="2",value3=Some(10.2),value4=TestData(value="asdf")),value3=List(1.1,2.2,3.3),value4=TestData3(value1=(2,"3",4.0)))"""
    )
  }
  "Macro.printApply" must "correctly print the code necessary to create the same instance for a case class with an ADT and Enum" in {
    TestData5(TestADT1("2"),TestEnum2).printApply should equal(
      """TestData5(value1=TestBaseADT(_type="TestADT1",value1=Some("2"),value2=None),value2=TestEnum(value1="TestEnum2"))"""
    )
  }
  "Macro.printApply" must "correctly print the code necessary to create the same instance for a case class with a type parameter" in {
    TestData6(value1=TestADT1(value1="2")).printApply should equal(
      """TestData6(value1=TestADT1(value1="2"))"""
    )
  }
  "Macro.printApply" must "correctly print the code necessary to create the same instance of a tuple" in {
    ("a",1,1.0).printApply should equal(
      """("a",1,1.0)"""
    )
  }

  "Macro.printUnapply" must "correctly print the code necessary to create the same instance of a single value" in {
    TestData(value="asdf").printUnapply should equal(
      """"asdf""""
    )
  }
  "Macro.printUnapply" must "correctly print the code necessary to create the same instance of a tuple" in {
    ("a",1,1.0).printUnapply should equal(
      """("a",1,1.0)"""
    )
  }
}

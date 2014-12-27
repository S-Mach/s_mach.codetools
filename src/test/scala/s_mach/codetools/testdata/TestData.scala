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
package s_mach.codetools.testdata

import s_mach.codetools.reflectPrint._

case class TestData(value: String)
object TestData {
  implicit val ReflectPrint_TestData = ReflectPrint.forProductType[TestData]
}

case class TestData2(value1: Int, value2: String, value3: Option[Double],value4: TestData)
object TestData2 {
  implicit val ReflectPrint_TestData = ReflectPrint.forProductType[TestData2]
}

case class TestData3(value1: (Int,String,Double))
object TestData3 {
  implicit val ReflectPrint_TestData = ReflectPrint.forProductType[TestData3]
}

case class TestData4(value1: Int, value2: TestData2, value3: List[Double], value4: TestData3)
object TestData4 {
  implicit val ReflectPrint_TestData = ReflectPrint.forProductType[TestData4]
}

sealed trait TestEnum extends Product
case object TestEnum1 extends TestEnum
case object TestEnum2 extends TestEnum
case object TestEnum3 extends TestEnum
object TestEnum {
  val values = List(TestEnum1,TestEnum2,TestEnum3)
  def apply(value1: String) : TestEnum = values.find(_.toString == value1).get
  def unapply(e:TestEnum) : Option[String] = Some(e.toString)
  implicit val ReflectPrint_TestEnum = ReflectPrint.forProductType[TestEnum]
}

sealed trait TestBaseADT extends Product
case class TestADT1(value1: String) extends TestBaseADT
object TestADT1 {
  implicit val ReflectPrint_TestADT1 = ReflectPrint.forProductType[TestADT1]
}
case class TestADT2(value1: Double) extends TestBaseADT
object TestBaseADT {
  def apply(_type: String, value1: Option[String], value2: Option[Double]) : TestBaseADT = {
    _type match {
      case "TestADT1" => TestADT1(value1.get)
      case "TestADT2" => TestADT2(value2.get)
    }
  }
  def unapply(tb:TestBaseADT) : Option[(String,Option[String],Option[Double])] = {
    tb match {
      case TestADT1(value1) => Some(("TestADT1",Some(value1),None))
      case TestADT2(value2) => Some(("TestADT2",None,Some(value2)))
    }
  }
  implicit val ReflectPrint_TestBaseADT = ReflectPrint.forProductType[TestBaseADT]
}

case class TestData5(value1: TestBaseADT, value2: TestEnum)

object TestData5 {
  implicit val ReflectPrint_TestData5 =
    ReflectPrint.forProductType[TestData5]
}

case class TestData6[A <: TestBaseADT](value1: A)

object TestData6 {
  implicit def ReflectPrint_Testdata6[A <: TestBaseADT](implicit aPrintable:ReflectPrint[A]) : ReflectPrint[TestData6[A]] =
    ReflectPrint.forProductType[TestData6[A]]
}
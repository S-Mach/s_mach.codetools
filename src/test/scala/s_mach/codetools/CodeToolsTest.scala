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

import org.scalatest.{FlatSpec, Matchers}

object CodeToolsTest {
  implicit class Age(val underlying: Int) extends AnyVal with IsValueClass[Int]

  trait WeightTag
  type Weight = Double with WeightTag with IsDistinctTypeAlias[Double]
  @inline def Weight(weight: Double) = weight.asInstanceOf[Weight]
}

class CodeToolsTest extends FlatSpec with Matchers {
  import CodeToolsTest._

  "Age.underlying" should "return underlying value" in {
    Age(10).underlying should be(10)
  }

  "Age.toString" should "return underlying toString" in {
    Age(10).toString should be("10")
  }

  "Age" should "implicitly convert to Int" in {
    def test(i: Int): Unit = {
      i should be(10)
    }
    test(Age(10))
  }

  "Weight" should "be a Double" in {
    Weight(150.0) should be (150.0)
  }

  "M[Weight]" should "implicitly upcast to M[Double]" in {
    val values = List(
      150.0,
      151.0,
      152.0
    )
    def upcast(weights: List[Double]): Unit = {
      weights should be (values)
    }
    upcast(values.map(Weight))
  }

  "M[Double]" should "implicitly downcast to M[Weight] even if M isn't covariant" in {
    // Use array since its invariant
    val values = Array(
      150.0,
      151.0,
      152.0
    )
    def downcast(weights: Array[Weight]): Unit = {
      weights should be (values)
    }
    // Manually invoke converter
    val _:Array[Weight] = distinctTypeAlias_MVtoMA(values)
    // Note: intellij chokes on this
    // Test implicit conversion
    downcast(values)
  }

  "M[Weight,String]" should "implicitly upcast to M[Double,String]" in {
    def upcast(test: Either[Double,String]): Unit = {
      test should be(Left(150))
    }
    upcast(Left(Weight(150)))
  }

  "M[Double,String]" should "implicitly downcast to M[Weight,String]" in {
    def downcast(test: Either[Weight,String]): Unit = {
      test should be(Left(Weight(150.0)))
    }
    val _:Either[Weight,String] =
      distinctTypeAlias_m2lVtoM2la(Left(150.0))
    val test:Either[Double,String] = Left(150.0)
    //     Note: intellij chokes on this
    downcast(test)
    // Note: this doesn't work - for whatever reason
    // Scala won't match the implicit when Right = Nothing
//    downcast(Left(150.0))
  }

  "M[String,Weight]" should "implicitly upcast to M[String,Double]" in {
    def upcast(test: Either[String,Double]): Unit = {
      test should be(Right(150))
    }
    upcast(Right(Weight(150)))
  }

  "M[String,Double]" should "implicitly downcast to M[String,Weight]" in {
    def downcast(test: Either[String,Weight]): Unit = {
      test should be(Right(Weight(150.0)))
    }
    val _:Either[String,Weight] =
      distinctTypeAlias_m2rVtoM2ra(Right(150.0))
    val test:Either[String,Double] = Right(150.0)
    //     Note: intellij chokes on this
    downcast(test)
    // Note: this doesn't work - for whatever reason
    // Scala won't match the implicit when Left = Nothing
//    downcast(Right(150.0))
  }

}

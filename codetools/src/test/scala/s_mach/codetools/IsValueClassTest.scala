package s_mach.codetools

import org.scalatest.{FlatSpec, Matchers}

object IsValueClassTest {
  implicit class Weight(val underlying: Double) extends AnyVal with IsValueClass[Double]
}
class IsValueClassTest extends FlatSpec with Matchers {
  import IsValueClassTest._

  "IsValueClass.toString" should "return underlying.toString" in {
    val test = 50.0
    Weight(test).toString should equal(test.toString)
  }
}

package qizero.model

import org.joda.time.DateTime
import org.scalatest.{Matchers, WordSpec}

class FixtureTest extends WordSpec with Matchers {
  import Fixture._

  case class Foo(
                  string: String,
                  bool: Boolean,
                  byte: Byte,
                  s: Short,
                  b: Int,
                  l: Long,
                  f: Float,
                  d: Double,
                  big: BigDecimal,
                  opt: Option[Int],
                  default: String = "default",
                  date: DateTime
                  )

  "fixture" should {
    "create new seed" in {
      val foo = Fixture.seed[Foo]
      foo.default shouldBe "default"
    }
  }
}

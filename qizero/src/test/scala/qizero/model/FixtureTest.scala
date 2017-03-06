package qizero.model

import org.joda.time.DateTime
import org.scalatest.{Matchers, WordSpec}
import qizero.entity.{Entity, Has, Id, TypedId}

class FixtureTest extends WordSpec with Matchers {

  case class BarId(value: Long) extends TypedId

  case class Bar(id: BarId) extends Entity with Id[BarId]

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
                  date: DateTime,
//                  bar: Has[Bar] = Has(Fixture.seed[Bar]),
                  bar: Has[Bar],
                  barOpt: Option[Has[Bar]]
                  )

  "fixture" should {
    "create new seed" in {
      val bar = Fixture.seed[Bar]
      val foo = Fixture.seed[Foo]
      foo.default shouldBe "default"
      foo.opt shouldBe None
      foo.barOpt shouldBe None
      println(foo)
    }
  }
}

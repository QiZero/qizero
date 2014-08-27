package qizero.entity

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class RelationshipTest extends WordSpec with Matchers {

  case class Foo(name: String, num: Int, id: Long) extends Entity with Id[Long]

  case class Bar(name: String, foo: Has[Foo], id: Long) extends Entity with Id[Long]

  case class Bars(foos: HasMany[Foo], id: Long) extends Entity with Id[Long]

  implicit val fooFormat = Json.format[Foo]
  implicit val barFormat = Json.format[Bar]

  "Relationship Has" should {
    "return id when HasId" in {
      val fooId = 1

      val bar = Bar("bar", Has[Foo](fooId), 1)

      bar.foo.isId shouldBe true
      bar.foo.id shouldBe fooId

    }

    "return entity whe HasEntity" in {
      val foo = Foo("foo", 10, 1)

      val bar = Bar("bar", Has(foo), 1)

      bar.foo.isId shouldBe true
      bar.foo.id shouldBe foo.id
      bar.foo.isEntity shouldBe true
      bar.foo.entity shouldBe foo
    }

    "implicit convert entity to HasEntity" in {
      val foo = Foo("foo", 10, 1)

      val bar = Bar("bar", foo, 1)

      bar.foo.isId shouldBe true
      bar.foo.id shouldBe foo.id
      bar.foo.isEntity shouldBe true
      bar.foo.entity shouldBe foo
    }

    "mapper HasId to json" in {
      val fooId = 1
      val bar = Bar("bar", Has[Foo](fooId), 1)

      val toJson = Json.toJson(bar)
      val fromJson = Json.fromJson[Bar](toJson).get

      fromJson shouldBe bar
    }

    "mapper HasEntity to json" in {
      val foo = Foo("foo", 10, 1)
      val bar = Bar("bar", Has(foo), 1)

      val toJson = Json.toJson(bar)
      val fromJson = Json.fromJson[Bar](toJson).get

      fromJson shouldBe bar
    }
    //    "return id when HasId" in {
    //
    //      val bar = Bar("bar", 1, 1)
    //
    //      bar.foo.isId shouldBe true
    //      bar.foo.id shouldBe fooId
    //
    //    }
  }

  "Relationship HasMany" should {
    "return ids when HasManyIds" in {
      val ids = Seq(1L, 2L, 3L)
      val bars = Bars(HasMany[Foo](ids), 1)

      bars.foos.ids should have size 3
      bars.foos.ids shouldBe ids
    }

    "return entities when HasManyEntities" in {
      val entities = Seq(
        Foo("foo1", 11, 1),
        Foo("foo2", 12, 2),
        Foo("foo3", 13, 3)
      )
      val bars = Bars(HasMany(entities), 1)

      bars.foos.ids should have size 3
      bars.foos.entities shouldBe entities
    }
  }

}

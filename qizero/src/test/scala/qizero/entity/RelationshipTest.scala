package qizero.entity

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class RelationshipTest extends WordSpec with Matchers {

  case class Foo(name: String, num: Int, id: Long) extends Entity with Id[Long]

  case class Bar(name: String, foo: Has[Foo], id: Long) extends Entity with Id[Long]

  case class Bars(foos: HasMany[Foo] = HasMany.None, id: Long) extends Entity with Id[Long]

  implicit val fooFormat = Json.format[Foo]
  implicit val barFormat = Json.format[Bar]
  implicit val barsFormat = Json.format[Bars]

  "Relationship Has" should {
    "return id when HasId" in {
      val fooId = 1

      val bar = Bar("bar", Has[Foo](fooId), 1)

      bar.foo.isId shouldBe true
      bar.foo.id shouldBe fooId

    }

    "return entity when HasEntity" in {
      val foo = Foo("foo", 10, 1)

      val bar = Bar("bar", Has(foo), 1)

      bar.foo.isId shouldBe true
      bar.foo.id shouldBe foo.id
      bar.foo.isEntity shouldBe true
      bar.foo.entity shouldBe foo
    }

    "implicit convert entity to HasEntity" in {
      val foo = Foo("foo", 10, 1)
      val hasFoo: Has[Foo] = Has(foo)
      val optFoo: Option[Has[Foo]] = hasFoo
      optFoo should not be None
      optFoo.get shouldBe hasFoo
      optFoo.get.id shouldBe hasFoo.id

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
  }

  "Relationship HasMany" should {
    "return ids when HasManyIds" in {
      val ids = List(1L, 2L, 3L)
      val bars = Bars(HasMany[Foo](ids), 1)

      bars.foos.ids should have size 3
      bars.foos.ids shouldBe ids
    }

    "return entities when HasManyEntities" in {
      val entities = List(
        Foo("foo1", 11, 1),
        Foo("foo2", 12, 2),
        Foo("foo3", 13, 3)
      )
      val bars = Bars(HasMany(entities), 1)

      bars.foos.ids should have size 3
      bars.foos.entities shouldBe entities
    }

    "implicit convert entity to HasManyEntities" in {
      val entities = List(
        Foo("foo1", 11, 1),
        Foo("foo2", 12, 2),
        Foo("foo3", 13, 3)
      )
      val bars = Bars(entities, 1)

      bars.foos.isEntities shouldBe true
      bars.foos.entities shouldBe entities
    }

    "mapper HasManyIds to json" in {
      val ids = List(1L, 2L, 3L)
      val bars = Bars(HasMany[Foo](ids), 1)

      val toJson = Json.toJson(bars)
      val fromJson = Json.fromJson[Bars](toJson).get

      fromJson shouldBe bars
    }

    "mapper HasManyEntities to json" in {
      val entities = List(
        Foo("foo1", 11, 1),
        Foo("foo2", 12, 2),
        Foo("foo3", 13, 3)
      )
      val bars = Bars(HasMany(entities), 1)

      val toJson = Json.toJson(bars)
      val fromJson = Json.fromJson[Bars](toJson).get

      fromJson shouldBe bars
    }

    "mapper HasEmpty to json" in {
      val bars = Bars(HasMany.None, 1)

      val toJson = Json.toJson(bars)
      val fromJson = Json.fromJson[Bars](toJson).get

      fromJson shouldBe bars
    }
  }

}

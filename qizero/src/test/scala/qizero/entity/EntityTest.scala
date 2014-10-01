package qizero.entity

import scala.util.Random
import org.scalatest._

class EntityTest extends WordSpec with Matchers {


  case class FooId(value: Long) extends TypedId

  case class Foo(name: String, id: FooId) extends Entity with Id[FooId]

  case class FooRow(name: String, id: Option[FooId])

  case class BarId(value: Long) extends TypedId

  case class Bar(name: String, email:Option[String] = None, foo: Has[Foo], id: BarId) extends Entity with Id[BarId]

  case class BarRow(name: String, fooId: FooId, id: Option[BarId])

  "Entity" should {
    "has id" in {
      val id = FooId(1)
      val foo = Foo("n", id)
      foo.id shouldBe id
      foo.id.value shouldBe id.value
    }
  }

  "Entity Mapper" should {
    "create a manual mapper" in {
      val mapper = Entity.mapper { in: FooRow => Foo(in.name, in.id.get)}
      val row = FooRow("test", Some(Random.nextInt))

      val entity = mapper(row)
      entity.id shouldBe row.id.get
      entity.name shouldBe row.name
    }

    "create a macro mapper" in {
      val mapper = Entity.mapper[FooRow, Foo]
      val row = FooRow("test", Some(Random.nextInt))

      val entity = mapper(row)
      entity.id shouldBe row.id.get
      entity.name shouldBe row.name
    }

    "create a mapper with Has" in {
      val mapper = Entity.mapper { in: BarRow =>
        Bar(in.name, None, Has[Foo](in.fooId), in.id.get)
      }

      val row = BarRow("test", Random.nextInt, Some(Random.nextInt))
      val entity = mapper(row)
      entity.id shouldBe row.id.get
      entity.name shouldBe row.name
      entity.foo.id shouldBe row.fooId
    }

    "materialzer mapper with Has" in {
      val mapper = Entity.mapper[BarRow, Bar]

      val row = BarRow("test", Random.nextInt, Some(Random.nextInt))
      val entity = mapper(row)
      entity.id shouldBe row.id.get
      entity.name shouldBe row.name
      entity.foo.id shouldBe row.fooId
    }
  }
}

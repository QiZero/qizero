package qizero.entity

import org.scalatest._
import scala.util.Random

class EntityTest extends WordSpec with Matchers {

  case class FooId(value: Long) extends TypedId

  case class Foo(name: String, id: FooId) extends Entity with Id[FooId]

  case class FooRow(name: String, id: Option[FooId])

  case class BarId(value: Long) extends TypedId

  case class Bar(name: String, email: Option[String] = None, foo: Has[Foo], fooOpt: Option[Has[Foo]], id: BarId) extends Entity with Id[BarId]

  case class BarRow(name: String, fooId: FooId, fooOptId: Option[FooId] = None, id: Option[BarId])

  case class Named(@InNamed("cba") abc: String, num: Int, @InNamed("abc") foo: Has[Foo], @InNamed("cbaId") abcId: FooId)

  case class NamedRow(cba: String, num: Int, abcId: FooId, cbaId: FooId)

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
        Bar(in.name, None, Has[Foo](in.fooId), in.fooOptId.map(Has[Foo](_)), in.id.get)
      }

      val row = BarRow("test", Random.nextInt, None, Some(Random.nextInt))
      val entity = mapper(row)
      entity.id shouldBe row.id.get
      entity.name shouldBe row.name
      entity.foo.id shouldBe row.fooId
    }

    "materialzer mapper with Has" in {
      val mapper = Entity.mapper[BarRow, Bar]

      val row = BarRow("test", Random.nextInt, None, Some(Random.nextInt))
      val entity = mapper(row)
      entity.id shouldBe row.id.get
      entity.name shouldBe row.name
      entity.foo.id shouldBe row.fooId
    }

    "materialzer mapper with InNamed" in {
      val mapper = Entity.mapper[NamedRow, Named]

      val row = NamedRow("test", Random.nextInt, Random.nextInt, Random.nextInt)
      val entity = mapper(row)
      entity.abc shouldBe row.cba
      entity.num shouldBe row.num
      entity.abcId shouldBe row.cbaId
      entity.foo.id shouldBe row.abcId
    }
  }
}

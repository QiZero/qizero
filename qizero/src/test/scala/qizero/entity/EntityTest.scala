package qizero.entity

import org.scalatest._

class EntityTest extends WordSpec with Matchers {

  case class FooId(value: Long) extends TypedId

  case class Foo(name: String, id: FooId) extends Entity with Id[FooId]

  "Entity" should {
    "has id" in {
      val id = FooId(1)
      val foo = Foo("n", id)

      foo.id shouldBe id
      foo.id.value shouldBe id.value
    }
  }
}

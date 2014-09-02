package qizero.entity

import org.scalatest.{FunSuite, Matchers}
import scala.language.experimental.macros

trait WithId[E <: WithId[E, ID], ID] {
  self: E =>

  def id: ID

  def withId: Copier[E, ID]
}

case class Foo(id: Long, name: String) extends WithId[Foo, Long] {
  self =>

  def withId = new Copier[Foo, Long] {
    def apply(in: Long) = self.copy(id = in)
  }
}

trait NewId[E <: NewId[E, ID], ID] {
  self: E =>
  def id: ID

  def withId(value:ID):E = Copier.copy[E, ID]
}
case class Bar(id:Long, name:String) extends NewId[Bar, Long] {
}

class CopierTest extends FunSuite with Matchers {

  test("Test copier") {
    val foo = Foo(1, "n")
    val copiedFoo = foo.withId(2)
    foo.name shouldBe copiedFoo.name
    copiedFoo.id shouldBe 2
  }

  test("Test Bar copier") {
    val foo = Bar(1, "n")
    val copiedFoo = foo.withId(2)
    foo.name shouldBe copiedFoo.name
    copiedFoo.id shouldBe 2
  }
}

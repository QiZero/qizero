package qizero.entity

import org.scalatest.{FunSuite, Matchers}

case class AId(value: Long) extends TypedId

class TypedIdTest extends FunSuite with Matchers {

  test("implicit convert") {
    val id = AId(1)
    val id1 = TypedId.toId[AId](1)
    val id2: AId = 1L
    val id3: AId = 1
    id shouldBe id1
    id shouldBe id2
    id shouldBe id3
  }

}

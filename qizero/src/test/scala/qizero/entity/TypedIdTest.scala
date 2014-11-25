package qizero.entity

import org.scalatest.{FunSuite, Matchers}

class TypedIdTest extends FunSuite with Matchers {

  case class TestId(value: Long) extends TypedId

  test("implicit convert") {
    val id = TestId(1)
    val id1 = TypedId.toId[TestId](1)
    val id2: TestId = 1L
    val id3: TestId = 1
    val value:Long = id
    id shouldBe id1
    id shouldBe id2
    id shouldBe id3
  }

}

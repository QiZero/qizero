package qizero.entity

import org.scalatest._

class MapperTest extends WordSpec with Matchers {

  case class Row(name: String, count: Int, id: Option[Long])

  case class Entity(name: String, id: Long)

  "Mapper" should {
    "create" in {
      val mapper = Mapper.as[Row, Entity]

      val row = Row("test", 1, Some(10))
      val entity = mapper(row)

      entity.id shouldBe 10
      entity.name shouldBe "test"

    }
  }
}

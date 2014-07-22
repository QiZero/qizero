package qizero.persistence.mapping

import org.scalatest.{Matchers, WordSpec}

class MapperTest extends WordSpec with Matchers {

  case class Row(name: String, count: Int, id: Option[Long])

  case class Entity(name: String, id: Long)(b:Int, c:Int)

  "mapper" should {
    "create" in {
//      val mapper = Mapper.create[Row, Entity]
//
//      val row = Row("test", 1, Some(10))
//      val entity = mapper(row)
//
//      entity.id shouldBe 10
//      entity.name shouldBe "test"

    }
  }
}

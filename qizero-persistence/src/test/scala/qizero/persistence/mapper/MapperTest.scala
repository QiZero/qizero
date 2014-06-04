package qizero.persistence.mapper

import org.scalatest.{Matchers, WordSpec}

class MapperTest extends WordSpec with Matchers {

  case class User(name: String, age: Int, id: Long)

  case class UserRow(name: String, age: Int, gener: String, id: Option[Long])

  val row = UserRow("test", 10, "m", Some(1))

  "mapper" should {
    "macro" in {
      val mapper = Mapper.create[UserRow, User]
      val u = mapper(row)
      u.name should be(row.name)
    }
  }
}

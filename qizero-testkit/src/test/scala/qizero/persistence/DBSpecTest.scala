package qizero.persistence

import org.scalatest.{Matchers, WordSpec}

class DBSpecTest extends WordSpec with Matchers with DBSpec {

  val dal = TestDAL
  import dal._
  import profile.simple._

  val ddl = Foos.ddl

  "the DBSpec trait" should {
    "create a session" in {
      val session = dynamicSession
      session should not be(null)
    }
    "create tables" in {
      val result = Foos.size.run
      result shouldBe 0
    }
    "do a rollback after test" in {
      Foos.insert(Foo("a", 1))
    }
  }
}

object TestDAL extends DAL("test") {

  import profile.simple._

  case class Foo(name:String, count:Int)

  class Foos(tag:Tag) extends Table[Foo](tag, "foo") {

    val name = column[String]("name")
    val count = column[Int]("count")

    def * = (name, count) <> (Foo.tupled, Foo.unapply)
  }

  lazy val Foos = TableQuery[Foos]
}
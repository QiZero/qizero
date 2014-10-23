package qizero.persistence.query

import org.scalatest.{Matchers, WordSpec}
import qizero.entity.Mapper
import qizero.model.Pagination
import qizero.persistence.{DBSpec, TestDAL}

class FinderTest extends WordSpec with Matchers with DBSpec {
  val dal = TestDAL
  val ddl = dal.ddl

  import dal._

  case class Foo(str: String, id: Long)

  object Foo {
    implicit val FooMapper = Mapper { in: TestDAL.FooRow => Foo(in.str, in.id.get)}
  }

  trait WithData {
    (1 to 10).foreach { i =>
      Foos.insert(FooRow(s"test$i", Some(i))).run
    }
  }

  "Finder" should {
    "find" in new WithData {
      val result = Foos.find.first.run
      result.str should startWith("test")
    }
    "find one" in new WithData {
      val result = Foos.find.one.run
      result shouldBe 'defined
    }
    "find list" in new WithData {
      val result = Foos.find.all.run
      result should have size (10)
    }
    "find page" in new WithData {
      val result = Foos.find.page(Pagination(1, 20)).run
      result.count shouldBe 10
    }
    "find slice" in new WithData {
      val result = Foos.find.slice(Pagination(1, 20)).run
      result.count shouldBe 10
    }
    "count" in new WithData {
      val result = Foos.find.count.run
      result shouldBe 10
    }
    "exist" in new WithData {
      val result = Foos.find.exists.run
      result shouldBe true
    }
  }
  "Finder Map" should {
    "find" in new WithData {
      val result = Foos.find.as[Foo].first.run
      result.str should startWith("test")
    }
    "find one" in new WithData {
      val result = Foos.find.as[Foo].one.run
      result shouldBe 'defined
    }
    "find list" in new WithData {
      val result = Foos.find.as[Foo].all.run
      result should have size (10)
    }
    "find page" in new WithData {
      val result = Foos.find.as[Foo].page(Pagination(1, 20)).run
      result.count shouldBe 10
    }
    "find slice" in new WithData {
      val result = Foos.find.as[Foo].slice(Pagination(1, 20)).run
      result.count shouldBe 10
    }
  }
}
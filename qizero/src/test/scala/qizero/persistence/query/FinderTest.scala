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
    val numElems = 50
    (1 to numElems).foreach { i =>
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
      result should have size (numElems)
    }
    "find page" in new WithData {
      val result = Foos.find.page(Pagination(1, 10)).run
      result.number shouldBe 1
      result.size shouldBe 10
      result.count shouldBe 10
      result.total shouldBe numElems
      result.totalPages shouldBe 5
    }
    "find slice" in new WithData {
      val result = Foos.find.slice(Pagination(1, 10)).run
      result.number shouldBe 1
      result.size shouldBe 10
      result.count shouldBe 10
    }
    "count" in new WithData {
      val result = Foos.find.count.run
      result shouldBe 50
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
      result should have size (numElems)
    }
    "find page" in new WithData {
      val result = Foos.find.as[Foo].page(Pagination(1, 20)).run
      result.number shouldBe 1
      result.size shouldBe 20
      result.count shouldBe 20
      result.totalPages shouldBe 3
      result.total shouldBe numElems
    }
    "find slice" in new WithData {
      val result = Foos.find.as[Foo].slice(Pagination(1, 20)).run
      result.number shouldBe 1
      result.size shouldBe 20
      result.count shouldBe 20
    }
  }
}
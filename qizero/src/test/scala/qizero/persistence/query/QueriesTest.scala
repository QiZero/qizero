package qizero.persistence.query

import org.scalatest.FunSuite
import qizero.persistence.{DBSpec, TestDAL}

class QueriesTest extends FunSuite with DBSpec {
  val dal = TestDAL
  val ddl = dal.ddl

  import dal._

  test("insert") {
    Foos.insert(FooRow("test"))
  }

  test("finder") {
    Foos.insert(FooRow("test"))
    Foos.insert(FooRow("test"))
    Foos.insert(FooRow("test"))
  }

  test("Optional Filter") {
    import dal.profile.simple._
    val opt:Option[String] = None
    val query = Foos
      .maybeFilter(Some("1"))(_.str === _)
      .maybeFilter(opt)(_.str === _)
      .maybeFilter(Some("2"))(_.str === _)
      .sortBy(_.str)

    val finder = query.find.all
    val result = finder.run
    println(finder.statement)
    println(result)
  }
}

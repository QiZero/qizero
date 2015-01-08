package qizero.persistence.query

import org.scalatest.{Matchers, FunSuite}
import qizero.model.SortBy
import qizero.persistence.{DBSpec, TestDAL}

class QueriesTest extends FunSuite with Matchers with DBSpec {
  val dal = TestDAL
  val ddl = dal.ddl

  import dal._
  import profile.simple._

  test("insert with long") {
    val row = InsertReturningIdAction(Foos).insert(FooRow("test")).run
//    val row = Foos.insert(FooRow("test")).run
    println(row)
    row.id shouldBe 'defined
  }

//  test("insert with typedId") {
//    val row = InsertReturningIdAction(Bars).insert(BarRow("test", 1)).run
//    //    val row = Foos.insert(FooRow("test")).run
//    println(row)
//    row.id shouldBe 'defined
//  }
//  test("finder") {
//    Foos.insert(FooRow("test")).run
//    Foos.insert(FooRow("test")).run
//    Foos.insert(FooRow("test")).run
//  }
//
//  test("Maybe Filter") {
//    import dal.profile.simple._
//    val opt:Option[String] = None
//    val query = Foos
//      .maybeFilter(Some("1"))(_.str === _)
//      .maybeFilter(opt)(_.str === _)
//      .maybeFilter(Some("2"))(_.str === _)
//      .sortBy(_.str)
//
//    val finder = query.find.all
//    val result = finder.run
//    println(finder.statement)
//    println(result)
//  }
//
//  test("Maybe SortBy") {
//    import dal.profile.simple._
//    val opt:Option[SortBy] = None
//    val query = Foos
//      .maybeSortBy(Some(SortBy.Desc.NullsLast))(_.str)
//      .maybeSortBy(opt)(_.str)
//
//    val finder = query.find.all
//    val result = finder.run
//    println(finder.statement)
//    println(result)
//  }
}

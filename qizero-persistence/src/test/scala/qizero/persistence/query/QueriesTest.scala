package qizero.persistence.query

import org.scalatest.FunSuite
import qizero.persistence.mapping.Mapper
import qizero.persistence.{DBSpec, TestDAL}

class QueriesTest extends FunSuite with DBSpec {
  val dal = TestDAL
  def ddl = dal.ddl

  import dal._
  import profile.simple._

  test("insert") {
    Foos.insert(FooRow("test"))
  }

  test("finder") {
    Foos.insert(FooRow("test"))
    Foos.insert(FooRow("test"))
    Foos.insert(FooRow("test"))

    val query = Foos.filter(_.str === "test")

//    val findFirst = query.find.first.run
//    val findOne = query.find.one.run
//    val findAll = query.find.all.run
//
//    println(findFirst)
//    println(findOne)
//    println(findAll)
//
//    val findFirst = query.find.first.run
//    val findOne = query.find.one.run
//    val findAll = query.find.all.run
//
//    println(findFirst)
//    println(findOne)
//    println(findAll)

  }
}

case class Foo(str: String, id: Long)

object Foo {

  implicit object mapper extends Mapper[TestDAL.FooRow, Foo] {
    def apply(r: TestDAL.FooRow) = Foo(
      str = r.str,
      id = r.id.get
    )
  }

}

case class Bar(str: String, foo: Foo, id: Long)

object Bar {

  implicit object mapper extends Mapper[TestDAL.BarRow, Bar] {
    def apply(r: TestDAL.BarRow) = Bar(
      str = r.str,
      foo = null,
      id = r.id.get
    )
  }

}
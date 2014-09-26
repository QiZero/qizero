package qizero.persistence.query

import org.scalatest.FunSuite
import qizero.entity.Mapper
import qizero.persistence.{DBSpec, TestDAL}

class QueriesTest extends FunSuite with DBSpec {
  val dal = TestDAL
  val ddl = dal.ddl

  import dal._
  import profile.simple._

  test("insert") {
    Foos.insert(FooRow("test"))
  }

  test("finder") {
    Foos.insert(FooRow("test"))
    Foos.insert(FooRow("test"))
    Foos.insert(FooRow("test"))

  }

}

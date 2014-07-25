package qizero.persistence

import query.Queries

object TestDAL
  extends DAL("test")
  with Tables
  with Queries

trait Tables {
  self: HasProfile =>

  import profile.simple._

  lazy val ddl = Foos.ddl

  case class FooRow(str: String, id: Option[Long] = None)

  class Foos(tag: Tag) extends Table[FooRow](tag, "foos") {
    val str = column[String]("str")
    val id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def * = (str, id.?) <>(FooRow.tupled, FooRow.unapply)
  }

  lazy val Foos = TableQuery[Foos]

  case class BarRow(str: String, fooId: Long, id: Option[Long] = None)

  class Bars(tag: Tag) extends Table[BarRow](tag, "bars") {
    val str = column[String]("str")
    val fooId = column[Long]("foo_id")
    val id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def * = (str, fooId, id.?) <>(BarRow.tupled, BarRow.unapply)
  }

  lazy val Bars = TableQuery[Bars]

}
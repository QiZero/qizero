package qizero.persistence

import scala.slick.jdbc.GetResult
import qizero.persistence.query.Queries

object TestDAL
  extends NamedDAL("test")
  with Tables
  with Queries

trait Tables {
  self: HasProfile =>

  import profile.simple._

  lazy val ddl = Foos.ddl ++ Bars.ddl

  case class FooRow(str: String, flag:Boolean = false, id: Option[Long] = None) extends AutoIncId[Long] {
    override def withId(id: Long): AutoIncId[Long] = copy(id = Some(id))
  }

  implicit val getFooRowResult = GetResult(r => FooRow(r.<<, r.<<))

  class Foos(tag: Tag) extends Table[FooRow](tag, "foos") {
    val str = column[String]("str")
    val flag = column[Boolean]("flag")
    val id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def * = (str, flag, id.?) <>(FooRow.tupled, FooRow.unapply)
  }

  lazy val Foos = TableQuery[Foos]

  case class BarRow(str: String, fooId: Long, id: Option[Long] = None) extends AutoIncId[Long] {
    override def withId(id: Long): AutoIncId[Long] = copy(id = Some(id))
  }

  implicit val getBarRowResult = GetResult(r => BarRow(r.<<, r.<<, r.<<))

  class Bars(tag: Tag) extends Table[BarRow](tag, "bars") {
    val str = column[String]("str")
    val fooId = column[Long]("foo_id")
    val id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def * = (str, fooId, id.?) <>(BarRow.tupled, BarRow.unapply)
  }

  lazy val Bars = TableQuery[Bars]

}
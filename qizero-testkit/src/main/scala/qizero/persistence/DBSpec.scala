package qizero.persistence

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.util.Try

abstract class DBSpec[D <: DAL](_dal: D) extends DBSpecLike {
  implicit val dal = _dal
}

trait DBSpecLike extends WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val dal: DAL

  import dal.profile.simple._

  def tables: Seq[TableQuery[_ <: Table[_]]]

  override protected def beforeAll(): Unit = {
    dal.db.withSession { implicit s =>
      tables.foreach { t =>
        val ddl = dal.profile.buildTableSchemaDescription(t.baseTableRow)
        Try(ddl.create)
      }
    }
  }

}
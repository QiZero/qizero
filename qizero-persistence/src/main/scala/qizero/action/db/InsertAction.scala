package qizero.action.db

import qizero.action.{DBSession, Action}
import qizero.persistence.DAL
import qizero.persistence.query.QueryId
import qizero.persistence.table.{RowId, HasId}
import scala.slick.lifted.Query

final case class Insert[R](query: Query[_, R, Seq], row: R)
                          (implicit val dal: DAL)
  extends Action[R] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = query.insertStatement

  protected def act(): R = {
    query.insert(row)
    row
  }


}

final case class InsertReturningId[R <: RowId[ID], ID](private val q: Query[_ <: HasId[ID], R, Seq], row: R)
                                                      (implicit val dal: DAL)
  extends Action[R] with DBSession with QueryId{

  private val query = ReturningId(q)(dal.profile).returningId(copyId)

  lazy val statement: String = query.insertStatement

  protected def act(): R = query.insert(row)

  private def copyId(row: R, id: ID): R = {
    row.withId(id).asInstanceOf[R]
  }
}
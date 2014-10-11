package qizero.action.db

import scala.slick.lifted.Query
import qizero.persistence._

trait InsertAction[R] extends QueriedAction[R] {
  self: DBAction[_] =>

  lazy val statement: String = {
    import dal.profile.simple._
    query.insertStatement
  }
}

final class Insert[R](val query: Query[_, R, Seq], row: R)
                     (implicit val dal: DAL)
  extends DBAction[R] with InsertAction[R] {

  import dal.profile.simple._

  protected def act(): R = {
    query.insert(row)
    row
  }
}

final class InsertReturningId[R <: AutoIncId[ID], ID](val query: Query[_ <: HasAutoIncId[ID], R, Seq], row: R)
                                                     (implicit val dal: DAL)
  extends DBAction[R] with InsertAction[R] {

  import dal.profile.simple._

  protected def act(): R = {
    query.returning(query.map(_.id)).into(copyId).insert(row)
  }

  private def copyId(row: R, id: ID): R = {
    row.withId(id).asInstanceOf[R]
  }
}
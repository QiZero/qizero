package qizero.action.db

import qizero.action.DBAction
import qizero.persistence.DAL
import scala.slick.lifted.Query

final case class Insert[R](q: Query[_, R, Seq], row: R)
                          (implicit val dal: DAL)
  extends DBAction[R] {

  protected def act(): R = {
    import dal.profile.simple._
    q.insert(row)
    row
  }
}

//final case class InsertReturningId[R <: Id[ID], ID](q: Query[_ <: HasId[ID], R, Seq], row: R)
//                                             (implicit val dal: DAL)
//  extends DBAction[R] {
//
//  protected def act(): R = {
//    val query = QueryId.ReturingId(q)(dal.profile).returningId(copyId)
//    val result = query.insert(row)
//    result
//  }
//
//  private def copyId(row: R, id: ID): R = {
//    row.withId(id).asInstanceOf[R]
//  }
//}
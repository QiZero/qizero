package qizero.persistence.query

import qizero.action.db._
import qizero.persistence.DAL
import qizero.persistence.table.{HasId, RowId}
import slick.lifted.Query

trait InsertQuery {
  implicit final class InsertReturningIdAction[ID, R <: RowId[ID]](q: Query[_ <: HasId[ID], R, Seq])(implicit dal: DAL) {
    def insert(row: R) = InsertReturningId(q, row)
  }

  implicit final class InsertAction[R](q: Query[_, R, Seq])(implicit dal: DAL) {
    def insert(row: R) = Insert(q, row)
  }
}

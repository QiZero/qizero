package qizero.persistence.query

import qizero.action.Action
import qizero.action.db._
import qizero.domain.{Page, Pagination}
import qizero.persistence.DAL
import qizero.persistence.mapping.Mapper
import qizero.persistence.table.{HasId, RowId}
import scala.slick.lifted.Query

object QueryHelper extends QueryHelper

trait QueryHelper {

  implicit final class FindAction[E, R](q: Query[E, R, Seq])(implicit dal: DAL) {
    def find = Find(q)
    def findOne = FindOne(q)
    def findAll = FindAll(q)
    def findPage(pagination: Pagination) = FindPage(q, pagination)
    def count = FindCount(q)
    def exist = FindExist(q)
  }

  implicit final class InsertReturningIdAction[ID, R <: RowId[ID]](q: Query[_ <: HasId[ID], R, Seq])(implicit dal: DAL) {
    def insert(row: R) = InsertReturningId(q, row)
  }

  implicit final class InsertAction[R](q: Query[_, R, Seq])(implicit dal: DAL) {
    def insert(row: R) = Insert(q, row)
  }

  implicit final class UpdateAction[R](q: Query[_, R, Seq])(implicit dal: DAL) {
    def update(row: R): Action[R] = ???
  }

  implicit final class MapperAction[R](action: Action[R]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(mapper)
  }

  implicit final class OptionMapperAction[R](action: Action[Option[R]]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(_.map(mapper))
  }

  implicit final class SeqMapperAction[R](action: Action[Seq[R]]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(_.map(mapper))
  }

  implicit final class PageMapperAction[R](action: Action[Page[R]]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map { r =>
      Page(r.content.map(mapper), r.pagination, r.total)
    }
  }

}

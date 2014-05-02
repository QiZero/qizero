package qizero.persistence.query

import qizero.action.{Action, DBAction}
import qizero.action.db._
import qizero.model.{Pagination, Page}
import qizero.persistence.DAL
import qizero.persistence.mapper.Mapper
import scala.slick.lifted.Query

trait QueryAction {

  implicit final class FindAction[E, R](q: Query[E, R, Seq])(implicit dal: DAL) {
    def find = Find(q)
    def findOne = FindOne(q)
    def findAll = FindAll(q)
    def findPage(pagination: Pagination) = FindPage(q, pagination)
    def count = FindCount(q)
    def exist = FindExist(q)
  }

  //  implicit final class InsertReturningIdAction[ID, R <: Id[ID]](q: Query[_ <: HasId[ID], R, Seq])(implicit dal: DAL) {
  //    def insert(row: R) = InsertReturningId(q, row)
  //  }

  implicit final class InsertAction[R](q: Query[_, R, Seq])(implicit dal: DAL) {
    def insert(row: R) = Insert(q, row)
  }

  implicit final class UpdateAction[R](q: Query[_, R, Seq])(implicit dal: DAL) {
    def update(row: R): Action[R] = ???
  }

  implicit final class MapperAction[R](action: DBAction[R]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(mapper)
  }

  implicit final class OptionMapperAction[R](action: DBAction[Option[R]]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(mapper)
  }

  implicit final class SeqMapperAction[R](action: DBAction[Seq[R]]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(mapper)
  }

  implicit final class PageMapperAction[R](action: DBAction[Page[R]]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(mapper)
  }

}
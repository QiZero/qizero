package qizero.persistence.query

import qizero.action.db._
import qizero.entity.Mapper
import qizero.model.SortBy
import qizero.persistence._
import scala.language.{existentials, higherKinds, implicitConversions}
import scala.slick.lifted._

trait Queries {

  implicit final class MaybeSortBy[E, U](query: Query[E, U, Seq]) {
    def maybeSortBy[V, T](sortBy: Option[SortBy])(f: E => Column[T]): Query[E, U, Seq] = {
      sortBy.map {
        case SortBy.Desc => query.sortBy(f(_).desc)
        case SortBy.Desc.NullsFirst => query.sortBy(f(_).desc.nullsFirst)
        case SortBy.Desc.NullsLast => query.sortBy(f(_).desc.nullsLast)
        case SortBy.Asc => query.sortBy(f(_).asc)
        case SortBy.Asc.NullsFirst => query.sortBy(f(_).asc.nullsFirst)
        case SortBy.Asc.NullsLast => query.sortBy(f(_).asc.nullsLast)
      }.getOrElse(query)
    }
  }

  implicit final class MaybeFilter[E, U](query: Query[E, U, Seq]) {
    def maybeFilter[V, T: CanBeQueryCondition](value: Option[V])(f: (E, V) => T): Query[E, U, Seq] = {
      value.map(v => query.withFilter(f(_, v))).getOrElse(query)
    }
  }

  implicit final class ToQueriedFinder[R](query: Query[_, R, Seq])(implicit dal: DAL) {
    def find: Finder[R] = new QueriedFinder(query)
  }

  implicit final class ToCompiledFinder[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])(implicit dal: DAL) {
    def find: Finder[R] = new CompiledFinder(compiled)
  }

  implicit final class InsertReturningIdAction[ID, R <: AutoIncId[ID]](query: Query[_ <: HasAutoIncId[ID], R, Seq])(implicit dal: DAL) {
    def insert(row: R) = new InsertReturningId(query, row)
  }

  implicit final class InsertAction[R](query: Query[_, R, Seq])(implicit dal: DAL) {
    def insert(row: R) = new Insert(query, row)
  }

  implicit final class Map[R](action: DBAction[R]) {
    def as[E](implicit mapper: Mapper[R, E]): DBAction[E] = new MapDBAction(action, mapper)
  }

}
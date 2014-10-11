package qizero.persistence.query

import scala.language.{existentials, higherKinds, implicitConversions}
import scala.slick.lifted._
import qizero.action.db._
import qizero.entity.Mapper
import qizero.persistence._

trait Queries {

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
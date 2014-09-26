package qizero.persistence.query

import scala.language.{existentials, higherKinds, implicitConversions}
import scala.slick.ast.BaseTypedType
import scala.slick.lifted._
import qizero.action.db._
import qizero.entity.Mapper
import qizero.persistence._

trait Queries
  extends IdQueries
  with FindQueries
  with InsertQueries
  with MapQueries

trait FindQueries {

  implicit final class ToQueriedFinder[R](query: Query[_, R, Seq])(implicit dal: DAL) {
    def find: Finder[R] = new QueriedFinder(query)
  }

  implicit final class ToCompiledFinder[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])(implicit dal: DAL) {
    def find: Finder[R] = new CompiledFinder(compiled)
  }

}

trait InsertQueries {

  implicit final class InsertReturningIdAction[ID, R <: RowId[ID]](query: Query[_ <: HasId[ID], R, Seq])(implicit dal: DAL) {
    def insert(row: R) = new InsertReturningId(query, row)
  }

  implicit final class InsertAction[R](query: Query[_, R, Seq])(implicit dal: DAL) {
    def insert(row: R) = new Insert(query, row)
  }

}

trait MapQueries {

  implicit final class Map[R](action: DBAction[R]) {
    def as[E](implicit mapper: Mapper[R, E]): DBAction[E] = new MapDBAction(action, mapper)
  }

}

trait IdQueries {

  implicit final class ById[R, ID](query: Query[_ <: HasId[ID], R, Seq])(implicit dal: DAL) {

    import dal.profile.simple._

    def byId(id: ID)(implicit m: BaseTypedType[ID]) = {
      query.filter(_.id === id)
    }
  }

  implicit final class ReturningId[R, ID](query: Query[_ <: HasId[ID], R, Seq])(implicit dal: DAL) {

    import dal.profile.simple._

    def returningId(): Profile#ReturningInsertInvokerDef[R, ID] = {
      query.returning(query.map(_.id))
    }

    def returningId(f: (R, ID) => R): Profile#IntoInsertInvokerDef[R, R] = {
      returningId().into(f)
    }
  }

}
package qizero.persistence.query

import qizero.persistence.Profile
import qizero.persistence.table.HasId
import scala.slick.ast.BaseTypedType
import scala.slick.lifted.Query

trait QueryId {

  implicit final class QueryById[R, ID](query: Query[_ <: HasId[ID], R, Seq])(implicit profile: Profile) {
    def byId(id: ID)(implicit m: BaseTypedType[ID]) = {
      import profile.simple._
      query.filter(_.id === id)
    }
  }

  implicit final class ReturningId[R, ID](query: Query[_ <: HasId[ID], R, Seq])(implicit profile: Profile) {
    def returningId(): Profile#ReturningInsertInvokerDef[R, ID] = {
      import profile.simple._
      query.returning(query.map(_.id))
    }

    def returningId(f: (R, ID) => R): Profile#IntoInsertInvokerDef[R, R] = {
      returningId.into(f)
    }
  }

}


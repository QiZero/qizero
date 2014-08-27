package qizero.action.db

import scala.language.higherKinds
import scala.slick.lifted.{RunnableCompiled, Query}
import qizero.action.{Action, DBSession}
import qizero.persistence.mapping.Mapper

trait DBAction[R] extends Action[R] {
  def statement: String
}

trait QueriedAction[R] extends DBSession {
  self: DBAction[_] =>

  def query: Query[_, R, Seq]
}

trait CompiledAction[R, C[_]] extends DBSession {
  self: DBAction[_] =>

  def compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]]
}

class MapDBAction[IN, OUT](action: DBAction[IN], mapper: Mapper[IN, OUT]) extends DBAction[OUT] {
  def statement: String = action.statement
  protected def act(): OUT = mapper(action.run)
}
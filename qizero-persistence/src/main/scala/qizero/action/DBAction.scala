package qizero.action

import qizero.persistence.{Session, DAL}
import scala.slick.SlickException

sealed trait HasSession {
  self: DBAction[_] =>

  implicit val dal: DAL

  protected def withSession[T](f: => T): T

  implicit final def dynamicSession: Session = {
    dal.session.dynamicSession
  }

}

trait DBSession extends HasSession {
  self: DBAction[_] =>

  override protected final def withSession[T](f: => T): T = {
    dal.db.withDynSession(f) // FIXME Should be withInSession
  }
}

trait DBTransaction extends HasSession {
  self: DBAction[_] =>

  override protected final def withSession[T](f: => T): T = {
    dal.db.withDynTransaction(f) // FIXME Should be withInTransaction
  }
}

abstract class DBAction[R] extends Action[R] with DBSession {
  override protected def invoke(): Response = {
    try {
      dynamicSession
      super.invoke()
    } catch {
      case ex: SlickException =>
        withSession(super.invoke)
    }
  }
}
package qizero.action

import qizero.persistence.{Session, DAL}
import scala.slick.SlickException

sealed trait HasSession {
  implicit val dal: DAL

  protected def withSession[T](f: => T): T

  implicit final def dynamicSession: Session = {
    dal.session.dynamicSession
  }

}

sealed trait DBAction extends Invoker with HasSession {
  self: Action[_] =>

  abstract override protected def invoke(): Result = {
    try {
      dynamicSession
      super.invoke()
    } catch {
      case ex: SlickException =>
        withSession(super.invoke)
    }
  }
}

trait DBSession extends DBAction {
  self: Action[_] =>

  protected final def withSession[T](f: => T): T = {
    dal.db.withDynSession(f) // FIXME Should be withInSession
  }
}

trait DBTransaction extends DBAction {
  self: Action[_] =>

  protected final def withSession[T](f: => T): T = {
    dal.db.withDynTransaction(f) // FIXME Should be withInTransaction
  }
}
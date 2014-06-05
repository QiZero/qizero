package qizero.action

import qizero.persistence.{Session, DAL}
import scala.util.{Success, Failure, Try}

sealed trait DBAction extends Invoker {
  self: Action[_] =>

  val dal: DAL

  implicit protected final def dynamicSession: Session = {
    dal.session.dynamicSession
  }

  protected def withSession[T](f: => T): T

// FIXME if use withIn then can remove the try to get dynamicSession
//  abstract override protected def invoke(): Result = {
//    withSession(super.invoke())
//  }
}

trait DBSession extends DBAction {
  self: Action[_] =>

  protected final def withSession[T](f: => T): T = {
    dal.db.withDynSession(f) // FIXME Should be withInSession
  }

  abstract override protected def invoke(): Result = {
    Try(dynamicSession) match {
      case Success(s) => super.invoke()
      case Failure(ex) => withSession(super.invoke())
    }
  }
}

trait DBTransaction extends DBAction {
  self: Action[_] =>

  protected final def withSession[T](f: => T): T = {
    dal.db.withDynTransaction(f) // FIXME Should be withInTransaction
  }

  abstract override protected def invoke(): Result = {
    Try(dynamicSession) match {
      case Success(s) => s.withTransaction(super.invoke())
      case Failure(ex) => withSession(super.invoke())
    }
  }
}
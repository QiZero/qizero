package qizero.action

import scala.util.{Failure, Success, Try}
import qizero.persistence.{DAL, Session}

sealed trait DBInvoker extends Invoker {
  self: Action[_] =>

  val dal: DAL

  implicit protected final def dynamicSession: Session = {
    dal.session.dynamicSession
  }

  protected def withSession[T](f: => T): T

  // FIXME try to use withIn then can remove the try to get dynamicSession
  //  abstract override protected def invoke(): Result = {
  //    withSession(super.invoke())
  //  }
}

trait DBSession extends DBInvoker {
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

trait DBTransaction extends DBInvoker {
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
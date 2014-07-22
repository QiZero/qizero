package qizero.persistence

import org.scalatest.{Suite, SuiteMixin}

trait DBSpec extends SuiteMixin {
  this: Suite =>

  val dal: DAL

  import dal.profile.simple._

  implicit protected final def dynamicSession = {
    dal.session.dynamicSession
  }

  def ddl: dal.profile.DDL

  protected def withDBFixture() = {
    ddl.create
  }

  abstract override def withFixture(test: NoArgTest) = {
    dal.db.withDynTransaction {
      withDBFixture()
      val outcome = super.withFixture(test)
      dynamicSession.rollback()
      outcome
    }
  }
}

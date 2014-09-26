package qizero.persistence

import scala.util.Try
import org.scalatest.{Suite, SuiteMixin}

trait DBSpec extends SuiteMixin {
  self: Suite =>

  val dal: DAL

  implicit protected final def dynamicSession = {
    dal.session.dynamicSession
  }

  def ddl: dal.profile.DDL

  protected def withDBFixture() = {
    import dal.profile.simple._
    Try(ddl.create)
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

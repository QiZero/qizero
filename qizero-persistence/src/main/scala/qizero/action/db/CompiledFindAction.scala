package qizero.action.db

import qizero.action.{DBSession, Action}
import qizero.domain.{Page, Pagination}
import qizero.persistence.DAL
import scala.language.existentials
import scala.language.higherKinds
import slick.lifted.{RunnableCompiled, Query}

final case class CompiledFind[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                      (implicit val dal: DAL)
  extends Action[R] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = compiled.selectStatement

  protected def act(): R = compiled.first
}

final case class CompiledFindOne[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                         (implicit val dal: DAL)
  extends Action[Option[R]] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = compiled.selectStatement

  protected def act(): Option[R] = compiled.firstOption
}

final case class CompiledFindAll[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                         (implicit val dal: DAL)
  extends Action[Seq[R]] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = compiled.selectStatement

  protected def act(): Seq[R] = compiled.list
}

final case class CompiledFindPage[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]], pagination: Pagination)
                                          (implicit val dal: DAL)
  extends Action[Page[R]] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = compiled.selectStatement

  protected def act(): Page[R] = {
    val query = compiled.extract
    val queryPage = query.drop(pagination.offset).take(pagination.pageSize)
    val result = queryPage.list
    val total = query.length.run
    Page(result, pagination, total)
  }
}

final case class CompiledFindCount[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                           (implicit val dal: DAL)
  extends Action[Int] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = compiled.selectStatement

  protected def act(): Int = {
    val query = compiled.extract
    query.length.run
  }
}

final case class CompiledFindExist[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                           (implicit val dal: DAL)
  extends Action[Boolean] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = compiled.selectStatement

  protected def act(): Boolean = {
    val query = compiled.extract.exists
    query.run
  }
}
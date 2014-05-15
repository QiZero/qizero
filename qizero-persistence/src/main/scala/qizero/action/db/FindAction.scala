package qizero.action.db

import qizero.action.{DBSession, Action}
import qizero.model.{Page, Pagination}
import qizero.persistence.DAL
import scala.slick.lifted.Query

final case class Find[R](query: Query[_, R, Seq])
                        (implicit val dal: DAL)
  extends Action[R] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = query.selectStatement

  protected def act(): R = query.firstOption.get
}

final case class FindOne[R](query: Query[_, R, Seq])
                           (implicit val dal: DAL)
  extends Action[Option[R]] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = query.selectStatement

  protected def act(): Option[R] = query.firstOption
}

final case class FindAll[R](query: Query[_, R, Seq])
                           (implicit val dal: DAL)
  extends Action[Seq[R]] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = query.selectStatement

  protected def act(): Seq[R] = query.list
}

final case class FindPage[R](query: Query[_, R, Seq], pagination: Pagination)
                            (implicit val dal: DAL)
  extends Action[Page[R]] with DBSession {

  import dal.profile.simple._

  lazy val statement: String = query.selectStatement

  protected def act(): Page[R] = {
    val queryPage = query.drop(pagination.offset).take(pagination.pageSize)
    val result = queryPage.list
    val total = query.length.run
    Page(result, pagination, total)
  }
}

final case class FindCount[R](private val q: Query[_, R, Seq])
                             (implicit val dal: DAL)
  extends Action[Int] with DBSession {

  import dal.profile.simple._

  lazy val query = q.length

  lazy val statement: String = query.selectStatement

  protected def act(): Int = query.run
}

final case class FindExist[R](private val q: Query[_, R, Seq])
                             (implicit val dal: DAL)
  extends Action[Boolean] with DBSession {

  import dal.profile.simple._

  lazy val query = q.exists

  lazy val statement: String = query.selectStatement

  protected def act(): Boolean = query.run
}
package qizero.action.db

import qizero.action.DBAction
import qizero.model.{Page, Pagination}
import qizero.persistence.DAL
import scala.slick.lifted.Query

final case class Find[R](query: Query[_, R, Seq])
                        (implicit val dal: DAL)
  extends DBAction[R] {

  protected def act(): R = {
    import dal.profile.simple._
    query.firstOption.get
  }
}

final case class FindOne[R](query: Query[_, R, Seq])
                           (implicit val dal: DAL)
  extends DBAction[Option[R]] {

  protected def act(): Option[R] = {
    import dal.profile.simple._
    query.firstOption
  }
}

final case class FindAll[R](query: Query[_, R, Seq])
                           (implicit val dal: DAL)
  extends DBAction[Seq[R]] {

  protected def act(): Seq[R] = {
    import dal.profile.simple._
    query.list
  }
}

final case class FindPage[R](query: Query[_, R, Seq], pagination: Pagination)
                            (implicit val dal: DAL)
  extends DBAction[Page[R]] {

  protected def act(): Page[R] = {
    import dal.profile.simple._
    val queryPage = query.drop(pagination.offset).take(pagination.pageSize)
    val result = queryPage.list
    val total = query.length.run
    Page(result, pagination, total)
  }
}

final case class FindCount[R](query: Query[_, R, Seq])
                             (implicit val dal: DAL)
  extends DBAction[Int] {

  protected def act(): Int = {
    import dal.profile.simple._
    val countQuery = query.length
    countQuery.run
  }
}

final case class FindExist[R](query: Query[_, R, Seq])
                             (implicit val dal: DAL)
  extends DBAction[Boolean] {

  protected def act(): Boolean = {
    import dal.profile.simple._
    val existQuery = query.exists
    existQuery.run
  }
}
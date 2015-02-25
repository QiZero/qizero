package qizero.action.db

import scala.language.higherKinds
import scala.slick.lifted.{Query, RunnableCompiled}
import qizero.model._
import qizero.persistence.DAL

sealed trait CompiledFindAction[R, C[_]] extends CompiledAction[R, C] {
  self: DBAction[_] =>

  lazy val statement: String = {
    import dal.profile.simple._
    compiled.selectStatement
  }
}

final class CompiledFindFirst[R, C[_]](val compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                      (implicit val dal: DAL)
  extends DBAction[R] with CompiledFindAction[R, C] {

  import dal.profile.simple._

  protected def act(): R = compiled.first

}

final class CompiledFindOne[R, C[_]](val compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                    (implicit val dal: DAL)
  extends DBAction[Option[R]] with CompiledFindAction[R, C] {

  import dal.profile.simple._

  protected def act(): Option[R] = compiled.firstOption
}

final class CompiledFindAll[R, C[_]](val compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                    (implicit val dal: DAL)
  extends DBAction[List[R]] with CompiledFindAction[R, C] {

  import dal.profile.simple._

  protected def act(): List[R] = compiled.list
}

final class CompiledFindPage[R, C[_]](val compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]], val pagination: Pagination)
                                     (implicit val dal: DAL)
  extends DBAction[Page[R]] with CompiledFindAction[R, C] {

  import dal.profile.simple._

  protected def act(): Page[R] = {
    val query = compiled.extract
    val queryPage = query.drop(pagination.offset).take(pagination.size)
    val result = queryPage.list
    val total = query.length.run
    new Page(result, pagination, total)
  }
}

final class CompiledFindSlice[R, C[_]](val compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]], val pagination: Pagination)
                                      (implicit val dal: DAL)
  extends DBAction[Slice[R]] with CompiledFindAction[R, C] {

  import dal.profile.simple._

  protected def act(): Slice[R] = {
    val query = compiled.extract
    val queryPage = query.drop(pagination.offset).take(pagination.size)
    val result = queryPage.list
    new Slice(result, pagination)
  }
}

final class CompiledFindCount[R, C[_]](val compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                      (implicit val dal: DAL)
  extends DBAction[Int] with CompiledFindAction[R, C] {

  import dal.profile.simple._

  protected def act(): Int = {
    val query = compiled.extract.length
    query.run
  }
}

final class CompiledFindExists[R, C[_]](val compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])
                                       (implicit val dal: DAL)
  extends DBAction[Boolean] with CompiledFindAction[R, C] {

  import dal.profile.simple._

  protected def act(): Boolean = {
    val query = compiled.extract.exists
    query.run
  }
}

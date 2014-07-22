package qizero.persistence.query

import qizero.action.db._
import qizero.domain.Pagination
import qizero.persistence.DAL
import scala.language.higherKinds
import slick.lifted.{RunnableCompiled, Query}

trait FindQuery {

  implicit final class FindAction[R](q: Query[_, R, Seq])(implicit dal: DAL) {
    def find = Find(q)
    def findOne = FindOne(q)
    def findAll = FindAll(q)
    def findPage(pagination: Pagination) = FindPage(q, pagination)
    def count = FindCount(q)
    def exist = FindExist(q)
  }

  implicit final class CompiledFindAction[R, C[_]](c: RunnableCompiled[_ <: Query[_, R, C], C[R]])(implicit dal:DAL) {
    def find = CompiledFind(c)
    def findOne = CompiledFindOne(c)
    def findAll = CompiledFindAll(c)
    def findPage(pagination: Pagination) = CompiledFindPage(c, pagination)
    def count = CompiledFindCount(c)
    def exist = CompiledFindExist(c)
  }
}

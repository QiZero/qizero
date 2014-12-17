package qizero.entity

import org.joda.time.DateTime
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

trait Entity

object Entity {
  def mapper[IN, OUT](f: IN => OUT) = Mapper(f)

  def mapper[IN, OUT]: Mapper[IN, OUT] = macro EntityMacros.materialize[IN, OUT]
}

private[entity] trait EntityWithId extends Entity {
  type ID

  def id: ID
}

trait Id[I] extends EntityWithId {
  _: Entity =>
  type ID = I

  def id: ID
}

trait CreatedAt {
  _: Entity =>
  def createdAt: DateTime
}

trait UpdatedAt {
  _: Entity =>
  def updatedAt: DateTime
}

trait Timestamp extends CreatedAt with UpdatedAt {
  _: Entity =>
}

private object EntityMacros {
  def materialize[IN: c.WeakTypeTag, OUT: c.WeakTypeTag](c: Context) = {
    import c.universe._

    def getExtractValue(tree: Tree) = tree match {
      case Literal(Constant(str: String)) => str
    }
    val inType = weakTypeOf[IN]
    val outType = weakTypeOf[OUT]

    def getParams() = {

      val inConstructor = inType.decl(termNames.CONSTRUCTOR) match {
        case NoSymbol => c.abort(c.enclosingPosition, "IN constructor not found")
        case s => s.asMethod
      }
      val outConstructor = outType.decl(termNames.CONSTRUCTOR) match {
        case NoSymbol => c.abort(c.enclosingPosition, "OUT constructor not found")
        case s => s.asMethod
      }

      val inParams = inConstructor.paramLists.head
      val outParams = outConstructor.paramLists.head

      outParams.flatMap { param =>
        val inMapperNamed = param.annotations.find(_.tree.tpe <:< c.weakTypeOf[InNamed]).map(_.tree.children.tail.head).map(getExtractValue)
        val paramName = param.name.toTermName
        val paramType = param.typeSignature
        val inName = inMapperNamed.map(TermName(_)).getOrElse(paramName)
        if (paramType <:< typeOf[Has[_]] || paramType <:< typeOf[Option[Has[_]]]) {
          val idTermName = TermName(inName.decodedName.toString + "Id")
          val inIdParamOpt = inParams.find(_.name == idTermName)
          inIdParamOpt match {
            case Some(idParam) =>
              val idParamName = idParam.name.toTermName
              if (paramType <:< typeOf[Option[Has[_]]]) {
                val entityType = paramType.typeArgs(0).typeArgs(0)
                Some(q"$paramName = in.$idParamName.map(id => new _root_.qizero.entity.HasId[$entityType](id))")
              } else {
                val entityType = paramType.typeArgs(0)
                Some(q"$paramName = new _root_.qizero.entity.HasId[$entityType](in.$idParamName)")
              }
            case None => c.abort(c.enclosingPosition, s"""Missing Param Id: $idTermName in (${inParams.mkString(",")})""")
          }
        } else {
          val inParamOpt = inParams.find(_.name == inName)
          inParamOpt match {
            case Some(inParam) =>
              val inParamName = inParam.name.toTermName
              val inParamType = inParam.typeSignature
              if (paramType =:= inParamType) Some(q"$paramName = in.$inParamName")
              else if (inParamType <:< typeOf[Option[_]] && inParamType.typeArgs(0) =:= paramType) Some(q"$paramName = in.$inParamName.get")
              else c.abort(c.enclosingPosition, "Param Type doesn't matched: " + paramName)
            case None if param.asTerm.isParamWithDefault => None
            case None => c.abort(c.enclosingPosition, s"""Missing Param: $inName in (${inParams.mkString(",")})""")
          }
        }
      }
    }

    val params = getParams()
    val result = q"""new _root_.qizero.entity.Mapper[$inType, $outType]{ def apply(in:$inType):$outType = new $outType(..$params) }"""
    result
  }
}
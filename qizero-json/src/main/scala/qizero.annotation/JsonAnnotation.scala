package qizero.annotation

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

class json extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro JsonMacro.impl
}

object JsonMacro {

  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def modifiedDeclaration(classDef: ClassDef, compDefOpt: Option[ModuleDef] = None) = {
      val (className, fields) = extractClassNameAndFields(classDef)
      val format = jsonFormatter(className, fields)
      val compDef = modifiedCompanion(compDefOpt, format, className)

      // Return both the class and companion object declarations
      c.Expr( q"""
        $classDef
        $compDef
      """)
    }

    def extractClassNameAndFields(classDef: ClassDef) = {
      try {
        val q"case class $className(..$fields) extends ..$bases { ..$body }" = classDef
        (className, fields)
      } catch {
        case _: MatchError => c.abort(c.enclosingPosition, "Annotation is only supported on case class")
      }
    }

    def jsonFormatter(className: TypeName, fields: List[ValDef]) = {
      val body = fields.length match {
        case 0 => c.abort(c.enclosingPosition, "Cannot create json formatter for case class with no fields")
        case 1 =>
          // Only one field, use the serializer for the field
          q"""{
              import play.api.libs.json._
              Format(
                __.read[${fields.head.tpt}].map(s => ${className.toTermName}(s)),
                new Writes[$className] { def writes(o: $className) = Json.toJson(o.${fields.head.name}) }
              )
            }"""
        case _ =>
          // More than one field, use Play's macro
          q"play.api.libs.json.Json.format[$className]"
      }
      q"implicit lazy val toJsonFormat = $body"
    }

    def modifiedCompanion(compDefOpt: Option[ModuleDef], format: ValDef, className: TypeName) = {
      compDefOpt map { compDef =>
        // Add the formatter to the existing companion object
        val q"object $obj extends ..$bases { ..$body }" = compDef
        q"""
          object $obj extends ..$bases {
            ..$body
            $format
          }
        """
      } getOrElse {
        // Create a companion object with the formatter
        q"object ${className.toTermName} { $format }"
      }
    }
    annottees.map(_.tree) match {
      case (classDef: ClassDef) :: Nil => modifiedDeclaration(classDef)
      case (classDef: ClassDef) :: (compDef: ModuleDef) :: Nil => modifiedDeclaration(classDef, Some(compDef))
      case _ => c.abort(c.enclosingPosition, "Invalid annottee")
    }
  }

}
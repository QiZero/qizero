package qizero.annotation

import org.scalatest._
import play.api.libs.json._

class JsonTest extends WordSpec with Matchers{

  @json case class City(name: String)

  @json case class Person(name: String, age: Int)

  "@json annotation" should {

    "create correct formatter for case class with 1 field" in {

      val city = City("San Francisco")
      val json = Json.toJson(city)
      json === JsString("San Francisco")
      Json.fromJson[City](json) === JsSuccess(city)
    }

    "create correct formatter for case class with >= 2 fields" in {

      val person = Person("Victor Hugo", 46)
      val json = Json.toJson(person)
      json === Json.obj(
        "name" -> "Victor Hugo",
        "age" -> 46
      )
      Json.fromJson[Person](json) === JsSuccess(person)
    }
  }

}

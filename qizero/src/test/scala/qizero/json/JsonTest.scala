package qizero.json

import org.scalatest.WordSpec

class Foo(name:String, num:Int)

case class Bar(name:String, num:Int)

class JsonTest extends WordSpec {

  "toJson" should {
  "create a formmatter" in {
    val foo = new Foo("a", 1)
//    Json.toFormat[Foo]
//    val json = play.api.libs.json.Json.toJson(foo)
  }
//    "create correct formatter for case class with 1 field" in {
//
//      val foo = Foo("San Francisco")
//      val json = Json.toJson(city)
//      json === JsString("San Francisco")
//      Json.fromJson[City](json) === JsSuccess(city)
//    }

//    "create correct formatter for case class with >= 2 fields" in {
//
//      val person = Person("Victor Hugo", 46)
//      val json = Json.toJson(person)
//      json === Json.obj(
//        "name" -> "Victor Hugo",
//        "age" -> 46
//      )
//      Json.fromJson[Person](json) === JsSuccess(person)
//    }
  }

}
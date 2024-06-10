package models

import play.api.libs.json.{Json, OFormat}

case class User(id: Int, name: String, password: String, age: Option[Int])

object User {
    implicit val userFormat: OFormat[User] = Json.format[User]
}

package models

import play.api.libs.json.{Json, OFormat}

case class Task(id: Int, name: String)

object Task {
    implicit val taskFormat: OFormat[Task] = Json.format[Task]
}

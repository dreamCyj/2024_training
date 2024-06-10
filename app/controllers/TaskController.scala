package controllers

import models.Task
import play.api.db._
import play.api.mvc._
import play.db.NamedDatabase
import services.UserService

import javax.inject.{Inject, Singleton}
import scala.collection.mutable.ListBuffer

@Singleton
class TaskController @Inject() (@NamedDatabase("default") db: Database, userService: UserService) extends Controller {

    def getTasks(id: Int): Action[AnyContent] = Action { implicit request =>
      val taskList = ListBuffer[Task]()
        db.withConnection { conn =>
            val sql = "select * from task where user_id = ?"
            val preparedStatement = conn.prepareStatement(sql)
            preparedStatement.setInt(1, id) //第1个参数是1
            val result = preparedStatement.executeQuery()
            while (result.next()) {
                val taskId = result.getInt("id")
                val taskName = result.getString("name")
                taskList += Task(taskId, taskName)
            }
            conn.close()
        }
        val Some(username) = userService.getName(id)
        Ok(views.html.task(username, taskList))
    }

    def delTask(id: Int): Action[AnyContent] = Action { implicit request =>
        db.withConnection { conn =>
            val sql = "delete from task where id = ?"
            val preparedStatement = conn.prepareStatement(sql)
            preparedStatement.setInt(1, id) //第1个参数是1
            preparedStatement.executeUpdate()
            conn.close()
        }
        Redirect(routes.TaskController.getTasks(1))
    }


}

package controllers

import play.api.db._
import play.api.mvc._
import play.db.NamedDatabase
import services.{TaskService, UserService}

import javax.inject.{Inject, Singleton}

@Singleton
class TaskController @Inject()
    (@NamedDatabase("default") db: Database, userService: UserService, taskService: TaskService) extends Controller {

    def getTasks(id: Int): Action[AnyContent] = Action { implicit request =>
      val taskList = taskService.getTasks(id)
      val Some(username) = userService.getName(id)
      Ok(views.html.task(username, taskList))
    }

    def delTask(id: Int): Action[AnyContent] = Action { implicit request =>
        taskService.delTask(id)
        val userId = request.session.get("userId").get
        Redirect(routes.TaskController.getTasks(userId.toInt))
    }

    def addTask(): Action[AnyContent] = Action { implicit request =>
        val values = request.body.asFormUrlEncoded
        val userId = request.session.get("userId").get
        values.foreach { data =>
            val name = data("newTask").head
            taskService.addTask(name, userId.toInt)
        }
        Redirect(routes.TaskController.getTasks(userId.toInt))
    }

}

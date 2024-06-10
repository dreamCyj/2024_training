package controllers

import models.User
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.db._
import play.api.libs.json.{Json, __}
import play.api.mvc._
import services.UserService

import javax.inject._
import scala.collection.mutable.ArrayBuffer
case class LoginData(username: String, password: String)

@Singleton
class HomeController @Inject() (@NamedDatabase("default") db: Database, userService: UserService) extends Controller {
  val loginForm: Form[LoginData] = Form(mapping(
    //用户名在3到10个字符
    "username" -> text(3, 10),
    "password" -> text(5)
  )(LoginData.apply)(LoginData.unapply))

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  def login(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def register(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.register(loginForm))
  }
  def dbTest: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val users = ArrayBuffer[User]()
    db.withConnection { conn =>
      //Statement对象用于执行静态SQL查询，通常用于执行不带参数的简单SQL语句
      val state = conn.createStatement()
      val result = state.executeQuery("select * from user")
      while (result.next()) {
        val userId = result.getInt("id")
        val name = result.getString("name")  //等价于result.getString(1) 表中第一列
        val age = result.getInt("age")
        users += User(userId, name,"***",Some(age))
      }
    }
    //删除password字段
    //todo 应该自定义序列化器
    val usersJson = Json.toJson(users.map { user =>
      Json.toJson(user).transform((__ \ Symbol("password")).json.prune).get
    })
    Ok(views.html.dbTest(usersJson))
  }

  //新增用户
  def createUser: Action[AnyContent] = Action { implicit request =>
    val vals = request.body.asFormUrlEncoded
    //vals = Some(ListMap(username -> List(1), password -> List(2)))
    vals.map { data =>
      //data = ListMap(username -> List(1), password -> List(2))
      //data("username")是个size=1的列表
      val username = data("username").head
      val password = data("password").head
      db.withConnection { conn =>
        val sql = "INSERT INTO user (name, password) VALUES (?, ?)"
        val preparedStatement = conn.prepareStatement(sql)
        try {
          preparedStatement.setString(1, username)
          preparedStatement.setString(2, password)
          preparedStatement.executeUpdate()
        } finally {
          preparedStatement.close()
        }
      }
      Redirect(routes.HomeController.dbTest()).withSession("username" -> username)
    }.getOrElse(Redirect(routes.HomeController.login()))
  }

  def validateLogin: Action[AnyContent] = Action { implicit request =>
    val vals = request.body.asFormUrlEncoded
    vals.map { data =>
      val username = data("username").head
      val password = data("password").head
      userService.validateUser(username, password) match {
        case Some(user: User) => Redirect(routes.TaskController.getTasks(user.id)).withSession("username" -> user.name)
        case None => Redirect(routes.HomeController.login()).flashing("error" -> "用户名或密码错误")
      }
    }.getOrElse(Redirect(routes.HomeController.login()))

  }
  def validateLoginForm: Action[AnyContent] = Action { implicit request =>
    //在Action中验证表单
    loginForm.bindFromRequest.fold(
      formWithError => BadRequest(views.html.login(formWithError)),
      loginData =>
        Redirect(routes.HomeController.dbTest()).withSession("username" -> loginData.username)
    )
  }

  def logout: Action[AnyContent] = Action {
    Redirect(routes.HomeController.login()).withNewSession
  }

}

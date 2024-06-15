package controllers

import actions.RateLimitedAction
import actors.SquareRootActor
import akka.actor.{ActorSystem, Props}
import akka.pattern.{AskTimeoutException, ask}
import akka.util.Timeout
import play.api._
import play.api.mvc._
import javax.inject._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Singleton
class HomeController @Inject() (config: Configuration, actorSystem: ActorSystem) (implicit ec: ExecutionContext) extends Controller {

    implicit val timeout: Timeout = Timeout(5.seconds)
    private val squareRootActor = actorSystem.actorOf(Props[SquareRootActor], "squareRootActor")
    private val rateLimitedAction = new RateLimitedAction(config)

    def lowLevel(num: Int): Action[AnyContent] = rateLimitedAction.async {
      (squareRootActor ? SquareRootActor.lowLevel(num)).mapTo[Double].map { result =>
          Ok(f"$result%.2f")
      }.recover {
          case _: AskTimeoutException => RequestTimeout("请求超时!!!!")
      }
    }
    def highLevel(num: Int): Action[AnyContent] = rateLimitedAction.async {

      (squareRootActor ? SquareRootActor.highLevel(num)).mapTo[Double].map { result =>
          Ok(f"$result%.2f")
      }.recover {
          case _: AskTimeoutException => RequestTimeout("请求超时!!!!")
      }
    }

    def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index())
    }

}

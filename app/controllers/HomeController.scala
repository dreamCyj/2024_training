package controllers

import actions.RateLimitedAction
import actors.RequestMessage.{FailureMessage, SuccessMessage}
import actors.SquareRootActor
import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import play.api._
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Singleton
class HomeController @Inject() (config: Configuration, actorSystem: ActorSystem) extends Controller {

    implicit private val executionContext: ExecutionContext = actorSystem.dispatchers.lookup("my-thread-pool")
    implicit val timeout: Timeout = Timeout(5.seconds)
    private val squareRootActor = actorSystem.actorOf(Props[SquareRootActor], "squareRootActor")
    private val rateLimitedAction = new RateLimitedAction(config)

    def lowLevel(num: Int): Action[AnyContent] = rateLimitedAction.async {
      (squareRootActor ? SquareRootActor.lowLevel(num)).map {
          case success: SuccessMessage => Ok(f"${success.result}%.2f")
          case failure: FailureMessage => Ok(failure.reason)
      }
    }
    def highLevel(num: Int): Action[AnyContent] = rateLimitedAction.async {
      (squareRootActor ? SquareRootActor.highLevel(num)).map {
          case success: SuccessMessage => Ok(f"${success.result}%.2f")
          case failure: FailureMessage => Ok(failure.reason)
      }
    }

    def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index())
    }

}

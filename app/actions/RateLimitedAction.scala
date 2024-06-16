package actions

import controllers.Assets.TooManyRequests
import play.api.Configuration
import play.api.mvc.{ActionBuilder, Request, Result}

import java.util.concurrent.atomic.AtomicLong
import scala.concurrent.{ExecutionContext, Future}
class RateLimitedAction(config: Configuration) (implicit ex: ExecutionContext) extends ActionBuilder[Request] {
    private val rateLimit: Int = config.getInt("api.rateLimit").getOrElse(10) // 默认限流值为 10
    private val requestCount = RateLimitedAction.requestCount
    def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
        if (requestCount.incrementAndGet() > rateLimit) {
            Future.successful(TooManyRequests("限流"))
        } else {
            block(request).map { request =>
                requestCount.decrementAndGet()
                request
            }
        }
    }
}

object RateLimitedAction {
    private val requestCount = new AtomicLong(0)
}


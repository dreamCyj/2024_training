package actors

import actors.RequestMessage.{FailureMessage, SuccessMessage}
import actors.SquareRootActor.{highLevel, lowLevel}
import akka.actor.{Actor, ActorRef, ReceiveTimeout}
import play.api.Logger

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.math.sqrt
import scala.util.Random

class SquareRootActor extends Actor{

    private val random = new Random()
    import context.dispatcher

    //设置超时时间 超过这个时间将会触发ReceiveTimeout消息
    context.setReceiveTimeout(5.minutes)
    override def receive: Receive = {
        case lowLevel(num: Int) =>

            val sender = super.sender
            val ec: ExecutionContext = context.system.dispatchers.lookup("my-thread-pool")
            val delay = random.nextInt(251)
            ec.execute(new Runnable {
                override def run(): Unit = {
                    Thread.sleep(delay)
                    //Logger.info(delay.toString)
                    send(delay, sender, num)
                }
            })

        case highLevel(num: Int) =>
            /**
             * super就是SquareRootActor super.sender就找到了发送方(谁发送消息给SquareRootActor)
             * 异步执行时(别的线程) 当前的sender已经不是当时发送消息的那个了 因此不能直接sender ! sqrt(num.toDouble)
             * 不然会找不到正确的sender 消息会交给死信邮箱
             */
            val sender = super.sender
            val delay = random.nextInt(251)
            //定时任务模拟高延迟
            context.system.scheduler.scheduleOnce(delay.millis) {
                //Logger.info(delay.toString)
                send(delay, sender, num)
            }
        //自定义超时逻辑 超时后结束 不再重试
        case ReceiveTimeout =>
            Logger.info(s"超时未收到消息，自动退出")
            context.stop(self)
    }

    //具体实现逻辑
    private def send(delay: Int, sender: ActorRef, num: Int): Unit = {
        if(delay < 100) {
            Logger.error("实现错误")
            sender ! FailureMessage("实现错误")
        } else if(delay <= 200) {
            sender ! SuccessMessage(sqrt(num.toDouble))
        } else {
            Logger.error("超时")
            sender ! FailureMessage("超时")
        }
    }
}

object SquareRootActor {
    //低级实现
    case class lowLevel(num: Int)
    //高级实现
    case class highLevel(num: Int)
}

object RequestMessage {
    //请求成功
    case class SuccessMessage(result: Double)
    //请求失败 实现失败/请求超时
    case class FailureMessage(reason: String)
}


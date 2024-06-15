package actors

import actors.SquareRootActor.{highLevel, lowLevel}
import akka.actor.{Actor, ReceiveTimeout}
import play.api.Logger

import scala.concurrent.duration.DurationInt
import scala.math.sqrt
import scala.util.Random

class SquareRootActor extends Actor{

    private val random = new Random()
    import context.dispatcher

    //设置超时时间 超过这个时间将会触发ReceiveTimeout消息
    context.setReceiveTimeout(1.minutes)
    override def receive: Receive = {
        case lowLevel(num: Int) =>
            val sender = super.sender
            val dispatcher = context.system.dispatchers.lookup("my-thread-pool")
            val delay = 100 + random.nextInt(151)
            dispatcher.execute(new Runnable {
                override def run(): Unit = {
                    Thread.sleep(delay)
                    //Logger.info(delay.toString)
                    if(delay > 200)
                        Logger.error("请求超时")
                    else
                        sender ! sqrt(num.toDouble)
                }
            })

        case highLevel(num: Int) =>
            /**
             * super就是SquareRootActor super.sender就找到了发送方(谁发送消息给SquareRootActor)
             * 异步执行时(别的线程) 当前的sender已经不是当时发送消息的那个了 因此不能直接sender ! sqrt(num.toDouble)
             * 不然会找不到正确的sender 消息会交给死信邮箱 共重试三次
             */
            val sender = super.sender
            val delay = 100 + random.nextInt(151)
            //定时任务模拟高延迟
            context.system.scheduler.scheduleOnce(delay.millis) {
                //Logger.info(delay.toString)
                if(delay > 200)
                    Logger.error("请求超时")
                else
                    sender ! sqrt(num.toDouble)
                //直接sender ! sqrt(num.toDouble)出现错误:
                //Actor[akka://application/deadLetters] sender死信 所以重试直到三次
            }
        //自定义超时逻辑 超时后结束 不再重试
        case ReceiveTimeout =>
            Logger.info(s"超时未收到消息，自动退出")
            context.stop(self)
    }
}

object SquareRootActor {
    case class lowLevel(num: Int)
    case class highLevel(num: Int)
}

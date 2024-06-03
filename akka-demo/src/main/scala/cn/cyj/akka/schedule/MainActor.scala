package cn.cyj.akka.schedule

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory


object MainActor {

    object ReceiverActor extends Actor {
        override def receive: Receive = {
            case x => println(x)
        }
    }

    def main(args: Array[String]): Unit = {

        val actorSystem = ActorSystem("actorSystem", ConfigFactory.load())
        val receiverActor = actorSystem.actorOf(Props(ReceiverActor), "receiverActor")
        //定时器
        import actorSystem.dispatcher

        import scala.concurrent.duration._

        //参数列表 启动延时 发送间隔 发送对象 消息
        //方式1 采用发送消息的形式
        actorSystem.scheduler.schedule(3.seconds, 2.seconds, receiverActor, "hhh")
        //方式2 采用自定义发送消息 结合函数实现
        actorSystem.scheduler.schedule(3.seconds, 2.seconds)(receiverActor ! "hhh")
        //实际开发中的写法
        actorSystem.scheduler.schedule(3.seconds, 2.seconds){
            receiverActor ! "hhh"
        }
    }


}

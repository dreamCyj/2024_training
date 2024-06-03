package cn.cyj.akka.demo

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Entrance {
    def main(args: Array[String]): Unit = {
        //1.创建自定义Actor Sender和Receiver
        //2.创建ActorSystem
        val actorSystem = ActorSystem("actorSystem", ConfigFactory.load())
        //3.通过ActorSystem 加载自定义的Actor
        val senderActor = actorSystem.actorOf(Props(SenderActor), "senderActor")
        val receiverActor = actorSystem.actorOf(Props(ReceiverActor), "receiverActor")

        senderActor ! "start"
    }

}

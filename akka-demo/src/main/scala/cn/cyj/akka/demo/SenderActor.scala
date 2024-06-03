package cn.cyj.akka.demo

import akka.actor.Actor

/**
 * 自定义发送消息的Actor
 */
object SenderActor extends Actor {
    override def receive: Receive = {
        case "start" =>
            println("SenderActor接收到消息：start")
            //获取receiverActor
            //路径： "akka://actorSystem的名字/user/Actor的名字"
            val receiverActor = context.actorSelection("akka://actorSystem/user/receiverActor")
            receiverActor ! SubmitTaskMessage("hello, 我是SenderActor")
        case SuccessSubmitTaskMessage(msg) =>
            println(s"SenderActor接收到回执消息：$msg")
    }
}

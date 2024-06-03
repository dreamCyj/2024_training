package cn.cyj.akka.demo

import akka.actor.Actor

/**
 * 自定义接收消息的Actor
 */
object ReceiverActor extends Actor {
    override def receive: Receive = {
        case SubmitTaskMessage(msg) =>
            println(s"我是ReceiverActor, 我接收到消息: $msg")
            sender ! SuccessSubmitTaskMessage("我是ReceiverActor, 接收成功")
    }
}

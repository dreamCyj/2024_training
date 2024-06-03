package cn.cyj.actor

import scala.actors.Actor

object ActorDemo4 {

    case class Message(id: Int, message: String)  //自定义的发送消息
    case class ReplyMessage(message: String, name: String) //自定义的接收消息

    object MessageActor extends Actor {
        override def act(): Unit = {
            loop {
                react {
                    case Message(id, message) => {
                        println(s"我是MessageActor,我接收到的消息是$id, $message")
                        //回复消息
                        sender ! ReplyMessage("MessageActor已收到", "cyj")

                    }
                }
            }
        }
    }
    //main方法的底层也是一个Actor
    def main(args: Array[String]): Unit = {
        MessageActor.start()
        //! 异步无返回 !? 同步有返回 !! 异步有返回
        //同步时 如果没有收到回复信息 不会继续往下执行
        val resp = MessageActor !? Message(1, "hello MessageActor 我是MainActor")
        val replyMessage: ReplyMessage = resp.asInstanceOf[ReplyMessage]
        println(s"我是MainActor, 我接收到的回复是${replyMessage.message}, ${replyMessage.name}")
        //异步
        val futureResp = MessageActor !! Message(1, "hello MessageActor 我是MainActor")
        //isSet检查是否收到回复信息
        while (!futureResp.isSet){}
        val futureReplyMessage: ReplyMessage = futureResp.apply().asInstanceOf[ReplyMessage]
        println(s"我是MainActor, 我接收到的回复是${futureReplyMessage.message}, ${futureReplyMessage.name}")
    }
}

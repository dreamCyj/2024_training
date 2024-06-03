package cn.cyj.actor

import scala.actors.Actor

object ActorDemo2 {

    object ActorSender extends Actor {
        override def act(): Unit = {
            //发送消息给ActorReceiver
            //! 异步无返回 !? 同步有返回 !! 异步有返回
            ActorReceiver ! "hello, ActorReceiver"
        }
    }

    object ActorReceiver extends Actor {
        override def act(): Unit = {
            //receive只能执行一次
            receive {
                case x: String => println(x)
            }
        }
    }

    def main(args: Array[String]): Unit = {
        ActorSender.start()
        ActorReceiver.start()
    }
}

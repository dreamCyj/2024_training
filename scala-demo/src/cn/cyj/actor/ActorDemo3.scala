package cn.cyj.actor

import java.util.concurrent.TimeUnit
import scala.actors.Actor

object ActorDemo3 {

    object ActorSender extends Actor {
        override def act(): Unit = {
            while (true){
                ActorReceiver ! "hello, ActorReceiver"
                TimeUnit.SECONDS.sleep(3)
            }
        }
    }

    object ActorReceiver extends Actor {
        override def act(): Unit = {
/*            //方式1 bad
            while (true) {
                receive {
                    case x: String => println(x)
                }
            }*/
            //方式2
            loop {
                react {
                    case x: String => println(x)
                }
            }
        }
    }

    def main(args: Array[String]): Unit = {
        ActorSender.start()
        ActorReceiver.start()
    }
}

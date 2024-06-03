package cn.cyj.actor

import scala.actors.Actor

object ActorDemo1 {

    //object Actor1 extends Actor
    //Actor1.start()
    class Actor1 extends Actor {
        override def act(): Unit = {
            println("actor1..." + "hello")
        }
    }

    class Actor2 extends Actor {
        override def act(): Unit = {
            println("actor2..." + "hello")
        }
    }

    def main(args: Array[String]): Unit = {

        var a1 = new Actor1
        a1.start()


        new Actor2().start()
    }
}

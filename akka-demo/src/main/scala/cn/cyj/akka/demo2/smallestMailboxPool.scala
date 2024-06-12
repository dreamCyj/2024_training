package cn.cyj.akka.demo2

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.BroadcastPool

class mailboxActor extends Actor {
    override def receive: Receive = {
        case msg: String => println(s"I am ${self.path.name}, message is: $msg")
        case _ => println("Invalid message!")
    }
}

object smallestMailboxPool extends App {
    val actorSystem = ActorSystem("Route")
    val router = actorSystem.actorOf(BroadcastPool(5).props(Props[mailboxActor]))
    for (i <- 1 to 10) {
        router ! s"hello $i"
    }
}

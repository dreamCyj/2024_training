package cn.cyj.akka.demo2

import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props}

import scala.concurrent.duration._
import scala.language.postfixOps

class Printer extends Actor {
    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
        println("I am starting!")
    }
    override def receive: Receive = {
        case msg: String => println(msg)
        case msg: Int => 1/0
    }
}

class IntAdder extends Actor {
    var sum = 0
    override def receive: Receive = {
        case msg: Int => sum = sum + msg
            println(sum)
        case msg: String => throw new IllegalArgumentException()
    }

    override def postStop: Unit = {
        println("I am stopping!")
    }
}
class SupervisorStrategy extends Actor {
    override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
        case _: ArithmeticException => Restart
        case _: IllegalArgumentException => Stop
        case _: NullPointerException => Resume
        case _: Exception => Escalate
    }

    val printer = context.actorOf(Props[Printer], "printer")
    val intAdder = context.actorOf(Props[IntAdder], "intAdder")

    override def receive: Receive = {
        case "start" =>
            printer ! "hello"
            printer ! 10
            intAdder ! 10
            intAdder ! 10
            intAdder ! "hello"
    }
}

object supervisor extends App {
    val actorSystem = ActorSystem("supervisor")
    val sup = actorSystem.actorOf(Props[SupervisorStrategy])
    sup ! "start"
}

package cn.cyj.akka.demo2

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object ActorSetup extends App {

    implicit val timeout = Timeout(10 seconds)
    val actorSystem = ActorSystem("actorSystem")
    val addActor: ActorRef = actorSystem.actorOf(Props[AddActor], "addActor")

/*    addActor ! 5
    addActor ! 4*/

    val future = (addActor ? 5).mapTo[Int]
    val sum = Await.result(future, 10 seconds)
    println(sum)

}

class AddActor extends Actor {
    var sum = 0
    override def receive: Receive = {
        case x: Int => sum = sum + x
        //println(s"current sum is : $sum")
        println(sender())
        sender ! sum


        case _ => println("bad data")
    }
}

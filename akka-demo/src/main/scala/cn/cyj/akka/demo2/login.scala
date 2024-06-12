package cn.cyj.akka.demo2

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}

class loginActor extends Actor{
    override def receive: Receive = {
        case x: String =>
            if (x == "cyj") {
                //change state
                context.become(isAuth)
            }
    }

    def isAuth: Receive = {
        case x: String =>
            if(x == "username") {
                println("cyj")
            }
            if(x == "logout") {
                println("logout successful")
                context.become(notAuth)
                //shutdown actor
                //context.stop(self)
            }
    }

    def notAuth: Receive = {

        case x: String =>
            if (x == "cyj") {
                context.become(isAuth)
            }
    }
}

object login extends App {
    val actorSystem = ActorSystem("actorSystem")
    val loginActor: ActorRef = actorSystem.actorOf(Props[loginActor], "addActor")
    loginActor ! "cyj"
    loginActor ! "username"
    loginActor ! "logout"
    //shutdown actor
    loginActor ! PoisonPill
    loginActor ! "username"

}

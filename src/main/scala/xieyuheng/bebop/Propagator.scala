package xieyuheng.bebop

import akka.actor.ActorRef

case class Propagator(actor: ActorRef) {
  def updateArg[E](c: E, n: Double): Unit = {
    actor ! (c, n)
  }
}

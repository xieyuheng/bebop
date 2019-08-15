package xieyuheng.bebop.ed

import akka.actor.ActorRef

case class Propagator(actor: ActorRef) {
  def updateArg[E](c: E, n: Int): Unit = {
    actor ! (c, n)
  }
}

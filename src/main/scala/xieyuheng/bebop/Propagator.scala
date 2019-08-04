package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

import java.util.UUID

case class Propagator(actor: ActorRef) {
  def updateArg[E](c:E, n: Int) {
    actor ! (c, n)
  }
}

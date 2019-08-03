package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

class Tran2[-A1, -A2, +R]
  (implicit
    arg1Lattice: JoinSemilattice[A1],
    arg2Lattice: JoinSemilattice[A2],
    retLattice: JoinSemilattice[R],
    system: ActorSystem) {

  // TODO
}

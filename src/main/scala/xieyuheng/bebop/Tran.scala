package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

class Tran1[A1, R]
  (implicit
    arg1Lattice: JoinSemilattice[A1],
    retLattice: JoinSemilattice[R],
    system: ActorSystem) {

  private object Tran1Actor {
    def props = Props(new Tran1Actor)
  }

  private object Msg {
    case class Connect(arg1: Cell[A1], ret: Cell[R])
  }

  private class Tran1Actor extends Actor {

    private def fun: Option[Function1[A1, R]] = None

    val log = Logging(context.system, this)

    def receive = {
      case Msg.Connect(arg1, ret) =>
        ???
    }
  }

  // private val actor = system.actorOf(Tran1Actor.props)
}

class Tran2[-A1, -A2, +R]
  (implicit
    arg1Lattice: JoinSemilattice[A1],
    arg2Lattice: JoinSemilattice[A2],
    retLattice: JoinSemilattice[R],
    system: ActorSystem) {


}

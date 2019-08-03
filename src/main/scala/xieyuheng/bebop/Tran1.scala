package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

class Tran1[A1, R]
  (fun: PartialFunction[A1, R])
  (implicit
    arg1Lattice: JoinSemilattice[A1],
    retLattice: JoinSemilattice[R],
    system: ActorSystem) {

  private object Tran1Actor {
    def props(arg1Cell: Cell[A1], retCell: Cell[R]) =
      Props(new Tran1Actor(arg1Cell, retCell))
  }

  private class Tran1Actor(arg1Cell: Cell[A1], retCell: Cell[R]) extends Actor {

    val log = Logging(context.system, this)

     private var arg1: Option[A1] = None

    def receive = {
      case (arg1: A1, 1) =>
        this.arg1 = Some(arg1)
        retCell.put(fun(arg1))
      case message =>
        log.info(s"received unknown message: ${message}")
    }
  }

  def connect(arg1Cell: Cell[A1], retCell: Cell[R]): Unit = {
    val actor = system.actorOf(Tran1Actor.props(arg1Cell, retCell))
    arg1Cell.asArgOf(actor, 1)
  }
}

object Tran1 {
  def apply[A1, R](fun: PartialFunction[A1, R])
    (implicit
      arg1Lattice: JoinSemilattice[A1],
      retLattice: JoinSemilattice[R],
      system: ActorSystem): Tran1[A1, R] =
    new Tran1(fun)
}

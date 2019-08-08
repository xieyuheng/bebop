package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import java.util.UUID

case class Fn2[A1, A2, R]
  (fn: PartialFunction[(A1, A2), R])
  (implicit
    val arg1Lattice: JoinSemilattice[A1],
    val arg2Lattice: JoinSemilattice[A2],
    val retLattice: JoinSemilattice[R],
    val system: ActorSystem) extends Tran2[A1, A2, R] {

  private object Fn2Actor {
    def props(arg1Cell: Cell[A1], arg2Cell: Cell[A2], retCell: Cell[R]) =
      Props(new Fn2Actor(arg1Cell, arg2Cell, retCell))
  }

  private class Fn2Actor
    (arg1Cell: Cell[A1], arg2Cell: Cell[A2], retCell: Cell[R]) extends Actor {

    val log = Logging(context.system, this)

    var arg1: Option[A1] = None
    var arg2: Option[A2] = None

    def exeIfPossible() = {
      (arg1, arg2) match {
        case (Some(a1), Some(a2)) =>
          retCell.put(fn(a1, a2))
        case _ => {}
      }
    }

    def receive = {
      case (a: A1, 1) =>
        arg1 = Some(a)
        exeIfPossible()
      case (a: A2, 1) =>
        arg2 = Some(a)
        exeIfPossible()
      case msg =>
        log.info(s"received unknown message: ${msg}")
    }
  }

  def connect(arg1Cell: Cell[A1], arg2Cell: Cell[A2], retCell: Cell[R]): Unit = {
    val uuid = UUID.randomUUID().toString
    val actor = system.actorOf(Fn2Actor.props(arg1Cell, arg2Cell, retCell), uuid)

    arg1Cell.asArgOf(Propagator(actor), 1)
    arg2Cell.asArgOf(Propagator(actor), 2)
  }
}

package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

import java.util.UUID

case class PrimtiveTran1[A1, R]
  (fun: PartialFunction[A1, R])
  (implicit
    val arg1Lattice: JoinSemilattice[A1],
    val retLattice: JoinSemilattice[R],
    val system: ActorSystem) extends Tran1[A1, R] {

  private object PrimtiveTran1Actor {
    def props(arg1Cell: Cell[A1], retCell: Cell[R]) =
      Props(new PrimtiveTran1Actor(arg1Cell, retCell))
  }

  private class PrimtiveTran1Actor(arg1Cell: Cell[A1], retCell: Cell[R]) extends Actor {

    val log = Logging(context.system, this)

    private var arg1: Option[A1] = None

    def receive = {
      case (a: A1, 1) =>
        arg1 = Some(a)
        retCell.put(fun(a))
      case msg =>
        log.info(s"received unknown message: ${msg}")
    }
  }

  def connect(arg1Cell: Cell[A1], retCell: Cell[R], name: String = ""): Propagator = {
    val actor = if (name == "") {
      val uuid = UUID.randomUUID().toString
      system.actorOf(PrimtiveTran1Actor.props(arg1Cell, retCell), name = uuid)
    } else {
      system.actorOf(PrimtiveTran1Actor.props(arg1Cell, retCell), name)
    }

    val propagator = Propagator(actor)
    arg1Cell.asArgOf(propagator, 1)
    propagator
  }
}

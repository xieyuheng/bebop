package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

import java.util.UUID

class Tran1[A1, R]
  (initAction: Option[PartialFunction[A1, R]] = None)
  (implicit
    arg1Lattice: JoinSemilattice[A1],
    retLattice: JoinSemilattice[R],
    system: ActorSystem) {

  private object Tran1Actor {
    def props(arg1Cell: Cell[A1], retCell: Cell[R]) =
      Props(new Tran1Actor(arg1Cell, retCell))
  }

  object msg {
    case class PutFun(fun: PartialFunction[A1, R])
  }

  private class Tran1Actor(arg1Cell: Cell[A1], retCell: Cell[R]) extends Actor {

    val log = Logging(context.system, this)

    private var action: Option[PartialFunction[A1, R]] = None
    private var arg1: Option[A1] = None

    def receive = {
      case msg.PutFun(fun) =>
        action = Some(fun)
      case (a: A1, 1) =>
        action match {
          case Some(fun) =>
            arg1 = Some(a)
            retCell.put(fun(a))
          case None =>
            // log.info(s"fun is not ready")
        }
      case msg =>
        log.info(s"received unknown message: ${msg}")
    }
  }

  def connect(arg1Cell: Cell[A1], retCell: Cell[R], name: String = ""): ActorRef = {
    val actor = if (name == "") {
      val uuid = UUID.randomUUID().toString
      system.actorOf(Tran1Actor.props(arg1Cell, retCell), name = uuid)
    } else {
      system.actorOf(Tran1Actor.props(arg1Cell, retCell), name)
    }

    initAction match {
      case Some(fun) =>
        actor ! msg.PutFun(fun)
      case None => {}
    }

    arg1Cell.asArgOf(actor, 1)

    actor
  }
}

object Tran1 {
  def empty[A1, R]
    (implicit
      arg1Lattice: JoinSemilattice[A1],
      retLattice: JoinSemilattice[R],
      system: ActorSystem): Tran1[A1, R] =
    new Tran1(None)

  def apply[A1, R](fun: PartialFunction[A1, R])
    (implicit
      arg1Lattice: JoinSemilattice[A1],
      retLattice: JoinSemilattice[R],
      system: ActorSystem): Tran1[A1, R] =
    new Tran1(Some(fun))
}

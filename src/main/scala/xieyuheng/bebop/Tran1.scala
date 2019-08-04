package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

import java.util.UUID

class PrimtiveTran1[A1, R]
  (initAction: Option[PartialFunction[A1, R]] = None)
  (implicit
    arg1Lattice: JoinSemilattice[A1],
    retLattice: JoinSemilattice[R],
    system: ActorSystem) {

  private object PrimtiveTran1Actor {
    def props(arg1Cell: Cell[A1], retCell: Cell[R]) =
      Props(new PrimtiveTran1Actor(arg1Cell, retCell))
  }

  object msg {
    case class PutFun(fun: PartialFunction[A1, R])
  }

  private class PrimtiveTran1Actor(arg1Cell: Cell[A1], retCell: Cell[R]) extends Actor {

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

  def connect(arg1Cell: Cell[A1], retCell: Cell[R], name: String = ""): Propagator = {
    val actor = if (name == "") {
      val uuid = UUID.randomUUID().toString
      system.actorOf(PrimtiveTran1Actor.props(arg1Cell, retCell), name = uuid)
    } else {
      system.actorOf(PrimtiveTran1Actor.props(arg1Cell, retCell), name)
    }

    initAction.foreach { fun => actor ! msg.PutFun(fun) }

    val propagator = Propagator(actor)
    arg1Cell.asArgOf(propagator, 1)
    propagator
  }

  def $ (
    arg1Cell: Cell[A1],
    propagatorName: String = "",
    cellName: String = "",
  ): (Propagator, Cell[R]) = {
    val retCell = Cell[R](cellName)
    val propagator = connect(arg1Cell, retCell, propagatorName)
    (propagator, retCell)
  }

  def apply (
    arg1Cell: Cell[A1],
    propagatorName: String = "",
    cellName: String = "",
  ): Cell[R] = {
    val retCell = Cell[R](cellName)
    connect(arg1Cell, retCell, propagatorName)
    retCell
  }
}

object Tran1 {
  def empty[A1, R]
    (implicit
      arg1Lattice: JoinSemilattice[A1],
      retLattice: JoinSemilattice[R],
      system: ActorSystem): PrimtiveTran1[A1, R] =
    new PrimtiveTran1(None)

  def apply[A1, R](fun: PartialFunction[A1, R])
    (implicit
      arg1Lattice: JoinSemilattice[A1],
      retLattice: JoinSemilattice[R],
      system: ActorSystem): PrimtiveTran1[A1, R] =
    new PrimtiveTran1(Some(fun))
}

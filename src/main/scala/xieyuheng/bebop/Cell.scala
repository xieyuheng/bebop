package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import java.util.UUID

class Cell[E]
  (name: Option[String] = None)
  (implicit
    lattice: JoinSemilattice[E],
    system: ActorSystem) {

  private object CellActor {
    def props = Props(new CellActor)
  }

  object msg {
    case class Foreach(f: Option[E] => Unit)
    case class Put(value: E)
    case class RegisterPropagator(propagator: Propagator, n: Int)
  }

  private class CellActor extends Actor {

    val log = Logging(context.system, this)

    private var propagatorInfo: List[(Propagator, Int)] = Nil

    private var content: Option[E] = None

    private def join(a: E) = {
      val c = content match {
        case Some(b) => lattice.join(a, b)
        case None => a
      }

      if (content != Some(c)) {
        content = Some(c)
        propagatorInfo.foreach { case (propagator, n) =>
          propagator.updateArg(c, n)
        }
      }
    }

    def receive = {
      case msg.Foreach(f) =>
        f(content)
      case msg.Put(a) =>
        join(a)
      case msg.RegisterPropagator(propagator, n) =>
        propagatorInfo = (propagator, n) :: propagatorInfo
      case msg =>
        log.info(s"received unknown message: ${msg}")
    }
  }

  val actor = {
    name match {
      case Some(name) =>
        system.actorOf(CellActor.props, name)
      case None =>
        val uuid = UUID.randomUUID().toString
        system.actorOf(CellActor.props, name = uuid)
    }
  }

  def foreach(f: Option[E] => Unit): Unit =
    actor ! msg.Foreach(f)

  def put(a: E): Unit =
    actor ! msg.Put(a)

  def asArgOf(propagator: Propagator, n: Int): Unit =
    actor ! msg.RegisterPropagator(propagator, n)
}

object Cell {
  def apply[E]
    (name: String = "")
    (implicit
      lattice: JoinSemilattice[E],
      system: ActorSystem): Cell[E] = {
    if (name == "") {
      new Cell(None)
    } else {
      new Cell(Some(name))
    }
  }
}

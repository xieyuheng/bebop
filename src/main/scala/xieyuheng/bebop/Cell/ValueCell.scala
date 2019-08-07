package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import java.util.UUID

class ValueCell[E]
  (implicit
    val lattice: JoinSemilattice[E],
    val system: ActorSystem) extends Cell[E] {

  private object ValueCellActor {
    def props = Props(new ValueCellActor)
  }

  object msg {
    case class Foreach(f: Option[E] => Unit)
    case class Put(value: E)
    case class RegisterPropagator(propagator: Propagator, n: Int)
    case class RegisterCell(cell: Cell[E])
  }

  private class ValueCellActor extends Actor {

    val log = Logging(context.system, this)

    private var registeredPropagators: List[(Propagator, Int)] = Nil
    private var registeredCells: List[Cell[E]] = Nil

    private var content: Option[E] = None

    private def join(a: E) = {
      val c = content match {
        case Some(b) => lattice.join(a, b)
        case None => a
      }

      if (content != Some(c)) {
        content = Some(c)

        registeredPropagators.foreach { case (propagator, n) =>
          propagator.updateArg(c, n)
        }

        registeredCells.foreach { case cell =>
          cell.put(c)
        }
      }
    }

    def receive = {
      case msg.Foreach(f) =>
        f(content)
      case msg.Put(a) =>
        join(a)
      case msg.RegisterPropagator(propagator, n) =>
        registeredPropagators = (propagator, n) :: registeredPropagators
      case msg.RegisterCell(cell) =>
        registeredCells = cell :: registeredCells
      case msg =>
        log.info(s"received unknown message: ${msg}")
    }
  }

  val actor = {
    val uuid = UUID.randomUUID().toString
    system.actorOf(ValueCellActor.props, name = uuid)
  }

  def foreach(f: Option[E] => Unit): Unit =
    actor ! msg.Foreach(f)

  def put(a: E): Unit =
    actor ! msg.Put(a)

  def asArgOf(propagator: Propagator, n: Int): Unit =
    actor ! msg.RegisterPropagator(propagator, n)

  def forward(cell: Cell[E]): Unit =
    actor ! msg.RegisterCell(cell)
}

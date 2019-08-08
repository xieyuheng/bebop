package xieyuheng.bebop

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import java.util.UUID

case class Cell[E]()
  (implicit
    elementJoin: Join[E],
    system: ActorSystem) {

  private object CellActor {
    def props = Props(new CellActor)
  }

  object msg {
    case class Foreach(f: Option[E] => Unit)
    case class Put(value: E)
    case class RegisterPropagator(propagator: Propagator, n: Double)
    case class RegisterCell(cell: Cell[E])
  }

  private class CellActor extends Actor {

    val log = Logging(context.system, this)

    private var registeredPropagators: List[(Propagator, Double)] = Nil
    private var registeredCells: List[Cell[E]] = Nil

    private var content: Option[E] = None

    private def join(value: E) = {
      val newValue = content match {
        case Some(oldValue) => elementJoin.join(oldValue, value)
        case None => value
      }

      if (content != Some(newValue)) {
        content = Some(newValue)

        registeredPropagators.foreach { case (propagator, n) =>
          propagator.updateArg(newValue, n)
        }

        registeredCells.foreach { case cell =>
          cell.put(newValue)
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
    system.actorOf(CellActor.props, name = uuid)
  }

  def foreach(f: Option[E] => Unit): Unit =
    actor ! msg.Foreach(f)

  def put(a: E): Cell[E] = {
    actor ! msg.Put(a)
    this
  }

  def asArgOf(propagator: Propagator, n: Double): Unit =
    actor ! msg.RegisterPropagator(propagator, n)

  def forward(cell: Cell[E]): Unit =
    actor ! msg.RegisterCell(cell)

  def unify(cell: Cell[E]): Unit = {
    cell.forward(this)
    this.forward(cell)
  }
}

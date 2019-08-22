package bebop.ed

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
    case class RegisterPropagator(propagator: Propagator, n: Int)
    case class RegisterCell(cell: Cell[E])
    case class SendToPropagator(propagator: Propagator, n: Int)
  }

  private class CellActor extends Actor {

    val log = Logging(context.system, this)

    private var registeredPropagators: List[(Propagator, Int)] = Nil
    private var registeredCells: List[Cell[E]] = Nil

    private var content: Option[E] = None

    private def sendToAll(): Unit = {
      content match {
        case Some(value) =>
          registeredPropagators.foreach { case (propagator, n) =>
            propagator.updateArg(value, n)
          }
          registeredCells.foreach { case cell =>
            cell.put(value)
          }
        case None => {}
      }
    }

    def receive = {
      case msg.Foreach(f) =>
        f(content)
      case msg.Put(value) =>
        val newValue = content match {
          case Some(oldValue) => elementJoin.join(oldValue, value)
          case None => value
        }
        content match {
          case Some(oldValue) =>
            if (oldValue != newValue) {
              content = Some(newValue)
              sendToAll()
            }
          case None =>
            content = Some(newValue)
            sendToAll()
        }
      case msg.RegisterPropagator(propagator, n) =>
        registeredPropagators = (propagator, n) :: registeredPropagators
      case msg.RegisterCell(cell) =>
        registeredCells = cell :: registeredCells
      case msg.SendToPropagator(propagator, n) =>
        content match {
          case Some(value) =>
            propagator.updateArg(value, n)
          case None => {}
        }
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

  def asArgOf(propagator: Propagator, n: Int): Unit = {
    actor ! msg.RegisterPropagator(propagator, n)
    actor ! msg.SendToPropagator(propagator, n)
  }

  def forward(cell: Cell[E]): Unit = {
    actor ! msg.RegisterCell(cell)
    foreach {
      case Some(value) => cell.put(value)
      case None => {}
    }
  }

  def unify(cell: Cell[E]): Unit = {
    cell.forward(this)
    this.forward(cell)
  }
}

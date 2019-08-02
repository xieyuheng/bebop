package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

class Cell[E]
  (implicit
    lattice: JoinSemilattice[E],
    system: ActorSystem) {

  private object CellActor {
    def props = Props(new CellActor)

    case class Foreach(f: Option[E] => Unit)
    case class Put(value: E)
    case class RegisterNeighbor(neighbor: ActorRef, n: Int)
  }

  private class CellActor extends Actor {

    val log = Logging(context.system, this)

    private var neighbors: List[(ActorRef, Int)] = Nil

    private var content: Option[E] = None

    private def join(a: E) = {
      val c = content match {
        case Some(b) => lattice.join(a, b)
        case None => a
      }

      if (content != Some(c)) {
        content = Some(c)
        neighbors.foreach { case (neighbor, n) =>
          neighbor ! (c, n)
        }
      }
    }

    def receive = {
      case CellActor.Foreach(f) =>
        f(content)
      case CellActor.Put(a) =>
        join(a)
      case CellActor.RegisterNeighbor(neighbor, n) =>
        neighbors = (neighbor, n) :: neighbors
      case message =>
        log.info(s"received unknown message: ${message}")
    }
  }

  private val actor = system.actorOf(CellActor.props)

  def foreach(f: Option[E] => Unit): Unit =
    actor ! CellActor.Foreach(f)

  def put(a: E): Unit =
    actor ! CellActor.Put(a)

  def asArgOf(tran: ActorRef, n: Int): Unit =
    actor ! CellActor.RegisterNeighbor(tran, n)
}

object CellApp extends App {
  implicit val intJoinSemilattice = new JoinSemilattice[Int] {
    def join(a: Int, b: Int) = a
  }

  implicit val system = ActorSystem("bebop")

  val cell = new Cell()

  cell.foreach(println)
  cell.put(123)
  cell.foreach(println)
  cell.put(123123)
  cell.foreach(println)
}

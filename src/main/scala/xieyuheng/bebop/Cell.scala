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
  }

  private object Msg {
    case class Foreach(f: Option[E] => Unit)
    case class Put(value: E)
  }

  private class CellActor extends Actor {

    private var content: Option[E] = None

    private def join(a: E) = {
      val old = content
      content match {
        case Some(b) =>
          content = Some(lattice.join(a, b))
        case None =>
          content = Some(a)
      }
      if (old != content) {
        //
      }
    }

    val log = Logging(context.system, this)

    def receive = {
      case Msg.Foreach(f) =>
        f(content)
      case Msg.Put(a) =>
        join(a)
    }
  }

  private val actor = system.actorOf(CellActor.props)

  def foreach(f: Option[E] => Unit): Unit =
    actor ! Msg.Foreach(f)

  def put(a: E): Unit =
    actor ! Msg.Put(a)
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
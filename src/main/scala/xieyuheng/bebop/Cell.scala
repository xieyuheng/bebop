package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging

class Cell[E](implicit lattice: JoinSemilattice[E]) extends Actor {
  private var content: Option[E] = None

  def add(a: E) = {
    val old = content
    content match {
      case Some(b) =>
        content = Some(lattice.join(a, b))
      case None =>
        content = Some(a)
    }
    if (old != content) {
      ???
    }
  }

  def value: Option[E] = content

  val log = Logging(context.system, this)

  def receive = {
    case "test" => log.info("received test")
    case _      => log.info("received unknown message")
  }
}

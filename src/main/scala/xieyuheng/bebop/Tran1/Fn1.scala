package xieyuheng.bebop

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import java.util.UUID

case class Fn1[A1, R]
  (fn: PartialFunction[A1, R])
  (implicit
    val arg1Join: Join[A1],
    val retJoin: Join[R],
    val system: ActorSystem) extends Tran1[A1, R] {

  private object Fn1Actor {
    def props(arg1Cell: Cell[A1], retCell: Cell[R]) =
      Props(new Fn1Actor(arg1Cell, retCell))
  }

  private class Fn1Actor
    (arg1Cell: Cell[A1], retCell: Cell[R]) extends Actor {

    val log = Logging(context.system, this)

    def receive = {
      case (a: A1, 1) =>
        retCell.put(fn(a))
      case msg =>
        log.info(s"received unknown message: ${msg}")
    }
  }

  def connect(arg1Cell: Cell[A1], retCell: Cell[R]): Unit = {
    val uuid = UUID.randomUUID().toString
    val actor = system.actorOf(Fn1Actor.props(arg1Cell, retCell), uuid)

    arg1Cell.asArgOf(Propagator(actor), 1)
  }
}

package bebop.ed

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import java.util.UUID

object dsl {
  def ifo[A]
    (control: Cell[Binary])
    (consequent: => Cell[A])
    (alternate: => Cell[A])
    (output: Cell[A])
    (implicit
      argJoin: Join[A],
      system: ActorSystem): Unit = {

    object IfoActor {
      def props
        (control: Cell[Binary],
          consequent: => Cell[A],
          alternate: => Cell[A],
          output: Cell[A]) =
        Props(new IfoActor(control, consequent, alternate, output))
    }

    class IfoActor
      (control: Cell[Binary],
        consequent: => Cell[A],
        alternate: => Cell[A],
        output: Cell[A]) extends Actor {

      val log = Logging(context.system, this)

      def receive = {
        case (binary: Binary, 1) =>
          if (binary.toBoolean) {
            consequent.forward(output)
          } else {
            alternate.forward(output)
          }
        case msg =>
          log.info(s"received unknown message: ${msg}")
      }
    }

    val uuid = UUID.randomUUID().toString
    val actor = system.actorOf(IfoActor.props(control, consequent, alternate, output), uuid)

    control.asArgOf(Propagator(actor), 1)
  }

  def ife[A]
    (control: Cell[Binary])
    (consequent: => Cell[A])
    (alternate: => Cell[A])
    (implicit
      argJoin: Join[A],
      system: ActorSystem): Cell[A] = {

    val output = Cell[A]()
    ifo(control)(consequent)(alternate)(output)
    output
  }
}

import org.scalatest._

import xieyuheng.bebop._

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

class heronSpec extends FlatSpec with Matchers {
  implicit val newReplaceOldDouble = JoinAble.newReplaceOld[Double]
  implicit val newReplaceOldBoolean = JoinAble.newReplaceOld[Boolean]
  implicit val system = ActorSystem("heronSpec")

  import system.dispatcher

  val Blink = 10.millis

  "propagator model" can "implement heron step" in {
    val heronStep = Fn2[Double, Double, Double] {
      case (x, guess) => (guess + x / guess) / 2
    }

    val x = Cell[Double]()
    val guess = Cell[Double]()
    val betterGuess = heronStep(x, guess)

    x.put(2)
    guess.put(1.4)

    system.scheduler.scheduleOnce(Blink) {
      betterGuess.foreach { content => println(content) }
    }
  }

  it can "implement sqrt by sqrtIter" in {
    def sqrt = Ap1[Double, Double] {
      case x =>
        val guess = Cell[Double]().put(1)
        sqrtIter(x, guess)
    }

    def sqrtIter = Cn2[Double, Double, Double] {
      case (x, guess, answer) =>
        ???
    }
  }
}

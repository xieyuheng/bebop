import org.scalatest._

import xieyuheng.bebop._

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

class heronSpec extends FlatSpec with Matchers {

  implicit val newReplaceOldDouble = new joinAble[Double] {
    def join(a: Double, b: Double) = b
  }

  implicit val system = ActorSystem("heronSpec")

  import system.dispatcher

  val Blink = 10.millis

  "propagator model" can "implement heron step" in {
    val heronStep = Fn2[Double, Double, Double] {
      case (x, g) => (g + x / g) / 2
    }

    val x = Cell()
    val guess = Cell()
    val betterGuess = heronStep(x, guess)

    x.put(2)
    guess.put(1.4)

    system.scheduler.scheduleOnce(Blink) {
      betterGuess.foreach { content => println(content) }
    }
  }

  it can "implement sqrtIter" in {

  }
}

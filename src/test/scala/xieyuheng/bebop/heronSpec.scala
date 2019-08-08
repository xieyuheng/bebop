import org.scalatest._

import xieyuheng.bebop._

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

class heronSpec extends FlatSpec with Matchers {
  implicit val newReplaceOldDouble = Join.newReplaceOld[Double]
  implicit val newReplaceOldBoolean = Join.newReplaceOld[Boolean]
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
        // Tran.switch(control, input, output)
        ???
    }
  }

  // it can "implement factorial" in {
  //   def factorial = Ap1[Double, Double] {
  //     case n =>
  //       val zero = Cell[Double]().put(0)
  //       val one = Cell[Double]().put(1)
  //       ife (equal(zero, n)) {
  //         one
  //       } {
  //         mul(n, factorial(sub(n, one)))
  //       }
  //   }
  // }
}

import org.scalatest._

import xieyuheng.bebop._
import xieyuheng.bebop.dsl._

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

class heronSpec extends FlatSpec with Matchers {
  implicit val newReplaceOldDouble = Join.newReplaceOld[Double]
  implicit val newReplaceOldBoolean = Join.newReplaceOld[Boolean]
  implicit val system = ActorSystem("heronSpec")

  import system.dispatcher

  val Blink = 100.millis

  "propagator model" can "implement heron step" in {
    def heronStep = Fn2[Double, Double, Double] {
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
    val mul = Fn2[Double, Double, Double] {
      case (x, y) => x * y
    }

    val sub = Fn2[Double, Double, Double] {
      case (x, y) => x - y
    }

    val abs = Fn1[Double, Double] {
      case x => scala.math.abs(x)
    }

    val eq = Fn2[Double, Double, Binary] {
      case (x, y) => if (x == y) Yes else No
    }

    val lt = Fn2[Double, Double, Binary] {
      case (x, y) => if (x < y) Yes else No
    }

    val gt = Fn2[Double, Double, Binary] {
      case (x, y) => if (x > y) Yes else No
    }

    def heronStep = Fn2[Double, Double, Double] {
      case (x, guess) => (guess + x / guess) / 2
    }

    def sqrt = Ap1[Double, Double] {
      case x =>
        val guess = Cell[Double]().put(1)
        sqrtIter(x, guess)
    }

    def goodEnough = Ap2[Double, Double, Binary] {
      case (x, guess) =>
        val epsilon = Cell[Double]().put(0.00000001)
        lt(abs(sub(mul(guess, guess), x)), epsilon)
    }

    def sqrtIter: Ap2[Double, Double, Double] = Ap2[Double, Double, Double] {
      case (x, guess) =>
        ife (goodEnough(x, guess)) {
          guess
        } {
          sqrtIter(x, heronStep(x, guess))
        }
    }

    val x = Cell[Double]
    val answer = sqrt(x)

    x.put(2)

    system.scheduler.scheduleOnce(Blink) {
      answer.foreach { content => println(content) }
    }
  }

  it can "use ifo" in {
    val flag = Cell[Binary]()
    val x = Cell[Double]()
    val y = Cell[Double]()
    val answer = Cell[Double]()

    ifo (flag) { x } { y } (answer)

    flag.put(Yes)

    x.put(1)
    y.put(2)

    system.scheduler.scheduleOnce(Blink) {
      answer.foreach { content => assert(content == Some(1)) }
    }
  }

  it can "use ife" in {
    val flag = Cell[Binary]()
    val x = Cell[Double]()
    val y = Cell[Double]()

    val answer = ife (flag) { x } { y }

    flag.put(Yes)

    x.put(1)
    y.put(2)

    system.scheduler.scheduleOnce(Blink) {
      answer.foreach { content => assert(content == Some(1)) }
    }
  }

  it can "implement factorial" in {
    val mul = Fn2[Double, Double, Double] {
      case (x, y) => x * y
    }

    val sub = Fn2[Double, Double, Double] {
      case (x, y) => x - y
    }

    val eq = Fn2[Double, Double, Binary] {
      case (x, y) => if (x == y) Yes else No
    }

    val zero = Cell[Double]().put(0)
    val one = Cell[Double]().put(1)

    def factorial: Ap1[Double, Double] = Ap1[Double, Double] {
      case n =>
        ife (eq(zero, n)) {
          one
        } {
          mul(n, factorial(sub(n, one)))
        }
    }

    val x = Cell[Double]()

    val answer = factorial(x)

    x.put(4)

    system.scheduler.scheduleOnce(Blink) {
      answer.foreach { content => assert(content == Some(24)) }
    }
  }
}

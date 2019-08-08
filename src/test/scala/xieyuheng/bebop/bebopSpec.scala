import org.scalatest._

import xieyuheng.bebop._

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

class bebopSpec extends FlatSpec with Matchers {
  implicit val newReplaceOldDouble = Join.newReplaceOld[Double]
  implicit val system = ActorSystem("bebopSpec")

  import system.dispatcher

  val Blink = 10.millis

  "Fn1" can "build propagator" in {
    val x = Cell()

    val add1 = Fn1[Double, Double] {
      case x => x + 1
    }

    val y = add1(x)
    val w = add1(x)
    val z = add1(y)

    x.put(1)

    system.scheduler.scheduleOnce(Blink) {
      x.foreach { content => assert(content == Some(1)) }
      y.foreach { content => assert(content == Some(2)) }
      z.foreach { content => assert(content == Some(3)) }
      w.foreach { content => assert(content == Some(2)) }
    }
  }

  "Cn1" can "build propagator" in {
    val x = Cell()

    val add1 = Fn1[Double, Double] {
      case x => x + 1
    }

    val add2 = Cn1[Double, Double] {
      case (a, o) =>
        val b = Cell()
        add1.connect(a, b)
        add1.connect(b, o)
    }

    val y = add2(x)
    val w = add2(x)
    val z = add2(y)

    x.put(1)

    system.scheduler.scheduleOnce(Blink) {
      x.foreach { content => assert(content == Some(1)) }
      y.foreach { content => assert(content == Some(3)) }
      z.foreach { content => assert(content == Some(5)) }
      w.foreach { content => assert(content == Some(3)) }
    }
  }

  "Ap1" can "build propagator" in {
    val x = Cell()

    val add1 = Fn1[Double, Double] {
      case x => x + 1
    }

    val add2 = Ap1[Double, Double] {
      case a => add1(add1(a))
    }

    val y = add2(x)
    val w = add2(x)
    val z = add2(y)

    x.put(1)

    system.scheduler.scheduleOnce(Blink) {
      x.foreach { content => assert(content == Some(1)) }
      y.foreach { content => assert(content == Some(3)) }
      z.foreach { content => assert(content == Some(5)) }
      w.foreach { content => assert(content == Some(3)) }
    }
  }

  "Fn2" can "build propagator" in {
    val x = Cell()
    val y = Cell()

    val add = Fn2[Double, Double, Double] {
      case (x, y) => x + y
    }

    val z = add(x, y)
    val w = add(x, y)

    x.put(1)
    y.put(2)

    system.scheduler.scheduleOnce(Blink) {
      x.foreach { content => assert(content == Some(1)) }
      y.foreach { content => assert(content == Some(2)) }
      z.foreach { content => assert(content == Some(3)) }
      w.foreach { content => assert(content == Some(3)) }
    }
  }

  "Cn2" can "build propagator" in {
    val x = Cell()
    val y = Cell()

    val add = Fn2[Double, Double, Double] {
      case (x, y) => x + y
    }

    val mul = Fn2[Double, Double, Double] {
      case (x, y) => x * y
    }

    val square = Cn1[Double, Double] {
      case (x, o) =>
        mul.connect(x, x, o)
    }

    val euclid = Cn2[Double, Double, Double] {
      case (x, y, o) =>
        add.connect(square(x), square(y), o)
    }

    val z = euclid(x, y)
    val w = euclid(x, y)

    x.put(3)
    y.put(4)

    system.scheduler.scheduleOnce(Blink) {
      x.foreach { content => assert(content == Some(3)) }
      y.foreach { content => assert(content == Some(4)) }
      z.foreach { content => assert(content == Some(25)) }
      w.foreach { content => assert(content == Some(25)) }
    }
  }

  "Ap2" can "build propagator" in {
    val x = Cell()
    val y = Cell()

    val add = Fn2[Double, Double, Double] {
      case (x, y) => x + y
    }

    val mul = Fn2[Double, Double, Double] {
      case (x, y) => x * y
    }

    val square = Ap1[Double, Double] {
      case x => mul(x, x)
    }

    val euclid = Ap2[Double, Double, Double] {
      case (x, y) =>
        add(square(x), square(y))
    }

    val z = euclid(x, y)
    val w = euclid(x, y)

    x.put(3)
    y.put(4)

    system.scheduler.scheduleOnce(Blink) {
      x.foreach { content => assert(content == Some(3)) }
      y.foreach { content => assert(content == Some(4)) }
      z.foreach { content => assert(content == Some(25)) }
      w.foreach { content => assert(content == Some(25)) }
    }
  }
}

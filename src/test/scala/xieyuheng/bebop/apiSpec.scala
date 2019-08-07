import org.scalatest._
import xieyuheng.bebop._

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

class apiSpec extends FlatSpec with Matchers {

  implicit val intJoinSemilattice = new JoinSemilattice[Int] {
    def join(a: Int, b: Int) = a
  }

  implicit val system = ActorSystem("apiSpec")

  import system.dispatcher

  val Blink = 10.millis

  "Fn1" can "build propagator" in {
    val x = new ValueCell

    val add1 = Fn1[Int, Int] {
      case x =>
        x + 1
    }

    val y = add1(x)
    val w = add1(x)
    val z = add1(y)

    x.put(1)

    system.scheduler.scheduleOnce((10).millis) {
      x.foreach { content => assert(content == Some(1)) }
      y.foreach { content => assert(content == Some(2)) }
      z.foreach { content => assert(content == Some(3)) }
      w.foreach { content => assert(content == Some(2)) }
    }
  }

  "Tr1" can "build propagator" in {
    val x = new ValueCell

    val add1 = Fn1[Int, Int] {
      case x =>
        x + 1
    }

    val add2 = Tr1[Int, Int] {
      case (a, o) =>
        val b = new ValueCell
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
    val x = new ValueCell

    val add1 = Fn1[Int, Int] {
      case x =>
        x + 1
    }

    val add2 = Ap1[Int, Int] {
      case a =>
        add1(add1(a))
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
}

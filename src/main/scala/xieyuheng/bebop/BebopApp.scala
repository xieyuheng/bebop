package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.{ Actor, ActorRef, ActorSystem, Props, PoisonPill }
import akka.event.Logging

import scala.concurrent.duration._

object BebopApp extends App {
  implicit val intJoinSemilattice = new JoinSemilattice[Int] {
    def join(a: Int, b: Int) = a
  }

  implicit val system = ActorSystem("bebop")


  // {
//     val x = new ValueCell

//     val tran = Fn1[Int, Int] {
//       case x =>
//         x + 1
//     }

//     val y = tran(x)
//     val w = tran(x)
//     val z = tran(y)

//     x.put(1)

//     import system.dispatcher

//     system.scheduler.scheduleOnce(500.millis) {
//       x.foreach { content => println(s"x: ${content}") }
//       y.foreach { content => println(s"y: ${content}") }
//       z.foreach { content => println(s"z: ${content}") }
//       w.foreach { content => println(s"w: ${content}") }
//     }
//   }

//   {
//     val x = new ValueCell

//     val add1 = Fn1[Int, Int] {
//       case x =>
//         x + 1
//     }

//     val add2 = Tr1[Int, Int] {
//       case (a, o) =>
//         val b = new ValueCell
//         add1.connect(a, b)
//         add1.connect(b, o)
//     }

//     val y = add2(x)
//     val w = add2(x)
//     val z = add2(y)

//     x.put(1)

//     import system.dispatcher

//     system.scheduler.scheduleOnce(500.millis) {
//       x.foreach { content => println(s"x: ${content}") }
//       y.foreach { content => println(s"y: ${content}") }
//       z.foreach { content => println(s"z: ${content}") }
//       w.foreach { content => println(s"w: ${content}") }
//     }
//   }

  {
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

    import system.dispatcher

    system.scheduler.scheduleOnce(500.millis) {
      x.foreach { content => println(s"x: ${content}") }
      y.foreach { content => println(s"y: ${content}") }
      z.foreach { content => println(s"z: ${content}") }
      w.foreach { content => println(s"w: ${content}") }
    }
  }
}

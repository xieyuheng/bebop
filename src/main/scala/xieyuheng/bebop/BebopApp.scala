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
  //   val x = Cell("x")
  //   val y = Cell("y")
  //   val z = Cell("z")
  //   val w = Cell("w")

  //   // val tran = Tran1[Int, Int] {
  //   //   case x =>
  //   //     x + 1
  //   // }

  //   val tran = Tran1.empty[Int, Int]

  //   val axy = tran.connect(x, y, "xy")
  //   val axw = tran.connect(x, w, "xw")
  //   val ayz = tran.connect(y, z, "yz")

  //   axy ! tran.msg.PutFun {
  //     case x =>
  //       x + 1
  //   }

  //   axw ! tran.msg.PutFun {
  //     case x =>
  //       x + 1
  //   }

  //   ayz ! tran.msg.PutFun {
  //     case x =>
  //       x + 100
  //   }

  //   println(ayz)

  //   println(x.actor)

  //   x.put(1)

  //   import system.dispatcher

  //   system.scheduler.scheduleOnce(500.millis) {
  //     x.foreach { content => println(s"x: ${content}") }
  //     y.foreach { content => println(s"y: ${content}") }
  //     z.foreach { content => println(s"z: ${content}") }
  //     w.foreach { content => println(s"w: ${content}") }
  //   }
  // }

  // {
  //   val x = Cell("x")

  //   val tran = Tran1[Int, Int] {
  //     case x =>
  //       x + 1
  //   }

  //   val (xy, y) = tran $ x
  //   val (xw, w) = tran $ x
  //   val (yz, z) = tran $ y

  //   println(yz)

  //   println(x.actor)

  //   x.put(1)

  //   import system.dispatcher

  //   system.scheduler.scheduleOnce(500.millis) {
  //     x.foreach { content => println(s"x: ${content}") }
  //     y.foreach { content => println(s"y: ${content}") }
  //     z.foreach { content => println(s"z: ${content}") }
  //     w.foreach { content => println(s"w: ${content}") }
  //   }
  // }

  {
    val x = Cell("x")

    val tran = Tran1[Int, Int] {
      case x =>
        x + 1
    }

    val y = tran(x)
    val w = tran(x)
    val z = tran(y)

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

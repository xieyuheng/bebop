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

  val x = new Cell()
  val y = new Cell()
  val z = new Cell()
  val w = new Cell()

  val tran = Tran1[Int, Int] {
    case x =>
      x + 1
  }

  tran.connect(x, y)
  tran.connect(x, w)
  tran.connect(y, z)

  x.put(1)

  import system.dispatcher

  system.scheduler.scheduleOnce(500.millis) {
    x.foreach { content => println(s"x: ${content}") }
    y.foreach { content => println(s"y: ${content}") }
    z.foreach { content => println(s"z: ${content}") }
    w.foreach { content => println(s"w: ${content}") }
  }
}

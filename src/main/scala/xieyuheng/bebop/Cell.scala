package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.ActorSystem

trait Cell[E] {
  implicit val lattice: JoinSemilattice[E]
  implicit val system: ActorSystem

  def foreach(f: Option[E] => Unit): Unit

  def put(a: E): Unit

  def asArgOf(propagator: Propagator, n: Int): Unit
}

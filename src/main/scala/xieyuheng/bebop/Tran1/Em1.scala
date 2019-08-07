package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.ActorSystem

case class Em1[A1, R]
  ()
  (implicit
    val arg1Lattice: JoinSemilattice[A1],
    val retLattice: JoinSemilattice[R],
    val system: ActorSystem) extends Tran1[A1, R] {

  def connect
    (_arg1Cell: Cell[A1],
      _retCell: Cell[R]): Unit = {}
}

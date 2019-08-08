package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.ActorSystem

case class Cn2[A1, A2, R]
  (connector: (Cell[A1], Cell[A2], Cell[R]) => Unit)
  (implicit
    val arg1Lattice: JoinSemilattice[A1],
    val arg2Lattice: JoinSemilattice[A2],
    val retLattice: JoinSemilattice[R],
    val system: ActorSystem) extends Tran2[A1, A2, R] {

  def connect
    (arg1Cell: Cell[A1],
      arg2Cell: Cell[A2],
      retCell: Cell[R]): Unit = {
    connector(arg1Cell, arg2Cell, retCell)
  }
}

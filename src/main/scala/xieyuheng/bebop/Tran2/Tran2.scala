package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.ActorSystem

trait Tran2[A1, A2, R] {
  implicit val arg1Lattice: JoinSemilattice[A1]
  implicit val arg2Lattice: JoinSemilattice[A2]
  implicit val retLattice: JoinSemilattice[R]
  implicit val system: ActorSystem

  def connect
    (arg1Cell: Cell[A1],
      arg2Cell: Cell[A2],
      retCell: Cell[R]): Unit

  def apply(arg1Cell: Cell[A1], arg2Cell: Cell[A2]): Cell[R] = {
    val retCell = new Cell[R]
    connect(arg1Cell, arg2Cell, retCell)
    retCell
  }
}

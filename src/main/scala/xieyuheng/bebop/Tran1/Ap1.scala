package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.ActorSystem

case class Ap1[A1, R]
  (applier: Cell[A1] => Cell[R])
  (implicit
    val arg1Lattice: JoinSemilattice[A1],
    val retLattice: JoinSemilattice[R],
    val system: ActorSystem) extends Tran1[A1, R] {

  def connect
    (arg1Cell: Cell[A1],
      retCell: Cell[R]): Unit = {
    val retCell = ValueCell[R]()
    applier(arg1Cell).forward(retCell)
  }

  def apply
    (arg1Cell: Cell[A1]): Cell[R] = {
    applier(arg1Cell)
  }
}

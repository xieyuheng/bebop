package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.ActorSystem

case class Tr1[A1, R]
  (connector: (Cell[A1], Cell[R]) => Unit)
  (implicit
    val arg1Lattice: JoinSemilattice[A1],
    val retLattice: JoinSemilattice[R],
    val system: ActorSystem) extends Tran1[A1, R] {

  def connect
    (arg1Cell: Cell[A1],
      retCell: Cell[R]): Unit = {
    connector(arg1Cell, retCell)
  }
}

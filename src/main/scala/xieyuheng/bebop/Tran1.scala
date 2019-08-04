package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.ActorSystem

trait Tran1[A1, R] {
  implicit val arg1Lattice: JoinSemilattice[A1]
  implicit val retLattice: JoinSemilattice[R]
  implicit val system: ActorSystem

  def connect
    (arg1Cell: Cell[A1],
      retCell: Cell[R],
      name: String = ""): Propagator

  def $
    (arg1Cell: Cell[A1],
      propagatorName: String = "",
      cellName: String = ""): (Propagator, Cell[R]) = {
    val retCell = ValueCell[R](cellName)
    val propagator = connect(arg1Cell, retCell, propagatorName)
    (propagator, retCell)
  }

  def apply
    (arg1Cell: Cell[A1],
      propagatorName: String = "",
      cellName: String = ""): Cell[R] = {
    val retCell = ValueCell[R](cellName)
    connect(arg1Cell, retCell, propagatorName)
    retCell
  }
}

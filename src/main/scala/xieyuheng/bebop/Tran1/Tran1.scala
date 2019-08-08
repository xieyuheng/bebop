package xieyuheng.bebop

import akka.actor.ActorSystem

trait Tran1[A1, R] {
  implicit val arg1Lattice: JoinAble[A1]
  implicit val retLattice: JoinAble[R]
  implicit val system: ActorSystem

  def connect
    (arg1Cell: Cell[A1],
      retCell: Cell[R]): Unit

  def apply(arg1Cell: Cell[A1]): Cell[R] = {
    val retCell = Cell[R]()
    connect(arg1Cell, retCell)
    retCell
  }
}

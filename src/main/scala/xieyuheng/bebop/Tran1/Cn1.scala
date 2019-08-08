package xieyuheng.bebop

import akka.actor.ActorSystem

case class Cn1[A1, R]
  (connector: (Cell[A1], Cell[R]) => Unit)
  (implicit
    val arg1Lattice: JoinAble[A1],
    val retLattice: JoinAble[R],
    val system: ActorSystem) extends Tran1[A1, R] {

  def connect
    (arg1Cell: Cell[A1],
      retCell: Cell[R]): Unit = {
    connector(arg1Cell, retCell)
  }
}

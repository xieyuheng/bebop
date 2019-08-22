package bebop.ed

import akka.actor.ActorSystem

case class Cn1[A1, R]
  (connector: (Cell[A1], Cell[R]) => Unit)
  (implicit
    val arg1Join: Join[A1],
    val retJoin: Join[R],
    val system: ActorSystem) extends Tran1[A1, R] {

  def connect
    (arg1Cell: Cell[A1],
      retCell: Cell[R]): Unit = {
    connector(arg1Cell, retCell)
  }
}

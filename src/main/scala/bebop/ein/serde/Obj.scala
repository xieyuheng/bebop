package bebop.ein.serde

sealed trait Obj
final case class MapObj(map: Map[String, Obj]) extends Obj
final case class StrObj(str: String) extends Obj
final case class NumObj(num: Double) extends Obj
case object NullObj extends Obj

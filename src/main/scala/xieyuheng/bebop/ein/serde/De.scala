package xieyuheng.bebop.ein.serde

/** Deserialize from Obj */
trait De[A] {
  def fromObj(obj: Obj): A
}

// TODO

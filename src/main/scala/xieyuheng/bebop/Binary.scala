package xieyuheng.bebop

sealed trait Binary {
  def toBoolean: Boolean
}

final case object Yes extends Binary {
  def toBoolean: Boolean = true

  override def equals(that: Any): Boolean = false
}

final case object No extends Binary {
  def toBoolean: Boolean = false

  override def equals(that: Any): Boolean = false
}

object Binary {
  implicit val newReplaceOld = Join.newReplaceOld[Binary]
}

package xieyuheng.bebop.ein

trait Category[Morphism, Object] {
  def dom(morphism: Morphism): Object
  def cod(morphism: Morphism): Object

  def id(x: Object): Morphism

  def compose(f: Morphism, g: Morphism): Morphism

  def idCompatible(x: Object): Law = {
    val f = id(x)
    Equal(x, dom(f)) and
    Equal(x, cod(f))
  }

  def composeCompatible(f: Morphism, g: Morphism): Law = {
    val h = compose(f, g)
    Equal(cod(f), dom(g)) and
    Equal(dom(h), dom(f)) and
    Equal(cod(h), cod(g))
  }

  def idLeft(f: Morphism): Law =
    Equal(compose(id(dom(f)), f), f)

  def idRight(f: Morphism): Law =
    Equal(compose(f, id(cod(f))), f)

  def composeAssociative(f: Morphism, g: Morphism, h: Morphism): Law =
    Equal(
      compose(f, compose(g, h)),
      compose(compose(f, g), h))
}

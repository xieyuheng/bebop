# Ein

Practical Category Theory.

This project introduces category theory to diligent programmers.

Aims:
- Each abstract concept should be introduced by many concrete examples
- Provide useful libraries for production

Features:
- Categories are implemented by type class

## Contains

- [TODO] serde -- serializing and deserializing Scala data
- Law based test framework

## Docs

Tutorials:
- [Monad and all that](docs/monad-and-all-that.md)

## Community

- We enforce C4 as collaboration protocol -- [The C4 RFC](https://rfc.zeromq.org/spec:42/C4)
- [Style Guide](STYLE-GUIDE.md) -- observe the style of existing code and respect it
- [Code of Conduct](CODE-OF-CONDUCT.md)

## References

- "Scala with Cats" by Noel Welsh and Dave Gurnell
  - read online: https://books.underscore.io/scala-with-cats/scala-with-cats.html

- "Category Theory for Programmers" by Bartosz Milewski
  - pdf source: https://github.com/hmemcpy/milewski-ctfp-pdf
  - Bartosz Milewski's video course:
    [Part I](https://www.youtube.com/playlist?list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_)
    [Part II](https://www.youtube.com/playlist?list=PLbgaMIhjbmElia1eCEZNvsVscFef9m0dm)
    [Part III](https://www.youtube.com/playlist?list=PLbgaMIhjbmEn64WVX4B08B4h2rOtueWIL)

- "Monad And All That" by John Hughes
  - [Origin Lecture Home Page](https://www.cs.uoregon.edu/research/summerschool/summer12/curriculum.html)
    - [Lecture 1 -- Monads](http://www.cse.chalmers.se/~rjmh/OPLSS/Monads%20and%20all%20that.pdf)
      -- [Exercises](http://www.cse.chalmers.se/~rjmh/OPLSS/Exercises.pdf)
    - [Lecture 2 -- Monad Transformers](http://www.cse.chalmers.se/~rjmh/OPLSS/Monads%20and%20all%20that%20--%20II.pdf)
    - [Lecture 3 -- Applicative Functors](http://www.cse.chalmers.se/~rjmh/OPLSS/Monads%20and%20all%20that%20--%20III.%20Applicative%20Functors.pdf)

- Typeclassopedia: https://wiki.haskell.org/Typeclassopedia

## Contributing

- Compile: `sbt compile`
- Run all tests: `sbt test`

## License

- [GPLv3](LICENSE)

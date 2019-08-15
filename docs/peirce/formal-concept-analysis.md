# Formal concept analysis

> It appears, then, that the rule for attaining
> the third grade of clearness of apprehension is as follows:
> Consider what effects,
> that might conceivably have practical bearings,
> we conceive the object of our conception to have.
> Then, our conception of these effects
> is the whole of our conception of the object.
> -- Peirce

Formal concept analysis (FCA) is a principled way of
deriving a concept hierarchy or formal ontology
from a collection of objects and their properties.

Each concept in the hierarchy
represents the objects sharing some set of properties;
and each sub-concept in the hierarchy
represents a subset of the objects
(as well as a superset of the properties)
in the concepts above it.

Formal concept analysis can be used as
an unsupervised machine learning technique
that applies mathematical lattice theory
to organize data based on objects and their shared attributes.

Data tables can be transformed into algebraic structures
called complete lattices, and that these can be utilized
for data visualization and interpretation.

A complete lattices is referred to as a **formal context**.

A **formal concept** is defined to be a pair (A, B),
where A is a set of objects (called the extent)
and B is a set of attributes (the intent) such that:
- the extent A consists of all objects
  that share the attributes in B, and dually
- the intent B consists of all attributes
  shared by the objects in A.

formal concepts form a richer lattice --
the context's **concept lattice**.

Formal concept analysis works on the binary relation:
"object has attribute",
which makes one think of general binary relational algebra.

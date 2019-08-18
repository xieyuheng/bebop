---
title: Propagator Note
---

# Propagator System

Propagator Model is independent of the language.
You should be able to write propagators in any language you choose,
and others should be able to write subsystems in their favorite language
that cooperate with your subsystems.

What is necessary is that all users agree on the protocol
by which propagators communicate with the cells
that are shared among subsystems.

Cells must support three operations:
1. add some content
2. collect the content currently accumulated
3. register a propagator to be notified when the accumulated content changes

When new content is added to a cell,
the cell must merge the addition with the content already present.

When a propagator asks for the content of a cell,
the cell must deliver a complete summary of the information
that has been added to it.

The merging of content must be commutative, associative, and idempotent.
The behavior of propagators must be monotonic
with respect to the lattice induced by the merge operation.

# Making Propagator Networks

The ingredients of a propagator network are cells and propagators.
The cells' job is to remember things; the propagators' job is to compute.

The analogy is that propagators are like
the procedures of a traditional programming language,
and cells are like the memory locations;
the big difference is that cells accumulate partial information
(which may involve arbitrary internal computations),
and can therefore have many propagators
reading information from them and writing information to them.

The two basic operations when making a propagator network
are making cells and attaching propagators to cells.
You already met one way to make cells in the form of define-cell;
we will talk about more later, but let's talk about propagators first.

# [NOTE] Propagator is first class Content

# [NOTE] push vs pull

Applicative order lambda calculus --
the propagators "push" data through the network.

Normal order lambda calculus --
the propagators "pull" only the data they need?

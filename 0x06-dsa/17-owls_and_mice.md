# owls and mice
* use network flow to solve the owls and mice problem
* **the problem**: suppose there are *M* mice on a field and a hungry owl is about to swoop in and pick one. further suppose that  there are *H* holes distributed across said field where said mice can hide in. each hole can only hold so much mice and the number of mice a hole can hide may or may not be the same as another hole. assuming that the owl can reach any of the mice and that each mouse is capable of running a radius, *r*, in any direction before being caught by the owl, what is the maximum number of mice that will be able to hide from the owl?
### walk-through
1. det'n which holes each mouse can hide in
    - have a circle of radius *r* around each mouse; any holes w/i said circle are holes the mouse can hide in
    - let a mouse and a hole it can hide in be nodes. have edges to connect mice and holes they can hide in (this is how we get a bipartite graph)
2. match mice with holes with the objective of maximising the overall safety of the group
    - have an array of size M (indices: 0 <= i < M ); array\[i\] represents mouse *i*
    - have an array of size H (indices: M <= i < M+H); array\[i\] represents hole *i*
    - place an edge w. zero flow and unit capacity, *0/1*, between mouse *i* and a hole it can reach
    - have a source node, *s*, with edges that have zero flow and unit capacity; each edge of said edges points to a mouse
    - have a sink node, *t*, with edges that come from a hole to the sink; each edge has zero flow and the capacity of a respective hole (now, we have a max flow problem)
3. run any max-flow algo; the max-flow value is the same as the max #mice that will be able to hide from the owl
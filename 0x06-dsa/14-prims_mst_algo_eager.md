# prim's minimum spanning tree algo
#### eager version
* lazy version inserts E edges into the PQ; each poll op on the PQ is O(log(E))
* instead of blindly inserting edges into a PQ, which could later become stale, the eager version tracks *(node, edge)* key-value pairs, that are easy to update and poll, to det'n the next best edge to add to the MST
> for amy MST w. directed edges, each node except the start node is paired with one, and only one, of its incoming edges
> the idea, in the eager version of the algo, is to det'n which of a node's incoming edges should be selscted to be added to the MST
* we *relax* (update) the destination node's most promising edge as we iterate over the edges of said node instead of adding edges to the PQ as we did in the lazy version
* Q: how will we retrieve and update said *(node, edge)* pairs efficiently?
    - glad you asked. one way is to use a IPQ (indexed PQ); it reduces the overall time complexity  from O(E\*log(E)) to O(E\*log(V)) because there are V k:v pairs in the IPQ.the poll/update ops in said IPQ take O(log(V)) time
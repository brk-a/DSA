# dinic's algo
* an efficient way of solving the unweighted bipartite matching problem
* a strongly polynomial max-flow algo that runs in O(V<sup>2</sup>E)
    - quite fast in practice for all graphs
    - works even better on bipartite graphs; has a time complexity of $O(\sqrt(VE))$ because it reduces to [hopcroft-karp][def]
* made by Yefim Dinitz; some idiot mispronounced his name after *"slightly modifying"* the algo
### analogy
* suppose you and a friend plan to meet at a coffee shop a few streets east of where you are. you have never been to said coffee shop, you do not know where it is but you know it is somewhere to the east of your position. how do you get there?
* with the info you have, it makes sense to move in a general easterly direction. the shop could be due east of your position, or south east; maybe north east. all we know is that moving in a general easterly direction guarantees positive progress towards the rendezvous point
    - this heuristic ensures that we continuously make progress towards whatever point of interest
* in this analogy, you are the source node, *s*. the coffe sop is the sink node , *t*
* the idea behind dinic's algo is to guide augmenting paths from *s* &rarr; *t* using a level graph to det'n which edges make progress towards the sink and which do not
* level of graph &rarr; levels obtained by performing a BFS from the source
    - assume a graph has L levels
    - start node is level zero
    - nodes directly accessible by start node are level 1
    - nodes directly accesible by level 1 nodes are level 2
    - nodes directky accessible by level *n-1* nodes are level *n*
    - sink node is level L
* an edge is part of a level graph IFF it makes progress towards the sink
    - edge must go from a node at level *l* to a node at level *l+1*
    - this constraint *prunes*  backwards and "sideways" edges (detours), that is, focuses on forward-pointing edges (edges that make continuous progress towards the sink)
    - residual edges can be made part of the level graph on condition that $capacity_{remaining} = capacity - flow > 0$
### steps
1. construct a level graph
    - perform a BFS from the source
    - label all levels of current flow graph
2. if sink node was never reached while building level graph, stop and return max flow
3. if sink node was reached, perform multiple DFSs using only valid edges in the level graph from *s*  &rarr; *t* until a *blocking flow* is reached
    - blocking flow &rarr; situation where we cannot find any more paths from *s* to *t* because most paths are saturated
4. repeat steps 1 to 3 until a blocking flow is reached
5. return the sum the bottleneck values of the augmenting paths found in step 3 above; this is the  max flow
### optimisations
* in its current form, the DFSs may result in dead ends
    - dead ends are undesirable because they extend the length of the journey from *s* to *t*
    - picture what would happen if the same dead end is taken multiple times during a blocking flow iteration
* Dinitz suggested *cleaning* the level graph (ridding it of all dead ends) before each blocking flow phase
* Even and Itai suggested pruning dead ends when backtracking during the DFS stage
    - this trick simplifies and speeds up the algo because dead ends are encountered once and once only
    - Even, by the way, is the idiot that mispronounced Dinitz's name
[def]: https://en.wikipedia.org/wiki/Hopcroft%E2%80%93Karp_algorithm
# ford-fulkerson max flow method
* **Q**: given an infinite input source, how much *flow* can be pused through the network assuming each edge has a pre-defined capacity?
* a greedy algorithm that computes the maximum flow in a flow network
* it is, sometimes, called a *"method"* instead of an *"algorithm"* because the approach to finding augmenting paths in a residual graph is not fully specified or it is specified in several implementations with different running times
* idea behind the algo viz: as long as there is a path from the source (start node) to the sink (end node), considering available capacity on all edges in the path, we send flow along one of the paths. we then find another path and so on. a path with available capacity is called an *augmenting path*
* augmenting path &rarr; a path of edges in the residual graph that has an unused capacity greater than zero from the source, *s*, to the sink, *t*
* bottleneck &rarr; the smallest remaining capacity of any edge along the augmenting path
    - ${\displaystyle min(edge_{capacity} - edge_{flow}) \forall edge \in path_{augmenting}}$
* augmenting the flow &rarr; updating the flow values along the augmenting path
    - for forward edges, this means **increasing** the flow by the bottleneck value
    - for residual edges, this means **decreasing** the flow by the bottleneck value
* residual edges &rarr; a path of edges in a residual graph that travel in the reverse order of the residual path
    - residual edges exist to *undo* bad/sub-optimal augmenting paths (those that do not lead to max flow)
* residual graph &rarr; the graph which contains residual edges. aka flow graph
* remaining capacity &rarr; the difference between an edge's capacity and flow for an edge, *e*, in a graph, *g*
    ${\displaystyle capacity_{remaining} = e_{capacity} - e_{flow} \forall e \in g}$
* :bulb: the sum of botlenecks found in each augmenting path is the max flow
### how it works
* Let *G( V , E )*  be a graph
* for each edge from *u* to *v*, let *c( u , v )* be the capacity and *f( u , v )*  be the flow
* we want to find the maximum flow from the source, *s*, to the sink, *t*
* after every step in the algo the following is maintained:

    ||||
    |:---|:---|:---|
    |Capacity constratints|${\displaystyle \forall (u,v)\in E:\ f(u,v)\leq c(u,v)}$|The flow along an edge cannot exceed its capacity|
    |skew symmetry|${\displaystyle \forall (u,v)\in E:\ f(u,v)=-f(v,u)}$|The net flow from *u* to *v* must be the opposite of the net flow from *v* to *u* |
    |flow conservation|${\displaystyle \forall u\in V:u\neq s{\text{ and }}u\neq t\Rightarrow \sum _{w\in V}f(u,w)=0}$|The net flow to a node is zero, except for the source, which "produces" flow, and the sink, which "consumes" flow|
    |value(f)|${\displaystyle \sum _{(s,u)\in E}f(s,u)=\sum _{(v,t)\in E}f(v,t)}$|The flow leaving from *s* must be equal to the flow arriving at *t*|

### ford-fulkerson algo overview
* input: a network ${\displaystyle G=(V,E)}$ with flow capacity, *c*, a source node, *s*, and a sink node, *t*
* output: compute a flow, *f*, from *s* to *t* of maximum value

    1. ${\displaystyle f(u,v)\leftarrow 0}$ for all edges ${\displaystyle (u,v)}$
    2. while there is a path, *p*, from *s* to *t* in ${\displaystyle G_{f}}$, such that ${\displaystyle c_{f}(u,v)>0}$ for all edges ${\displaystyle (u,v)\in p}$:
        * find ${\displaystyle c_{f}(p)=\min\{c_{f}(u,v):(u,v)\in p\}}$
        * for each edge ${\displaystyle (u,v)\in p}$
            - ${\displaystyle f(u,v)\leftarrow f(u,v)+c_{f}(p)} (Send flow along the path)$
            - ${\displaystyle f(v,u)\leftarrow f(v,u)-c_{f}(p)} (The flow might be "returned" later)$

* "←" denotes assignment. for instance, "largest ← item" means that the value of *largest* changes to the value of *item*
* "return" terminates the algo and outputs the value that follows
### complexity
* assuming one uses a DFS to find and augmenting path, the algo runs at O(fE) time where *f* is max flow and *E* is #edges of graph
* there are faster algos and heuristics
    - edmonds-karp algo uses BFS and runs at O(E<sup>2</sup>V)
    - capacity scaling adds a heuristic on top of ford-fulkerson to pick larger paths first; runs at O(E<sup>2</sup>log(U))
    - dinic's algo uses a combination of DFS and BFS to find augmenting paths; runs at O(V<sup>2</sup>E)
    - push-relabel algo uses a concept of maintaining a *pre-flow* instead of finding augmenting paths to achieve a max-flow solution; O(V<sup>2</sup>E) or O(V<sup>2</sup>E<sup>1/2</sup>) variant
    - **important**: the time complexities for flow algos are quite pessimistic; said algos operate quicker in practice. this makes it hard to compare the performance of flow algos solely based on complexity

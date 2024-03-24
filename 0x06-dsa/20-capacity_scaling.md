# capacity scaling
* more a heuristic than an algo
* idea is to prefer to direct flow through the edges w. larger capacity to those w. smaller cap
    - prioritise taking edges w. a larger capacity to avoid ending up with a path that has a small bottleneck value
* **recall** max flow using DFS
    - worst case scenario: bottleneck value of 1 every iteration; complexity O(fE) (augment flow, *f*, by one unit every iteration)
        1.  start at *s*
        2. follow an edge if cap<sub>remaining</sub> $>$ 0
        3. repeat step 2 until *t*
        4. calc *bottleneck* val
        5. augment (update) flow by:
            - adding said val to flow along forward edges
            - subtracting said value from flow along residual edges
        6. repeat steps 1 to 5 until you get an augmenting path w. max flow
    - this means lots of time to arrive at the max flow
    - what if we could send flow through the edges with the highest cap first?
        * eea...sy! we get to the max flow quicker
        * enter capacity scaling...
### cap scaling algo
* let *U* be the value of the largest edge capacity in the initial flow graph
* let $\Delta$ be the largest power of 2 $\leq$ *U*
* the ***capacity scaling heuristic*** says that you should only take edges whose cap<sub>remaining</sub> is $\geq \Delta$ in order to achieve a better runtime
    - algo will repeatedly find augmenting paths w. cap<sub>remaining</sub>     $\geq \Delta$ until no more paths satisfy the criteria
    - it, then, decreases the value of $\Delta$ by dividing it by 2 ($\Delta$ /= 2) and repeat while $\Delta>0$
* cap scaling with a DFS is bounded by O(E<sup>2</sup>log(U)); O(EVlog(U)), if the shortest augmenting path (eg. edmonds-karp) is used. the latter, however, seems slower in practice
### cap scaling walkthrough
> assumption: you have a bipartite graph. all edges have a flow and cap value
1. compute *U*
    - $U = \displaystyle\max_{\forall i \in n}(capacity_i)$ where *n* &rarr; #nodes and *i* &rarr; node i
2. compute $\Delta$
    - $\Delta = 2^j \leq U$ where *j* is a positive whole number
3. find an augmenting path whose edges have a cap<sub>remaining</sub> $\geq \Delta$
    - starting from the source, follow edges that have a capacity >= delta
4. calc *bottleneck* value
    - $val_{bottleneck} = \displaystyle\min_{\forall i \in n}(capacity_i - flow_i)$
5. augment flow of edges in said path
    - $capacity_i = flow_i + val_{bottleneck}$ if *i* &rarr; forward edge
    - $capacity_i = flow_i - val_{bottleneck}$ if *i* &rarr; residual edge
6. find the new value of $\Delta$
    - $\Delta = \Delta \div 2$
7. repeat steps 1 to 6 while $\Delta > 0$
8. calculate the max flow
    - max flow is the sum of all *bottleneck* values found during each iteration
    - two ways to find max flow
        - $flow_{max} = \displaystyle\sum_{i}val_{bottleneck}_i$ where *i* is the i<sup>th</sup> iteration
        - $flow_{max} = \displaystyle\sum_{i}flow_i$ where *i* is an edge that goes directly to the sink node, *t*
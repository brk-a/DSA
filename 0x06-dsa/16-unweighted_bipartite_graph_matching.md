# unweighted bipartite graph matching
* emphasis on using max flow to find a matching path
* bipartite graph &rarr; a graph whose vertices can  be split into two independent groups, *U* and *V*, such that every edge connects between *U* and *V*
    - definition 2: a graph that is two-colourable
    - definition 3: a graph that has no cycle with an odd length
* maximum cardinality bipartite matching &rarr; bipartite graph matching optimised for maximum pairs that can be connected to each other
    - applications of MCBM
        1. matching candidates to jobs, chairs to desks, dating profiles, location-specific data (eg. SSA & MENA website of a multi-national org)

    ||bipartite graph|non-bipartite graph|
    |:---:|:---|:---|
    |unweighted edges|Easy. Max flow algos. Repeated augmenting paths w. DFS. Hopcroft-Karp algo|Hard. Edmond's blossom algo |
    |weighted edges|Hard. Min-cost-max-flow algo, Hungarian algo (perfect match), LP network simplex|Quite hard. Use a DP soln for small graphs|

    - weighted edges are harder to that unweighted; unbipartite graphs are harder to that bipartite
* emphasis on an unweighted, bipartite graph
* greedy approach will not result in MCBM
    - what now?
    - glad you asked. we turn our problem into a network flow problem then solve it as such
        1. have a sink, *t*, and source, *s*
        2. assign zero flow and unit capacity (capacity of 1) to each edge
            - each edge has `0/1` meaning a flow of zero and a capacity of 1
            - unit capacity enforces the *at-most-one-of-whatever-we-are-considering* constraint
            - say the constraint was *at most N*. the capacity will be N; each edge will have `0/N`
        3. make the edges directed (assuming they were not)
        4. introduce the *s* and *t* nodes at the respective ends of the graph
            - source node *s* is where we start, sink node, *t* is where we end up 
        5. perform steps *ii* and *iii* above at the edges that form when the source and sink nodes are added
        6. use any max flow algo to push flow through the network

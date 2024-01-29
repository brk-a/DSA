# graphs
aka networks

### graph theory
* the mathematical theory of properties and application(s) of graphs

### types of graphs
* graphs are either
    * directed or undirected
    * weighted or unweighted
##### undirected
* edges have no orientation
* edge (u, v) is identical to edge (v, u)
* example: cities as nodes and bidirectional roads as edges
##### directed
* aka digraph
* edges have orientation
* edge (u, v) is not identical to edge (v, u)
    * edge (u, v) is the edge *u* &rarr; *v* and not *v* &rarr; *u*
* example: people as nodes and gifts as edges
##### weighted
* edges have weights; a weight is a value assigned to an edge by whatever rationale
* the edges of an unweighted graph, technically, have a weight of 1
    * why?
        * a weight of zero means that an edge does not exist ([additive identity][def])
        * a weight that is not zero means that the edge exists
        * a weight of 1 means the edge exists but the scalar has no effect on the vector ([multiplicative identity][def2])
* can be directed or undirected
* *(u, v, w)* represents an edge, directed or undirected, connecting *u* and *v* with weight *w*

### special graphs
##### 1. tree
* an undirected graph with no cycles
* has N nodes and N-1 edges
##### 2. rooted tree
* a directed graph that has a designated root node
* N nodes, N-1 edges
* every edge points either away from or towards said root node
    * nodes point *towards* the root: anti-arborescence (aka in-tree)
    * nodes point *away from* the root: arborescence (aka out-tree)
##### 3. directed acyclic graph (DAG)
* a directed graph with no cycles
* all out-trees are DAGs but not all DAGs are out-trees
##### 4. bipartite graph
* an undirected graph with cycles and whose vertices can be split into  two independent groups, *u* and *v* such that every edge connects to *u* and *v*
* there is no odd-length cycle aka two colourable
##### 5. complete graph
* a directed or undirected graph that has a unique edge for every pair of nodes
* a complete graph of *n* vertices is denoted as K~n~

### how to represent a graph
##### 1. adjacency matrix
* in an adjacency matrix, cell m\[i\][j] represents the edge weight, *w*, of going from node *i* to node *j*
* the cost to move to self is zero (which make sense because a node is itself)
* example

    ```text
    [
        [0, 4, 1, 0]
        [0, 0, 6, 0]
        [4, 1, 0, 2]
        [0, 0, 0, 0]
    ]
    ```

|pros|cons|
|:---:|:---:|
|space-efficient when representing dense graphs|requires O(V<sup>2</sup>) space|
|edge weight look-up is O(1)|iterating over all edges takes O(V<sup>2</sup>) time|
|simplest way to represent a graph||

##### 2. adjacency list
* a map of nodes to lists of edges \[and weights\]
* example

    A &rarr; \[(B, 4), (C, 1)\]
    B &rarr; \[(C, 6)\]
    C &rarr; \[(A, 4), (B, 1), (D, 2)\]
    D &rarr; \[\]

    * node C can reach
        * A at a cost of 4
        * B at a cost of 1
        * D at a cost of 2

pros|cons|
|:---:|:---:|
|space-efficient when representing sparse graphs|less space-efficient for dense graphs|
|iterating over all edges is efficient|edge weight look-up is O(E)|
||slightly more complex than adjacency matrix to represent a graph|

##### 3. edge list
* represents a graph as an unordered list of edges
* consider the triplets (u, v, w): the cost from node *u* to *v* is *w*
* example

    \[(C, A, 4), (A, C, 1), (B, C, 6), (A, B,4), (C, B, 1), (C, D, 2)\]

* this representation is seldom used because it lacks structure, however, it is conceptually simple and practical in a handful of algos

|pros|cons|
|:---:|:---:|
|space-efficient when representing sparse graphs|less space-efficient for dense graphs|
|iterating over all edges is efficient|edge weight look-up is O(E)|
|simple way to represent a graph||

###  how to approach graph problems
* ask yourself:
    1. is the graph directed? (directed/undirected)
    2. are the edges of the graph weighted? (weighted/unweighted)
    3. will the graph you encounter be sparse? (sparse/dense)
    4. should you use an adjacency matrix, adjacency list, edge list or some other structure to represent the graph? (matrix/list/edge list/other)

[def]: https://math.libretexts.org/Bookshelves/PreAlgebra/Prealgebra_1e_(OpenStax)/07%3A_The_Properties_of_Real_Numbers/7.05%3A_Properties_of_Identity_Inverses_and_Zero
[def2]: https://math.libretexts.org/Bookshelves/PreAlgebra/Prealgebra_1e_(OpenStax)/07%3A_The_Properties_of_Real_Numbers/7.05%3A_Properties_of_Identity_Inverses_and_Zero
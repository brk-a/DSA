# common graph problems

###  how to approach graph problems
* ask yourself:
    1. is the graph directed? (directed/undirected)
    2. are the edges of the graph weighted? (weighted/unweighted)
    3. will the graph you encounter be sparse? (sparse/dense)
    4. should you use an adjacency matrix, adjacency list, edge list or some other structure to represent the graph? (matrix/list/edge list/other)

#### shortest path problem
* given a weighted graph, find the shortest path of edges from start node  to end node

    <ul>
    <li>- [x] directed</li>
    <li>- [x] weighted</li>
    <li>- [] sparse</li> &rarr; variable input size
    </ul>

* algos to use
    * BFS &rarr; unweighted graph
    * Dijkstra's
    * Bellman-Ford
    * Floyd-Warshall
    * A<sup>*</sup>
    * ...

#### connectivity problem
* given a graph, does there exist a path between node X and Y?

    <ul>
    <li>- [] directed</li> &rarr; different rules for directed and undirected
    <li>- [] weighted</li> &rarr; does not matter
    <li>- [] sparse</li> &rarr; variable input size
    </ul>

* algos to use
    * BFS, DFS or any other  search algo
    * apply a union-find data structure

#### negative cycles problem
* given a weighted digraph, are there negative cycles? if yes, where?
    * a negative cycle is, simply, a cycle whose sum of weights is less than zero

    <ul>
    <li>- [x] directed</li>
    <li>- [x] weighted</li>
    <li>- [] sparse</li> &rarr; variable input size
    </ul>

* algos to use
    * Bellman-Ford
    * Floyd-Warshall

#### strongly connected components problem
* given a directed graph, are there SCCs? if yes, where?
    * an SCC is, simply, a cycle within a directed graph where every vertex in said cycle can reach, directly or indirectly, every other vertex in the same cycle

    <ul>
    <li>- [x] directed</li>
    <li>- [] weighted</li> &rarr; does not matter
    <li>- [] sparse</li> &rarr; variable input size
    </ul>

* algos to use
    * Tarjan's
    * Kosaraju's

#### travelling salesman problem
* given a directed, weighted graph, what is the shortest possible route that visits each node exactly once and returns to the origin?

    <ul>
    <li>- [x] directed</li>
    <li>- [x] weighted</li>
    <li>- [] sparse</li> &rarr; variable input size
    </ul>

* algos to use
    * Held-Karp
    * branch-and-bound
    * ...
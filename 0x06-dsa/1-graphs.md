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
    <li>- [] sparse</li>
    </ul>

    neither sparse or dense because variable input size

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
    <li>- [] directed</li>
    <li>- [] weighted</li>
    <li>- [] sparse</li>
    </ul>

    - not directed because different rules for directed and undirected
    - weight does not matter
    - neither sparse or dense because variable input size

* algos to use
    * BFS, DFS or any other  search algo
    * apply a union-find data structure

#### negative cycles problem
* given a weighted digraph, are there negative cycles? if yes, where?
    * a negative cycle is, simply, a cycle whose sum of weights is less than zero

    <ul>
    <li>- [x] directed</li>
    <li>- [x] weighted</li>
    <li>- [] sparse</li>
    </ul>

    - neither sparse or dense because variable input size

* algos to use
    * Bellman-Ford
    * Floyd-Warshall

#### strongly connected components problem
* given a directed graph, are there SCCs? if yes, where?
    * an SCC is, simply, a cycle within a directed graph where every vertex in said cycle can reach, directly or indirectly, every other vertex in the same cycle

    <ul>
    <li>- [x] directed</li>
    <li>- [] weighted</li>
    <li>- [] sparse</li>
    </ul>

    - weight does not matter
    - neither sparse or dense because variable input size

* algos to use
    * Tarjan's
    * Kosaraju's

#### travelling salesman problem
* given a directed, weighted graph, what is the shortest possible route that visits each node exactly once and returns to the origin?

    <ul>
    <li>- [x] directed</li>
    <li>- [x] weighted</li>
    <li>- [] sparse</li>
    </ul>

    - neither sparse or dense because variable input size

* algos to use
    * Held-Karp
    * branch-and-bound
    * ...

#### bridges problem
* given a graph, is there a bridge (cut edge)?
    * a bridge is any edge in a graph whose removal increases the number of connected components (increases the number of graphs)
    * bridges hint at weaknesses , bottlenecks, vulnerabilities etc

    <ul>
    <li>- [] directed</li>
    <li>- [] weighted</li>
    <li>- [] sparse</li>
    </ul>

    - neither sparse or dense because variable input size

* algos to use
    * 

#### articulation points problem
* given a graph, are there articulation points (cut vertex)?
    * an articulation point is any vertex in a graph whose removal increases the number of connected components (increases the number of graphs)
    * articulation points hint at weaknesses , bottlenecks, vulnerabilities etc

    <ul>
    <li>- [] directed</li>
    <li>- [] weighted</li>
    <li>- [] sparse</li>
    </ul>

    - neither sparse or dense because variable input size

* algos to use
    *  

#### minimum spanning tree problem
* given a graph and a target total weight, how many minimum spanning trees are there?
    * a minimum spanning tree is a sub-set of the edges of a connected, edge-weighted graph that connects all the vertices together w/o any cycles and has the minimum possible total target weight (a tree that spans al the nodes at the least cost)
    * MSTs are applied in least cost networks, circuit design, transportation networks etc

    <ul>
    <li>- [] directed</li>
    <li>- [x] weighted</li>
    <li>- [] sparse</li>
    </ul>

    - weighted because constraints have a target total weight
    - neither sparse or dense because variable input size

* algos to use
    *  Kruskal's
    * Prim's
    * Boruvka's

#### network flow: max flow problem
* given an infinite input source and a network, how much *flow* (throughput) can be pushed through said network?
    * a network is a directed, weighted graph whose weights represent the capacity of the edge
    * edges represent, among others:
        1. roads w. vehicles
        2. pipes w. water
        3. corridors w. people
    * *flow* represents, among others:
        1. #vehicles the roads sustain in traffic
        2. amount/volume of water that flows through the pipes
        3. #people that navigate the corridors

    <ul>
    <li>- [x] directed</li>
    <li>- [x] weighted</li>
    <li>- [] sparse</li>
    </ul>

    - weighted because constraints have a target total weight
    - neither sparse or dense because variable input size

* algos to use
    * Ford-Fulkerson
    * Edmonds-Karp
    * Dinic's
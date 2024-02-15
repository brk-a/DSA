# bellman-ford algorithm
* a SSSP algo (can find the shortest path from a node to any other node)
* not ideal, however, for SSSP problems because its time complexity is O(EV)
    * dijkstra's is O((E+V)log(V)) when using a binary heap PQ
* on the other hand, bellman-ford algo works with negative edge weights (unlike dijkstra's)
    * it can detect negative cycles
    * application: in finance to perform arbitrage between two markets
* negative cycle &rarr; a cycle whose net cost is less than zero
    *  negative cycle has occurred if an algo can find a better path beyond the optimal solution

### bf algo

    ```text
        //E -> #edges in graph
        //V -> #vertices in graph
        //S -> id of start node
        //D -> array of size V that tracks the best distance from S to each node
    ```
# dijkstra's algorithm
* a SSSP (single-source shorest path) algo for graph w. non-negative edge weights
* time complexity is, typically<sup>*</sup>, O(E*log(V)) where *E* is #edges and *V* is #vertices of a graph
    * <sup>*</sup>keyword *typically*. this performance depends on
        1. how the algo is implemented
        2. what data structures are used
### constraints
1. graph must contain non-negative edge weights
    * why?
        * glad you asked. non-negative edge weights ensure that the optimal distance of a node, once visited, cannot be improved 
        * why?
            * ea...sy! this property causes dijkstra's algo to act in a greedy manner, that is, it always selects the next most promising node
### lazy dijkstra's algo, summarised
1. maintain a `dist` array where the distance to every node (from the start, of course) is positive infinity
2. set the distance to the start node, `s`, to zero
3. maintain a PQ (priority queue) of key-value pairs, `(nodeIndex, distance)`, that tell what node to visit next based on sorted min value
4. place (s, 0) on the PQ
5. while PQ is not empty, get the next most promising `(nodeIndex, distance)` pair from said queue
6. iterate outwards over all edges starting at the current node
7. relax each edge
8. append a new `(nodeIndex, distance)` pair to the PQ for every rick-laxation (see what I did there?)
### pseudo-code for lazy dijkstra's algo

    ```text
        //lazy dijkstra's algo
        // g -> adjacency list that reps weighted graph
        // n - #nodes in graph
        // s -> idex of start node (0 <= s < n)
        function dijkstra(g, n, s):
            vis = [false, ..., false] //size n
            dist = [inf, ..., inf] //size n
            dist[s] = 0
            pq = empty priority queue
            pq.insert((s, 0))

            while pq.size()!=0:
                index, minValue = pq.poll()
                vis[index] = true
                if dist[index]<minValue:
                    continue
                for (edge: g[index]):
                    if vis[edge.to]:
                        continue
                    newDist = dist[index] + edge.cost //edge.cost is the same as edge.weight or edge.dist
                    if newDist<dist[edge.to]:
                        dist[edge.to] = newDist
                        pq.insert((edge.to, newDist))
            return dist
    ```
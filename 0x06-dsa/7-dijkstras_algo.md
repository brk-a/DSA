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
###  lazy dijkstra's algo
#### pseudo-code for lazy dijkstra's core algo

    ```text
        //lazy dijkstra's algo
        // g -> adjacency list that reps weighted graph
        // n - #nodes in graph
        // s -> index of start node (0 <= s < n)
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

#### shortest path
* this is all well and good; it returns the optimal distance to every node given a starting point. what if we want the optimal path to a specific node given a start node?
    * ea...sy! track the index of the nodes visited
        * idea is to maintain an array representing the path taken and relax it every time an optimal option presents itself
        * said array will contain the optimal path when the algo iterates over all nodes
        * return this array
* pseudo-code

    ```code
        //runs dijkstra's algo and returns two arrays: one that contains the shortest distance to every node from the start node, s, and one that has the shortest path to an end node, e, from a start node, s
        // g -> adjacency list that reps weighted graph
        // n - #nodes in graph
        // s -> index of start node (0 <= s < n)
        // prev -> array containing shortest path
        function dijkstra(g, n, s):
            vis = [false, ..., false] //size n
            prev = [null, ..., null] //size n
            dist = [inf, ..., inf] //size n
            dist[s] = 0
            pq = empty priority queue
            pq.insert((s, 0))

            while pq.size()!=0:
                index, minValue = pq.poll()
                vis[index] = true
                if dist[index]<minValue:
                    continue
                
                for(edge: g[index]):
                    if vis[edge.to]:
                        continue
                    newDist = dist[index] + edge.cost
                    if newDist<dist[edge.to]:
                        prev[edge.to] = index
                        dist[edge.to] = newDist
                        pq.insert((edge.to, newDist))
            
            return (dist, prev)

        //finds shortest path using the `prev` array returned above
        // g -> adjacency list that reps weighted graph
        // n - #nodes in graph
        // s -> index of start node (0 <= s < n)
        // e -> index of end node (0 <= e < n)
        function findShortestPath(g, n, s, e):
            dist, prev = dijkstra(g, n, s)
            path = []
            if dist[e]==inf:
                return path
            
            for(at=e; at!=null; at=prev[at]):
                path.add(at)
            path.reverse()

            return path
    ```

#### stopping early
* suppose one knows the start and destination node. does one need to visit every node in the graph?
    * TLDR: yes and no:
        * yes, in the worst case (say, the end node is at the end of the PQ)
        * no, one can stop the algo as soon as the end node is visited
* the rationale behind stopping early is this: dijkstra's algo each next-most-promising-node in order, therefore, the shortest distance will not change when the destination node is visited
    * simple language: dijkstra's algo is greedy (in other words, [efficient][def]), therefore, gets the shortest path as soon as it visits the destination node
* pseudo-code

    ```text
        //finds shortest path using the `prev` array returned above
        // g -> adjacency list that reps weighted graph
        // n - #nodes in graph
        // s -> index of start node (0 <= s < n)
        // e -> index of end node (0 <= e < n)
        function dijkstra(g, n, s, e):
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
                for(edge: g[index]):
                    if vis[edge.to]:
                        continue
                    newDist = dist[index] + edge.to
                    if newDist<dist[edge.to]:
                        dist[edge.to] = newDist
                        pq.insert((edge.to, newDist))
                if index==e:
                    return dist[e]
                return inf
                
    ```

#### drawbacks
* inefficient for dense graphs because of how it inserts duplicate key-value pairs in the PQ
    * now, it is more efficient to insert a new k-v pair (O(lon(n)) time) in the PQ than it is to update an existing key's value (O(n) time)
    * this has a vulnerability: there will be stale (outdated) k-v pairs in the PQ
    * vulnerability manifests in dense graphs
### eager djikstra's algo
* fixes the drawbacks of the lazy dijkstra's algo: O(n) time to update k-v pairs in a PQ
* how?
    * glad you asked: by updating the k-v pairs in O(log(n)) time
    * how?
        * eeaaa...sy! IPQs (indexed priority queues)
* WTF is an IPQ?
    * a PQ that implements reads in O(1) time and writes in O(log(n)) time
* pseudo-code

    ```text
        //runs dijkstra's algo and returns the shortest distance between the start and every other node
        // g -> adjacency list that reps weighted graph
        // n - #nodes in graph
        // s -> index of start node (0 <= s < n) 
        function dijkstra(g, n, s):
            vis = [false, ..., false] //size n
            dist = [inf, ..., inf] //size n
            dist[s] = 0
            ipq = empty IPQ
            ipq.insert(s, 0)

            while ipq.size()!=0:
                index, minValue = ipq.poll()
                vis[index] = true
                if dist[index]<minValue:
                    continue
                for (edhe: g[index]):
                    if(vis[edge.to]):
                        continue
                    newDist = dist[index] + edge.cost
                    if newDist<dist[edge.to]:
                        dist[edge.to] = newDist
                        if !ipq.contains(edge.to):
                            ipq.insert(edge.to)
                        else:
                            ipq.decreaseKey(edge.to, newDist)
            return dist
    ```

### D-ary heap optimisation
* for dense graphs, there are more updates (ie decreaseKey operations) to k-v pairs than there are dequeue (poll operations)
* a D-ary heap can optimise operations
    * a D-ary heap is a heap variant where each node has, at most, D children
    * the children speed up decreaseKey ops at the expense of costlier removals
* what is the optimal D-ary heap degree (what is the optimal value of D) to maximise the performance of dijkstra's algo?
    * TLDR: generally, D = E/V, however, there better be very few, if any, decreaseKey ops
    * generally, D = E/V is the best degree to use to balance between removals decreaseKey ops
    * the algo's time complexity will be O(log<sub>E/V</sub>(V)). this is alright for dense graphs that have a lot of decreaseKey ops
* is there a way to tell if a graph will have many decreaseKey ops?
    * i do not know
* **fibonacci heap**
    * this is the state-of-the-art (Sun, 11 Feb, 2024) optimisation
    * gives the algo a time complexity of O(E + Vlog(V))
    * in practice, however, it is difficult to implement and has a large enough constant armotised overhead to make it impractical unless the graph is quite large
        * big data? AI? quantum comps?

[def]: https://www.oxfordlearnersdictionaries.com/definition/english/efficiency
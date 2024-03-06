# prim's minimum spanning tree algo
#### lazy version

* MST, in an undirected, weighted graph, is a subset of edges on said graph which connects all vertices w/o creating a cycle and at the minimum possible total edge cost
    - a graph can have more than one MST, however, by definition, the cost will be the same for each
    - a graph must contain one, and only one, single connected component for a MST to exist
* prim's algo is greedy and works well on dense graphs
    - it meets or improves on the time bounds of its popular rivals (Kruskal's and Boruvka's)
    - algo must be run on each connected component individually when finding the min spanning forest on a disconnected graph
* the lazy version has a run time of O(E\*log(E)); the eager version has a better runtime: O(E\*log(V)) 
    - E &rarr; #edges
    - V &rarr; #vertices
### lazy prim's MST algo overview
1. maintain a PQ that sorts edges based on min edge cost
    - this will be used to determmine the next node to visit and edge used to get there
2. start the  algo on any node, s
    - mark `s` as visited
3. iterate over all edges of `s`
    - add them to PQ
4. while PQ is not empty and MST has not been formed, dequeue the next cheapest edge from the PQ
    - if the dequeued edge is outdated (the node it points to has already been visited), skip it and poll again
    - else:
        * mark current node as visited
        * add selected edge to MST
5. iterate over the new current node's edges and add all its edges to the PQ
    - do not add edges which point to already visited nodes to the PQ
### pseudo-code

    ```text
        n = #nodes in graph
        pq = PQ data structure. stores edge objects as {start_node, end_node, cost} tuples. sorts edges based min edge cost
        g = graph representing an adjacency list of weighted edges
        //each undirected edge is represented as two directed edges: one to and one from a node
        //prefer an ajacency matrix to an ajacency list for dense graphs
        visited = [false, ..., false] //tracks whether a node, i, has been visited; size n
        function lazyPrims(s=0): //s -> index of start_node (0 <= s < n)
            m = n - 1 // #edges in MST
            edgeCount, mstCount = 0, 0
            mstEdges = [null, ..., null] // size m (m for mombasa)
            addEdges(s)

            while(!pq.isEmpty() and edgeCount!=m):
                edge = pq.dequeue()
                nodeIndex = edge.to

                if(visited[nodeIndex]):
                    continue
                
                mstEdges[edgeCount++] = edge
                mstCost += edge.cost

                addEdges(nodeIndex)

            if(edgeCount!=m):
                return (null, null) #no MST exists

            return (mstCost, mstEdges)

        function addEdges(nodeIndex){
            //mark current node as visited
            visited[nodeIndex] = true

            //iterate over all edges going outwards from current node
            //add edges to PQ (which point to unvisited nodes)
            edges = g[nodeIndex]
            for(edge: edges):
                if(!visited[edge.to]):
                    pq.enqueue(edge)
        }
    ```
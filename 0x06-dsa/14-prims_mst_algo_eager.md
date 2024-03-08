# prim's minimum spanning tree algo
#### eager version
* lazy version inserts E edges into the PQ; each poll op on the PQ is O(log(E))
* instead of blindly inserting edges into a PQ, which could later become stale, the eager version tracks *(node, edge)* key-value pairs, that are easy to update and poll, to det'n the next best edge to add to the MST
> for amy MST w. directed edges, each node except the start node is paired with one, and only one, of its incoming edges.
> the idea, in the eager version of the algo, is to det'n which of a node's incoming edges should be selscted to be added to the MST
* we *relax* (update) the destination node's most promising edge as we iterate over the edges of said node instead of adding edges to the PQ as we did in the lazy version
* Q: how will we retrieve and update said *(node, edge)* pairs efficiently?
    - glad you asked. one way is to use a IPQ (indexed PQ); it reduces the overall time complexity  from O(E\*log(E)) to O(E\*log(V)) because there are V k:v pairs in the IPQ.the poll/update ops in said IPQ take O(log(V)) time
### eager prim's MST algo overview
1. maintain a min IPQ of size V that sorts vertex-edge pairs, *(v, e)*, by the min edge cost of *e*
    - all the vertices have a best value of *(v, null)* in the IPQ
2. start the algo on any node, *s*
    - mark *s* as visited
    - relax all edges of *s*
3. while IPQ is not empty and MST has not been formed
    - dequeue the next best *(v, e)* pair from the IPQ
    - mark node *v* as visited
    - add edge *e* to MST
4. relax all edges of *v* while making sure not to relax any edge that points to a node which has already been visited
    - *relaxing*, in this context, means updating the entry for node *v* in the IPQ from *(v, e<sub>old</sub>)* to *(v, e<sub>new</sub>)* if the new edge, *e<sub>new</sub>*, from *u* &rarr; *v* has a lower cost than e<sub>old</sub>
### pseudo-code

    ```text
        n = #nodes in graph
        ipq = IPQ data structure. stores (node_index, edge_object) pairs
            //`edge_object` is a {start_node, end_node, cost} tuple
            //IPQ sorts (node_index, edge_object) pairs based on min edge cost
        g = graph representing an adjacency list of weighted edges
            //each undirected edge is rep'd as two directed edges: one to and another from a node. prefer an adjacency matrix to an adjacency list for dense graphs
        visited = [false, ..., false] //visited[i] tracks whether node i has been visited; size n

        function eagerPrims(s=0):
            //s -> index of start node (0 <= s < n)
            m = n - 1 // #edges in MST
            edgeCount, mstCost = 0, 0
            mstEdges = [null, ..., null] //size m

            relaxEdgesAtNode(s)

            while(!ipq.isEmpty() && edgeCount!=m):
                //extract next best (node_index, edge_object) pair from ipq
                destNodeIndex, edge = ipq.dequeue()

                mstEdges[edgeCount++] = edge
                mstCost += edge.cost

                relaxEdgesAtNode(desNodeIndex)
            if(edgeCount!=m):
                return (null, null) //no MST exists
            return (mstCost, mstEdges)

        function relaxEdgesAtNode(currentNodeIndex) {
            //mark current node as visited
            visited[currentNodeIndex] = true

            //get all edges going outward from curr node
            edges = g[currentNodeIndex]

            for(edge: edges):
                destNodeIndex = edge.to

                //skip edges pointing to already visited nodes
                if(visited[destNodeIndex]):
                    continue
                
                //rick-lax the edges
                if(visited[destNodeIndex]):
                    //insert edge into ipq; first time
                    ipq.insert(destNodeIndex, edge)
                else:
                    //improve the cheapest edge at destNodeIndex with the current edge in the IPQ
                    ipq.decreaseKey(destNodeIndex, edge)
        }
    ```
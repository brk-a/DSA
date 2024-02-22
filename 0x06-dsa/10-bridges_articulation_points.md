# bridges and articulation points
* bridge (aka cut edge) is any edge in a graph whose removal increases the number of connected components
* articulation point (aka cut vertex) is any vertex/node in a graph whose removal increases the number of connected components
* consider the following graph

    ```text
        [
            (0, 1),
            (0, 2),
            (1, 2),
            (2, 3),
            (2, 5),
            (3, 4),
            (5, 6),
            (5, 8),
            (6, 7),
            (7, 8),
        ]

        how to read: (0, 1) means there is an edge between node zero and node one (node 0 is connected to node 1)
    ```

* edges (2, 3), (2, 5) and (3, 4) are bridges
* vertices 2, 3 and 5 are articulation points
* bridges annd articulation points hint at weaknesses , bottlenecks, vulnerabilities etc
* idea is to:
    1. perform a DFS traversal
    2. label nodes w. an increasing id value as you go
    3. track the id of each node and the smallest *low-link* value
        - during the DFS, bridges will be found where the id of the node the edge is coming from is less than the low-link value of the node said edge is going to
        - *low-link* value of a node is the smallest/lowest id reachable from that node when doing a DFS, including itself
    4. return the bridges/articulation points as case may be
### finding low-link values
* consider the graph above
* set the low-link value of each to the corresponding node id
    * zero for node zero, 1 for node 1 etc
* start at node zero
    1. is there a way to node zero from node zero?
        - yes: take it and update the low-link value to zero
        - no: continue
    2. is there a way to node zero from node 1?
        - yes: take it and update the low-link value to zero
        - no: is there a way to node 1 from node 1?
            * yes: take it and update the low-link value to 1
            * no: continue
    3. is there a way from node N to node zero?
        - yes: take it and update the low-link value to zero
        - no: is there a way to node 1 from node N?
            * yes: take it and update the low-link value to 1
            * no: is there a way to node k from node N?
                * yes: take it and update the low-link value to k
                * no: recurse for k = {2, 3, ..., N} until a low-link value is found
### complexity
* O(V(V+E)) time because
    - one DFS to label all nodes
    - V more DFSs to find all low-link values
* we can optimise the algo if we update the low-link values in one pass
* the optimisation results in O(V+E) time
### pseudo-code: finding bridges

    ```code
        id = 0
        g = adjacency list with undirected edges
        n = size of graph

        //in these arrays, index i is the node i
        ids = [0, 0, ..., 0] //size n
        low = [0, 0, ..., 0] //size n
        visited = [false, false, ..., false] //size n

        function findBridges():
            bridges = []
            //find all bridges in graph across various connected components
            for(i:=0; i<n; i++):
                if(!visited[i]):
                    dfs(i, -1, bridges)
            return bridges
        
        function dfs(at, parent, bridges):
            //perform DFS to find bridges
            //at -> current node, parent -> prev node
            //`bridges` array has an even length always and
            // indices (2*i, 2*i +1) form a bridge
            //par example: nodes at indices (0, 1) are a bridge, as are (2, 3) etc
            visited[at] = true
            id += 1
            low[at] = ids[at] = id

            //for each node from node `at` to node `to`
            for(to: g[at]):
                if(to==parent):
                    continue
                if(!visited[to]):
                    dfs(to, at, bridges)
                    low[at] = min(low[at], low[to])
                    if(ids[at]<low[to]):
                        bridges.add(at)
                        bridges.add(to)
                else:
                    low[at] = min(low[at], ids[to])  
    ```

### articulation points
* on a connected components with three or more vertices, if an edge, *(u, v)*, is a bridge, then either node *u* or *v* is an articulation point
    *  this condition is not sufficient for all articulation points
    * consider the following graph

        ```text
            [
                (0, 1),
                (0, 2),
                (1, 2),
                (2, 3),
                (2, 4),
                (3, 4),
            ]
        ```

    * node 2 is an articulation point even though there are no bridges
*  recall, when finding a bridge, we find a cycle and set the low-link value as the lesser of `low[at]` and `ids[to]` during the callback
    * notice that, at the end of the stack frame, all the lo-link values are equal to the id of the parent node (one which started the cycle)
    * that, right there, is an insight: a cycle will be a node whose id is equal to its low-link value after the stack frame has cleared
    * the start of that cycle is the articulation point
* in other words

    ```text
        if(id(e.from)==lowlink(e.to)) {
            //we have a cycle
        }

        //e must be an articulation point
    ```

* a cycle is an indication of a strongly connected component. if one removes the start node, assuming it was connected to another component, then there will be at least two components. the start node, by definition, is an articulation point
    * EXCEPTION: start node has zero or 1 outgoing directed edges. either the node is a singleton/stand-alone (zero case) or part of(trapped in) the cycle (1 case)
    * the `id(e.from)==lowlink(e.to)` is met but the node has either zero or 1 outgoing edges
    * example

        ```text
            [
                (0, 1),
                (1, 2),
                (2, 3),
                (3, 0),
            ]
        ```

    * say node zero is the start node. it is not an articulation point because it is part of the cycle not the parent (matter of fact, there is no parent here)
    * say we added another node, 4, from zero viz:

        ```text
            [
                (0, 1),
                (0, 4),
                (1, 2),
                (2, 3),
                (3, 0),
            ]
        ```

    * node zero becomes an articulation point
### pseudo-code: finding articulation points

    ```code
        id = 0
        g = adjacency list with undirected edges
        n = size of graph
        outEdgeCount = 0

        //in these arrays, index i is the node i
        ids = [0, 0, ..., 0] //size n
        low = [0, 0, ..., 0] //size n
        visited = [false, false, ..., false] //size n
        isArt = [false, ..., false] //size n

        function findBridges():
            bridges = []
            //find all bridges in graph across various connected components
            for(i:=0; i<n; i++):
                if(!visited[i]):
                    outEdgeCount = 0 //reset edge count
                    dfs(i, i, -1)
                    isArt[i] = (outEdgeCount > 1)
            return isArt
        
        function dfs(root, at, parent):
            //perform DFS to find bridges
            //at -> current node, parent -> prev node
            if(parent==root):
                return outEdgeCount++
            
            visited[at] = true
            id += 1
            low[at] = ids[at] = id

            //for each node from node `at` to node `to`
            for(to: g[at]):
                if(to==parent):
                    continue
                if(!visited[to]):
                    dfs(root, to, at)
                    low[at] = min(low[at], low[to])
                    //articulation point found via bridge
                    if(ids[at]<low[to]):
                        isArt[at] = true
                    //articulation point found via cycle
                    if(ids[at]==low[to]):
                        isArt[at] =true
                else:
                    low[at] = min(low[at], ids[to])  
    ```
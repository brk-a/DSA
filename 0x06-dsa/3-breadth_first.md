# # breadth-first search
* does as its name suggests: traverses the edges/nodes of a graph breadth first
    * breadth first: pick a node. explore all the children nodes of that node. pick one of the children nodes. repeat process w/o visiting already visited nodes 
* time complexity is O(V+E) where *V* is the #vertices (nodes) and *E* is #edges
* particularly useful for finding the shortest path on unweighted graphs

### how it works
* consider the following pseudo-code

    ```text
    //global/class scope vars
    n = #nodes in graph
    g adjacency list that reps an unweighted graph

    // s = start node; e = end node; 0<=e, s<n
    function bfs(s, e):
        //start at node s
        prev = solve(s)

        //return reconstructed path from s to e
        return reconstructPath(s, e, prev)

    function solve(s):
        q = queue data structure w. enqueue and dequeue
        q.enqueue(s)

        visited = [false, ..., false]  //size n
        visited[s] = true

        prev = [null, ..., null]  //size n
        while !q.isEmpty()
            node = q.dequeue()
            children = g.get(node)

            for(next: children):
                if !visited[next]:
                    q.enqueue(next)
                    visited[next] = true
                    prev[next] = node
        return prev
    
    function reconstructPath(s, e, prev):
        //create a path from e to s
        path = []
        for(at=e; at!=null; at=prev[at]):
            path.add(at)
        
        path.reverse() //s -> e

        //return path when s and e are connected
        if path[0]==s:
            return path
        
        return []
    ```

* utilises a queue to track nodes
    * current node is added to queue
    * tracker pointer points to current node
    * children nodes are added to queue
    * tracker pointer moves to next item on queue
    * its children are added to queue IFF they are not on the queue
    * repeat process until all nodes are on the queue

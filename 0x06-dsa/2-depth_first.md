# depth-first search
* does as its name suggests: traverses the edges/nodes of a graph depth first
    * depth first: pick a node, follow one edge and so on until the terminal node. return to the highest-level unexplored node. repeat process w/o visiting already visited nodes 
* time complexity is O(V+E) where *V* is the #vertices (nodes) and *E* is #edges

### how it works
* consider the following pseudo-code

    ```text
        // global/class scope vars
        n = number of nodes
        g = adjacency list that represents a graph
        visited = [false, ..., false] //size n

        function dfs(at):
            if visited[at]: return
            visited[at] = true

            neighbours = graph[at]
            for next in neighbours:
                dfs(next)
        
        //start dfs at node zero
        start_node = 0
        dfs(start_node)
    ```

* you need:
    1. *n* &rarr; #nodes
    2. *g* &rarr; the graph to traverse
    3. *visited* &rarr; an array of booleans, size *n*, that is initially set to false
* to call the *dfs* algo, you must initialise a start node and pass said node to *dfs*
* the algo accepts a node, *at*, as an arg. it is recursive
    * base case: has current node, *at*, been visited?
        * yes: return (backtrack to the highest-level unexplored node)
        * no: carry on
    * set the value of *visited* at *at* to `true`
    * explore neighbours of the node
        * get all the neighbours from the adjacency list
        * call the algo on each (this is the recursion part)
* utilises a stack to track the nodes
    * current node is added to stack
    * tracker pointer points to current node
    * one child node is added to stack
    * tracker pointer moves to next item on stack
    * one of its children is added to stack IFF it is not on the stack
    * repeat process until all nodes are on the stack

### use case: connected components
#### problem
* given a graph that may have multiple disjoint components, how many components, if any, are there?
#### solution
* label all nodes using the range [0, n) where *n* is the number of nodes
* idea is to perform DFS such that all connected components are assigned a common id
    1. start at node zero. visit all the nodes connected to said node
    2. assign said nodes an id, say, zero
    3. go to node 1. check if it has already been visited
        * yes: skip
        * no: perform steps 1 and 2
    4. repeat until all nodes in the range [0, n) are covered
    5. return the max common id number
* pseudo-code

    ```text
        // global/class scope vars
        n = number of nodes
        g = adjacency list that represents a graph
        visited = [false, ..., false] //size n
        components = empty int array //size n
        count = 0 #components

        function findComponents():
            for(i=0; i<n; i++):
                if !visited[i]:
                    count++
                    dfs(i)
            return (count, components)

        function dfs(at):
            visited[at] = true
            components[at] = count
            for(next: g[at]):
                dfs(next)
    ```

### what else can DFS do?
* compute a graph's MST
* detect and locate cycles in a graph
* detect if a graph is bipartite
* find strongly connected components
* sort the nodes of a graph topologically
* find bridges and articulation points
* find augmenting paths in a flow network
* generate mazes
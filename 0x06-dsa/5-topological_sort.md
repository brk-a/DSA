# topological sort
* a topological ordering is where nodes in a directed graph have an order of appearance/placement in said graph
    * simple language: one can put the nodes of the graph in a straight line, in a certain order, with each node pointing to whatever node it should point to
    * top orderings are not unique
    * not every graph has a topological ordering. a graph that contains a cycle has no valid ordering
        * cycles are like circles: no end and no beginning
    * DAGs are graphs w. directed edges and no cycles. these are the only type of graph with a valid top ordering
        * also, rooted trees, by definition, have top ordering
    * Q: how do I verify that my graph has no directed cycles?
        * A: glad you asked. use Tarjan's strongly connected component algo among others
* many real-world problems canbe modelled as a graph with directed edges where some events must occur before others
    * school class prerequisites
    * programme dependencies
    * event scheduling
    * assembly instructions
    * ...
* a top sort can find a top ordering in O(V+E) time where *V* is the #vertices and *E* is #edges

### how it works
* uses dfs algo to visit all unvisited nodes of a graph, given a starting point
    1. pushes the value of current node onto the stack
    2. visits a unvisited child node
    3. child node becomes current node
    4. checks if node is leaf node
        * yes:
            * adds value of node to ordering
            * sets parent node as current node
            * performs steps 2 - 4 until leaf node is reached
        * no: carry on
    5. performs steps 1 to 4 until stack is empty

* pseudo-code

    ```text
        //assumption: graph is sorted as an adjacency list
        function topsort(graph):
            N = graph.NumberOfNodes()
            V = [false, ..., false] //size N
            ordering = [0, ..., 0] //size N
            i = N - 1 // index for ordering array

            for(at=0; at<N; at++):
                if V[at]==false:
                    visitedNodes = []
                    dfs(at, V, visitedNodes, graph)
                    for nodeId in visitedNodes:
                        ordering[i] = nodeId
                        i -= 1
            return ordering

        function dfs(at, V, visitedNodes, graph):
            V[at] = true

            edges = graph.getEdgesOutFromNode(at)
            for edge in edges:
                if V[edge.to]==false:
                    dfs(edge.to, V, visitedNodes, graph)
            
            visitedNodes.add(at)
    ```

* optimising the algo

    ```text
        //assumption: graph is sorted as an adjacency list
        function topsort(graph):
            N = graph.NumberOfNodes()
            V = [false, ..., false] //size N
            ordering = [0, ..., 0] //size N
            i = N - 1 // index for ordering array

            for(at=0; at<N; at++):
                if V[at]==false:
                    i = dfs(i, at, V, ordering, graph)

            return ordering

        function dfs(i, at, V, ordering, graph):
            V[at] = true

            edges = graph.getEdgesOutFromNode(at)
            for edge in edges:
                if V[edge.to]==false:
                    i = dfs(i, edge.to, V, ordering, graph)
            
            ordering[i] = at
            return i - 1
    ```
# eulerian paths and circuits
* a eulerian path (aka eulerian trail) is a path of edges that visits all the edges in a graph once and only once
    - one must choose a start node carefully to avoid being unable to visit some edges
* a eulerian circuit (aka eulerian cycle) is a eulerian path that starts and ends on the same vertex
    - you can begin at any node on the graph if you know that the graph contains said circuit
    - goes w/o saying that if the graph has no eulerian circuit, you will experience one or both of the following
        1. not able to visit all edges
        2. not able to return to start node
* conditions for a valid eulerian path and circuit
    - depends on the graph in question; there are four flavours of the euler path/circuit problem we care about

    ||eulerian circuit|eularian path|
    |:---|:---|:---|
    |undirected graph|every vertex has an even degree|one of the following: 1. every vertex has an even degree 2. exactly two vertices have an odd degree|
    |directed graph|every vertex has equal in-degree and out-degree| all of the following: 1. at most one vertex has out-degree less in-degree = 1 2. at most one vertex has in-degree less out-degree = 1 3. all other vertices have out-degree less in-degree = 0|

    - all vertices with non-zero degree need to belong to a single connected component

* node degree
    - depends on whether the graph is directed
    - in an undirected graph, node degree is, simply, the number of edges attached to said node
    - in a directed graph, there are two forms of node degree: in-degree and out-degree
        * in-degree is, simply, the number of incoming edges
        * out-degree is the number of outgoing edges
        * node degree, therefore, is X in, Y out, where X and Y and #edges
* how to find an eulerian path given a direncted graph graph
    1. determine if an eulerian path exists in the first place
        - recall: for an eulerian path to exist
            * at most one vertex has out-degree less in-degree = 1
            * at most one vertex has in-degree less out-degree = 1
            * all other vertices have out-degree less in-degree = 0 (that is, equal in and out-degree)
        - loop through all the edges. count the #in and out-degrees eg

            |node|in|out|
            |:---:|:---:|:---:|
            |0|0|0|
            |1|1|2|
            |2|3|3|
            |3|3|3|
            |4|2|2|
            |5|1|1|
            |6|2|1|

        - notice that node 1 and 6 satisfies conditions one and three respectively; the rest satisfy condition two. a eulerian path, therefore, exists
        - an eulerian circuit exists if all nodes satisfy condition two; it does not matter whether or not they satisfy the other two
    2. find a valid starting node
        - this is, typically, the node that satisfys condition one
        - in our example, the start node is node 1
        - a valid end node, typically, satisfies condition three, therefore, node 6 will be the end node
    3. perform a DFS with a twist:
        - deduct 1 from the corresponding `out` array of a node when an outgoing path from said node is take
        - the DFS will get stuck at some point (the value of `out` at that point will be zero); prepend that node to the solution array
        - go back to the previous node: is the value of `out` equal to zero (are there outgoing paths)?
            * yes: recurse
            * no: prepend node to solution array
        - repeat until all values in `out` are zero
* an algo that finds an eulerian path also finds an eulerian cycle
    - applies to directed and undirected graphs
* time complexity is O(E) where *E* is the #edges
    - there are two cacluations: 
        1. computing in and out-degrees
        2. DFS
    - both are linear in the #edges
* pseudo-code

    ```text
        //global/class scope vars
        n = #vertices in graph
        m = #edges in graph
        g adjacency list representing directed graph

        in = [0, ..., 0] //size n
        out = [0, ..., 0] //size n

        path = empty integer linked-list data structure

        function findEulerianPath():
            countInOutDegrees()
            if(!graphHasEulerianPath()):
                return null
            dfs(findStartNode())

            //return eulerian path if we traversed all the edges
            // the graph might be disconnected, therefore, it is
            //impossible to have an euler path
            if(path.size()==m+1):
                return path
            return null

        function countInOutDegrees():
            for(edges in g):
                for(edge in edges):
                    out[edge.from]++
                    in[edge.to]++

        function graphHasEulerianPath():
            start_nodes, end_nodes = 0, 0
            for(i=0; i<n; i++):
                if((out[i]-in[i]>1) || (in[i]-out[i]>1)):
                    return false
                else if(out[i]-in[i]==1):
                    start_nodes++
                else if(int[i]-out[i]==1):
                    end_nodes++
            return (end_nodes==0 && start_nodes==0) || (end_nodes==1 && start_nodes==1)

        function findStartNode():
            start = 0
            for(i=0; i<n; i++):
                //unique starting node
                if(out[i]-in[i]==1):
                    return i
                
                //start at any node that has an outgoing edge
                if(out[i]>0):
                    start = i
            return start

        function dfs(at):
            //while current node has outgoing edges
            while(out[at]!=0):
                //select the next unvisited outgoing node
                next_edge = g[at].get(--out[at])
                dfs(next_edge.to)
            path.insertFirst(at)
    ```
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
    |undirected graph|every vertex has an even degree|one of the following:
    1. every vertex has an even degree
    2. exactly two vertices have an odd degree|
    |directed graph|every vertex has equal in-degree and out-degree| all of the following:
    1. at most one vertex has out-degree less in-degree = 1
    2. at most one vertex has in-degree less out-degree = 1
    3. all other vertices have out-degree less in-degree = 0|

    - all vertices with non-zero degree need to belong to a single connected component

* node degree
    - depends on whether the graph is directed
    - in an undirected graph, node degree is, simply, the number of edges attached to said node
    - in a directed graph, there are two forms of node degree: in-degree and out-degree
        * in-degree is, simply, the number of incoming edges
        * out-degree is the number of outgoing edges
        * node degree, therefore, is X in, Y out, where X and Y and #edges
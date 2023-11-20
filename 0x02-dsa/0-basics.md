# basics

### graph
* a graph is, simply, a combination of nodes and edges (nodes are, 
also, called vertices)
* think of a node as a *thing* and an edge as the *relationship*
between two *things*
* directed vs undirected graphs
    * edges in a directed graph have a *direction*; those in an
    undirected graph do not
* *neighbour nodes* are nodes that can be accessed from a particular
node, of course, following the direction rules of the graph
* an *adjacency list* is a great way to represent a graph in code
    * example

        ```javascript
            {
                a: ["b", "c"],
                b: ["d"],
                c: ["e"],
                d: [],
                e: ["b"],
                f: ["d"],
            }
        ```

    * dictionary in py, unordered map in java and C, object in JS etc
    * keys of the adjacency list are the nodes; values are the neighbours
    of a key
*   

### depth-first traversal
* traverse a branch up to a leaf node and, only then, traverse another child
of the ultimate parent. repeat recursively
* example: graph laid out above, start at `a`
    * [a → b → d], [c → e → b → d]
    * output (skip duplicates): a, b, d, c, e
    * cannot get to `f`
* implements a stack (LIFO)

### breadth-first traversal
* traverse the children of a parent node. pick a child. repeat recursively
* example: graph laid out above, start at `a` (default: pick left child first)
    * [a → b], [a → c], [b → d], [c → e], \[d\], [e → b], [b → d]
    * output (skip duplicates): a, b, c, d, e
    * cannot get to `f`
* imlements a queue (FIFO)
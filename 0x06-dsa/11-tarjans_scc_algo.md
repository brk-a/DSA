# tarjan's algo to find strongly connected components
* SCCs (strongly connected component) are self-contained cycles w/i a directed graph where every vertex can reach every other component in the same cycle
* the low-link value of a node, in this context, is the smallest/lowest node id reachable from that node (including itself) when performing a DFS
* at the end of the call stack frame, all the nodes that have the same low-link value are members of a strongly connected component
    * wait, there's more...
        - a catch, to be specific
        - assignment of low-link values depends on the order in which the DFS visits the nodes, that is, you will get different results if you took a different path to the same destination
        - tarjan's algo maintains a stack invariant that prevents SCCs from intefering with each others' low-link val
        - a stack in variant is a set, usually a stack, of valid nodes from which to update low-link values
            - nodes are added to the stack of valid nodes as they are explored for the first time
            - nodes are removed from the stack each time a complete SCC is found
    * say *u* and *v* are nodes in a graph annd we are, currently, exploring node *u*. the low-link value update conditions is: 
        - to update *u*'s low-link value to *v*'s:
            1. there has to be a path of edges from *u* to *v*
            2. node *v* must be on the stack
### complexity
* instead of updating the low-link values *ex post facto* (in the call stack when the DFS callbacks return), said values will be updated *on the fly* (when the node is visited), therefore, time complexity will be O(V+E)
### pseudo-code

    ```
        
    ```

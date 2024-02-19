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

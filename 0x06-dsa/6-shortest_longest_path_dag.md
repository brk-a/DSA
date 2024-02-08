# shortest and longest paths on DAGs
> recall: a DAG is a directed graph w/o cycles
> trees, by definition, are DAGs
> > CAVEAT: undirected trees are not DAGS
### SSSP problem on DAGs
* SSSP &rarr; single-source shortest path
* SSSP problem on DAGs can be solved efficiently in O(V+E) time because the nodes can be ordered using a toplogical ordering and processed sequentially
* NBA to efficiently solve the SSSP problem is Dijkstra's algo, however, it will nnot work with negative edge weights
### using topsort to solve SSSP problem
* given a starting point on a directed, weighted graph, come up with a mapping of the shortest distance between the starting point and all other nodes
    * method 1: topsort
        1. initialise a mapping of nodes and distance from start node

            |\infty|\infty|\infty|\infty|\infty|\infty|\infty|\infty|\infty|\infty|
            |:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
            |A|B|C|D|E|F|G|H|I|J|

        2. assume `A` is the start node. set A's distance value to zero

            |0|\infty|\infty|\infty|\infty|\infty|\infty|\infty|\infty|\infty|
            |:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
            |A|B|C|D|E|F|G|H|I|J|

        3. visit all immediate child nodes
        4. for each, check whether the current distance value (the weight) is less than the one in the mapping
            * if yes: replace the value in the mapping w. the current one
            * if no: carry on
        5. set current node to be the next node in the mapping
        6. repeat steps 3 to 5 until all nodes in mapping are covered
### longest path on DAG
* on a non-DAG graph, the problem is NP-hard, however, on DAGs the problem can be solved in O(V+E) time
* idea is simple:
    1. multiply all edge value by -1
    2. find the shorest path
    3. multiply edge values by -1
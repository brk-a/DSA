# FW algo
* a APSP (all-pairs shortest path) algo
    * APSP  algos can find the shortest path between all pairs of nodes
* idea is to gradually build all the intermediate routes between nodes *i* and *j* to find the optimal path
    * say *m[i][j]* = a, *m[i][k]* = b and  *m[k][j]* = c
    * say a = b + c
    * FW algo will prefer *b+c* to *a*, that is, travel from *i* to *k* at a cost of *b* then travel from *k* to *j* at a cost of *c* instead of travelling from *i* to *j* at a cost of *a* even if *a* = *b* + *c*
* time and space complexity are O(V<sup>3</sup>) where V &rarr; #vertices
* ideal for graphs with no more than a few hundred nodes
* optimal way to represent a graph is with a 2D adjacency matrix, *m*, where *m[i][j]* is the edge weight from node *i* to node *j*

    ```text
        [
            [0, 4, 1, 9],
            [3, 0, 6, 11],
            [4, 1, 0, 2],
            [6, 5, -4, 0],
        ]

        assumption: weight from a node to itself is zero
    ```

* say node *i* and *j* are not connected; values in the adjacency matrix will be pisitive infinity


    ```text
        [
            [0, 4, 1, inf],
            [inf, 0, 6, inf],
            [4, 1, 0, 2],
            [inf, inf, inf, 0],
        ]

        assumption: weight from a node to itself is zero
        caveat: make sure that your programming language supports a special constant for infinity (both +ve and -ve) such that x + +inf = +inf and +inf + +inf = +inf. do not use 2^31 - 1 as inf because it will cause an integer overflow; use a large constant, eg. 10^7, instead 
    ```

* FW algo uses dynamic programming to cache previous optimal solutions
    * the cache is called a memo table
### compare various SP algos

||BFS|Dijkstra's|Bellman-Ford|Floyd-Warshall|
|:---:|:---:|:---:|:---:|:---:|
|complexity|O(V+E)|O((V+E)log(V))|O(VE)|O(V^3)|
|recommended graph size|large|medium & large|small & medium|small|
|good for APSP?|only works on unweighted graphs|ok|bad|yes|
|can detect negative cycles?|no|no|yes|yes|
|SP on graph w. weighted edges|incorrect SP answer|best algo|works|bad, in general|
|SP on graph w. unweighted edges|best algo|ok|bad|bad, in general|

### memo table
* let *dp* be a 3D matrix of size n * n * n
    * dp[k][i][j] &rarr; shortest path from *i* to *j* through nodes {0, 1, ..., k-1, k}
* for each value of k, k = 0, 1, ..., n-1, build an optimal solution that
    * routes through 0
    * routes through 0 and 1
    * routes through 0, 1, and 2
    * ...
    * routes through 0, 1, 2, ..., n-1
* dp[n-1] is the 2D matrix solution that we are after
*  *dp[0][i][j]* = *m[i][j]*
    * in the beginning, the optimal solution from *i* to *j* is, simply, the distance in the adjacency matrix
* *dp[k][i][j]* = min(*dp[k-1][i][j]*, *dp[k-1][i][k]* + *dp[k-1][k][j]*) when k!=0
* space complexity is O(V<sup>3</sup>) because the memo table, *dp*, has three dimensions: *i*, *j* and *k*
    * notice that the loop is over *k*; it starts from zero then 1, then 2 and so forth
    * current result build on the previous one because state *k-1* is required to compute state *k*
    * however, it is possible to compute the state *k* in place. this saves a dimension in memory, therefore, the space complexity is reduced to O(V<sup>2</sup>)
    * the new recurrence relation viz:
        * *dp[i][j]* = *m[i][j]* IFF k=0
        * *dp[i][j]* = min(*dp[i][j]*, *dp[i][k]* + *dp[k][j]*) when k!=0

### pseudo-code

    ```code
        //global/class scope vars
        n = size of adjacency matrix
        dp = memo table that contains APSP solution
        next = matrix used to  reconstruct shortest paths

        function floydWarshall(m):
            setup(m)

            //execute FW APSP algo
            for(k:=0; k<n; k++):
                for(i:=0; i<n; i++):
                    for(j:=0; j<n; j++):
                        if(dp[i][k]+dp[k][j]<dp[i][j]):
                            dp[i][j] = dp[i][k]+dp[k][j]
                            next[i][j] = next[i][k]
            
            //detect -ve cycles
            propagateNegativeCycles(dp, n)

            //return APSP matrix
            return dp
        
        function setup(m):
            dp = empty matrix of  size n * n

            //should contain null values by default
            next = empty integer matrix of size n * n

            //deep copy of  input matrix and set up `next` matrix for path reconstruction
            for(i:=0; i<n; i++):
                for(j:=0; j<n; j++):
                    dp[i][j] = m[i][j]
                    if(m[i][j]!=+inf):
                        next[i][j] = j

        function propagateNegativeCycles(dp, n):
            //execute FW APSP algo again, however, set the distance between teo nodes to 
            //negative infinity if the it can be improved. every edge, (i, j), marked
            //with -inf is either part of a negative cycle or reaches into said cycle
            for(k:=0; k<n; k++):
                for(i:=0; i<n; i++):
                    for(j:=0; j<n; j++):
                        if(dp[i][k]+dp[k][j]<dp[i][j]):
                            dp[i][j] = -inf
                            next[i][j] = -1

        function reconstructPath(start, end):
            //reconstructs the shortest path between nodes `start` and `end`
            //run the `floydWarshall` solver before calling this method
            //returns null if path is affected by -ve cycle
            
            path = []

            //check if there is a path between start and end node
            if(dp[start][end]==+inf):
                return path
            
            at := start
            //reconstruct path from `next` matrix
            for(; at!=end; at=next[at][end]):
                if(at==-1):
                    return
                path.add(at)
            
            if(next[at][end]==-1):
                path.add(end)
                return path
    ```
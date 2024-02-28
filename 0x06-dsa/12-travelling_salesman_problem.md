# travelling salesman problem (TSP)
* emphasis on a solution using dynamnic programming
### the problem
* given a list of cities and distances between each pair of cities, what is the shortest possible route that visits each city exactly once and returns to the origin city
* alternative way to put it: given a complete graph with weighted edges(as an adjacency matrix), what is Hamiltonian cycle (path that visits every node once) of minimum cost?
### NP
* the problem has been proven to be [NP-complete][def]
* brute-forcing takes O(n!) time because the algo will go through every permutation of node orderings
* using dynamic programming takes O(n<sup>2</sup>2<sup>n</sup>) time; quite the improvement from the brute force approach
    - this improvement makes it feasible to solve graphs with up to 23 nodes on a typical computer

    |n|n!|(n^2)(2^n)|
    |:---:|:---:|:---:|
    |0|1|0|
    |1|1|2|
    |2|2|16|
    |3|6|72|
    |4|24|256|
    |5|120|800|
    |6|720|2,304|
    |...|...|...|
    |15|1307674,368,000|7,372,800|
    |16|20,922,789,888,000|16,777,216|
    |17|355,687,428,096,000|37,879,808|

* clearly, the brute-force approoach works when `n` is small and the DP one works slightly better when `n` is large
### how to implement DP to the TSP
* idea is to compute the optimal solution for all the sub-paths of length N while using the info from the already-known optimal, partial tours of length N-1
* set-up info
    1. pick a starting node, S, where 0 <= S < N
    2. store the optimal value from S to each node, X (X != S). this will solve the TSP problem for N = 2
* to compute the optimal solution for paths of length 3, we need to remember (store) two things for each of the N = 2 cases
    1. the set of visited nodes in the sub-path
    2. the index of the last visited node in the path

    - these form the DP state
    - there are N possible nodes that could have been visited last and 2<sup>N</sup> possible sub-sets of visited nodes, therefore, the space needed to store the answer to each sub-problem is bounded by O(N2<sup>N</sup>)
* visited nnodes as a bit field
    * the best way to represent the set of visited nodes is to use a single 32-bit integer; it is compact, quick and is easy to cache in a memo table
    * flip the *i*th bit of the *i*th node node to 1 when said node is visited
        - say there are four nodes in a graph. starting at node zero, we move to node 1
        - the binary representation will be viz: 0011<sub>2</sub> = 3, assuming the least significant bit is on the right
        - same graph; visit node zero and 3
        - the binary representation will be viz: 1001<sub>2</sub> = 9, assuming the least significant bit is on the right
        - same graph; visit node zero and 2
        - the binary representation will be viz: 0101<sub>2</sub> = 5, assuming the least significant bit is on the right
* what happens when 3 <- n <= N?
    * eaa...sy! take the solved sub-paths from n-1 and add another edge extending to a node which has not already been visited from the last visited node (which has been saved)
        - par example: say there are four nodes in a graph. starting at node zero, we move to node 3
        - the binary representation will be viz: 1001<sub>2</sub> = 9, assuming the least significant bit is on the right
        - notice that, from node 3's perspective, nodes 1 and 2 are unvisited. this means two states, 1011<sub>2</sub>=11 and 1101<sub>2</sub>=13, will be created and added to the memo table
        - for n nodes that have two unvisted nodes, this will be 2<sup>n</sup> entries in the memo table
        - keep doing this until allnodes have been visited
* completing the TSP tour
    * loop over to the end state in the memo table for every possible end position
        - end state is where the binary representation is all 1s, eg, 1111
    * minimise the look-up value plus the cost of going back to S, the starting point
### pseudo-code

    ```text
        //finds the minimum TSP tour cost
        // m -> 2D adjacency matrix representing a graph
        // S -> start node (0<= S < N)
        function tsp(m, S):
            N = m.size

            //initialise memo table
            //fill it with null vals or +inf
            memo = 2D table of size N by 2^N
            setup(m, memo, S, N)
            solve(m, memo, S, N)
            minCost = findMinCost(m, memo, S, N)
            tour = findOptimalTour(m, memo, S, N)

            return (minCost, tour)

            function setup():
                //initialises memo table by caching
                //the optimal solution from the start node to every other node
                for(i:=0; i<N; i++):
                    if(i==S):
                        continue
                    //store the optimal value from node S to each node i
                    //(this is given as input in the adjacency matrix, m)
                    memo[i[1<<S | 1<<i]] = m[S][i]

            function solve():
                for(r:=3; r<=N; r++):
                    //combinations function generates all bit sets of
                    //size N with r bits set to 1
                    //eg. combination (3, 4) base 10 = {0111, 1011, 1101, 1110} base 2
                    for subset in combinations(r, N):
                        if(notInS, subset):
                            continue
                        for(next:=0; next<N; next++):
                            if(next==S || notIn(next, subset)):
                                continue
                            //the subset state w/o the next node
                            state = subset ^ (1<<next)
                            minDist = +inf
                            //`e` is short for `end node`
                            for(e=0; e<N; e++):
                                if(e==S || e==next || notIn(e, subset)):
                                    continue
                                newDistance = memo[e][state] + m[e][next]
                                if(newDistance<minDist):
                                    minDist = newDistance
                            memo[next][subset] = minDist

            function findOptimalCost(m,memo, S, N):
                //the end state is the bit mask with N
                //bits set to 1 (think 2^N - 1 )
                END_STATE = (1<<N) - 1

                minTourCost = +inf
                for(e=0; e<N; e++):
                    if(e==S):
                        continue
                    tourCost = memo[e][END_STATE] + m[e][S]
                    if(tourCost<minTourCost):
                        minTourCost =  tourCost
                return minTourCost

            function findOptimalTour(m, memo, S, N):
                lastIndex = S
                state = (1<<N) - 1 //end state
                tour = array of size N+1

                for(i:=N-1; i>=1; i--):
                    index = -1
                    for(j=0; j<N; j++):
                        if(j==S || notIn(j, state)):
                            continue
                        prevDist = memo[index][state] + m[index][lastIndex]
                        newDist = memo[j][state] + m[j][lastIndex]
                        if(newDist<prevDist):
                            index = j
                    tour[i] = index
                    state = state ^ (1<<index)
                    lastIndex = index
                tour[0] = tour[N] = S
                return tour

            function combinations(r, n):
                //generates all bit sets of size n with r bits set to 1
                subsets = []
                combinations(0, 0, r, n, subsets)
                return subsets

            function combinations(set, at, r, n, subsets):
                //recursive method to generate bit sets
                if(r=0):
                    subsets.add(set)
                else:
                    for(i=at; i<n; i++):
                        //flip the ith bit
                        set = set | (1<<i)
                        combinations(set, i+1, r-1, n, subsets)

                        //backtrack and flip off ith bit
                        set = set & ~(1<<i)

            function notIn(i, subset):
                //returns `true` when ith bit in `subset` is not set
                return ((1<<i) & subset) = 0
    ```

[def]: https://www.britannica.com/science/NP-complete-problem
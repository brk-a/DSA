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
* 

[def]: https://www.britannica.com/science/NP-complete-problem
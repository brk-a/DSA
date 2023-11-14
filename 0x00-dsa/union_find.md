# union find

### what it is
* a data structure that tracks elements that are split into
one or more disjoint sets
* has two primary ops: `find` and `union`

### when and where a union find is used
* Kruskal's minimum spanning tree algo
* grid percolation
* network connectivity
* least common ancestor in trees
* image processing

### complexity

    |operation|time complexity|
    |:---:|:---:|
    |construction|O(n)|
    |union|α(n)|
    |find|α(n)|
    |get component size|α(n)|
    |check if connected|α(n)|
    |count components|O(1)|

* α(n) -> [armotised constant time][def]

### Kruskal's algo
* there is a graph, G = (V, E); find a minimum spanning tree (does
not have to be unique)

> > min spanning tree  is a subset of the edeswhich connect all vertices
in the graph at the least total edge cost

##### how to achieve this
* sort edges in ascending order of weight
* walk through the sorted edges and look at the two nodes the edge belongs
to. if the nodes are already unified, fail to include the edge, else, include
and unify the nodes
* algo terminates when every edge has been processed or all vertices have been
unified

### union and find ops

##### find
* create a bijection (mappping) of objects and integers in the range [0, n)
    * not necessary but allows an array-based union find
* have an array. said array stores union find info. each index has an associated 
object that is looked up during the mapping
    - each index represents the index of the paprent node
    - each node is a root node (is its own parent) initially
* in case of collisions (two nodes have the same parent or a node is a child of a child node), use the index of the ultimate parent node
* **does not use path compression**
> > **summary:** to *find* which compoment a particular element belongs to, find the root of that components by following the parent nodes until a self loop reached


##### union
> > **summary:** to *unify* two elements, find the root nodes of each component and if
the root nodes are different, make one the parent of the other

##### remarks
* in this data structure, one cannot *un-union* an element because it is inefficient
* the number of components is equal to the number of roots remaining
* number of root nodes never increase

##### path compression
* have all node pointing to one parent, the ultimate parent, such that only the
ultimate parent has child nodes
* eliminates the need to traverse the sequence of nodes on the way to a parent
* 

[def]: https://stackoverflow.com/questions/200384/what-is-constant-amortized-time
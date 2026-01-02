# heaps and heap-sort

## 0. intro
### 0.1. WTF is a heap?
* a tree-based data structure that satisfies the heap property
    * TF is the *heap property*?
        * in a max heap, for any given node, `C`, if `P` is the parent node of `C`, then the key (the value) of `P` is greater than or equal to the key of `C`
        * in a min heap, the key of `P` is less than or equal to the key of `C`
    * the node at the *"top"* of the heap (with no parents) is called the root node
    * see illustration below

    ```mermaid
        graph TD
            A[Max Heap] --> B[Root: Maximum Element]
            A --> C[Complete Binary Tree]
            A --> D[Parent > Children]
            A --> E[Array Representation]
            A --> F[Left Child: 2*index + 1]
            A --> G[Right Child: 2*index + 2]
            A --> H[Parent: floor((index - 1)/2)]
            A --> I[heapifyUp: Bubble Up]
            A --> J[heapifyDown: Bubble Down]
            A --> K[Insert: O(log n)]
            A --> L[Extract Max: O(log n)]
            A --> M[Build Heap: O(n)]

            N[Min Heap] --> O[Root: Minimum Element]
            N --> P[Complete Binary Tree]
            N --> Q[Parent < Children]
            N --> R[Array Representation]
            N --> S[Left Child: 2*index + 1]
            N --> T[Right Child: 2*index + 2]
            N --> U[Parent: floor((index - 1)/2)]
            N --> V[heapifyUp: Bubble Up]
            N --> W[heapifyDown: Bubble Down]
            N --> X[Insert: O(log n)]
            N --> Y[Extract Min: O(log n)]
            N --> Z[Build Heap: O(n)]
    ```

* heap is one maximally efficient implementation of an abstract data type called a **priority queue** and, in fact, priority queues are often referred to as *"heaps"*, regardless of how they may be implemented
* in a heap, the highest (or lowest) priority element is always stored at the root, however, a heap is not a sorted structure; it can be regarded as being partially ordered
### 0.2. how TF are heaps useful?
* a heap is a useful data structure when it is necessary to repeatedly remove the object with the highest (or lowest) priority or when insertions need to be interspersed with removals of the root node
    * **for the gamers out there:** your favourite leaderboard is an example of a heap
        * incomplete data (users deactivating or deleting their accounts) and/or constantly-changing data in a dataset (people's scores change all the time) are where the heap shines
## 1. heaps and binary trees
### 1.1. TF is a binary tree?
* a tree data structure in which each node has at most two children
    * a *k*-ary tree where k = 2
* said children are referred to as the *left child* and the *right child*
*  **a recursive definition using set theory:** a binary tree is a triple, `(L, S, R)`, where `L` and `R` are binary trees or the empty set and `S` is a singleton (a single–element set) containing the root

    ```mermaid
        graph TB
            A((1)) --> B((2))
            A --> C((3))
            B --> D((4))
            B --> E((5))
            C --> F((6))
            C --> G((7))
    ```

* binary trees are populated L-R, T-B
### 1.2. use binary trees to define heaps
* a heap is a data structure that statisfies the conditions of both a complete binary tree and the heap property
* example using max heap

    ```mermaid
        graph TD
            A((10)) --- B((7))
            A  --- C((5))
            B --- D((3))
            B --- E((6))
            C --- F((4))
    ```


* there are three levels on this heap: root (level 1), level 2 (nodes with keys 7 and 5) and level 3 (nodes with keys 3, 6, and 4)
* notice how node `C` has only one child; that child, however, is the left child, therefore, the binary tree property is satisfied ✅
* notice how the value (aka **key** in priority-queue language) of any parent is greater than or equal to the key of the child(ren), therefore, the heap property is satisfied ✅
* we can represent said heap using an array
    * how TF do we do that?
        * glad you asked...
    * let `A` be an empty array of size `n` where `n` is the number of nodes in a heap, `H`
        * from our example above, the array *viz*:

        |val|||||||
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|1|2|3|4|5|

    * place the keys of each node into the array: traverse the heap T-B and L-R
        * the filled array from our example *viz*:

        |val|10|7|5|3|6|4|
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|1|2|3|4|5|

* it quickly becomes clear that we cannot tell which key belongs to which node when we flatten the heap into an array
    * we can find patterns based on the properties of a heap/binary tree
    * focus on key `7` from our array above
    
        ```plaintext
            given the node A[1]:
                parent node: A[0]
                left child: A[3]
                right child: A[4]
        ```

    * we see the following
        * subtract one from the index to get to a parent node
        * add 2 to the index to get to a left child
        * add 3 to the index to get to the right child
    * focus on key `5` from our array above
    
        ```plaintext
            given the node A[2]:
                parent node: A[0]
                left child: A[5]
                right child: none (would have been `A[6]` if it existed)
        ```

    * we see the following
        * subtract one from the index then find the integer quotient of the result and two to get to a parent node
            * $\lfloor\frac{2-1}{2}\rfloor = \lfloor\frac{1}{2}\rfloor = 0$
        * multiply the index by 2  and add one to get to the left child
            * $(2 \times 2) + 1 = 5$
        * multiply the index by 2  and add two to get to the right child
            * $(2 \times 2) + 2 = 6$
    * focus on key `3` from our array above
    
        ```plaintext
            given the node A[3]:
                parent node: A[1]
                left child: none (would have been `A[7]` if it existed)
                right child: none (would have been `A[8]` if it existed)
        ```

    * we see the following
        * subtract one from the index then find the integer quotient of the result and two to get to a parent node
            * $\lfloor\frac{3-1}{2}\rfloor = \lfloor\frac{2}{2}\rfloor = 1$
        * multiply the index by 2  and add one to get to the left child
            * $(3 \times 2) + 1 = 7$
        * multiply the index by 2  and add two to get to the right child
            * $(3 \times 2) + 2 = 8$
    * it appears we have a pattern 
        * given a node at index, `i`, on an array, `A`,<br/><br/> the  parent node is at index $\lfloor\frac{i-1}{2}\rfloor$ <br/><br/> the left child is at index $(i \times 2) + 1$ <br/><br/> and the right child is at index $(i \times 2) + 2$ <br/><br/>
    * 3:18:59b
## 2. 
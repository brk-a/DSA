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
            A --> H["Parent: floor((index - 1)/2)"]
            A --> I[heapifyUp: Bubble Up]
            A --> J[heapifyDown: Bubble Down]
            A --> K["Insert: O(log n)"]
            A --> L["Extract Max: O(log n)"]
            A --> M["Build Heap: O(n)"]

            N[Min Heap] --> O[Root: Minimum Element]
            N --> P[Complete Binary Tree]
            N --> Q[Parent < Children]
            N --> R[Array Representation]
            N --> S[Left Child: 2*index + 1]
            N --> T[Right Child: 2*index + 2]
            N --> U["Parent: floor((index - 1)/2)"]
            N --> V[heapifyUp: Bubble Up]
            N --> W[heapifyDown: Bubble Down]
            N --> X["Insert: O(log n)"]
            N --> Y["Extract Min: O(log n)"]
            N --> Z["Build Heap: O(n)"]
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
        * given a non-root node at index, `i`, on an array, `A`,<br/><br/> the  parent node is at index $\lfloor\frac{i-1}{2}\rfloor$ <br/><br/> the left child is at index $(i \times 2) + 1$ <br/><br/> and the right child is at index $(i \times 2) + 2$ <br/><br/>
### 1.3. properties of heaps that are relevant to finding its time complexity
* the relationship between a heap's number of nodes, `n`, and its height, `h`, is $h = log_2(n)$ assuming said heap is fully filled (all nodes are populated and each parent has two children)
    * **example 1:** `n` = 3
        * the heap looks like this

        ```mermaid
            graph TD
                A(()) --- B(())
                A --- C(()) 
        ```

        * height of the heap, `h`, is simply the number of levels; in this case `h` = 2
        * $log_2(3) \approx 1.58$ and $\lceil 1.58 \rceil = 2 = h$ ✅
    * **example 2:** `n` = 7
        * the heap looks like this

        ```mermaid
            graph TD
                A(()) --- B(())
                A --- C(())
                B --- D(())
                B --- E(())
                C --- F(())
                C --- G(())
        ```

        * height of the heap, `h`, is 3
        * $log_2(7) \approx 2.8$ and $\lceil 2.8 \rceil = 3 = h$ ✅
    * **example 3:** run the numbers starting at `n`  1 ending at `n` = 20

        |n|log<sub>2</sub>(n)|h|ceil(log(n))|
        |:---:|:---:|:---:|:---:|
        |1|0|1|0|
        |2|1|2|2|
        |3|1.58|2|2|
        |4|2|3|3|
        |5|2.32|3|3|
        |6|2.58|3|3|
        |7|2.8|3|3|
        |8|3|4|4|
        |9|3.16|4|4|
        |10|3.32|4|4|
        |11|3.46|4|4|
        |12|3.58|4|4|
        |13|3.7|4|4|
        |14|3.81|4|4|
        |15|3.91|4|4|
        |16|4|5|5|
        |17|4.09|5|5|
        |18|4.17|5|5|
        |19|4.25|5|5|
        |20|4.32|5|5|

    * **conclusion:** $h = \lceil log_2(n) \rceil$
* given a heap whose number of nodes is `n`, the number of nodes at the leaf level, assuming the tree is fully filled up, are $\frac{n}{2}$
    * **example 1:** `n` = 3
        * the heap looks like this

        ```mermaid
            graph TD
                A(()) --- B(())
                A --- C(()) 
        ```

        * there are two nodes at the leaf level
        * $\frac{3}{2} = 1.5$ and $\lceil 1.5 \rceil = 2$ ✅
    * **example 2:** `n` = 7
        * the heap looks like this

        ```mermaid
            graph TD
                A(()) --- B(())
                A --- C(())
                B --- D(())
                B --- E(())
                C --- F(())
                C --- G(())
        ```

        * there are four nodes at the leaf level
        * $\frac{7}{2} = 3.5$ and $\lceil 3.5 \rceil = 4$ ✅
    * **example 3:** run the numbers starting at `n`  1 ending at `n` = 20

        |n|n/2|#leaf nodes|ceil(n/2)|
        |:---:|:---:|:---:|:---:|
        |1|0.5|1|1 ✅|
        |2|1|1|2|
        |3|1.5|2|2 ✅|
        |4|2|1|3|
        |5|2.5|2|3|
        |6|3|3|4|
        |7|3.5|4|4 ✅|
        |8|4|1|5|
        |9|4.5|2|5|
        |10|5|3|6|
        |11|5.5|4|6|
        |12|6|5|7|
        |13|6.5|6|7|
        |14|7|7|8|
        |15|7.5|8|8 ✅|
        |16|8|1|9|
        |17|8.5|2|9|
        |18|9|3|10|
        |19|9.5|4|10|
        |20|10|5|11|

    * **conclusion:** the relationship, $\text{number of leaf nodes} = \lceil \frac{n}{2} \rceil$ , holds when the heap is fully populated (n = 1, 3, 7, 15 etc)
* given a heap whose number of nodes is `n` and is fully populated
    * there are approximately $\frac{n}{2}$ nodes at level $n$
    * there are approximately $\frac{n}{4}$ nodes at level $n-1$
    * generally, there are apprximately $\frac{n}{2^k}$ nodes at level $n-k+1$ where $1 \leq k \leq n$
## 2. everything about the `insert` method
### 2.1. max heap
* we will use the following max heap throughout this section

    ```mermaid
        graph TD
            A((10)) --- B((7))
            A  --- C((5))
            B --- D((3))
            B --- E((6))
            C --- F((4))
    ```

* this is the representaion of said max heap in pseudo-code

    ```plaintext
        class maxHeap:
            define heap size in a variable, `m`
            create an array, `data`, of the size defined above
            function Insert(num):
                add a new data point to the heap while maintaining the heap property
            function Peek():
                return a data point 
    ```

* this is the array representation of said heap

    |val|10|7|5|3|6|4|
    |:---|:---|:---|:---|:---|:---|:---|
    |idx|0|1|2|3|4|5|

### 2.2. `insert` method
* adds a new element/key/data point to the heap while maintaining the heap property
* **three steps**
    * **step one:** add said key to the bottom level of the heap
    * **step two:** compare the newly-added key with its parent; stop if the order is correct, else, go to step three
    * **step three:** swap said key with its parent and return to step two
* **assumptions**
    * the heap is mutable
    * the heap has the capacity to hold more keys
* **example: add key `11` to the heap**
    * step one: add said key to the bottom level of the heap

    ```mermaid
        graph TD
            A((10)) --- B((7))
            A  --- C((5))
            B --- D((3))
            B --- E((6))
            C --- F((4))
            C --- G((11))
    ```

    * step two: compare `11` with `5`, its parent
        * 11 &gt; 5, therefore, swap

    * step three: swap `11` and `5` the return to step two

        ```mermaid
            graph TD
                A((10)) --- B((7))
                A  --- C((11))
                B --- D((3))
                B --- E((6))
                C --- F((4))
                C --- G((5))
        ```

        * compare `11` with `10`: 11 &gt; 10, therefore, swap
        * swap `11` and `10` and return to step two

        ```mermaid
            graph TD
                A((11)) --- B((7))
                A  --- C((10))
                B --- D((3))
                B --- E((6))
                C --- F((4))
                C --- G((5))
        ```

        * heap in current state satisfies the heap property, therefore, stop; return the latest version of the heap
> **important!** <br/> the tree structure is merely a representation/visualisation; actual operations occur in the array data structure which is linear <br/> the algo has to figure out the index of the parent node before it can compare and/or swap elements in said array
#### 2.2.1. pseudo-code for `insert` method

    ```plaintext
        // assume an array, `data`, holds the heap
        Function Insert(num):
            // step one: add said key to the bottom level of the heap
            data.append(num)

            // step two: compare the newly-added key with its parent; stop if the order is correct, else, go to step three
            i = data.length - 1 // index of `num`
            while i > 0 && (i-1) / 2 >= 0:
                if data[i] > data[(i-1)/2]:
                    // step three: swap said key with its parent and return to step two
                    tmp = data[i]
                    data[i] = data[(i-1)/2]
                    data[(i-1)/2] = tmp

                    i = (i-1) / 2
                else:
                    // the `stop` part in step two
                    break
    ```

* **caveat:**
    * this implementations assumes that the elements of `data` are of type `int`; the comparison mechanism, `if data[i] > data[(i-1)/2]:`, will change based on element type
#### 2.2.2. time complexity
* **best case** is trivial

# heaps and heap-sort

## 0. intro

<div style="text-align: justify;">

### 0.1. WTF is a heap?
* a tree-based data structure that satisfies the heap property
    * TF is the *heap property*?
        * in a max heap, for any given node, `C`, if `P` is the parent node of `C`, then the key (the value) of `P` is greater than or equal to the key of `C`
        * in a min heap, the key of `P` is less than or equal to the key of `C`
    * the node at the *"top"* of the heap (with no parents) is called the root node
    * see illustrations below

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
    ```

    ```mermaid
        graph TD

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
                A(( )) --- B(( ))
                A --- C(( )) 
        ```

        * height of the heap, `h`, is simply the number of levels; in this case `h` = 2
        * $log_2(3) \approx 1.58$ and $\lceil 1.58 \rceil = 2 = h$ ✅
    * **example 2:** `n` = 7
        * the heap looks like this

        ```mermaid
            graph TD
                A(( )) --- B(( ))
                A --- C(( ))
                B --- D(( ))
                B --- E(( ))
                C --- F(( ))
                C --- G(( ))
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
                A(( )) --- B(( ))
                A --- C(( )) 
        ```

        * there are two nodes at the leaf level
        * $\frac{3}{2} = 1.5$ and $\lceil 1.5 \rceil = 2$ ✅
    * **example 2:** `n` = 7
        * the heap looks like this

        ```mermaid
            graph TD
                A(( )) --- B(( ))
                A --- C(( ))
                B --- D(( ))
                B --- E(( ))
                C --- F(( ))
                C --- G(( ))
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
                // the `stop` bit in step two
                break
```

* **caveat:**
    * this implementations assumes that the elements of `data` are of type `int`; the comparison mechanism, `if data[i] > data[(i-1)/2]:`, will change based on element type
### 2.3. time complexity
* **best case** time complexity is trivial
    * say you add a new element whose value is smaller than that of its immediate parent: said element stays where it is added
    * example: add 5 to the heap
        * `5`'s parent is `10` and 5 &lt; 10, therefore, no further action is required
    * **conclusion: time complexity in the best case is O(1), that is,** $T(n) \in O(1)$
* **worst case** time complexity occurs when the element must be bubbled up to the root (newly-added element is larger than root)
    * in our example above, we added `11` to the heap
    * said element ended up being the root, that is, it moved/bubbled up $n$ levels (the height of the heap)
        * we know that the relationship between the number of nodes, `n`, and height of a heap, `h`, is $h = log(n)$
        * the algo will perform log(n) swaps to get the newly-added element from the leaf to the root
    * **conclusion: time complexity in the worst case is O(log(n)), that is,** $T(n) \in O(log(n))$
* **average case** time complexity depends on the expected number of swaps
    * $T(n) \propto \mathbb{E}[X]$ <br/> where $\mathbb{E}[X]$ is the expected number of swaps
    * the random variable, `X`, is the event where a newly-inserted element is swapped with its parent at each level
        * said event may or may not happen at each level: (one if it happens, else, zero)
        * the leaf level will be level zero; the root level will be level `n-1` (0-9 incusive is 10 digits)
    * the probability relies on the following assumptions
        * newly-added element is arbitrary and random (the chance that said element is larger than its parent is 50% always)
        * comparsion at each level is an independent event (the $iid$ property): the results of the comparsion at level `h` are independent of those at level `h-1`, however, swapping at level `h-1` only occurs when a swap or comparison or both occur at level `h`
        * **conclusion: at each level, the probability of swapping $(\frac{1}{2})^i$ where i is the level number**
    * $\mathbb{E}[X] = \sum_{i=1}^h i \cdot P(X=i)$ <br/><br/> the random var is 1 and the probability is $(\frac{1}{2})^i$ <br/><br/> $\mathbb{E}[X] = \sum_{i=1}^h (\frac{1}{2})^i$ <br/><br/> $\mathbb{E}[X] =  1 + \frac{1}{2} + \frac{1}{4} + ... + (\frac{1}{2})^h$ <br/><br/> we are looking at an infinite geometric series
    > **geometric series:** <br/> a series where each term is a constant multiple (ratio) of the previous term <br/><br/> $S = \sum_{k=0}^\infin ar^k$ <br/><br/> `a` is the first term and `r` is the common ratio (|r| < 1 for convergence) <br/><br/> the sum of an infinite geometric series is given by <br/><br/> $S = \frac{a}{1-r} \ \text{for} \ |r| \lt 1$ <br/><br/>

    * in our case: `a` = 1, `r` = 1/2 <br/><br/> we know that $\sum_{k-0}^\infin r^k = \frac{1}{1-r}$ <br/><br/>$\mathbb{E}[X] \approx \frac{1}{1-\frac{1}{2}} = 2$ <br/><br/> we can replace the constant with a number, $C$, to generalise the solution because $h$, the height of the tree, is not infinitely large<br/><br/> $\therefore \mathbb{E}[X] \approx C$ <br/><br/>
    * **conclusion: time complexity in the average case is O(1), that is,** $T(n) \in O(1)$
## 3. everything about the `peek` method
### 3.1. max heap
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

### 3.2. `peek` method
#### 3.2.1. pseudo-code for `peek` method

```plaintext
    // assume an array, `data`, holds the heap
    Function Peek():
        return data[0]
```

* all the `peek` method does is to return the key at the root node
### 3.3. time complexity
* the best, average and worst-case time  complexity are trivial: constant time, that is, $T(n) \in O(1)$
## 4. everything about the `delete` method
### 4.1. max heap
* we will use the following max heap throughout this section

    ```mermaid
        graph TD
            A((11)) --- B((7))
            A  --- C((10))
            B --- D((3))
            B --- E((6))
            C --- F((4))
            C --- G((5))
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

    |val|11|7|10|3|6|4|5|
    |:---|:---|:---|:---|:---|:---|:---|:---|
    |idx|0|1|2|3|4|5|6|

### 4.2. the `delete` method
* deletes the root node not any other node
* returns the key of said deleted root node
* the resulting data structure is not a heap because it does not satisfy the heap property; we need to re-arrange said data structure to form a heap
    * the `delete` method must also "*heapify*" the nodes left after deleting the root
#### 4.2.1. steps
* ~~five~~ steps
* **step zero:** store the key of the root in a variable
* **step one:** replace the key of root with the key of last node at the last level
* **step two:** delete the last node at the last level (the one whose key you used in step two)
* **step three:** perform *"heapify"* to restore the heap property
    ```plaintext
        set index = 0 (root)
        while index has a left child:
            find largest child
            if heap[index] < heap[largest_child]:
                swap heap[index]  and heap[largest_child]
                move index to largest_child
            else:
                break
    ```
* **step four:** return the key of the original root (the one from step zero)
* **example:  apply `delete` method to the max heap above**
    * step zero: store the key of the root in a variable
        * set variable `root_key` to `11`
    * step one: replace root key with the last key at bottom level of the heap

        ```mermaid
            graph TD
                A((5)) --- B((7))
                A  --- C((10))
                B --- D((3))
                B --- E((6))
                C --- F((4))
                C --- G((5))
        ```

    * step two: delete the last node at the last level

        ```mermaid
            graph TD
                A((5)) --- B((7))
                A  --- C((10))
                B --- D((3))
                B --- E((6))
                C --- F((4))
        ```

    * step three: perform *"heapify"* to restore the heap property
        * compare `5` with `10` and `7`: 10 &gt; 7 &gt; 5, therefore, `largest_child` = 10
        * swap `5` and `10`

        ```mermaid
            graph TD
                A((10)) --- B((7))
                A  --- C((5))
                B --- D((3))
                B --- E((6))
                C --- F((4))
        ```

        * heap in current state satisfies the heap property, therefore, stop; no need to make more comaprisons
    * step four: return the key of the original root
        * return `11`

#### 4.2.2. pesudo-code for `delete` method

```plaintext
    // assume an array, `data`, holds the heap
    Function Delete():
        last_key = data[last_index]
        data.remove(last_index) // step two

        root_key = data[0] // step zero
        data[0] = last_key // step one
        Heapify(0) // step three

        return root_key // step four
```

#### 4.2.3. TF is *heapify*?
* glad you asked...
* a fundamental operation used to maintain or establish the heap property in a binary heap
    * ensures that each parent node is greater than or equal to (in a max-heap) or less than or equal to (in a min-heap) its children
* primarily used during heap construction, element insertion and deletion; the heap property may be violated after modifying the heap structure
* process involves recursively comparing a node with its children and swapping it with the larger (for max-heap) or smaller (for min-heap) child until the sub-tree rooted at the node satisfies the heap property
    * starting from the last non-leaf node and moving up to the root when building a heap from an unsorted array, ensure the entire structure satisfies the heap property
* here is its pseudo-code

    ```plaintext
        // assume an array, `data`, holds the heap
        Function Heapify(i):
            while (2*i)+1 < data.length():
                maxChildIndex = MaxChild(i)
                if data[i] < data[maxChildIndex]:
                    temp = data[maxChildIndex]
                    data[maxChildIndex] = data[i]
                    data[i] = temp
                    i = maxChildIndex
                else:
                    break

        Function MaxChild(i):
            left = 2*i + 1
            right = 2*i + 1
            if right >= data.length() || data[left] > data[right]:
                return left
            else:
                return right
    ```

### 4.3. time complexity
#### 4.3.1. complete pseudo-code
```plaintext
    // assume an array, `data`, holds the heap
    Function Delete():
        last_key = data[last_index]
        data.remove(last_index) // step two

        root_key = data[0] // step zero
        data[0] = last_key // step one
        Heapify(0) // step three

        return root_key // step four
    
    Function Heapify(i):
        while (2*i)+1 < data.length():
            maxChildIndex = MaxChild(i)
            if data[i] < data[maxChildIndex]:
                temp = data[maxChildIndex]
                data[maxChildIndex] = data[i]
                data[i] = temp
                i = maxChildIndex
            else:
                break

    Function MaxChild(i):
        left = 2*i + 1
        right = 2*i + 1
        if right >= data.length() || data[left] > data[right]:
            return left
        else:
            return right
```

#### 4.3.2. time complexity of `Delete` and `MaxChild`
* `MaxChild` takes constant time, that is, $f(n) = C$
* `Delete` proper takes constant time too: $f(n) = C$
* `Heapify` is where the magic occurs, therefore, the time complexity of `Delete` is directly proportional to that of `Heapify`
#### 4.3.3. time complexity of `Heapify`
* **best-case** time complexity is trivial
    * say you have a heap whose node count  is one: it takes constant time to delete said node
    * `Heapify` does not run at all
    * **conclusion: time complexity in the best case is O(1), that is,** $T(n) \in O(1)$
* **worst-case** time complexity occurs during `Heapify(0)`, that is, swapping level by level from the root to the leaf
    * recall: the number of nodes, `n`, of a heap and height, `h`, have the following relationship: $h = log(n)$
    *  **conclusion: time complexity in the worst case is O(log(n)), that is,** $T(n) \in O(log(n))$
* **average case** time complexity depends on the expected number of swaps
    * $T(n) \propto \mathbb{E}[X] \ \text{where} \ \mathbb{E}[X] \ \text{is the expected number of swaps}$
    * random variable, `X`, is the event where the new root element is swapped at each level
        * number of swaps needed to bubble down a node to its proper position
        * said event may or may not happen at each level: (one if it happens, else, zero)
    * the probability relies on the following assumptions
        * newly-rooted node is neither arbitrary nor random because it was at the leaf in the original heap (the chance that said node is larger than its child is 100% almost always)
        * comparsion at each level is an independent event (the $iid$ property): the results of the comparsion at level `h` are independent of those at level `h-1`, however, swapping at level `h-1` only occurs when a swap or comparison or both occur at level `h`
        * **conclusion: at each level, the probability of swapping is almost always 100%**
    * we know that <br/> $\mathbb{E}[X] = \sum_{i=1}^h i \cdot P(X=i)$ <br/><br/> in our case, $P(X=i) = 1$ and $i=1$ <br/><br/> $\mathbb{E}[X] = \sum_{i=1}^h 1 = h$ <br/><br/> recall: $h \approx log_2n$ <br/><br/> $\mathbb{E}[X] \approx log_2n$ <br/><br/> drop the constants to generalise the solution <br/><br/> $\therefore \ \mathbb{E}[X] \approx O(log \ n)$ <br/><br/>
    *  **conclusion: time complexity in the average case is O(log(n)), that is,** $T(n) \in O(log(n))$

## 5. summary table of time complexity for `insert`, `peek` and `delete` methods

|scenario|insert|peek|delete|
|:---|:---|:---|:---|
|best-case|$O(1)$|$O(1)$|$O(1)$|
|average-case|$O(1)$|$O(1)$|$O(log(n))$|
|worst-case|$O(log(n))$|$O(1)$|$O(log(n))$|

### 5.1. average-case scenario: why TF does it take constant time to insert yet logarithmic time to delete?
* good question...
* most inserted elements/nodes end up near the leaves of the heap: this requires few or no swaps to satisfy the heap property
    * since a majority of nodes in a complete binary tree are near the bottom, the probability of needing to bubble up multiple levels is low, leading to constant-time performance on average
* deleting always removes the root (the max or min element) and replaces it with the last element which must be heapified downward
    * this process often requires traversing from root to leaf, $O(log \ n)$ levels, because the replacement element is typically small (in a max-heap) or large (in a min-heap) necessitating multiple comparisons and swaps

## 6. everything about heap-sort
### 6.1. pre-requisites
* we will use the following throughout this section
    * an array, `A`, that we will use to create a max heap

    |val|3|7|6|10|5|4|
    |:---|:---|:---|:---|:---|:---|:---|
    |idx|0|1|2|3|4|5|

    * **important!:** the array is not a representation of the heap *yet*; we will use said array to create a heap
### 6.2. build your own ... heap
* two strategies to turn a linear data structure, say, an array, to a heap
    * naive approach
    * restore-heap-property approach
#### 6.2.1. **strategy one:** naive approach
* steps
    * start with an empty array to repesent the max heap
    * apply `insert` iteratively to the elements of `A`
* time complexity
    * **worst case**
        * the first insertion takes O(1) because the heap was previously empty
        * second insertion takes O(log 1); 1 is the size of the heap so far
        * third insertion takes O(log 2); 2 is the size of the heap so far: we appear to have a pattern
        * the last insertion tales O(log (n-1)); (n-1) is the size of the heap so far: said pattern appears to hold
        * add the time complexities <br/><br/> $T(n) = O(log \ 1 + log \ 2 + log \ 3 + ... + log \ n)$ <br/><br/> we know from the logarithmic product rule that $\sum_{i=1}^n log \ i = log(1 \cdot 2 \cdot ... \cdot n)$ <br/> and $1 \cdot 2 \cdot ... \cdot n = n!$ <br/><br/> $O(log \ 1 + log \ 2 + log \ 3 + ... + log \ n) = log(n!)$ <br/><br/> we know that $log(n!) \leq log(n^n) = n \ log \ n$ <br/><br/> $\therefore \ T(n) \approx O(n \ log \ n)$ &nbsp; this is consistent with our findings in sub-section 2.3 above <br/><br/>
        * **conclusion: worst-case time complexity is linearithmic time, that is,** $T(n) \in O(n \ log \ n)$
#### 6.2.2. **strategy two:** restore-heap-property approach
* treat the input array as a heap whose heap property needs to be restored
* steps
    * begin at the last non-leaf node
    * apply `heapify`to each node in reverse level order (from bottom to top)
* **assumption:** the root node is the only node that breaks the heap property (this may not be the case in real life)
* how TF does one get to the index of the *last non-leaf node*?
    * great question ... <br/><br/> $i_{\text{last non-leaf node}} = \frac{n}{2} - 1$ &nbsp; where $n$ is `A.length`
    * **example: the array in sub-section 6.1 above**
        * n = 6 and i<sub>last non-leaf</sub> = (6/2) - 1 = 3 - 1 = 2
        * the last non-leaf node is at index 2; said node has the key `6`
        * see illustration below

        ```mermaid
            graph TD
                A((3)) --- B((7))
                A --- C((6))
                B --- D((10))
                B --- E((5))
                C --- F((4))
        ```

* pseudo-code for `build_heap` method

    ```plaintext
        // assume `data` is the array we need to turn into a heap
        Function BuildHeap():
            for each index i from (data.length / 2) -1 to 0:
                Heapify(i)
    ```

* apply `build_heap` to our example in sub-section 6.1 above
    * i<sub>last non-leaf</sub> = (6/2) - 1 = 3 - 1 = 2 and key<sub>i</sub> = 6
        * compare `6` with its child, `4`
            * heap property is satisfied; continue

            ```mermaid
                graph TD
                    A((3)) --- B((7))
                    A --- C((6))
                    B --- D((10))
                    B --- E((5))
                    C --- F((4))
            ```

    *  i<sub>last non-leaf</sub> = i<sub>prev</sub> - 1 = 2 - 1 = 1 and key<sub>i</sub> = 7
        * compare `7` with `10` and `5`
            * 10 &gt; 5, therefore, `10` is the `maxChild`
            * 10 &gt; 7, therefore swap `7` and `10`
            * heap property satisfied; continue

            ```mermaid
                graph TD
                    A((3)) --- B((10))
                    A --- C((6))
                    B --- D((7))
                    B --- E((5))
                    C --- F((4))
            ```

    * i<sub>last non-leaf</sub> = i<sub>prev</sub> - 1 = 1 - 1 = 0 and key<sub>i</sub> = 3
        * compare `3` with `10` and `6`
            * 10 &gt; 6, therefore, `10` is the `maxChild`
            * 10 &gt; 3, therefore, swap `3` and `10`
        * compare `3` with `7` and `5`
            * 7 &gt; 5, therefore, `7` is the `maxChild`
            * 7 &gt; 3, therefore, swap `3` and `7`         
        * heap property satisfied; done ✅

            ```mermaid
                graph TD
                    A((10)) --- B((7))
                    A --- C((6))
                    B --- D((3))
                    B --- E((5))
                    C --- F((4))
            ```
    > **important!** <br/> Heapify(0) restores the heap prop on the whole binary tree because the root is at index zero <br/> all that happens IRL is swapping of elements on an array; the binary tree is simply a visualisation
* time complexity
    * **worst case**
        * add the time it takes to *heapify* each sub-heap <br/><br/> $T(n) = \sum_{i=0}^{h-1}( \text{no. of nodes at depth} \ i) \times (\text{work per node at depth} \ i)$ <br/><br/> there are, roughly, $h-1$ levels, where $h$ is the height of the heap, because we do not heapify leaf-level nodes; we begin at the last non-leaf nodes<br/> *heapify* operation on each node takes a constant amount of time which is the same for each node and <br/> $\text{work done per node at level} \ i \propto i$ &nbsp; because the work increases as the number of nodes increase<br/><br/> **recall:** <br/>1. we apply *heapify* upwards (beginning at the last non-leaf node all the way to the root): let the starting point be `level 1` (because the leaf level will be level zero) and the root level be `level (h-1)` (0-9 inclusive is 10 digits) <br/> 2. the number of nodes roughly halves as we go up a level <br/><br/> we can estimate the number of nodes at level $i$ *viz:* $\frac{n}{2^{i+1}}$ where `n` is the number of nodes in the heap <br/><br/> $T(n) =  \sum_{i=0}^h(\frac{n}{2^{i+1}} \times i)$ <br/><br/> $T(n) =  n \times \sum_{i=0}^h(\frac{i}{2^{i+1}})$ <br/><br/> we have seen $\sum_{i=0}^h(\frac{i}{2^{i+1}})$ before; this is a geometric series (finite one this time around)<br/> $\sum_{i=0}^\infin(\frac{i}{2^{i+1}}) \approx 2$ <br/><br/> $T(n) \approx n \times 2$ <br/><br/> drop the constants to generalise the solution ~~also, this is an upper-bound calculation~~ <br/><br/> $\therefore \ T(n) \approx O(n)$ <br/><br/>
        * **conclusion: worst-case time complexity is linear time, that is,** $T(n) \in O(n)$
> **conclusion:** <br/> 1. better to "*fix a broken heap*" than to build a heap from scratch; *ex nihilo nihil fit* <br/> 2. the naive way is not always the easiest; the contrarian way is, by definition, counter-intuitive
### 6.3. heapsort
* we will use the array in sub-section 6.1 above as an example and for illustration
#### 6.3.1. steps
* build a heap using the input array (max heap if sorting in descending order, else, min heap)
* initilise an empty array
* repeatedly extract the max (if a max-heap) or min (if a min heap) using the `delete` method
* place said extracted elements at the end of said initialised array; this results in a sorted array
#### 6.3.2. example: sort an array in descending order
* input array: [3, 7, 6, 10, 5, 4]
    * val-idx representation

        |val|3|7|6|10|5|4|
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|1|2|3|4|5|

    * tree representation

        ```mermaid
            graph TD
                A((3)) --- B((7))
                A --- C((6))
                B --- D((10))
                B --- E((5))
                C --- F((4))
        ```
* **step one:** build a max heap using the input array

    |val|10|6|7|3|5|4|
    |:---|:---|:---|:---|:---|:---|:---|
    |idx|0|1|2|3|4|5|

    ```mermaid
        graph TD
            A((10)) --- B((7))
            A --- C((6))
            B --- D((5))
            B --- E((4))
            C --- F((3))
    ```

* **step two:** initialise an empty array

    |val|||||||
    |:---|:---|:---|:---|:---|:---|:---|
    |idx|0|~~1~~|~~2~~|~~3~~|~~4~~|~~5~~|
* **steps three and four:**  repeatedly extract the max using the `delete` method and append extracted elements to said initialised array
    * number of iterations 6 (`A.length`); 0-9 is 10 digits
    * iteration 1
        * `delete()` returns `10`
        * heap is re-organised

            ```mermaid
                graph TD
                    A((7)) --- B((6))
                    A --- C((5))
                    B --- D((3))
                    B --- E((4))
            ```

        * append `10` to initialised array

        |val|10||||||
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|~~1~~|~~2~~|~~3~~|~~4~~|~~5~~|
    
    * iteration 2
        * `delete()` returns `7`
        * heap is re-organised

            ```mermaid
                graph TD
                    A((6)) --- B((5))
                    A --- C((4))
                    B --- D((3))
            ```
        
        * append `7` to initialised array

        |val|10|7|||||
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|1|~~2~~|~~3~~|~~4~~|~~5~~|

    * iteration 3
        * `delete()` returns `6`
        * heap is re-organised

            ```mermaid
                graph TD
                    A((5)) --- B((4))
                    A --- C((3))
            ```
        * append `6` to initialised array

        |val|10|7|6||||
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|1|2|~~3~~|~~4~~|~~5~~|

    * iteration 4
        * `delete()` returns `5`
        * heap is re-organised

            ```mermaid
                graph TD
                    A((4)) --- B((3))
            ```
        
        * append `5` to initialised array

        |val|10|7|6|5|||
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|1|2|3|~~4~~|~~5~~|

    * iteration 5
        * `delete()` returns `4`
        * heap is re-organised

            ```mermaid
                graph TD
                    A((3))
            ```
        
        * append `4` to initialised array

        |val|10|7|6|5|4||
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|1|2|3|4|~~5~~|

    * iteration 6
        * `delete()` returns `3`
        * input heap is empty
        * append `3` to initialised array

        |val|10|7|6|5|4|3|
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|1|2|3|4|5|
    
    * input heap is empty (`A.length` == 0); stop
        * return newly-built array

        ```mermaid
            graph TD
                A((10)) --- B((7))
                A --- C((6))
                B --- D((4))
                B --- E((5))
                C --- F((3))
        ```


        |val|10|7|6|5|4|3|
        |:---|:---|:---|:---|:---|:---|:---|
        |idx|0|1|2|3|4|5|

#### 6.3.3. pseudo-code for heap-sort

```plaintext
    Function Heapsort(A):
        // step two: initialise an empty array
        B = []

        // step one: build a heap using input array
        BuildHeap(A)

        for i from 0 to A.length - 1:
            // step three: repeatedly extract the max using the `delete` method
            nodeKey = Delete(A)
            // step four: append extracted elements to said initialised array
            B.append(nodeKey)

        return B
```

#### 6.3.4. time complexity
* heap-sort depends on `delete` and `build_heap`; its performance, therefore, is proportional to the performance of its dependencies
    * `build_heap` has a time complexity (worst-worst-case) of $O(n)$
        * $T(n)_{build-heap} \approx O(n)$
    * the `for` loop that iteratively calls the `delete` method has the following time complexity
        * `delete` has a time complexity (worst-worst-case) of $O(log \ n)$
        * WTF happens when `delete` is called iteratively?
            * glad you asked ...

            ||time complexity|
            |:---|:---|
            |first delete|$O(log(n))$|
            |second delete|$O(log(n-1))$|
            |third delete|$O(log(n-2))$|
            |...|...|
            |last delete|$O(log(1))$|

            * add up the time complexities <br/><br/> $T(n)_\text{for loop} = O(log(n) + log(n-1) + ... + log(1))$ <br/> ~~ah, sh!t, here we go again...~~ &nbsp; we have seen this before: the logarithmic sum turns out to be the log of the factorial of the highest/limit term <br/><br/> $T(n)_\text{for loop} = O(log(n!))$ <br/> we have also seen that $log(n!) \leq log(n^n) = n \ log \ n$ <br/><br/> $\therefore \ T(n)_\text{for loop} \approx O(n \ log \ n)$ <br/><br/>
        * **conclusion: the time complexity of the `for`loop that iteratively calls the `delete` method is linearithmic, that is,** $T(n) \approx O(n \ log \ n)$
* time complexity of heap sort can be expressed as <br/><br/> $T(n) = T(n)_\text{build heap} + T(n)_\text{for loop}$ <br/><br/> $T(n)  = O(n + n \cdot log(n))$ <br/> the linearithmic term, $n \cdot log(n)$, is the dominant term (it grows faster), therefore, we may ignore the linear term, $n$ <br/><br/> $\therefore \ T(n)  \approx O(n \ log \ n)$
* **conclusion: the time complexity of heap-sort is linearithmic, that is,** $T(n) \in O(n \ log \ n)$

</div>

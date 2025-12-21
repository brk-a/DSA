# merge k sorted lists

### problem
* you are given an array, `lists`, of $k$ sorted linked lists
* each list is sorted in ascending order
* merge all linked lists and return merged list

### assumptions
* *nada*

### constraints
* *nada*

### examples
#### example 1
```plaintext
    input: lists = [[1, 4, 5], [1, 3, 4], [2, 6]]
    output: [1, 1, 2, 3, 4, 4, 5, 6]
    explanation: the linked lists are:
        [
            1 -> 4 -> 5,
            1 -> 3 -> 4,
            2 -> 6
        ]
    merging them creates the following:
    1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6 
```

#### example 2
```plaintext
    input: lists = []
    output: []
    explanation: ex nihilo nihil fit
```

#### example 3
```plaintext
    input: lists = [[]]
    output: []
    explanation: see example 2
```

#### example 4
```plaintext
    input: lists = [[1, 4], [2, 5], [3, 6]]
    output: [1, 2, 3, 4, 5, 6]
    explanation: the linked lists are:
        [
            1 -> 4,
            2 -> 5,
            3 -> 6
        ]
    merging them creates the following:
    1 -> 2 -> 3 -> 4 -> 5 -> 6 
```

### approach(es)
#### approach 1: brute force O(N log N) time and O(N) space
*$N$ is the sum of all values of all linked lists in `lists`*
> **idea:** create a slice of elements using the input. sort said slice; build a linked list using sorted slice
* have a variable, `sl`, that holds the values of the linked lists
* have a variable, `ll`, that is the head of a linked list
*  for each linked list starting at `lists[0]`
    * push `lists[i].Val` to `sl`
* sort the elements of `sl` in ascending order
* for each element in `sl` starting at `sl[0]`
    * set `ll.Val` to `sl[i]`
    * set `ll.Next` to `sl[i+1]` if `i+1` exists, else, `null`
* return `ll`
#### approach 2: priority queue O(N log K) time and O(K) space
*$k$ is the length of `lists`*
> **idea:** push all elements to a min heap
* have a variable, `min_heap`, to be the min heap
* have a variable `ll`, that is the head of a linked list
* for each linked list in `lists`
    * push said list head to `min_heap`

        ```plaintext
            value = 1(L1, i0)
            value = 2(L2, i0)
            value = 3(L3, i0)
            ...
            value = N(Lk, i0)
        ```
    
    * pop the value `(L0, i0)` and append to `ll`

        ```plaintext
            value = 2(L2, i0)
            value = 3(L3, i0)
            ...
            value = N(Lk, i0)
        ```

    * push another value to `min_heap`

        ```plaintext
            value = 2(L2, i0)
            value = 3(L3, i0)
            ...
            value = N(Lk, i0)
            value = 2(L0, i1) <-------
        ```
* repeat previous step until `min_heap` is empty
* return `ll`

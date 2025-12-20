# merge two sorted lists

### problem
* given `h1` and `h2`, the heads of two sorted linked lists, `list1` and `list2`, merge said lists
* return one sorted list and its head
* said list should be made by splicing the nodes of the first two lists

### assumptions
* *nada*

### constaints
* 

### examples
#### example 1
```plaintext
    input: h1 = [1, 2, 4], h2 = [1, 3, 4]
    output: merged_list = [1, 1, 2, 3, 4, 4], head = 1
    explanation: none needed
```

#### example 2
```plaintext
    input: h1 = [], h2 = []
    output: merged_list = [], head = `null`
    explanation: none needed
```

#### example 3
```plaintext
    input: h1 = [], h2 = [0]
    output: merged_list = [0], head = 0 
    explanation: none needed
```

### approach(es)
#### approach 1: simultaneous iteration and sorting in place O(N+M) time and  O(1) space
* have a dummy node, `res`, to simplify edge case handling and track the result head
* use `curr` pointer to build the merged list iteratively
* while both `h1` and `h2` have nodes
    * compare `h1.Val` and `h2.Val`
        * attach the smaller node to `curr.Next` and advance that list's pointer
    * move `curr` to the newly attached node
    * attach remaining nodes from whichever list still has nodes
* return `res.Next` (skipping dummy node)
> time: O(n + m) where n, m are lengths of input lists <br/> space: O(1) excluding output
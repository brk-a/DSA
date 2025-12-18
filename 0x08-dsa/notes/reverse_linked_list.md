# reverse linked list

### problem
* given the head of a singly linked list, return the reversed list

### assumptions
* **nada**

### constraints
* number of nodes is in the range of [0, 5000]
* -5000 &le; Node.val &le; 5000

### examples
#### example 1
```plaintext
    input: lst = [1, 2, 3, 4, 5]
    output: [5, 4, 3, 2, 1]
```

#### example 2
```plaintext
    input: lst = [1, 2]
    output: [2, 1]
```

#### example 3
```plaintext
    input: lst = []
    output: []
```

### approach(es)
#### approach 1: stack: O(N) time and O(N<sup>2</sup> ) space
* **idea:** push all nodes onto a stack then pop them to rebuild the list in reverse
* push all nodes onto stack (except possibly last)
* pop from stack to rebuild reversed list
* return the new head (previous tail)
#### approach 2: iteration O(N) time
* **idea:** gradually build the linked list in reverse
* have variables: `current`, `previous` and `next`
* for each node of the linked list starting at `head`
    * as long as the current node does not point to `null`
        * assign the current node to... well... `current`
        * assign `current.Next` to `next`
        * assign `previous` to `current`
        * assign `next` to `current`
* return `previous`

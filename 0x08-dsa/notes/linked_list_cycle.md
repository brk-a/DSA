# linked list cycle

### problem
* given `head`, the head of a linked list, determine if the list has a cycle in it
> a linked list has a cycle when there is some node on the list that can be reached again by continually following the `next` pointer
* internally, `pos` is used to denote the index of the node that `tail`'s `next` pointer is connected to
* **important:** `pos` must not be passed as a parameter to your function
* return `true` when there is a cycle in the linked lists, else, `false`

### assumptions
* *nada*

### constraints
* 

### examples
#### example 1
```plaintext
    input: head = [3, 2, 0, 4], pos = 1
    output: true
    explanation: there is a cycle in the linked list: element 4's next points to element 2
```

#### example 2
```plaintext
    input: head = [3, 2, 0, 4], pos = `null`
    output: true
    explanation: there is no cycle in the linked list
```

#### example 3
```plaintext
    input: head = [3, 2, 0, 4], pos = 2
    output: true
    explanation: there is a cycle in the linked list: element 4's next points to element 0
```

### approach(es)
#### approach 1: Floyd's Tortoise and Hare (slow-fast pointer) O(N) time and O(1) space
* have two pointers: `slow` and `fast`
    * the former moves one node at a time; the latter traverses the list once before the former moves
    * `fast` is ahead of 
> **idea:** the values `slow` and `fast` pointers being the same tells us the linked list that has a cycle
* 
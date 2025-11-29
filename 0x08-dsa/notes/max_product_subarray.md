# max product subarray problem

### problem
* given an integer array, `nums`, find a sub-array that has the
largest product

### assumptions
* the answer will fit into a 32-bit int

### constraints
* 

### examples
#### example 1

```plaintext
    input: nums = [2, 3, -2, 4]
    output: 6
    explanation: [2, 3] has the largest product 6
```

#### example 2

```plaintext
    input: nums = [-2, 0, -1]
    output: 0
    explanation: result cannot be 2 because [-2, -1] is not a sub-array
```

### apparoach(es)
#### approach 1: brute force O(N<sup>2</sup>) time
* have two pointers, `i` and `j`, initialised to zero
* have a variable, `max_prod`, initialised to -&infin;
* have a variable, `curr_prod`, initialised to 1
* for each element in `nums` beginning at `nums[0]`
    * set `curr_prod` to the product of `nums[i]` and `nums[j]` where j = i, i+1, ..., `nums.length - 1`
    * check whether `curr_prod` &gt; `max_prod`
        * set `max_prod` to `curr_prod` if yes, else, continue
* increment `i` by one and repeat the previous step
* return `max_prod`
#### approach 2:
* 

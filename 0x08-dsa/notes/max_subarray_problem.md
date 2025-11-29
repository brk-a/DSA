# maximum subarray problem

### problem
* given an array of integers, `nums`,  find the sub-array that has
the largest sum and return said sum

### assumptions
* *nada*

### examples
#### example 1

```plaintext
    input: [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    output: 6
    explanation: the sub-array [4, -1, 2, 1] has the largest sum 6
```

#### example 2

```plaintext
    input: [1]
    output: 1
    explanation: the sub-array [1] has the largest sum 1
```

#### example 3

```plaintext
    input: [5, 4, -1, 7, 8]
    output: 23
    explanation: the sub-array [5, 4, -1, 7, 8] has the largest sum 23
```

### constraints
* 1 &le; nums.length &le; 10<sup>5</sup>
* 

### approach(es)
#### approach 1: brute force O(N<sup>2</sup>) time
* have two pointers, `i` and `j` initialised to zero
* have a var, `max_sum`, initialised to `-inf`
* have a var, `curr_sum`, initialised to zero
* for each element in `nums` beginning at `nums[0]`
    * set `curr_sum` to the sum of `nums[i]` and `nums[j]` where `j` = i, i+1 ..., `nums.length - 1`
    * check whether `curr_sum` &gt; `max_sum`
        * set `max_sum` to `curr_sum` if yes, else, continue
* increment `i` by one and repeat the previous step
* return `max_sum`
#### approach 2: Kadane's algo O(N) time and O(1) space
* * have a pointer, `i` initialised to zero
* have a var, `max_sum`, initialised to `-inf`
* have a var, `curr_sum`, initialised to zero
* for each element in `nums` beginning at `nums[0]`
    * check whether the sum of `nums[i]` and `curr_sum` is greater than `curr_sum`
        * set `curr_sum` to said sum if yes, else, continue
    * check whether `curr_sum` is greater than `max_sum`
        * set `max_sum` to `curr_sum` if yes, else, continue
* return `max_sum`

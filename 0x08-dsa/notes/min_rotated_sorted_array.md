# minimum in rotated sorted array

### problem
* suppose an array of length `n`, sorted in ascending order, is rotated
between 1 and `n` times
* example: `[4, 5, 6, 7, 0, 1, 2]` has been rotated 4 times and `[0, 1, 2, 4, 5, 6, 7]`
has been rotated 7 times (also zero times if you think about it)
> **notice:** rotating an array, `[a[0], a[1], ..., a[n-1]]`, one time results in `[a[n-1], a[0], a[1], ..., a[n-2]]`
* given the sorted rotated array,  `nums`, of unique elements, return the minimum element of this array

### assumptions
* each element in `nums` is unique

### constraints
* algo must run in at least O(lon N) time

### examples
#### example 1

```plaintext
    input: nums = [3, 4, 5, 1, 2]
    output: 1
    explanation: the original array, [1, 2, 3, 4, 5], was rotated three times
```

### approach(es)
#### approach 1: brute force O(N) time
* have a variable `min_elem` initialised to `inf`
* have a pointer, `i` initialised to `nums[0]`
* for each element starting at `nums[0]`
    * check if `nums[i]` &gt; `min_elem`
        * set `min_elem` to `nums[i]` if yes, else, continue
* return `min_elem`
#### approach 2: divide-and-conquer O(log N) time
* aka binary search algo
* have a variable, `nums_mid`, initialised to `inf`
* find the middle index of `nums`; call it `mid`
* set `min_elem` to `nums[mid]`
* check whether the element at `nums[mid-1]` is greater than `nums[mid]`
    * repeat steps two to four on  `nums[0 : mid]`, else, repeat said
    steps on `nums[mid : nums.length]`
* return `min_length`
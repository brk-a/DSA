# search rotated sorted array

### problem
* prior to being passed to your function, `nums` is possibly rotated at an unknown
pivot index, `k` (1 &le; k &lt; nums.length), such that the resulting array is 

    ```plaintext
        [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]
    ```
* said array is zero-indexed, for example, `[0, 1, 2, 4, 5, 6, 7]` rotated at pivot
 index 3 is `[4, 5, 6, 7, 0, 1, 2]`
* given the array, `nums`, after possible rotataion and an integer target, return the
index of the target if it is in `nums`, else, `-1`   

### assumptions

### examples
#### example 1
```plaintext
    input: nums = [4, 5, 6, 7, 0, 1, 2], target = 0
    output: 4 
```

#### example 2
```plaintext-4, 
    input: nums = [4, 5, 6, 7, 0, 1, 2], target = 3
    output: -1 
```

#### example 3
```plaintext
    input: nums = [1] target = 0
    output: -1 
```

### constraints
* you must write an algo that has O(log N) time complexity

### approach(es)
#### approach 1: brute force O(N) time
* have a pointer, `i`initialised to zero
    * i = 0, 1, ..., nums.length-1
* for each elements in `nums` starting at `nums[0]`
    * check whether `nums[i]` is equal to `target`
        * return `i` if yes, else, continue
* return `-1` if `target` is not in `nums`

#### approach 2: binary search O(log N) time
* have two pointers, `l` and `r` initialised to zero and `nums.length` - 1 respectively
* while `l` &lt; `r`
    * set `mid` to `(l+r) /2`
    * check whether `nums[mid]` is equal to `target`
        * return `mid` if yes, else, carry on
    * check whether `nums[l]` &le; `nums[mid]`
        * check whether `nums[l]` &le; `target` && `target` &lt; `nums[mid]` if yes, else, continue
            * set `r` to `mid` - 1 if yes, else, set `l` to `mid` + 1
        * check whether `nums[mid]` &lt; `target` && `target` &le; `nums[r]`
            * set `l` to `mid` + 1 if yes, else, set `r` to `mid` - 1
* return `-1` if `target` is not in `nums`
# three-sum problem

### problem
* given an integer array, `nums`, return all the triplets, `nums[i], nums[j], nums[k]`,
such that `i` &ne; `j`, `j` &ne; `k`, `i` &ne; `k` and `nums[i]` + `nums[j]` + `nums[k]` &equals; zero

### assumptions
* *nada*

### constraints
* solution must not contain duplicate triplets
* 3 &le; nums.length &le; 3000
* -10<sup>5</sup> &le; nums[i] &le; 10<sup>5</sup>

### examples
#### example 1

```plaintext
    input: nums = [-1, 0, 1, 2, -1, -4]
    output: [[-1, -1, 2], [-1, 0, 1]]
    explanation: 
        nums[0] + nums[1] + nums[2] = (-1) + 1 + 0 = 0
        nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0
        nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0

        the distinct triplets are [-1, 0, 1 ] and [-1, -1, 2]

    the order of the elements in the triplets is irrelevant
```

#### example 2

```plaintext
    input: nums = [1, 0, 1]
    output: []
    explanation: sum of possible triplet(s) is not zero
```

### approach(es)
#### approach 1: brute force O(N<sup>3</sup>) time
* sort `nums` in ascending order &rarr; O(N log N) time
* have three pointers: `i`, `j` and `k`
    * 0 &le; i &le; nums.length - 3
    * 1 &le; j &le; nums.length - 2
    * 2 &le; k &le; nums.length - 1
* have two empty arrays: `final_array` and `temp_array`
* for each element of `nums`
    * check whether the sum of `nums[i]`, `nums[j]` and `nums[k]` is zero
        * append `nums[i]`, `nums[j]` and `nums[k]` to `temp_array` if yes, else, carry on
        * check whether the array, `temp_array`, exists in `final_array`
            * append `temp_array` to `final_array` if not, else, carry on
        * clear `temp_array`
        * increase `j` and `k` by one
    * increase `i` by one
* return `final_array`
#### approach 2: binary search on sorted array O(N<sup>2</sup>) time
* sort `nums` in ascending order &rarr; O(N log N) time
* initialise an empty array `final_array` to store the unique triplets
* iterate through the array with an index `i` and value `v`
  * skip the current element if it is the same as the previous element to avoid duplicates
* for each element, set two pointers: `l` (left) immediately after `i` and `r` (right) at the end of the array
    * while `l` is less than `r`:
        * calculate the sum of elements at indices `i`, `l`, and `r`
        * if the sum is less than zero, move the left pointer `l` rightwards to increase the sum
        * if the sum is greater than zero, move the right pointer `r` leftwards to decrease the sum
        * if the sum is zero:
            * append the triplet `[v, nums[l], nums[r]]` to `final_array`
            * skip over duplicate values for the left pointer `l` by increasing it while values are the same
            * skip over duplicate values for the right pointer `r` by decreasing it while values are the same
        * increment the left pointer `l` and decrement the right pointer `r`
* return `final_array`


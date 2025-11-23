# two sum problem

### problem
* given an array of intergers, `nums`, and an integer, `target`, return the
indices of two numbers that add up to `target`

### assumptions
* there is one and only one solution in the input array
* the same element cannot be used twice
* the answer can be returned in any order

### examples
#### example 1

```plaintext
    input: nums = [2, 7, 11, 15], target = 9
    output: [0, 1]
    explanation: nums[0] and nums[1] add up to 9, therefore, return [0, 1]
```

#### example 2

```plaintext
    input: nums = [3, 2, 4], target = 6
    output: [1, 2]
```

#### example 3

```plaintext
    input: nums = [3, 3], target = 6
    output: [0, 1]
```
### constraints
* 2 &le; nums.length &le; 10<sup>4</sup>
* -10<sup>9</sup> &le; nums[i] &le; 10<sup>9</sup>
* -10<sup>9</sup> &le; target &le; 10<sup>9</sup>
* **only one valid answer exists**

> **Would be great if the algo has a time complexity of less than O(N<sup>2</sup>)**

### approach(es)
#### approach 1: brute force O(N<sup>2</sup>) time
* have two pointers: one to `nums[0]` and the other to `nums[1]`
* compare the sum of elements to `target`
* if sum == `target` return indices 0 and 1, else, point the second pointer to `nums[2]`
* repeat steps until you get a solution or the first pointer gets to the end of the array
* if first pointer gets to end, move it to `nums[2]` and move the second pointer to `nums[3]`
* repeat steps until you get a solution
#### approach 2: map O(N) time
* have an empty map
    * values are the indices of said keys in `nums`
    * keys are `nums[i]` if the difference between `nums[i]` is not a key in the may
* for each element starting at `nums[0]`, find the difference between `target` and `nums[i]`
    * if said difference is a key in the map, return the value of the key and `i`, else, add `nums[i]` as a key and `i` as a value to the map
* increase `i` by 1 and repeat the previous step
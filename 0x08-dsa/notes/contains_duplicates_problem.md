# contains duplicate problem

## problem
* given an array of ints, `nums`, return `true` if any value appears at least twice, else, `false`

## assumptions
* *nada*

## examples
### example 1

```plaintext
    input: nums = [1, 2, 3, 1]
    output: true
```

### example 2

```plaintext
    input: nums = [1, 2, 3, 4]
    output: false
```

### example 3

```plaintext
    input: nums = [1, 1, 1, 2, 3, 2, 4, 3, 1, 4]
    output: true
```

## constraints
* 1 &le; nums.length &le; 10<sup>5</sup>
* -10<sup>9</sup> &le; nums[i] &le; 10<sup>9</sup>

## approach(es)
### approach 1: brute force O(N<sup>2</sup>) time
* have a pointer that points to `nums[0]` (i = zero)
* for each element starting at `nums[i+1]` check if the element pointed to by the pointer is equal to `nums[i]`
    * return `true` if yes, else, move the pointer to `nums[1]` and repeat this step
### approach 2: sort the array O($NlogN$) time
* sort the array in ascending order
* have a pointer that points to `nums[0]`
* starting at i = zero, check if `nums[i]` and `nums[i+1]` are equal
    * return `true` if yes, else, move the pointer one position forward and repeat this step
### approach 3: map O(N) time and space
* have a map
    * the keys will be the unique elements in `nums`
    * the values will be ... anything, really; I will set mine to `true`
* for each element starting at `nums[0]` check if said element exists in the map
    * return `true` if yes, else, add it as a key to the map and carry on
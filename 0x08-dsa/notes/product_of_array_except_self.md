# product of array except self

## problem
* given an array of inetrgers, `nums`, return an array, `answer`, such
that `answer[i]` is equal to the product of each element in `nums` except
`nums[i]`

## assumptions
* the product of any prefix or suffix of `nums` is guaranteed to fit a
32-bit int

## examples
### example 1 

```plaintext
    input: nums = [1, 2, 3, 4]
    output: answer = [24, 12, 8, 6]
```

### example 2 

```plaintext
    input: nums = [-1, 1, 0, -3, 3]
    output: answer = [0, 0, 9, 0, 0]
```

## constraints
* write an algo that runs in O(N) time; do not use the division operator
* 2 &le; nums.length &le; 10<sup>5</sup>
* -30 &le; nums[i] &le; 30

## approaches
### approach 1: brute force O(N<sup>2</sup>) time
> violates the time complexity requirement in the instructions
* have two pointers: `i` and `j`
* for each element starting at `nums[0]`
    * multiply `nums[i]` and `nums[j]` if `i` &ne; `j`, else, continue
    * set `answer[i]` to be the product of the i<sup>th</sup> iteration
* return `answer`
### approach 2: prefix-suffix product O(N) space and time
> this is the correct solution according to the instructions
* have two arrays
    * `lp` &rarr; left product; product of `nums[i]` and `nums[i+1]` as long as `i` &lt; nums.length
    * `rp` &rarr; right product; product of `nums[i+1]` and `nums[i]` as long as `i` &lt; nums.length and `i` &gt; zero
* set `answer[i]` to be the product of `lp[i]` and `rp[i]`
* return `answer`
### approach 3: division method O(N) space and time
> instructions specificaly say not to use this
* check if at least two elements of `nums` are zero
    * set `answer` to be all zeroes (length num.length) and return `answer` if yes, else, continue
* check if any element of `nums` is zero
    * if yes: 
        * find the index, `i`, of the zero in `nums`
        * set `answer[i]` to be the product of the rest of the elements
        * set the rest of the elements in `answer` to zero
        * return `answer`
    * else:
        * continue
* find the product of the elements of `nums`, `prod`
* for each element in `nums`
    * set `answer[i]` to be the int quotient of `prod` and `nums[i]`
    * return `answer`
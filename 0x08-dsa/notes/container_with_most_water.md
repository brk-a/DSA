# container with most water

### problem
* you are given an integer array, `height`, of length $n$
* there are `n` vertical lines drawn such that the two
endpoints of the $i$<sup>th</sup> line are `(i, 0)` and
`(i, height[i])`
* find two lines that, together with the x-axis, form
a container such that said container contains the most
water
* return the maximum amount of water a container can store

### assumptions
* none of the elements in `height` are negative

### constraints
* you may not slant the container
* heights will fit an `Int32` number

### examples
#### example 1

```plaintext
    input: height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
    output: 49
    explanation: the above vertical lines are represented by 
        array [1, 8, 6, 2, 5, 4, 8, 3, 7]
        in this case, the max area of water the container can
        hold is 49 (7 * 7; get it?)
```

#### example 2

```plaintext
    input: height = [1, 1]
    output: 1
```

### approach(es)
#### approach 1: brute force O(N<sup>2</sup>) time
* have two pointers, `i` and `j`
    * 0 &le; i &lt; height.length - 1
    * 1 &le; j &lt; height.length
* have a variable, `max_amount`, initialised to zero
* for each element in `height` starting at `height[0]`
    * set `temp_max_amount` to the product of  min(`height[i]`, `height[j]`) and the difference between `i` and `j`
        
        ```plaintext
            temp_max_amount = min(height[i], height[j]) * (j - i)
        ```

    * check whether `temp_max_amount` is greater than `max_amount`
        * set `max_amount` to `temp_max_amount` if yes, else, carry on
    * increase `j` by one
* increase `i` by one and repeat previous step
* return `max_amount`
### approach 2: two-pointer, bidirectional O(N) time and space
* have two pointers, `l` and `r`, initialised to zero and `height.length` - 1 respectively
* have a variable, `max_amount`, initialised to zero
* for each element in `height` starting at `height[0]`
    * set `temp_max_amount` to the product of  min(`height[l]`, `height[r]`) and the difference between `l` and `r`
        
        ```plaintext
            temp_max_amount = min(height[l], height[r]) * (r - l)
        ```

    * check whether `temp_max_amount` is greater than `max_amount`
        * set `max_amount` to `temp_max_amount` if yes, else, carry on
    * check whether `height[l]` is less than `height[r]`
        * increase `l` by one if yes, else, decrease `r` by one
* return `max_amount`

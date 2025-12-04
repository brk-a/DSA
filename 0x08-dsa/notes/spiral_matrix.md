# spiral matrix

### problem
* given a `m` by `n` matrix, `matrix`, return all the elementsof `matrix in spiral order

### assumptions
* elements of `matrix`, `matrix[i][j]`, fit a `Int32` number

### constraints
* m == `matrix.length`
* n == `matrix[i].length`
* 1 &le; m, n &le; 10
* -100 &le; `matrix[i][j]` &le; 100

### examples
#### example 1

```plaintext
    input: matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
    output: [1, 2, 3, 6, 9, 8, 7, 4, 5]
```

#### example 2

```plaintext
    input: matrix = [[1, 2, 3, 4], [5, 6, 7, 8]]
    output: [1, 2, 3, 4, 8, 7, 6, 5]
```

### approach(es)
#### approach 1: brute force O(N) time
* have an empty array, `result`
* have the following variables
    * `rows` and `cols` initialised to `len(matrix)` and `len(matrix[0])` respectively
	* `left`, `right`, `top` and `bottom` initialised to zero, `cols`-1, zero, `rows`-1 respectively
        * top-left of `matrix` is [0, 0], bottom-right is [`rows`-1, `cols`-1]  etc
* while `left` is less than `right` and `top` is less than `bottom`
    * traverse left
        * append `matrix[top][col]` to `result` where `left` &le; `col` &le; `right`
    * traverse down
        * append `matrix[top][row]` to `result` where `top` &le; `row` &le; `bottom`
    * traverse right
        * check whether `left` is strictly less than `right` and `top` is strictly less than `bottom`
            * append `matrix[bottom][col]` to `result` where `right` &ge; `col` &gt; `left` if yes, else, continue 
    * traverse up
        * append `matrix[row][left]` to `result` where `bottom` &ge; `row` &gt; `top`
    * increase `left` and `top` by one; decrease `right` and `bottom` by one
* return `result`
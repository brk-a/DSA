# set matrix zeroes

### problem
* given a `m` by `n` integer matrix, `matrix`, set row `i` and column `j` to
zero if element `matrix[i][j]` is zero

### assumptions
* elements of `matrix` fit `Int32` integer

### constraints
* you must do it in place

### examples
#### example 1

```plaintext
    input: matrix = [[1, 1, 1], [1, 0, 1], [1, 1, 1]]
    output: [[1, 0, 1], [0, 0, 0], [1, 0, 1]]
```

#### example 2

```plaintext
    input: matrix = [[0, 1, 2, 0], [3, 4, 5, 2], [1, 3, 1, 5]]
    output: [[0, 0, 0, 0], [0, 4, 5, 0], [0, 3, 1, 0]]
```

### approach(es)
#### approach 1: brute force O(M<sup>N</sup>) time and O(M+N) space
* determine `m` and `n`
    * `m` &rarr; `matrix.length`
    * `n` &rarr; `matrix[i].length` where  0 &le; `i` &lt; `matrix.length`
* have two variables, `rows` and `cols`, to track the indices where the zeroes occur in `matrix`
    * initialise both to all `false`
* for each element in `matrix` starting at `matrix[0][0]`
    * check if said element is equal to zero
        * set `row[i]` or `col[j]` respectively to `true` if yes, else, continue
* for each element in `rows[i]` and `cols[j]` strting at `i` and `j` = zero
    * check whether `rows[i]` or `cols[j]` is `true`
        * set `matrix[i][j]` to zero if yes, else, continue
* return `matrix`
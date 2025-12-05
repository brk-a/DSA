# rotate image

### problem
* you are given a `m` by `n` matrix, `matrix`, that represents an image
* rotate the image 90<sup>o</sup>, that is, a quarter rotation clockwise

### assumptions
* elements of `matrix` fit a `Int32` integer
* input matrix will not always be a square matrix

### constraints
* you have to rotate the image in place

### examples
#### example 1

    ```plaintext
        input: matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
        output: [[7, 4, 1], [8, 5, 2], [9, 6, 3]]
    ```

#### example 2

    ```plaintext
        input: matrix = [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12]]
        output: [[4, 9, 5, 1], [8, 10, 6, 2], [12, 11, 7, 3]]
    ```

### approah(es)
#### approach 1: transpose and reverse O(N<sup>2</sup>) time
* idea is two-fold
    * turn the rows to columns (or columns to rows if you like)
    * reverse the elements of each row
* transposing
    * `m` and `n` are `matrix.length` and `matrix[0].length` respectively
    * have two pointers, `i` and `j`
        * 0 &le; `i` &lt; `n`
        * `i`+ 1 &le; `j` &lt; `m`
    * for each element of `matrix` starting at `matrix[0][0]`
        * set `matrix[i][j]` and `matrix[j][i]` to `matrix[j][i]` and `matrix[i][j]` respectively
* reversing
    * `m` and `n` are `matrix.length` and `matrix[0].length` respectively
    * have three pointers, `i`, `j` and `k`
        * 0 &le; `i` &lt; `n`
        * 0 &le; `j` &lt; `k`
        * `j` &le; `k` &lt; `n`-1
    * for each element of `matrix` starting at `matrix[0][0]`
        * set `matrix[i][j]` and `matrix[i][k]` to `matrix[i][k]` and `matrix[i][j]` respectively
* return `matrix`
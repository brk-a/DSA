# word search

### problem
* given a `m` by `n` grid of characters, `board`, and a string,
`word`, return `true` if `word` exists in `board`, else, `false`
* `word` can be constructed from letters of sequentially adjacent
cells; an adjacent cell to cell `board[i][j]` is any of the
following cells: `board[i-1][j]`, `board[i][j-1]`, `board[i+1][j]`
 and `board[i][j+1]`

||||||
|:---:|:---:|:---:|:---:|:---:|
||||||
|||`board[i-1][j]`|||
||`board[i][j-1]`|`board[i][j]`|`board[i][j+1]`||
|||`board[i+1][j]`|||
||||||

* a cell cannot be used more than once

### assumptions
* elements of `board` are not necessarily unique

### constraints
* 

### examples
#### example 1

```plaintext
    input: board = [["A", "B", "C", "E"], ["S", "F", "C", "S"], ["A", "D", "E", "E"]]
            word = "ABCCED"
    output: true
```

#### example 2

```plaintext
    input: board = [["A", "B", "C", "E"], ["S", "F", "C", "S"], ["A", "D", "E", "E"]]
            word = "ABCCED"
    output: true
```

### approach(es)
#### approach 1: depth-first algo
* implement DFS on the matrix
package arrays

import "fmt"

func WordSearch() {
    board1 := [][]byte{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}}
    board2 := [][]byte{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}}
    board3 := [][]byte{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}}
    
    words := []string{"ABCCED", "SEED", "ESEASA"}
    boards := [][][]byte{board1, board2, board3}
    
    fmt.Println("Word search solution using depth-first search...")
    fmt.Print("==================== ==================== ===================\n")
    
    for i, word := range words {
        fmt.Printf("Test %d: board = %q word = %q\n", i+1, boards[i], word)
        result := word_search(boards[i], word)
        fmt.Printf("Result: %v\n", result)
        fmt.Print("==================== ==================== ===================\n")
    }

}

func word_search(board [][]byte, word string) bool {
    if len(board) == 0 || len(board[0]) == 0 {
        return false
    }
    
    m, n := len(board), len(board[0])
    
    for i := 0; i < m; i++ {
        for j := 0; j < n; j++ {
            if dfs(board, i, j, word) {
                return true
            }
        }
    }
    return false
}

func dfs(board [][]byte, i, j int, word string) bool {
    // Base case: word fully matched
    if len(word) == 0 {
        return true
    }
    
    m, n := len(board), len(board[0])
    
    // Check bounds and character match
    if i < 0 || i >= m || j < 0 || j >= n || board[i][j] != word[0] {
        return false
    }
    
    // Mark current cell as visited
    c := board[i][j]
    board[i][j] = '*'
    
    // Directions: up, right, down, left
    directions := [4][2]int{{-1, 0}, {0, 1}, {1, 0}, {0, -1}}
    
    // Try all 4 directions recursively
    for _, dir := range directions {
        if dfs(board, i+dir[0], j+dir[1], word[1:]) {
            board[i][j] = c // backtrack
            return true
        }
    }
    
    // Backtrack
    board[i][j] = c
    return false
}

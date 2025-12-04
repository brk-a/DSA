package arrays

import "fmt"

func SpiralMatrix() {
	arr1 := [][]int{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}
	arr2 := [][]int{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}}
	arr3 := [][]int{{1, 2, 3, 4}, {5, 6, 7, 8}}
	arr1_copy := make([][]int, len(arr1))
	arr2_copy := make([][]int, len(arr2))
	arr3_copy := make([][]int, len(arr3))
	for i := 0; i < len(arr1); i++ {
		arr1_copy[i] = make([]int, len(arr1[i]))
		copy(arr1_copy[i], arr1[i])
	}
	for i := 0; i < len(arr2); i++ {
		arr2_copy[i] = make([]int, len(arr2[i]))
		copy(arr2_copy[i], arr2[i])
	}
	for i := 0; i < len(arr3); i++ {
		arr3_copy[i] = make([]int, len(arr3[i]))
		copy(arr3_copy[i], arr3[i])
	}

	fmt.Println("Spiral matrix solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := spiral_matrix_brute_force(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1_copy, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := spiral_matrix_brute_force(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2_copy, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := spiral_matrix_brute_force(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3_copy, sol3)
	fmt.Print("==================== ==================== ===================\n")
	// fmt.Println("Spiral matrix solution using ...")
	// fmt.Print("==================== ==================== ===================\n")
	// fmt.Println("Running first test ...")
	// sol1 = spiral_matrix_(arr1)
	// fmt.Printf("nums: %v and solution: %v\n", arr1_copy, sol1)
	// fmt.Print("==================== ==================== ===================\n")
	// fmt.Println("Running second test ...")
	// sol2 = spiral_matrix_(arr2)
	// fmt.Printf("nums: %v and solution: %v\n", arr2_copy, sol2)
	// fmt.Print("==================== ==================== ===================\n")
	// fmt.Println("Running third test ...")
	// sol3 = spiral_matrix_(arr3)
	// fmt.Printf("nums: %v and solution: %v\n", arr3_copy, sol3)
	// fmt.Print("==================== ==================== ===================\n")
}

func spiral_matrix_brute_force(matrix [][]int) []int {
    var result []int
    if len(matrix) == 0 || len(matrix[0]) == 0 {
        return result
    }
    
    rows, cols := len(matrix), len(matrix[0])
    left, right, top, bottom := 0, cols-1, 0, rows-1
    
    for left <= right && top <= bottom {
        // Top row: left to right
        for col := left; col <= right; col++ {
            result = append(result, matrix[top][col])
        }
        top++
        
        // Right column: top to bottom
        for row := top; row <= bottom; row++ {
            result = append(result, matrix[row][right])
        }
        right--
        
        // Bottom row: right to left (only if rows remain)
        if top <= bottom {
            for col := right; col >= left; col-- {
                result = append(result, matrix[bottom][col])
            }
            bottom--
        }
        
        // Left column: bottom to top (only if columns remain)
        if left <= right {
            for row := bottom; row >= top; row-- {
                result = append(result, matrix[row][left])
            }
            left++
        }
    }
    return result
}

// func spiral_matrix_(matrix [][]int) []int {
// 	var result []int

// 	return result
// }

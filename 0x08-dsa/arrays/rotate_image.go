package arrays

import "fmt"

func RotateImage() {
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

	fmt.Println("Rotate image solution using transpose-and-reverse...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := rotate_image_transpose_and_reverse(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1_copy, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := rotate_image_transpose_and_reverse(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2_copy, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := rotate_image_transpose_and_reverse(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3_copy, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func rotate_image_transpose_and_reverse(matrix [][]int) [][]int {
	m, n := len(matrix), len(matrix[0])

	// transpose the matrix
	for i := 0; i < n; i++ {
		for j := i + 1; j < m; j++ {
			matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
		}
	}

	// reverse the elements of each row
	for i := range matrix {
		for j, k := 0, n-1; j < k; j, k = j+1, k-1 {
			matrix[i][j], matrix[i][k] = matrix[i][k], matrix[i][j]
		}
	}

	return matrix
}

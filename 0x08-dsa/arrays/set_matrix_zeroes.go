package arrays

import "fmt"

func SetMatrixZeroes() {
	arr1 := [][]int{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}}
	arr2 := [][]int{{0, 1, 1}, {1, 0, 1}, {1, 1, 0}}
	arr3 := [][]int{{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}}
	arr1_copy := make([][]int, len(arr1))
	arr2_copy := make([][]int, len(arr2))
	arr3_copy := make([][]int, len(arr3))
	for i:=range len(arr1){
		arr1_copy[i] = make([]int, len(arr1[i]))
		copy(arr1_copy[i], arr1[i])
	}
	for i:=range len(arr2){
		arr2_copy[i] = make([]int, len(arr2[i]))
		copy(arr2_copy[i], arr2[i])
	}
	for i:=range len(arr3){
		arr3_copy[i] = make([]int, len(arr3[i]))
		copy(arr3_copy[i], arr3[i])
	}

	fmt.Println("Set matrix zeroes solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := set_matrix_zeroes_brute_force(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1_copy, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := set_matrix_zeroes_brute_force(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2_copy, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := set_matrix_zeroes_brute_force(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3_copy, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Set matrix zeroes solution using brute force (method two)...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = set_matrix_zeroes_brute_force_two(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1_copy, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = set_matrix_zeroes_brute_force_two(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2_copy, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = set_matrix_zeroes_brute_force_two(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3_copy, sol3)
	fmt.Print("==================== ==================== ===================\n")
	// fmt.Println("Set matrix zeroes solution using two pointers method...")
	// fmt.Print("==================== ==================== ===================\n")
	// fmt.Println("Running first test ...")
	// sol1 = set_matrix_zeroes_(arr1)
	// fmt.Printf("nums: %v and solution: %v\n", arr1_copy, sol1)
	// fmt.Print("==================== ==================== ===================\n")
	// fmt.Println("Running second test ...")
	// sol2 = set_matrix_zeroes_(arr2)
	// fmt.Printf("nums: %v and solution: %v\n", arr2_copy, sol2)
	// fmt.Print("==================== ==================== ===================\n")
	// fmt.Println("Running third test ...")
	// sol3 = set_matrix_zeroes_(arr3)
	// fmt.Printf("nums: %v and solution: %v\n", arr3_copy, sol3)
	// fmt.Print("==================== ==================== ===================\n")
}

func set_matrix_zeroes_brute_force(matrix [][]int) [][]int {
	m, n := len(matrix), len(matrix[0])
	rows := make([]bool, m)
	cols := make([]bool, n)
	res := make([][]int, m)
	for  i := 0; i < m; i++ {
		res[i] = make([]int, len(matrix[i]))
		copy(res[i], matrix[i])
	}

	// First pass to find zeroes
	for i := 0; i < m; i++ {
		for j := 0; j < n; j++ {
			if matrix[i][j] == 0 {
				rows[i] = true
				cols[j] = true
			}
		}
	}

	// Second pass to set zeroes
	for i := 0; i<m; i++ {
		for j := 0; j<n; j++ {
			if rows[i] || cols[j] {
				res[i][j] = 0
			}
		}
	}

	return res
}

func set_matrix_zeroes_brute_force_two(matrix [][]int) [][]int {
	m, n := len(matrix), len(matrix[0])
	rows := make([]bool, m)
	cols := make([]bool, n)
	res := make([][]int, m)
	for  i := 0; i < m; i++ {
		res[i] = make([]int, len(matrix[i]))
		copy(res[i], matrix[i])
	}

	for i := 0; i < m; i++ {
		for j := 0; j < n; j++ {
			if matrix[i][j] == 0 {
				rows[i] = true
				cols[j] = true
			}
		}
	}

	for i := 0; i < m; i++ {
		if rows[i] {
			for j := 0; j < n; j++ {
				res[i][j] = 0
			}
		}
	}

	for j := 0; j < n; j++ {
		if cols[j] {
			for i := 0; i < m; i++ {
				res[i][j] = 0
			}
		}
	}

	return res
}

// func set_matrix_zeroes_(matrix [][]int) [][]int {

// 	return matrix
// }

package arrays

import (
	"fmt"

	"github.com/brk-a/go-dsa/utils"
)

func ContainerWithMostWater() {
	arr1 := []int{1, 8, 6, 2, 5, 4, 8, 3, 7}
	arr2 := []int{1, 1}
	arr3 := []int{1, 0, 4, 2, 1, 4}

	fmt.Println("Container with the most water solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := container_with_most_water_brute_force(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := container_with_most_water_brute_force(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := container_with_most_water_brute_force(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Container with the most water solution using two pointers method...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = container_with_most_water_two_pointers_bidirectional(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = container_with_most_water_two_pointers_bidirectional(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = container_with_most_water_two_pointers_bidirectional(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func container_with_most_water_brute_force(height []int) int {
	max_amount := 0
	n := len(height)

	for i := 0; i < n-1; i++ {
		for j := i + 1; j < n; j++ {
			temp_max_amount := utils.FindMin(height[i], height[j]) * (j - i)
			if temp_max_amount > max_amount {
				max_amount = temp_max_amount
			}
		}
	}

	return max_amount
}

func container_with_most_water_two_pointers_bidirectional(height []int) int {
	max_amount := 0
	n := len(height)
	l, r := 0, n-1

	for l < r {
		temp_max_amount := utils.FindMin(height[l],  height[r]) * (r - l)

		if temp_max_amount > max_amount {
			max_amount = temp_max_amount
		}

		if height[l] < height[r] {
			l++
		} else {
			r--
		}
	}

	return max_amount
}

package arrays

import (
	"fmt"
	"math"
)

func MinRotatedSortedArray(){
	arr1 := []int{4, 5, 6, 7, 0, 2, 3}
	arr2 := []int{1}
	arr3 := []int{-1, 2, 3, 4, 5, 6, 7}

	fmt.Println("Minimum rotated sorted array solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := min_rotated_sorted_array_brute_force(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := min_rotated_sorted_array_brute_force(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := min_rotated_sorted_array_brute_force(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Minimum rotated sorted array solution using binary search algo...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = min_rotated_sorted_array_binary_search(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = min_rotated_sorted_array_binary_search(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = min_rotated_sorted_array_binary_search(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func min_rotated_sorted_array_brute_force(nums []int) int {
	min_elem := math.MaxInt32

	for _, v := range nums {
		if v < min_elem {
			min_elem = v
		}
	}

	return min_elem
}

func min_rotated_sorted_array_binary_search(nums []int) int {
	min_elem := math.MaxInt32
	l, r := 0, len(nums) -1

	for l < r {
		mid := (l+r) / 2
		if nums[mid] > nums[r] {
			l = mid + 1
		} else {
			r = mid
		}
	}

	min_elem = nums[l]

	return min_elem
}
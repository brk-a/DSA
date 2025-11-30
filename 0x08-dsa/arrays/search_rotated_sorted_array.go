package arrays

import "fmt"

func SearchRotatedSortedArray() {
	arr1 := []int{4, 5, 6, 7, 0, 1, 2}
	arr2 := []int{1}
	arr3 := []int{4, 5, 6, 7, 0, 1, 2}
	target1 := 0
	target2 := 0
	target3 := 3

	fmt.Println("Search rotated sorted array solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := search_rotated_sorted_array_brute_force(arr1, target1)
	fmt.Printf("nums: %v, target: %v  and solution: %v\n", arr1, target1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := search_rotated_sorted_array_brute_force(arr2, target2)
	fmt.Printf("nums: %v, target: %v  and solution: %v\n", arr2, target2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := search_rotated_sorted_array_brute_force(arr3, target3)
	fmt.Printf("nums: %v, target: %v  and solution: %v\n", arr3, target3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Search rotated sorted array solution using binary search algo...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = search_rotated_sorted_array_binary_search(arr1, target1)
	fmt.Printf("nums: %v, target: %v  and solution: %v\n", arr1, target1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = search_rotated_sorted_array_binary_search(arr2, target2)
	fmt.Printf("nums: %v, target: %v  and solution: %v\n", arr2, target2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = search_rotated_sorted_array_binary_search(arr3, target3)
	fmt.Printf("nums: %v, target: %v  and solution: %v\n", arr3, target3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func search_rotated_sorted_array_brute_force(nums []int, target int) int {
	for i, v := range nums {
		if v == target {
			return i
		}
	}

	return -1
}

func search_rotated_sorted_array_binary_search(nums []int, target int) int {
	l, r := 0, len(nums)-1

	for l <= r {
		mid := (l + r) / 2

		if nums[mid] == target {
			return mid
		}

		if nums[l] < nums[mid] {
			if nums[l] <= target && target < nums[mid] {
				r = mid -1
			} else {
				l = mid + 1
			}
		} else {
			if nums[mid] < target && target <= nums[r] {
				l = mid + 1
			} else {
				r = mid -1
			}
		}
	}

	return -1
}

package arrays

import (
	"fmt"
	"sort"
)

func ContainsDuplicates() {
	arr1 := []int{1, 2, 3, 1}
	arr2 := []int{1, 2, 3, 4}
	arr3 := []int{1, 1, 1, 2, 3, 2, 4, 3, 1, 4}

	fmt.Println("Contains-duplicates solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := contains_duplicates_brute_force(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := contains_duplicates_brute_force(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := contains_duplicates_brute_force(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Contains-duplicates solution using sorted array...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = contains_duplicates_sorted_array(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = contains_duplicates_sorted_array(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = contains_duplicates_sorted_array(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Contains-duplicates solution using maps...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = contains_duplicates_map(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = contains_duplicates_map(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = contains_duplicates_map(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func contains_duplicates_brute_force(nums []int) bool {
	dup := false

	for i := range nums {
		for j := i + 1; j < len(nums); j++ {
			if nums[i] == nums[j] {
				dup = true
				return dup
			}
		}
	}

	return dup
}

func contains_duplicates_sorted_array(nums []int) bool {
	dup := false

	// sort array in place
	sort.Ints(nums)

	for i := 0; i < len(nums)-1; i++ {
		if nums[i] == nums[i+1] {
			dup = true
			return dup
		}
	}

	return dup
}

func contains_duplicates_map(nums []int) bool {
	dup := false
	mp := make(map[int]bool)

	for i := range nums {
		if _, ok := mp[nums[i]]; ok {
			dup = true
			return dup
		} else {
			mp[nums[i]] = true
		}
	}

	return dup
}

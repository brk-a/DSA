package arrays

import "fmt"

func two_sum_map(nums []int, target int) []int {
	var result []int
	mp := make(map[int]int)

	for i:=range(nums) {
		diff := target - nums[i]
		if j, ok := mp[diff]; ok {
			//present: return value and `i`
			result = append(result, j, i)
		} else {
			// absent: add to map
			mp[nums[i]] = i
		}
	}

	return result
}

func two_sum_brute_force(nums []int, target int) []int {
	var result []int

	for i:=0; i<len(nums); i++{
		for j:=i+1; j<len(nums); j++ {
			if nums[i] + nums[j] == target {
				result = append(result, i, j)
			}
		}
	}

	return result
}

func TwoSum(){
	arr1 := []int{2, 7, 11, 15}
	arr2 := []int{3, 2, 4}
	arr3 := []int{3, 3}
	target1 := 22
	target2 := 7
	target3 := 6

	fmt.Println("Two sum solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := two_sum_brute_force(arr1, target1)
	fmt.Printf("nums: %v, target: %d and solution: %v\n", arr1, target1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := two_sum_brute_force(arr2, target2)
	fmt.Printf("nums: %v, target: %d and solution: %v\n", arr2, target2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := two_sum_brute_force(arr3, target3)
	fmt.Printf("nums: %v, target: %d and solution: %v\n", arr3, target3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Two sum solution using maps...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = two_sum_map(arr1, target1)
	fmt.Printf("nums: %v, target: %d and solution: %v\n", arr1, target1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = two_sum_map(arr2, target2)
	fmt.Printf("nums: %v, target: %d and solution: %v\n", arr2, target2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = two_sum_map(arr3, target3)
	fmt.Printf("nums: %v, target: %d and solution: %v\n", arr3, target3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}
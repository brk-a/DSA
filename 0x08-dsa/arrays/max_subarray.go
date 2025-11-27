package arrays

import (
	"fmt"
	"math"

	"github.com/brk-a/go-dsa/utils"
)

func MaxSubArray() {
	arr1 := []int{-2, 1, -3, 4, -1, 2, 1, -5, 4}
	arr2 := []int{1}
	arr3 := []int{5, 4, -1, 7, 8}

	fmt.Println("Max sum sub-array solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := max_subarray_brute_force(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := max_subarray_brute_force(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := max_subarray_brute_force(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Max sum sub-array solution using Kadane's algo...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = max_subarray_kadane_algo(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = max_subarray_kadane_algo(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = max_subarray_kadane_algo(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func max_subarray_brute_force(nums []int) int {
	max_sum := math.MinInt32

	for i := range nums {
		curr_sum := 0
		for j := i; j < len(nums); j++ {
			curr_sum += nums[j]
			max_sum = utils.FindMax(max_sum, curr_sum)
		}
	}

	return max_sum
}

func max_subarray_kadane_algo(nums []int) int {
	max_sum := math.MinInt32
	curr_sum := 0

	for _, v := range nums {
		curr_sum = utils.FindMax(v, curr_sum+v);
		max_sum = utils.FindMax(max_sum, curr_sum);
	}

	return max_sum
}

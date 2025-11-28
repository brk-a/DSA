package arrays

import (
	"fmt"
	"math"

	"github.com/brk-a/go-dsa/utils"
)

func MaxProductSubArray() {
	arr1 := []int{-2, 1, -3, 4, -1, 2, 1, -5, 4}
	arr2 := []int{2, 3, -2, 4}
	arr3 := []int{-2, 0, -1}

	fmt.Println("Max product sub-array solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := max_prod_subarray_brute_force(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := max_prod_subarray_brute_force(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := max_prod_subarray_brute_force(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Max product sub-array solution using a Kadane-like algo...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = max_prod_subarray_kadane_like_algo(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = max_prod_subarray_kadane_like_algo(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = max_prod_subarray_kadane_like_algo(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func max_prod_subarray_brute_force(nums []int) int {
	if len(nums) == 0 {
		return 0
	}
	max_prod := math.MinInt32

	for i := range nums {
		curr_prod := 1
		for j := i; j < len(nums); j++ {
			curr_prod *= nums[j]
			max_prod = utils.FindMax(max_prod, curr_prod)
		}
	}

	return max_prod
}

func max_prod_subarray_kadane_like_algo(nums []int) int {
	if len(nums) == 0 {
		return 0
	}
	max_prod := math.MinInt32
	curr_max_prod := nums[0]
	curr_min_prod := nums[0]

	for _, v := range nums {
		temp_max := curr_max_prod
		curr_max_prod = utils.FindMax(v, utils.FindMax(curr_max_prod*v, curr_min_prod*v))
		curr_min_prod = utils.FindMin(v, utils.FindMin(temp_max*v, curr_min_prod*v))
		max_prod = utils.FindMax(max_prod, curr_max_prod)
	}

	return max_prod
}

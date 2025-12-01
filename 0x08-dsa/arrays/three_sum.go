package arrays

import (
	"fmt"
	"sort"

	"github.com/brk-a/go-dsa/utils"
)

func ThreeSum() {
		arr1 := []int{1, 2, 3, 4}
	arr2 := []int{-1, 1, 0, -3, 3}
	arr3 := []int{-1, 0, 1, 2, -1, -4}

	fmt.Println("Three-sum solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := three_sum_brute_force(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := three_sum_brute_force(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := three_sum_brute_force(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Three-sum solution using binary search method...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = three_sum_sorted_binary_search(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = three_sum_sorted_binary_search(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = three_sum_sorted_binary_search(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func three_sum_brute_force(nums []int) [][]int {
	sort.Ints(nums)

	var final_array [][]int
	var temp_array []int
	n := len(nums)

	for i, v := range nums{
		for j:=i+1; j<n-1; j++ {
			for k:= j+1; k<n; k++ {
				if v+nums[j]+nums[k] == 0 {
					temp_array = append(temp_array, v, nums[j], nums[k])
					if !utils.ContainsSlice(final_array, temp_array) {
						final_array = append(final_array, temp_array)
					}
				}
				temp_array = []int{}
			}
		}
	}

	return final_array
}

func three_sum_sorted_binary_search(nums []int) [][]int {
	var final_array [][]int
	n := len(nums)

	for i, v := range nums {
		// skip over duplicates
		if i>0 && v == nums[i-1] {
			continue
		}

		l, r := i+1, n-1
		for l < r {
			sum := v + nums[l] + nums[r]
			if sum < 0 {
				l++
			} else if sum > 0 {
				r--
			} else {
				final_array = append(final_array, []int{v, nums[l], nums[r]})
				for l < r && nums[l] == nums[l+1] {
					l++
				}
				for l < r && nums[r] ==nums[r-1] {
					r--
				}

				l++
				r--
			}
		}
	}

	return final_array
}

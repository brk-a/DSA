package arrays

import "fmt"

func ProductArrayExceptSelf(){
	arr1 := []int{1, 2, 3, 4}
	arr2 := []int{-1, 1, 0, -3, 3}
	arr3 := []int{1, 1, 1, 2, 3, 2, 4, 3, 1, 4}

	fmt.Println("Product array sans self solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := product_array_sans_self_brute_force(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := product_array_sans_self_brute_force(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := product_array_sans_self_brute_force(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Product array sans self solution using prefix-suffix method...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = product_array_sans_self_prefix_suffix(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = product_array_sans_self_prefix_suffix(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = product_array_sans_self_prefix_suffix(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Product array sans self solution using division method...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = product_array_sans_self_division_method(arr1)
	fmt.Printf("nums: %v and solution: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = product_array_sans_self_division_method(arr2)
	fmt.Printf("nums: %v and solution: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = product_array_sans_self_division_method(arr3)
	fmt.Printf("nums: %v and solution: %v\n", arr3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func product_array_sans_self_brute_force(nums []int) []int {
	l := len(nums)
	answer := make([]int, l)

	for i := range nums{
		prod := 1
		for j, v := range nums {
			if i != j {
				prod *= v
			}
		}
		answer[i] = prod
	}

	return answer
}

func product_array_sans_self_prefix_suffix(nums []int) []int {
	l := len(nums)
	answer := make([]int, l)
	lp := make([]int, l)
	rp := make([]int, l)
	lp[0] = 1
	rp[l-1] = 1

	for i := 1; i<l; i++{
		lp[i] = lp[i-1] * nums[i-1]
	}
	for i := l-2; i >= 0; i-- {
		rp[i] = rp[i+1] * nums[i+1]
	}
	for i := range l {
		answer[i] = lp[i] * rp[i]
	}

	return answer
}

func product_array_sans_self_division_method(nums []int) []int {
	l := len(nums)
	answer := make([]int, l)
	prod := 1
	zero_count := 0
	zero_index := -1

	// Count zeroes and identify zero index if any
    for i, v := range nums {
        if v == 0 {
            zero_count++
            zero_index = i
            if zero_count > 1 {
                // More than one zero - answer all zeroes
                return answer // already initialised with zeroes
            }
        }
    }

	// If exactly one zero, answer is zero except product of all non-zero at zero's position
    if zero_count == 1 {
        prod := 1
        for i, v := range nums {
            if i != zero_index {
                prod *= v
            }
        }
        answer[zero_index] = prod
        return answer
    }

	// if no zeroes, calculate the product of all elements and divide by elements of nums
	for _, v := range nums {
		prod *= v
	}
	for i, v := range nums {
		answer[i] = prod / v
	}
	return answer
}
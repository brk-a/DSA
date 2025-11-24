package arrays

import (
	"fmt"
	"math"
)

func BestTimeToBuyStock() {
	arr1 := []int{7, 1, 5, 3, 6, 4}
	arr2 := []int{7, 6, 4, 3, 1}

	fmt.Println("Best time to buy stock using brute force ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := max_profit_brute_force(arr1)
	fmt.Printf("prices: %v and profit: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := max_profit_brute_force(arr2)
	fmt.Printf("prices: %v and profit: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Best time to buy stock using one-pass strategy...")
	fmt.Println("Running first test ...")
	sol1 = max_profit_one_pass(arr1)
	fmt.Printf("prices: %v and profit: %v\n", arr1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = max_profit_one_pass(arr2)
	fmt.Printf("prices: %v and profit: %v\n", arr2, sol2)
	fmt.Print("==================== ==================== ===================\n")
}

func max_profit_brute_force(prices []int) int {
	profit := 0

	for i := range(prices) {
		for j := i+1; j < len(prices); j++ {
			if p := prices[j] - prices[i]; p > profit {
				profit = p
			}
		}
	}

	return profit
}

func max_profit_one_pass(prices []int) int {
	profit := 0
	min_price := math.MaxInt32

	for _, mp := range(prices) {
		if mp < min_price {
			min_price = mp
		} else if p := (mp - min_price); p > profit {
			profit = p
		}
	}

	return profit
}

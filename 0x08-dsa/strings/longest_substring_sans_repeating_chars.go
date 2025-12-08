package strings

import (
	"fmt"

	"github.com/brk-a/go-dsa/utils"
)

func LongestSubstringSansRepeatingChars() {
	str1 := "abcabcbb"
	str2 := "bbbbb"
	str3 := "pwwkew"

	fmt.Println("Longest Substring Sans Repeating Chars solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := longest_substring_sans_repeating_chars_brute_force(str1)
	fmt.Printf("s: %v, and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := longest_substring_sans_repeating_chars_brute_force(str2)
	fmt.Printf("s: %v, and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := longest_substring_sans_repeating_chars_brute_force(str3)
	fmt.Printf("s: %v, and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Longest Substring Sans Repeating Chars solution using maps...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = longest_substring_sans_repeating_chars_map(str1)
	fmt.Printf("s: %v, and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = longest_substring_sans_repeating_chars_map(str2)
	fmt.Printf("s: %v, and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = longest_substring_sans_repeating_chars_map(str3)
	fmt.Printf("s: %v, and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func longest_substring_sans_repeating_chars_brute_force(s string) int {
	max_len := 0
	n := len(s)

	for i := range n { // i:=0; i<n; i++
		for j := i; j < n; j++ {
			if utils.AllUnique(s, i, j) {
				if max_len < j-i+1 {
					max_len = j - i + 1
				}
			}
		}
	}

	return max_len
}

func longest_substring_sans_repeating_chars_map(s string) int {
	max_len := 0
	n := len(s)
	mp := make(map[byte]int)
	left := 0

	for right := range n{
		if i, v := mp[s[right]]; v && i >= left {
			left = i+1
		}
		mp[s[right]] = right
		
		curr_len := right - left + 1
		if max_len < curr_len {
			max_len = curr_len
		}
	}

	return max_len
}

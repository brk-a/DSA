package strings

import (
	"fmt"

	"github.com/brk-a/go-dsa/utils"
)

func PalindromicSubstrings() {
	str1 := "abcabcbabad"
	str2 := "bbcbbd"
	str3 := "pwewkew"

	fmt.Println("Palindromic sub-strings solution using brute force method...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := palindromic_substrings_brute_force(str1)
	fmt.Printf("s: %v, and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := palindromic_substrings_brute_force(str2)
	fmt.Printf("s: %v, and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := palindromic_substrings_brute_force(str3)
	fmt.Printf("s: %v, and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Palindromic sub-strings solution using expand-the-centre method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = palindromic_substrings_expand_the_centre(str1)
	fmt.Printf("s: %v, and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = palindromic_substrings_expand_the_centre(str2)
	fmt.Printf("s: %v, and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = palindromic_substrings_expand_the_centre(str3)
	fmt.Printf("s: %v, and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func palindromic_substrings_brute_force(s string) int {
	n := len(s)
	if n == 0 {
		return 0
	}
	strings_found := 0

	for i := range n {
		for j := i; j < n; j++ {
			if utils.IsPalindromeTwo(s[i : j+1]) {
				strings_found++
			}
		}
	}

	return strings_found
}

func palindromic_substrings_expand_the_centre(s string) int {
	n := len(s)
	if n == 0 {
		return 0
	}
	strings_found := 0

	expand := func(left, right int) int {
		exp := 0
		for left >= 0 && right < n && s[left] == s[right] {
			exp++
			left--
			right++
		}

		return exp
	}

	for i := range n {
		// handle odd length
		strings_found += expand(i, i)

		// handle even length
		strings_found += expand(i, i+1)
	}

	return strings_found
}

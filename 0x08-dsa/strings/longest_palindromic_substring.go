package strings

import (
	"fmt"

	"github.com/brk-a/go-dsa/utils"
)

func LongestPalindromicSubstring() {
	str1 := "abcabcbabad"
	str2 := "bbcbbd"
	str3 := "pwewkew"

	fmt.Println("Longest palindromic sub-string solution using brute force...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := longest_palindromic_substring_brute_force(str1)
	fmt.Printf("s: %v, and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := longest_palindromic_substring_brute_force(str2)
	fmt.Printf("s: %v, and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := longest_palindromic_substring_brute_force(str3)
	fmt.Printf("s: %v, and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Longest palindromic sub-string solution using maps...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = longest_palindromic_substring_(str1)
	fmt.Printf("s: %v, and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = longest_palindromic_substring_(str2)
	fmt.Printf("s: %v, and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = longest_palindromic_substring_(str3)
	fmt.Printf("s: %v, and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func longest_palindromic_substring_brute_force(s string) string {
	n := len(s)
	if n == 0 {
		return ""
	}
	if n == 1 {
		return s
	}

	var longest_str string
	max_pal_len := 1

	for i := range n {
		for j := i + 1; j <= n; j++ {
			sub_str := s[i:j]
			if utils.IsPalindrome(sub_str) && len(sub_str) > max_pal_len {
				longest_str = sub_str
				max_pal_len = len(sub_str)
			}
		}
	}

	return longest_str
}

func longest_palindromic_substring_(s string) string {
	n := len(s)
	if n == 0 {
		return ""
	}
	if n == 1 {
		return s
	}

	start, max_pal_len := 0, 1
	expand := func(left, right int) (int, int) {
		for left >= 0 && right < n && s[left] == s[right] {
			left--
			right++
		}

		return left + 1, right - 1
	}

	for i := range n {
		// handle odd length
		l1, r1 := expand(i, i)

		// handle even length
		l2, r2 := expand(i, i+1)

		//handle longer substring
		l, r := l1, r1
		if r2-l2 > r-l {
			l, r = l2, r2
		}
		if r-l+1 > max_pal_len {
			start = l
			max_pal_len = r - l + 1
		}
	}
	return s[start : start+max_pal_len]
}

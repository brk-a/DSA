package strings

import "fmt"

func LongestSubstringCharReplacement() {
	str1 := "ABAB"
	str2 := "AABABBA"
	str3 := "PWWKEW"
	k1 := 2
	k2 := 1
	k3 := 3

	fmt.Println("Longest Substring With Character Replacement solution using brute force method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := longest_substring_char_replacement_brute_force(str1, k1)
	fmt.Printf("s: %v, k: %v and solution: %v\n", str1, k1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := longest_substring_char_replacement_brute_force(str2, k2)
	fmt.Printf("s: %v, k: %v and solution: %v\n", str2, k2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := longest_substring_char_replacement_brute_force(str3, k3)
	fmt.Printf("s: %v, k: %v and solution: %v\n", str3, k3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Longest Substring With Character Replacement solution using map-with-sliding-window method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = longest_substring_char_replacement_map(str1, k1)
	fmt.Printf("s: %v, k: %v and solution: %v\n", str1, k1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = longest_substring_char_replacement_map(str2, k2)
	fmt.Printf("s: %v, k: %v and solution: %v\n", str2, k2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = longest_substring_char_replacement_map(str3, k3)
	fmt.Printf("s: %v, k: %v and solution: %v\n", str3, k3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func longest_substring_char_replacement_brute_force(s string, k int) int {
	max_len := 0
	n := len(s)

	for i := 0; i < n; i++ {
		for j := i; j < n; j++ {
			mp := make(map[byte]int)
			for m := i; m <= j; m++ {
				mp[s[m]]++
			}

			max_freq := 0
			for _, count := range mp {
				if count > max_freq {
					max_freq = count
				}
			}

			replacement_idx := (j - i + 1) - max_freq
			if replacement_idx <= k {
				if (j - i + 1) > max_len {
					max_len = j - i + 1
				}
			}
		}
	}

	return max_len
}

func longest_substring_char_replacement_map(s string, k int) int {
	max_len, max_count, start := 0, 0, 0
	n := len(s)
	mp := make (map[byte]int)

	for end:=0; end<n; end++{
		mp[s[end]]++

		if mp[s[end]] > max_count {
			max_count = mp[s[end]]
		}

		if (end - start + 1) - max_count > k {
			mp[s[start]]--
			start++
		}

		max_len = end-start+1
	}

	return max_len
}

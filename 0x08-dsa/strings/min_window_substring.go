package strings

import (
	"fmt"
	"math"

	"github.com/brk-a/go-dsa/utils"
)

func MinimumWindowSubstring() {
	str1 := "ADOBECODEBANC"
	str2 := "a"
	str3 := "PWWKEWEKWWP"
	t1 := "ABC"
	t2 := "aa"
	t3 := "WWW"

	fmt.Println("Minimum window substring solution using brute force method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := minimum_window_substring_brute_force(str1, t1)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str1, t1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := minimum_window_substring_brute_force(str2, t2)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str2, t2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := minimum_window_substring_brute_force(str3, t3)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str3, t3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Minimum window substring solution using map-with-sliding-window method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = minimum_window_substring_map(str1, t1)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str1, t1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = minimum_window_substring_map(str2, t2)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str2, t2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = minimum_window_substring_map(str3, t3)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str3, t3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func minimum_window_substring_brute_force(s, t string) string {
	min_win_sub := ""
	m, n := len(s), len(t)

	if m == 0 || n == 0 {
		return min_win_sub
	}

	t_map := make(map[byte]int)
	for i := range n {
		t_map[t[i]]++
	}

	min_start, min_len := 0, math.MaxInt32
	for start := range m {
		s_map := make(map[byte]int)
		for end := start; end < m; end++ {
			s_map[s[end]]++

			if utils.ContainsAll(s_map, t_map) {
				if (end - start + 1) < min_len {
					min_start = start
					min_len = end - start + 1
				}
				break
			}
		}
	}

	if min_len == math.MaxInt32 {
		min_win_sub = ""
	} else {
		min_win_sub = s[min_start : min_start+min_len]
	}

	return min_win_sub
}

func minimum_window_substring_map(s, t string) string {
	min_win_sub := ""
	m, n := len(s), len(t)

	if m == 0 || n == 0 {
		return min_win_sub
	}

	t_map := make(map[byte]int)
	for i := range n {
		t_map[t[i]]++
	}

	start, end, min_start, min_len := 0, 0, 0, math.MaxInt32
	need, have := len(t_map), 0
	w_map := make(map[byte]int)
	for end < m {
		char := s[end]
		w_map[char]++

		if count, ok := t_map[char]; ok && w_map[char] == count {
			have++
		}

		for have == need {
			if (end - start + 1) < min_len {
				min_start = start
				min_len = end - start + 1
			}

			char_start := s[start]
			w_map[char_start]--

			if count, ok := t_map[char_start]; ok && w_map[char_start] < count {
				have--
			}

			start++
		}
		end++
	}

	if min_len == math.MaxInt32 {
		min_win_sub = ""
	} else {
		min_win_sub = s[min_start : min_start+min_len]
	}

	return min_win_sub
}

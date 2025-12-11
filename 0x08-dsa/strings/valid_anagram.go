package strings

import (
	"fmt"

	"github.com/brk-a/go-dsa/utils"
)

func ValidAnagram() {
	str1 := "anagram"
	str2 := "maina"
	str3 := "tit"
	t1 := "nagaram"
	t2 := "mania"
	t3 := "tat"

	fmt.Println("Valid anagram solution using brute force method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := valid_anagram_brute_force(str1, t1)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str1, t1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := valid_anagram_brute_force(str2, t2)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str2, t2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := valid_anagram_brute_force(str3, t3)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str3, t3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Valid anagram solution using map method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = valid_anagram_map(str1, t1)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str1, t1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = valid_anagram_map(str2, t2)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str2, t2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = valid_anagram_map(str3, t3)
	fmt.Printf("s: %v, t: %v and solution: %v\n", str3, t3, sol3)
	fmt.Print("==================== ==================== ===================\n")
}

func valid_anagram_brute_force(s, t string) bool {
	is_anagram := false
	m, n := len(s), len(t)

	if m != n {
		return is_anagram
	}

	is_anagram = utils.SortString(s) == utils.SortString(t)

	return is_anagram
}

func valid_anagram_map(s, t string) bool {
	is_anagram := false
	m, n := len(s), len(t)

	if m != n {
		return is_anagram
	}

	mp := make(map[rune]int)
	for _, char := range s {
		mp[char]++
	}

	for _, char := range t {
		mp[char]--

		if mp[char] < 0 {
			return is_anagram
		}
	}

	for _, char := range mp{
		if char != 0 {
			return is_anagram
		}
	}

	is_anagram = true

	return is_anagram
}

package strings

import (
	"fmt"

	"github.com/brk-a/go-dsa/utils"
)

func GroupAnagrams(){
	str1 := []string{"anagram", "maina", "tit", "nagaram", "mania", "tat"}
	str2 := []string{"a"}
	str3 := []string{""}
	str4 := []string{"eat", "tea", "tan", "ant", "ate", "bat"}

	fmt.Println("Group anagram solution using brute force method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := group_anagrams_brute_force(str1)
	fmt.Printf("s: %v and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := group_anagrams_brute_force(str2)
	fmt.Printf("s: %v and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := group_anagrams_brute_force(str3)
	fmt.Printf("s: %v and solution: %v\n", str3,  sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running fourth test ...")
	sol4 := group_anagrams_brute_force(str4)
	fmt.Printf("s: %v and solution: %v\n", str4, sol4)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("group anagram solution using map method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = group_anagrams_map(str1)
	fmt.Printf("s: %v and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = group_anagrams_map(str2)
	fmt.Printf("s: %v and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = group_anagrams_map(str3)
	fmt.Printf("s: %v and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running fourth test ...")
	sol4 = group_anagrams_map(str4)
	fmt.Printf("s: %v and solution: %v\n", str4, sol4)
	fmt.Print("==================== ==================== ===================\n")
}

func group_anagrams_map(strs []string) [][]string {
	group_anagrams := [][]string{}
	n := len(strs)
	if n < 1 {
		return group_anagrams
	}

	mp := make(map[string][]string)
	for _, word :=range strs {
		sorted_word := utils.SortString(word)
		mp[sorted_word] = append(mp[sorted_word], word)
	}

	for _, group := range mp {
		group_anagrams = append(group_anagrams, group)
	}

	return group_anagrams
}

func group_anagrams_brute_force(strs []string) [][]string {
    group_anagrams := [][]string{}
    n := len(strs)
    used := make([]bool, n) // Track which strings have been grouped

    for i := 0; i < n; i++ {
        if used[i] {
            continue
        }
        group := []string{strs[i]}
        used[i] = true

        for j := i + 1; j < n; j++ {
            if !used[j] && utils.AreAnagrams(strs[i], strs[j]) {
                group = append(group, strs[j])
                used[j] = true
            }
        }
        group_anagrams = append(group_anagrams, group)
    }

    return group_anagrams
}

package strings

import "fmt"

func ValidParentheses() {
	str1 := "()"
	str2 := "{}()[]"
	str3 := "([]}]"
	str4 := "([{[()]}])"

	fmt.Println("Valid parentheses solution using brute force method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := valid_parentheses_brute_force(str1)
	fmt.Printf("s: %v and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := valid_parentheses_brute_force(str2)
	fmt.Printf("s: %v and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := valid_parentheses_brute_force(str3)
	fmt.Printf("s: %v and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running fourth test ...")
	sol4 := valid_parentheses_brute_force(str4)
	fmt.Printf("s: %v and solution: %v\n", str4, sol4)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Valid parentheses solution using map method ...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 = valid_parentheses_map(str1)
	fmt.Printf("s: %v and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 = valid_parentheses_map(str2)
	fmt.Printf("s: %v and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 = valid_parentheses_map(str3)
	fmt.Printf("s: %v and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running fourth test ...")
	sol4 = valid_parentheses_map(str4)
	fmt.Printf("s: %v and solution: %v\n", str4, sol4)
	fmt.Print("==================== ==================== ===================\n")
}

func valid_parentheses_map(s string) bool {
	if len(s) == 0 {
		return false // Empty string is invalid
	}

	stack := []byte{}
	mp := map[byte]byte{
		')': '(',
		']': '[',
		'}': '{',
	}

	for _, char := range s {
		if expected, ok := mp[byte(char)]; ok {
			// Pop if stack not empty and top matches expected open
			if len(stack) > 0 && stack[len(stack)-1] == expected {
				stack = stack[:len(stack)-1]
			} else {
				return false
			}
		} else {
			// Push opening brackets
			stack = append(stack, byte(char))
		}
	}

	return len(stack) == 0
}

func valid_parentheses_brute_force(s string) bool {
	if len(s) == 0 {
		return true
	}

	// Try to remove one pair in this call
	for i := 0; i < len(s)-1; i++ {
		pair := s[i : i+2]
		if pair == "()" || pair == "[]" || pair == "{}" {
			// remove this pair and recurse
			ns := s[:i] + s[i+2:]
			return valid_parentheses_brute_force(ns)
		}
	}

	// no pair found to remove, but string not empty
	return false
}

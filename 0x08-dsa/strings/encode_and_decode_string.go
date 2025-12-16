package strings

import (
	"fmt"
	"strconv"
	"strings"
)

type Codec struct{}

func EncodeDecodeString() {
	str1 := []string{"abcabcbabad", "bbcbbd", "pwewkew"}
	str2 := []string{"Hello, ", "world!"}
	str3 := []string{"rambo", "bambo", "boom!", "boom!", "abracadabra!", "brrr...cha!"}

	fmt.Println("Encode-decode string solution: ## ENCODING ##...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	sol1 := encode(str1)
	fmt.Printf("vector: %v, and solution: %v\n", str1, sol1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	sol2 := encode(str2)
	fmt.Printf("vector: %v, and solution: %v\n", str2, sol2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	sol3 := encode(str3)
	fmt.Printf("vector: %v, and solution: %v\n", str3, sol3)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Encode-decode string solution: ## DECODING ##...")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running first test ...")
	str1 = decode(sol1)
	fmt.Printf("s: %v, and solution: %v\n", sol1, str1)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running second test ...")
	str2 = decode(sol2)
	fmt.Printf("s: %v, and solution: %v\n", sol2, str2)
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running third test ...")
	str3 = decode(sol3)
	fmt.Printf("s: %v, and solution: %v\n", sol3, str3)
	fmt.Print("==================== ==================== ===================\n")
}

func encode(strs []string) string {
	var builder strings.Builder
	for _, str := range strs {
		builder.WriteString(strconv.Itoa(len(str)))
		builder.WriteByte('#')
		builder.WriteString(str)
	}
	return builder.String()
}

func decode(s string) []string {
	var result []string
	i := 0
	n := len(s)

	for i < n {
		// Find the '#' delimiter
		j := strings.IndexByte(s[i:], '#')
		if j == -1 {
			break // Malformed input
		}
		j += i // Absolute position

		// Parse length
		length, err := strconv.Atoi(s[i:j])
		if err != nil {
			break // Malformed length
		}

		i = j + 1 // Skip past '#'

		// Extract string of exact length
		if i+length <= n {
			result = append(result, s[i:i+length])
			i += length
		} else {
			break // Incomplete string
		}
	}
	return result
}

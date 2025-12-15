package utils

func IsPalindromeTwo(s string) bool {
	n := len(s)
	is_palindrome := false

	for i:=0; i<n/2; i++ {
		if s[i] != s[n-i-1] {
			return is_palindrome
		}
	}

	is_palindrome = true
	return is_palindrome
}
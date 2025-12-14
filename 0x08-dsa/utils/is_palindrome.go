package utils

func IsPalindrome(s string) bool {
	is_palindrome := false
	left, right := 0, len(s) -1

	for left < right {
		if s[left] != s[right] {
			return is_palindrome
		}
		left++
		right--
	}

	is_palindrome = true
	return is_palindrome
}

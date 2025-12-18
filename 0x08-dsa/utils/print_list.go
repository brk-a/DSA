package utils

import (
	"fmt"
	"strings"
)

func PrintList(head *ListNode) string {
	if head == nil {
		return "[]"
	}
	result := []string{}
	for head != nil {
		result = append(
			result,
			fmt.Sprintf("%d", head.Val),
		)
		head = head.Next
	}
	return fmt.Sprintf("[%s]", strings.Join(result, ", "))
}

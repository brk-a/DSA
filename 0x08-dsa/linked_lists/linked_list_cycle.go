package linkedlists

import (
	"fmt"

	"github.com/brk-a/go-dsa/utils"
)

func LinkedListCycle() {
	tests := []struct {
		name     string
		vals     []int
		pos      int
		expected bool
	}{
		{"cycle at pos 1", []int{3, 2, 0, 4}, 1, true},
		{"no cycle", []int{3, 2, 0, 4}, -1, false},
		{"cycle at pos 2", []int{3, 2, 0, 4}, 2, true},
		{"single node", []int{1}, -1, false},
		{"two nodes no cycle", []int{1, 2}, -1, false},
		{"self cycle", []int{1}, 0, true},
		{"empty", []int{}, -1, false},
		{"nil head", []int{}, -1, false},
	}

	for _, tt := range tests {
			head := utils.CreateCycledList(tt.vals, tt.pos)
			result := linked_list_cycle_slow_fast_moment(head)
			if result != tt.expected {
				fmt.Printf("Expected %v, got %v", tt.expected, result)
			}
	}
}

func linked_list_cycle_slow_fast_moment(head *utils.ListNode) bool {
	if head == nil || head.Next == nil {
		return false
	}

	slow, fast := head, head
	for fast != nil && fast.Next != nil {
		slow = slow.Next
		fast = fast.Next.Next
		if slow == fast {
			return true
		}
	}
	return false
}

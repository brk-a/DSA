package utils

type ListNode struct {
	Val  int
	Next *ListNode
}

func BuildList(vals []int) *ListNode {
    if len(vals) == 0 {
        return nil
    }
    head := &ListNode{Val: vals[0]}
    curr := head
    for i := 1; i < len(vals); i++ {
        curr.Next = &ListNode{Val: vals[i]}
        curr = curr.Next
    }
    return head
}
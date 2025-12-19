package utils

func CreateCycledList(vals []int, pos int) *ListNode {
    if len(vals) == 0 {
        return nil
    }
    head := BuildList(vals)
    
    if pos == -1 {
        return head
    }
    
    curr := head
    target := head
    for i := 0; i < pos; i++ {
        target = target.Next
    }
    
    for curr.Next != nil {
        curr = curr.Next
    }
    curr.Next = target
    
    return head
}
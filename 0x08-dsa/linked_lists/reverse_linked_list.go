package linkedlists

import (
	"fmt"

	"github.com/brk-a/go-dsa/utils"
)

func ReverseLinkedList() {
	tests := []struct {
        name     string
        input    []int
        expected []int
    }{
        {"Example 1", []int{1, 2, 3, 4, 5}, []int{5, 4, 3, 2, 1}},
        {"Example 2", []int{1, 2}, []int{2, 1}},
        {"Empty", []int{}, []int{}},
        {"Boundary", []int{5000, -4000, 3000, -200, 100, 5, -4, 3, -2, 1}, 
                      []int{1, -2, 3, -4, 5, 100, -200, 3000, -4000, 5000}},
    }

    fmt.Println("Reverse linked list solution using stack and iterative approcahes ...")
	fmt.Print("==================== ==================== ===================\n")
    
    for _, tt := range tests {
        // Build input list
        input := utils.BuildList(tt.input)
        
        // Test stack approach
        fmt.Printf("%s:\n", tt.name)
        fmt.Printf("  Input:  %s\n", utils.PrintList(input))
        stackResult := reverse_linked_list_stack_approach(input)
        fmt.Printf("  Stack:  %s\n", utils.PrintList(stackResult))
		fmt.Print("==================== ==================== ===================\n")
        
        // Rebuild for iterative test
        input = utils.BuildList(tt.input)
        iterativeResult := reverse_linked_list_iterative_approach(input)
        fmt.Printf("  Iter:   %s\n", utils.PrintList(iterativeResult))
        fmt.Printf("  Expected: %v\n\n", tt.expected)
		fmt.Print("==================== ==================== ===================\n")
    }
}

func reverse_linked_list_stack_approach(head *utils.ListNode) *utils.ListNode {
    if head == nil || head.Next == nil {
        return head
    }

    // Step 1: Push all nodes onto stack
    stack := []*utils.ListNode{}
    curr := head
    for curr != nil {
        stack = append(stack, curr)
        curr = curr.Next
    }

    // Step 2: Pop from end (original tail first) to build reversed list
    if len(stack) == 0 {
        return nil
    }
    
    // Last element in stack (original head) becomes new tail
    new_head := stack[0]  // First pushed = original tail = new head
    prev := new_head
    prev.Next = nil
    
    // Connect remaining nodes in reverse order
    for i := 1; i < len(stack); i++ {
        curr := stack[i]
        curr.Next = prev
        prev = curr
    }
    
    return new_head
}

func reverse_linked_list_iterative_approach(head *utils.ListNode) *utils.ListNode {
    prev_node := (*utils.ListNode)(nil)
    curr_node := head
    
    for curr_node != nil {
        next_node := curr_node.Next      // Save next
        curr_node.Next = prev_node       // Reverse link
        prev_node = curr_node            // Move prev forward
        curr_node = next_node            // Move curr forward
    }
    return prev_node
}

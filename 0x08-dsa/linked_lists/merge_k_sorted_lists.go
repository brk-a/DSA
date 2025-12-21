package linkedlists

import (
	"container/heap"
	"fmt"
	"sort"

	"github.com/brk-a/go-dsa/utils"
)


func MergeKSortedLists() {
    fmt.Println("Merge k sorted lists ...")
    fmt.Print("==================== ==================== ===================\n")

    // Test 1: Example 1
    fmt.Println("Running test 1: lists=[[1,4,5],[1,3,4],[2,6]] ...")
    lists1 := []*utils.ListNode{
        utils.BuildList([]int{1, 4, 5}),
        utils.BuildList([]int{1, 3, 4}),
        utils.BuildList([]int{2, 6}),
    }
    expected1 := utils.BuildList([]int{1, 1, 2, 3, 4, 4, 5, 6})
    
    resultBF1 := merge_k_sorted_lists_brute_force(lists1)
    resultPQ1 := merge_k_sorted_lists_priority_queue(lists1)
    fmt.Printf("Input: %v\n", lists1)
    fmt.Printf("Brute Force: %v (expected: %v)\n", utils.PrintList(resultBF1), utils.PrintList(expected1))
    fmt.Printf("Priority Queue: %v (expected: %v)\n", utils.PrintList(resultPQ1), utils.PrintList(expected1))
    fmt.Print("==================== ==================== ===================\n")

    // Test 2: Example 2 - empty lists
    fmt.Println("Running test 2: lists=[] ...")
    lists2 := []*utils.ListNode{}
    resultBF2 := merge_k_sorted_lists_brute_force(lists2)
    resultPQ2 := merge_k_sorted_lists_priority_queue(lists2)
    fmt.Printf("Input: %v\n", lists2)
    fmt.Printf("Brute Force: %v (expected: [])\n", utils.PrintList(resultBF2))
    fmt.Printf("Priority Queue: %v (expected: [])\n", utils.PrintList(resultPQ2))
    fmt.Print("==================== ==================== ===================\n")

    // Test 3: Example 3 - single empty list
    fmt.Println("Running test 3: lists=[[]] ...")
    lists3 := []*utils.ListNode{nil}
    resultBF3 := merge_k_sorted_lists_brute_force(lists3)
    resultPQ3 := merge_k_sorted_lists_priority_queue(lists3)
    fmt.Printf("Input: %v\n", lists3)
    fmt.Printf("Brute Force: %v (expected: [])\n", utils.PrintList(resultBF3))
    fmt.Printf("Priority Queue: %v (expected: [])\n", utils.PrintList(resultPQ3))
    fmt.Print("==================== ==================== ===================\n")

    // Test 4: Example 4
    fmt.Println("Running test 4: lists=[[1,4],[2,5],[3,6]] ...")
    lists4 := []*utils.ListNode{
        utils.BuildList([]int{1, 4}),
        utils.BuildList([]int{2, 5}),
        utils.BuildList([]int{3, 6}),
    }
    expected4 := utils.BuildList([]int{1, 2, 3, 4, 5, 6})
    
    resultBF4 := merge_k_sorted_lists_brute_force(lists4)
    resultPQ4 := merge_k_sorted_lists_priority_queue(lists4)
    fmt.Printf("Input: %v\n", lists4)
    fmt.Printf("Brute Force: %v (expected: %v)\n", utils.PrintList(resultBF4), utils.PrintList(expected4))
    fmt.Printf("Priority Queue: %v (expected: %v)\n", utils.PrintList(resultPQ4), utils.PrintList(expected4))
    fmt.Print("==================== ==================== ===================\n")
}


func merge_k_sorted_lists_priority_queue(lists []*utils.ListNode) *utils.ListNode {
	min_heap := &utils.NodeHeap{}
	heap.Init(min_heap)

	for _, list := range lists {
		if list != nil {
			heap.Push(min_heap, list)
		}
	}

	dummy := &utils.ListNode{}
	curr := dummy

	for min_heap.Len() > 0 {
		small := heap.Pop(min_heap).(*utils.ListNode)
		curr.Next = small
		curr = curr.Next

		if small.Next != nil{
			heap.Push(min_heap, small.Next)
		}
	}

	return dummy.Next
}

func merge_k_sorted_lists_brute_force(lists []*utils.ListNode) *utils.ListNode {
    // Extract all values into a slice
    values := []int{}
    for _, list := range lists {
        for current := list; current != nil; current = current.Next {
            values = append(values, current.Val)
        }
    }
    
    // Sort the values
    sort.Ints(values)
    
    // Build new sorted linked list
    if len(values) == 0 {
        return nil
    }
    
    dummy := &utils.ListNode{}
    current := dummy
    for _, val := range values {
        current.Next = &utils.ListNode{Val: val}
        current = current.Next
    }
    
    return dummy.Next
}
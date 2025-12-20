package linkedlists

import (
	"fmt"

	"github.com/brk-a/go-dsa/utils"
)

func MergeTwoSortedLists() {
    fmt.Println("Merge two sorted lists by iteration and sorting in place ...")
    fmt.Print("==================== ==================== ===================\n")

    // Test 1: Example 1
    fmt.Println("Running test 1: h1=[1,2,4], h2=[1,3,4] ...")
    h1_1 := utils.BuildList([]int{1, 2, 4})
    h2_1 := utils.BuildList([]int{1, 3, 4})
    result1 := merge_two_sorted_lists(h1_1, h2_1)
    expected1 := utils.BuildList([]int{1, 1, 2, 3, 4, 4})
    fmt.Printf("Input: h1=%v, h2=%v\n", utils.PrintList(h1_1), utils.PrintList(h2_1))
    fmt.Printf("Output: %v (expected: %v)\n", utils.PrintList(result1), utils.PrintList(expected1))
    fmt.Print("==================== ==================== ===================\n")

    // Test 2: Example 2 - both empty
    fmt.Println("Running test 2: h1=[], h2=[] ...")
    result2 := merge_two_sorted_lists(nil, nil)
    fmt.Printf("Input: h1=%v, h2=%v\n", utils.PrintList(nil), utils.PrintList(nil))
    fmt.Printf("Output: %v (expected: [])\n", utils.PrintList(result2))
    fmt.Print("==================== ==================== ===================\n")

    // Test 3: Example 3 - one empty
    fmt.Println("Running test 3: h1=[], h2=[0] ...")
    h2_3 := utils.BuildList([]int{0})
    result3 := merge_two_sorted_lists(nil, h2_3)
    expected3 := utils.BuildList([]int{0})
    fmt.Printf("Input: h1=%v, h2=%v\n", utils.PrintList(nil), utils.PrintList(h2_3))
    fmt.Printf("Output: %v (expected: %v)\n", utils.PrintList(result3), utils.PrintList(expected3))
    fmt.Print("==================== ==================== ===================\n")
}


func merge_two_sorted_lists(h1 *utils.ListNode, h2 *utils.ListNode) *utils.ListNode {
    res := &utils.ListNode{}
    curr := res

    for h1 != nil && h2 != nil {
        if h1.Val <= h2.Val {
            curr.Next = h1
            h1 = h1.Next
        } else {
            curr.Next = h2
            h2 = h2.Next
        }
        curr = curr.Next
    }

    if h1 != nil {
        curr.Next = h1
    } else {
        curr.Next = h2
    }

    return res.Next
}

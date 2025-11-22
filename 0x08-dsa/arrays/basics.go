package arrays

import "fmt"

func ArrayBasics(){
	// create/declare/initialise an array/slice
	arr1 := []int{10, 20, 30, 40, 50, 60}

	fmt.Printf("Array elements: %v\n", arr1)

	// access element at posistion 2
	fmt.Printf("Element at position 2: %d\n", arr1[2])

	// modify element at index 3
	fmt.Printf("Array elements before modification of element at index 3: %v\n", arr1)
	arr1[3] = 100
	fmt.Printf("Array elements after modification of element at index 3: %v\n", arr1)

	//insert an element at position 1
	fmt.Printf("Array elements before inserting 250 at index 1: %v\n", arr1)
	idx := 1
	val := 250
	arr1 = append(arr1[:idx], append([]int{val}, arr1[idx:]...)...)
	fmt.Printf("Array elements after inserting 250 at index 1: %v\n", arr1)

	//delete an element at position 2
	fmt.Printf("Array elements before deleting element at index 2: %v\n", arr1)
	idx = 2
	arr1 = append(arr1[:idx], arr1[idx+1:]...)
	fmt.Printf("Array elements after deleting element at index 2: %v\n", arr1)
}
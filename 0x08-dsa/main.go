package main

import (
	"fmt"
	// "time"

	arrays "github.com/brk-a/go-dsa/arrays"
	linked_lists "github.com/brk-a/go-dsa/linked_lists"
	strings "github.com/brk-a/go-dsa/strings"
)

func main(){
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running all tasks/problems ...")
	fmt.Print("==================== ==================== ===================\n")
	// time.Sleep(5)
	fmt.Print("########## ########## ########## ########## ########## ######\n")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("Running array tasks/problems ...")
	fmt.Print("==================== ==================== ===================\n")
	arrays.Arrays()
	// time.Sleep(5)
	fmt.Print("########## ########## ########## ########## ########## ######\n")
	fmt.Println("Running string tasks/problems ...")
	fmt.Print("==================== ==================== ===================\n")
	strings.Strings()
	fmt.Print("==================== ==================== ===================\n")
	// time.Sleep(5)
	fmt.Print("########## ########## ########## ########## ########## ######\n")
	fmt.Println("Running linked list tasks/problems ...")
	fmt.Print("==================== ==================== ===================\n")
	linked_lists.LinkedLists()
	fmt.Print("==================== ==================== ===================\n")
	// time.Sleep(5)
	fmt.Print("########## ########## ########## ########## ########## ######\n")
	fmt.Print("==================== ==================== ===================\n")
	fmt.Println("All done!")
}
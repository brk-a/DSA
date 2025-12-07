package strings

import (
	"fmt"
	"strings"
)

func StringBasics() {
	fmt.Println("Strings basics")
	s1, s2 := "Hello", "world!"
	s := s1 + ", " + s2 // Hello, world!
	fmt.Printf("String concatenation: s1: %v, s2: %v and s1+, +s2: %v\n", s1, s2, s)

	n := len(s)
	fmt.Printf("Length of string: s: %v and len(s): %v\n", s, n)

	b := s[0]   // H
	c := s[1:4] // ell
	d := s[7:]  // world!
	e := s[:5]  // Hello
	fmt.Println("String indexing...")
	fmt.Printf("s: %v and s[0]: %#U\n", s, b)
	fmt.Printf("s: %v and s[1:4]: %v\n", s, c)
	fmt.Printf("s: %v and s[7:]: %v\n", s, d)
	fmt.Printf("s: %v and s[:5]: %v\n", s, e)

	fmt.Println("String iteration...")
	for i, rv := range s {
		fmt.Printf("%#U starts at byte position %d\n", rv, i)
	}

	by := []byte(s) // convert `string` to slice of `byte`
	s = string(by)  // convert slice of `byte` to `string`
	fmt.Println("String conversion...")
	fmt.Printf("string to []byte. string: %v, []byte: %v\n", s, by)
	fmt.Printf("[]byte to string. []byte: %v, string: %v\n", by, s)

	fmt.Println("build strings using Builder...")
	var builder strings.Builder
	builder.WriteString("Hello, ")
	builder.WriteString("world!")
	sb := builder.String()
	fmt.Printf("String nuild using Builder: %s\n", sb)
}

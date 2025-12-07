# strings

* a string in Go is a sequence of characters

### characteristics
* immutable
    * manipulating a string is to create a new one
* underlying data structure is a read-only slice of `byte`
    * the `string` type in Go is a two-word data structure: a pointer to the data and the length of said string
* UTF-8 encoded
    * can represent any unicode character; each character may be composed of one or more bytes

### common ops
* creation

    ```go
        s := "Hello, world!"
    ```

* concatenation

    ```go
        s1, s2 := "Hello", "world!"
        s := s1 + ", " + s2 // Hello, world!
        // notice the use of double quotes
    ```

* string length
    * returns the number of bytes, not the number of characters, in a string

    ```go
        s := "Hello, world!"
        n := len(s) // 13
    ```

* indexing and slicing
    * indexing returns a slice

    ```go
        s := "Hello, world!"
        b := s[0] // H
        c := s[1:4] // ell
        d := s[7:] // world!
        e := s[:5] // Hello
    ```

* iteration
    * `range` provides the index, i, and unicode code point, rune/rune value

    ```go
        s := "Hello, world!"
        for i, rv := range s {
            fmt.Printf("%#U starts at byte position %d\n", rv, i)
        }
    ```

* conversion
    * `string` can be converted into `byte` slices and *vice versa*

    ```go
        s := "Hello, world!"
        b := []byte(s) // convert `string` to slice of `byte`
        s = string(b) // convert slice of `byte` to `string`
    ```

### performance considerations
* memory allocation
    * strings are immutable; operations that modify a string, say, concatenation, create new strings, therefore, requiring more memory resources
* optimisation techniques
    * use `strings.Builder` to efficiently build strings; this minimises memory copying by maintaining an internal buffer

        ```go
            var builder strings.Builder
            builder.WriteString("Hello, ")
            builder.WriteString("world!")
            s := builder.String()
        ```

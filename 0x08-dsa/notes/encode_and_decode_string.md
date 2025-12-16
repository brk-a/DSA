# encode and decode string

### problem
* design an algorith to encode a list of strings into a string
* the encoded string will be sent over the network to be decoded into the original list of strings
* `machine 1`, the sender, has the function

    ```go
        func encode (vector []string) string {
            // your code here
        }
    ```

* `machine 2`, the receiver, has the function

    ```go
        func decode (s string) []string {
            // your code here
        }
    ```

* `machine 1` does the following: `encoded_strs := encode(vector)` and `machine 2` does the following: `strs := decode(encoded_strs)`
* `strs` in `machine 2` should be the same as `vector` in `machine 1`
* **required:** implement `encode` and `decode` functions

### assumptions

### constraints
* you are not allowed to solve the problem using any serialise methods such as `eval()`

### examples
#### example 1
```plaintext
    input: vector = ["Hello", "world!"]
    output:
    explanation:
```

#### example 2
```plaintext
    input:
    output:
    explanation:
```

#### example 3
```plaintext
    input:
    output:
    explanation:
```

### approach(es)
#### approach 1: brute force O(N<sup></sup>) time
* 
#### approach 2: O() time
* 
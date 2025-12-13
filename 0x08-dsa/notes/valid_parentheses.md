# valid parentheses

### problem
* given a string, `s`, containing the characters `(`,`)`,`[`,`]`,`{` and `}`, determine whether said string is valid
    * return `true` if valid, else, `false`
* `s` is valid IFF
    * all open brackets are closed by the same type of brackets
    * open brackets are closed in the correct order
    * every close bracket has a corresponding open bracket of the same type
### assumptions
* `s` consists of the characters `(`,`)`,`[`,`]`,`{` and `}` only

### constraints
* 1 &le; s.length &lt;10<sup>4</sup>

### examples
#### example 1
```plaintext
    input: s = "()"
    output: true
    explanation: none needed
```
#### example 2
```plaintext
    input: s = "{}()[]"
    output: true
    explanation: none needed
```

#### example 3
```plaintext
    input: s = "(]"
    output: false
    explanation: none needed
```

#### example 4
```plaintext
    input: s = "([])"
    output: true
    explanation: none needed
```
### approach(es)
#### approach 1: brute force O(N<sup>2<sup>) time
> **idea**: repeatedly remove any occurrence of "()", "[]", or "{}" from the string. if the string becomes empty, it is valid; if no more removals are possible but the string is non‑empty, it is invalid.
* implement recursion

#### approach 2: maps O(N) time and space
* have a map, `mp` whose keys, `k`, are the closing brackets and values,`v`, are the respective opening brackets
* implement a stack check of `s`
    * for each character of `s` starting at `s[0]`
        * check if `s[i]` exists as a key in `mp`
            * return `false` if not, else, continue
        * push `mp[s[i]]` onto the stack
        * check if `s[i+1]` is equal to any `v` of `mp`
            * pop `s[i]` from stack if yes, else, continue
    * check if stack is empty
        * return `true` if yes, else, `false`
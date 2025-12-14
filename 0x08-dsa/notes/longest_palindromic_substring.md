# longest palindromic substring

### problem
* given a string, `s`, return the longest palindromic substring of `s`

### assumptions
* `s` contains latin characters (excluding those with diacritics) and "arabic" numerals
    * /a-zA-Z0-9/g

### constraints
* 1 &le; s.length &lt; 1000

### exammples
#### example 1
```plaintext
    input: s = "babad"
    output: "bab" or "aba" (whichever the algo finds first)
    explanation: none needed
```

#### example 2
```plaintext
    input: s = "cbbd"
    output: "bb"
    explanation: none needed 
```

### approach(es)
#### approach 1: brute force O(N<sup>3</sup>)
* list all possible combinations of `s` and check whether they are a palindrome
* have two pointers, `i` and `j`
    * 0 &le; i &lt; s.length
    * i+1 &le; j &lt; s.length
* have a variable, `palindrome`, that stores said palindrome
* have a flag, `max_pal_len`, initialised to zero
* for each element of `s` starting at `s[0]`
    * check whether the combination `s[i : j]` is a palindrome
        * set `palindrome` to `s[i : j]` if yes, else, continue
        * check whether `palindrome`.length is greater than `max_pal_len`
            * set `max_pal_len` to ``palindrome`.length if yes, else, continue
return `palindrome`
#### approach 2:  O(N<sup>2</sup>)
* treat each index as a potential centre for odd/even-length palindromes, expanding outward
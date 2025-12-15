# palindromic substrings

### problem
* given a string, `s`, return the nunmber of palindromic substrings in it
* a string is a palindrome when it reads the same backwards as forwards
* a sub-string is a contiguous sequence of characters  within the string
### assumptions
* `s` consists of lowercase latin characters (w/o diacritics) used in English
    * /a-z/g

### constraints
* 1 &le; s.length &le; 1000

### examples
#### example 1
```plaintext
    input: s = "abc"
    output: 3
    explanation: three palindromic strings: "a", "b" and "c"
```

#### example 2
```plaintext
    input: s = "aaa"
    output: 6
    explanation: six palindromic strings: "a", "a", "a", "aa", "aa"  and "aaa"
```

#### example 3
```plaintext
    input: s = "abcabc"
    output: 6
    explanation: six palindromic strings: "a", "b", "c", "a", "b" and "c"
```

### approach(es)
#### approach 1: brute force O(N<sup>3</sup>) time
> **idea:** create all possible sub-string combinations. see whether said combinations are palindromes. count those that are and return said count
* 
#### approach 2: expand-the-centre O(N<sup>2</sup>) time
> **idea:** traverse the input string once. check whether combination at each iteration is a palindrome. count it if it is, else, continue. return said count
* 
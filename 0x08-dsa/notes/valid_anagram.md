# valid anagram

### problem
* given two strings, `s` and `t`, return `true` if `t` is an anagram of `s`, else, `false`
> **anagram** &rarr; a word or phrase formed by re-arranging the letters of a different word or phrase, typically using all original letters exactly once

### assumptions
`m`, `n` == `s`.length, `t`.length
1 &le; m, n &lt; 5 * 10<sup>4</sup>

### constraints
* `s` and `t` consist of lowercase latin characters (w/o diacritics) used in English
    * /a-z/g

### examples
#### example 1
```plaintext
    input: s = "anagram", t = "nagaram"
    output: true
```

#### example 2
```plaintext
    input: s = "tit", t = "tat"
    output: false
```

### approach(es)
#### approach 1: brute force O( M log N) time
#### approach 2: map O(N) time and space
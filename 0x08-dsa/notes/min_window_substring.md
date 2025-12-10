# minimum window substring

### problem
* given two strings, `s` and `t`, of lengths `m` and `n` respectively, return the
minimum window substring of `s` such that every character in `t`, including duplicates,
is included in the window, else, return an empty string, `""`

### assumptions
* `s` and `t` consist of latin characters (w/o diacritics) used in English
    * /a-zA-Z/g

### constraints
* `m` == `s.length`
* `n` == `t.length`
* 1 &le; `m`, `n` &le; 10<sup>5</sup>

### examples
#### example 1
```plaintext
input: s = "ADOBECODEBANC" t = "ABC"
output: "BANC"
explanation: the minimum window substring, "BANC", includes "A", "B" and "C" from string `t`
```

#### example 2
```plaintext
input: s = "a" t = "aa"
output: ""
explanation: the entire string `s` is in the minimum window 
```

#### example 3
```plaintext
input: s = "a" t = "a"
output: "a"
explanation: the entire string `t` must be included in the window. since that is not possible, return `""` 
```

### approach(es)
#### approach 1: brute force O(M<sup>2</sup>N) time
* have two pointers, `i` and `j`
    * 0 &le; i &lt; s.length
    * i &le; j &lt; s.length
* have variables `min_win_sub` and `min_len` initialised to an emoty string and zero respectively
* for each element of `s` starting at `s[0]`
    * create a map, `mp` whose keys, `k`, are the various combinations of `s` and
    values, `v`, are the lengths of `k`
    * for each `k` in `mp`
        * check whether `k` is a substring of `t`
            * set `min_len` to `v` if yes, else, continue
            * check whether `min_win_sub`.length is &lt; `min_len`
                * set `min_win_sub` to `k` if yes, else, continue
* return `min_win_sub`
#### approach 2: maps O(N) time and space
* have variables `min_win_sub` and `min_len` initialised to an emoty string and zero respectively
* a map, `mp` whose keys, `k`, are the characters of `k` and values, `v`, are the frequencies of said chars
* have two pointers, `i` and `j`
    * 0 &le; i &lt; s.length
    * i &le; j &lt; s.length
* for each element in `s` strting at `s[0]`
    * create a map, `s_mp`, whose keys, `k`, are the characters of `k` and values, `v`, are the frequencies of said chars
    * check whether the keys of `s_mp` are in those of `mp`
        * set `min_len` to the length of `s[i]`, else, continue
    * set `min_win_sub` to `s[i]` when `min_len` = `t`.length
* return `min_win_sub`
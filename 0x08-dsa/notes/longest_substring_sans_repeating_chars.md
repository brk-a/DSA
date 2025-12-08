# longest substring *sans* repeating characters

### problem
* given a string, `s`, return the length of the longest substring that does not have repeating characters

### assumptions
* `s` consists of latin characters (w/o diacritics), "arabic" numerals and other symbols and spaces used in English
    * r'\w+'

### constraints
* 0 &le; s.length &le; 5 * 10<sup>4</sup>

### examples
#### example 1

```plaintext
    input: s = "abcabcbb"
    output: 3
    explanation: the longest non-repeating substring is `abc` whose length is 3
```

#### example 2

```plaintext
    input: s = "bbbbb"
    output: 1
    exlanation: the longest non-repeating substring is `b` whose length is 1
```

#### example 3

```plaintext
    input: s = "pwwkew"
    output: 3
    explanation: explanation: the longest non-repeating substring is `wke` whose length is 3
        Note: `pwke` is not correct because it is a sub-sequence, not a substring
```

### approach(es)
#### approach 1: brute force O(N<sup>3</sup>) time and O(N) space
* have a variable, `max_length`, initalised to zero
* use two pointers, `i` and `j` to parse various combinations of `s`
    * 0 &le; `i` &lt; `s.length`
    * `i` &le; `j` &lt; `s.length`
* for each element of `s` starting at `s[0]`
    * check whether the substring made by `s[i]`:`s[j]` is unique and that its length is more than `max_length`
        * set max_length` to the length of said substring, else, carry on
* return `max_length`
#### approach 2: map O(N) time and space
* have a map, `mp` whose keys, `k`, are the characters in `s` and values, `v`, are the indices of said keys in `s`
* have a variable, `max_length`, initalised to zero
* have two variables, `left` and `right`, initalised to zero
* for each element of `s` starting at `s[right]`
    * check whether that element exists on `mp` and whether its index, `i`,  is &ge; `left`
        * set `left` to `i`+ 1 if yes, else, continue
    * set `mp[s[right]]` to `right`
    * check whether `max_length` is less than `right` - `left` + 1
        * set `max_length` to `right` - `left` + 1 if yes, else, continue
* return `max_length`

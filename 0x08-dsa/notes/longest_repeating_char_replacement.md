# longest repeating with character replacement

### problem
* you are given a string, `s`, and an integer, `k`.
* you can choose any character of said string and change it to any other uppercase English character
* you can perform this operation k times
* return the length of the longest substring containing the same letter you can get after performing the operations above

### assumptions
* `s` consists of latin characters (w/o diacritics) used in English
    * /a-zA-Z/g

### constraints
* 1 &le; s.length &le; 10<sup>5</sup>
* 1 &le; k &le; s.length

### examples
#### example 1

```plaintext
    input: s = "ABAB", k = 2
    output: 4
    explanation: replace both As with Bs or vice versa
```

#### example 2

```plaintext
    input: s = "AABABBA", k = 1
    output: 4
    explanation: replace the A at index 3 with B to form "AABBBA"

    The substring "BBBB" has the longest repeating characters, 4.
    Alternative: replace the B at index 2 with A: output = 4 and longest
    substring is "AAAA"
```

### approach(es)
#### approach 1: brute force O(N<sup>3</sup>)
* idea is to use nested loops to come up with all possible combinations based on `s` and `k`
then return the length of the one that has the longest repeating character
* initialise `max_len` to zero
* outer loop
    *  have a map whose keys, `i`, are the various combinations of `s` and values, `v`, are
    the frequencies of the letters of said keys
* inner loop
    * compare each frequency, `f`, with `k`
        * if `f` = `k`, replace `k` adjacent characters, else, continue
        * set `max_len` to `f` + `k` if `f` + `k` > `max_len`, else, continue
* return `max_len`
#### approach 2: map with sliding window O(N) time and space
* have a map, `mp`, whose keys, `i` are the various combinations of `s` and values, `v`, are
the frequencies of the letters of said keys
* initialise `max_len` to zero
* for each combination of elements in `s`
    * set said combinations as `i` and the frequency of characters in said combinations to `v`
    * check whether any frequency in `mp` &ge; `k`
        * check whether said combination can be edited if yes, else, continue
            * replace the odd character if yes, else, continue
            * set `max_length` to the length of the new combination
* return `max_len`
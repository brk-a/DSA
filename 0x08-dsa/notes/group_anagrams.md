# group anagrams

### problem
* given an array of strings, `strs`, group the anagrams together
* feel free to return the answer in any order

### assumptions
* `s` consists of lowercase latin characters (w/o diacritics) used in English
    * /a-z/g

### constraints
* 1 &le; strs.length &le; 10<sup>5</sup>

### examples
#### example 1
```plaintext
    input: strs = ["eat", "tea", "tan", "ant", "ate", "bat"]
    output: [["bat"], ["ant", "tan"], ["eat", "tea", "ate"]]
    explanation: 
        - there is no string in `strs` that is an anagram of "bat"
        - "ant" and "tan" are anagrams; group them
        - "eat", "ate" and "tea" are anagrams; group them
```

#### example 2
```plaintext
    input: strs = [""]
    output: [[""]]
    explanation:  none needed
```

#### example 3
```plaintext
    input: strs = ["a"]
    output: [["a"]]
    explanation: none needed
```

### approach(es)
#### approach 1: brute force O(N<sup>2</sup> * K log K) time
* let `k` be the length of a string in `strs` and `n` be `strs`.length
* sort said string
* hash the sorted string and store said hash in `og_hash`
* for each of the other strings in `strs`
    * sort each
    * hash each string
    * check whether hashes match with `og_hash`
        * group those hashes that match, else, continue
* return array of grouped strings
#### approach 2: hash map O(N * K log K) time
* let `k` be the length of a string in `strs` and `n` be `strs`.length
* have a map, `mp`, whose keys, `k`, are sorted strings in `strs` and values, `v`, are the hashes of said keys
* 
#!/usr/bin/env python3

MAX_CHAR = 26
ORD_A = ord('a')


def group_anagrams_together_freq_as_keys(arr: list[str]) -> list[list[str]]:
    if not isinstance(arr, list) or len(arr) == 0:
        return []
    
    result = []
    mp = {}
    n = len(arr)

    for i in range(n):
        key = get_hash(arr[i])
        if key not in mp:
            mp[key] = len(result)
            result.append([])

        result[mp[key]].append(arr[i])

    return result


def group_anagrams_together_words_as_keys(arr: list[str]) -> list[list[str]]:
    if not isinstance(arr, list) or len(arr) == 0:
        return []
    
    result = []
    mp = {}
    n = len(arr)

    for i in range(n):
        s = arr[i]
        s = ''.join(sorted(s))

        if s not in mp:
            mp[s] = len(result)
            result.append([])

        result[mp[s]].append(arr[i])

    return result


def get_hash(s: str) -> str:
    hash_list = []
    freq = [0] * MAX_CHAR

    for char in s:
        freq[ord(char) - ORD_A] += 1

    for i in range(MAX_CHAR):
        hash_list.append(str(freq[i]))
        hash_list.append("$")

    return "".join(hash_list)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "arr": ["act", "god", "cat", "dog", "tac"],
            "expected": [
                ["act", "cat", "tac"],
                ["god", "dog"],
            ],
        },
                {
            "id": 2,
            "arr":  ["listen", "silent", "enlist", "abc", "cab", "bac", "rat", "tar", "art"],
            "expected": [
                ['listen', 'silent', 'enlist'],
                ['abc', 'cab', 'bac'],
                ['rat', 'tar', 'art']
            ],
        },
        {
            "id": 3,
            "arr": [],
            "expected": [],
        },
        {
            "id": 4,
            "arr": ["cat", "dog", "listen"],
            "expected": [
                ["cat"],
                ["dog"],
                ["listen"],
            ],
        },
    ]

    print("=================== 25. group anagrams together problem ===================")
    for name, func in [("sorted words as keys", group_anagrams_together_words_as_keys), ("frequencies as keys", group_anagrams_together_freq_as_keys)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["arr"])
            print(f"Test {test['id']}: s={test['arr']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

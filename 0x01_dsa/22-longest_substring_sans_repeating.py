#!/usr/bin/env python3

MAX_CHAR = 26
ORD_A = ord('a')

def longest_substring_sans_repeat_brute_force(s :str) -> int:
    if not isinstance(s, str):
        return -1

    n = len(s)
    result = 0

    for i in range(n):
        visited = [False] * MAX_CHAR
        for j in range(i, n):
            if visited[ord(s[j]) - ORD_A]:
                break # break when current char is visited
            else:
                result = max(result, j-i+1)
                visited[ord(s[j]) - ORD_A] = True

    return result 


def longest_substring_sans_repeat_sliding_window(s :str) -> int:
    if not isinstance(s, str) or len(s) == 0:
        return -1

    n = len(s)

    if n == 0 or n == 1:
        return n

    left, right = 0, 0
    visited = [False] * MAX_CHAR
    result = 0
    while right < n:
        while visited[ord(s[right]) - ORD_A]: # move left pointer to next index because repeating char found
            visited[ord(s[left]) - ORD_A] = False
            left += 1
        
        visited[ord(s[right]) - ORD_A] = True

        # len of curr win = r - l + 1; calc and update accordingly
        result = max(result, (right - left + 1))
        right += 1

    return result

    


def longest_substring_sans_repeat_last_index(s :str) -> int:
    if not isinstance(s, str) or len(s) == 0:
        return -1

    n = len(s)
    result = 0
    last_index = [-1] * MAX_CHAR
    start = 0

    for end in range(n):
        start = max(start, last_index[ord(s[end]) - ORD_A] + 1)
        result = max(result, end - start + 1)
        last_index[ord(s[end]) - ORD_A] = end

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "string": "aaa",
            "expected": 1
        },
        {
            "id": 2,
            "string": "abcdefabcbb",
            "expected": 6
        },
        {
            "id": 3,
            "string": "",
            "expected": -1
        },
        {
            "id": 14,
            "string": "abc",
            "expected": 3
        },
    ]

    print("=================== 22. Longest sub-string sans repeating problem ===================")
    for name, func in [("brute force",longest_substring_sans_repeat_brute_force), ("sliding window", longest_substring_sans_repeat_sliding_window), ("last index", longest_substring_sans_repeat_last_index)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["string"])
            print(f"Test {test['id']}: matrix={test['string']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

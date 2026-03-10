#!/usr/bin/env python3

def smallest_window_containing_characters_brute_force(s: str, p: str) -> str:
    if not isinstance(s, str) or not isinstance(p, str) or len(s) == 0 or len(p) == 0:
        return ""
    
    n = len(s)
    min_len = float('inf')
    result = ""

    for i in range(n):
        for j in range(i, n):
            sub = s[i:j+1]
            if has_all_chars(sub, p):
                curr_len = len(sub)
                if curr_len < min_len:
                    min_len = curr_len
                    result = sub

    return result


def smallest_window_containing_characters_binary_search(s: str, p: str) -> str:
    if not isinstance(s, str) or not isinstance(p, str) or len(s) == 0 or len(p) == 0:
        return ""
    
    m, n = len(s), len(p)
    if m < n:
        return ""

    min_len = float('inf')
    low, high = n, m
    idx = -1
    while low <= high:
        mid = (low + high) // 2
        valid, start = is_valid(s, p, mid)

        if valid:
            if mid < min_len:
                min_len = mid
                idx = start
            high = mid - 1
        else:
            low = mid + 1

    if idx == -1:
        return ""

    return s[idx: idx + min_len]



def smallest_window_containing_characters_sliding_window(s: str, p: str) -> str:
    if not isinstance(s, str) or not isinstance(p, str) or len(s) == 0 or len(p) == 0:
        return ""
    
    n, m = len(s), len(p)
    if n < m:
        return ""

    count_p = [0] * 256
    count_s = [0] * 256

    for char in p:
        count_p[ord(char)] += 1

    start = 0
    start_idx = -1
    min_len = float('inf')
    counter = 0

    for j in range(n):
        count_s[ord(s[j])] += 1

        if count_p[ord(s[j])] != 0 and count_s[ord(s[j])] <= count_p[ord(s[j])]:
            counter += 1

        if counter == m:
            while count_s[ord(s[start])] > count_p[ord(s[start])] or count_p[ord(s[start])] == 0:
                if count_s[ord(s[start])] > count_p[ord(s[start])]:
                    count_s[ord(s[start])] -= 1
                start += 1

            length = j - start + 1
            if min_len > length:
                min_len = length
                start_idx = start

    if start_idx == -1:
        return "-1"

    return s[start_idx: start_idx + min_len]

def has_all_chars(sub: str, p: str) -> bool:
    counter = [0] * 256

    for char in p:
        counter[ord(char)] += 1
    
    for char in sub:
        if counter[ord(char)] > 0:
            counter[ord(char)] -= 1

    for val in counter:
        if val > 0:
            return False

    return True


def is_valid(s: str, p: str, mid: int) -> bool | int:
    counter = [0] * 256
    distinct = 0
    n = len(s)

    for i in p:
        if counter[ord(i)] == 0:
            distinct += 1
        counter[ord(i)] += 1

    curr_count = 0
    for i in range(n):
        counter[ord(s[i])] -= 1
        if counter[ord(s[i])] == 0:
            curr_count += 1

        if i > mid:
            counter[ord(s[i - mid])] += 1
            if counter[ord(s[i - mid])] == 1:
                curr_count -= 1

        if i >= mid - 1:
            if curr_count == distinct:
                return True, i - mid + 1

    return False, -1


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "s": "timetopractice",
            "p": "toc",
            "expected": "toprac",
        },
          {
            "id": 2,
            "s": "zoomlazapzo",
            "p": "oza",
            "expected": "apzo",
        },
          {
            "id": 3,
            "s": "",
            "p": "aaa",
            "expected": "",
        },
          {
            "id": 4,
            "s": "goatmatata",
            "p": "",
            "expected": "",
        },
          {
            "id": 5,
            "s": "kakambweha",
            "p": "awe",
            "expected": "weha",
        },
    ]

    print("=================== 24. Smallest window containing substring problem ===================")
    for name, func in [("brute force", smallest_window_containing_characters_brute_force), ("binary search", smallest_window_containing_characters_binary_search), ("sliding window", smallest_window_containing_characters_sliding_window)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["s"], test["p"])
            print(f"Test {test['id']}: s={test['s']}, p={test['p']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

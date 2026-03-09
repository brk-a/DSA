#!/usr/bin/env python3


def max_char_repeating_substring_brute_force(s: str) -> str:
    if not isinstance(s, str) or len(s) == 0:
        return ""

    n = len(s)
    max_count = 0
    result = s[0]

    for i in range(n):
        counter = 0
        for j in range(i, n):
            if s[i] != s[j]:
                break
            counter += 1

        if counter > max_count:
            max_count = counter
            result = s[i]

    return result


def max_char_repeating_substring_optimised_nested_loops(s: str) -> str:
    if not isinstance(s, str) or len(s) == 0:
        return ""

    n = len(s)
    max_count = 0
    result = s[0]

    i = 0
    while i < n:
        counter = 1
        while  i+1 < n and s[i] == s[i+1]:
            i += 1
            counter += 1
        
        if counter > max_count:
            max_count = counter
            result = s[i]

        i += 1

    return result


def max_char_repeating_substring_counter_variable(s: str) -> str:
    if not isinstance(s, str) or len(s) == 0:
        return ""

    n = len(s)
    max_count = 0
    result = s[0]
    counter = 1

    for i in range(1, n):
        if s[i] == s[i - 1]:
            counter += 1
        else:
            counter = 1

    if counter > max_count:
        max_count = counter
        result = s[i - 1]

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "string": "aaa",
            "expected": "a",
        },
        {
            "id": 2,
            "string": "abcdefabcbb",
            "expected": "b",
        },
        {
            "id": 3,
            "string": "",
            "expected": "",
        },
        {
            "id": 4,
            "string": "abc",
            "expected": "",
        },
        {
            "id": 5,
            "string":  "aaaabbcbbb",
            "expected": "a",
        },
    ]

    print("=================== 23. Maximum character repeating sub-string problem ===================")
    for name, func in [("brute force",max_char_repeating_substring_brute_force), ("optimised nested loops", max_char_repeating_substring_optimised_nested_loops), ("counter variable", max_char_repeating_substring_counter_variable)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["string"])
            print(f"Test {test['id']}: matrix={test['string']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3


def find_zero_with_fartjest_ones_brute_force(s: str) -> int:
    if not isinstance(s, str) or len(s) == 0:
        return -1

    ones = [i for i, ch in enumerate(s) if ch == '1']
    if not ones:
        return -1

    result = -1
    for i, ch in enumerate(s):
        if ch == '0':
            nearest = min(abs(i - j) for j in ones)
            result = max(result, nearest)

    return result


def find_zero_with_fartjest_ones_one_var(s: str) -> int:
    if not isinstance(s, str) or len(s) == 0:
        return -1

    n = len(s)
    INF = n + 1

    # distance to nearest 1 on the left
    left = [INF] * n
    last_one = -INF
    for i in range(n):
        if s[i] == '1':
            last_one = i
        if last_one != -INF:
            left[i] = i - last_one

    # distance to nearest 1 on the right
    right = [INF] * n
    last_one = INF * 2
    for i in range(n - 1, -1, -1):
        if s[i] == '1':
            last_one = i
        if last_one < INF * 2:
            right[i] = last_one - i

    # if there are no ones at all
    if all(d == INF for d in left) and all(d == INF for d in right):
        return -1

    result = -1
    for i, ch in enumerate(s):
        if ch == '0':
            nearest = min(left[i], right[i])
            result = max(result, nearest)

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "s": "1000101",
            "expected": 2,
        },
        {
            "id": 2,
            "s": "1000",
            "expected": 3,
        },
        {
            "id": 3,
            "s": "",
            "expected": -1,
        },
        {
            "id": 4,
            "s": 1000,
            "expected": -1,
        },
        {
            "id": 5,
            "s": "1001010000001010101000001010",
            "expected": 3,
        },
    ]

    print("=================== 39. Find zero with fartest ones in binary array problem ===================")
    for name, func in [("brute force", find_zero_with_fartjest_ones_brute_force), ("one variable", find_zero_with_fartjest_ones_one_var)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["s"])
            print(f"Test {test['id']}: s={test['s']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3

import math


def longest_mountain_subarray_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    result = 0

    for i in range(n):
        j = i + 1
        increase, decrease = 0, 0

        while j < n and nums[j] > nums[j - 1]:
            increase = 1
            j += 1

        while j < n and nums[j] < nums[j - 1]:
            decrease = 1
            j += 1

        if increase and decrease:
            result = max(result, j - 1)

    return result


def longest_mountain_subarray_peak_expansion(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    if n < 3:
        return 0

    result = 0
    i = 1
    while i <= n - 2:
        if nums[i] > nums[i - 1] and nums[i] > nums[i + 1]:
            counter = 0
            j = 1
            while j > 0 and nums[j] > nums[j] > nums[j - 1]:
                counter += 1
                j -= 1
            while i <= n - 2 and nums[i] > nums[i + 1]:
                counter += 1
                i += 1
            result = max(result, counter)
        else:
            i += 1
    if result > 0:
        return result + 1

    return result



def longest_mountain_subarray_two_pointer(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    i, j, k = 0, -1, -1
    p, d, n = 0, 0, 0
    l = len(nums)

    if l < 3:
        return 0

    for i in range(l - 1):
        if nums[i + 1] > nums[i]:
            if k != -1:
                k, j = -1, -1
            if j == -1:
                j = i
        else:
            if nums[i + 1] < nums[i]:
                if j != -1:
                    k = i + 1
                if k != -1 and j != -1:
                    if d < k - j + 1:
                        d = k - j + 1
            else:
                k, j = -1, -1
    if k != -1 and j != -1:
        if d < k - j + 1:
            d = k - j + 1

    return d


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, 2, 2],
            "expected": 0,
        },
        {
            "id": 2,
            "nums": [1, 3, 1, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5] ,
            "expected": 11,
        },
        {
            "id": 3,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 4,
            "nums": "",
            "expected": -1,
        },
        {
            "id": 5,
            "nums": [3, 1],
            "expected": 0,
        },
    ]

    print("=================== 42. Longest mountain sub-array problem ===================")
    for name, func in [("brute force", longest_mountain_subarray_brute_force), ("peak expansion", longest_mountain_subarray_peak_expansion), ("two pointers", longest_mountain_subarray_two_pointer)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

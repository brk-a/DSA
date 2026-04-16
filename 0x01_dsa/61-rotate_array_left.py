#!/usr/bin/env python3

import copy
import math


def rotate_array_left_brute_force(nums: list[int], k: int) -> list[int]:
    if not isinstance(nums, list) or not isinstance(k, int) or k < 0 or len(nums) == 0:
        return []

    if k == 0:
        return nums

    n = len(nums)
    nums = copy.deepcopy(nums)

    for i in range(k):
        first = nums[0]
        for j in range(n - 1):
            nums[j] = nums[j + 1]
        nums[n - 1] = first

    return nums


def rotate_array_left_tmp_array(nums: list[int], k: int) -> list[int]:
    if not isinstance(nums, list) or not isinstance(k, int) or k < 0 or len(nums) == 0:
        return []

    if k == 0:
        return nums

    n = len(nums)
    nums = copy.deepcopy(nums)
    k %= n
    tmp = [0] * n

    for i in range(n - k):
        tmp[i] = nums[k + i]

    for i in range(k):
        tmp[n - k + i] = nums[i]

    for i in range(n):
        nums[i] = tmp[i]

    return nums


def rotate_array_left_reversal_algo(nums: list[int], k: int) -> list[int]:
    if not isinstance(nums, list) or not isinstance(k, int) or k < 0 or len(nums) == 0:
        return []

    if k == 0:
        return nums

    n = len(nums)
    nums = copy.deepcopy(nums)
    k %= n

    reverse(nums, 0, k - 1)
    reverse(nums, k, n - 1)
    reverse(nums, 0, n - 1)

    return nums


def rotate_array_left_juggling_algo(nums: list[int], k: int) -> list[int]:
    if not isinstance(nums, list) or not isinstance(k, int) or k < 0 or len(nums) == 0:
        return []

    if k == 0:
        return nums

    n = len(nums)
    nums = copy.deepcopy(nums)
    k %= n
    cycles = math.gcd(n, k)

    for i in range(cycles):
        start_elem = nums[i]
        curr_idx = i

        while True:
            next_idx = (curr_idx + k) % n
            if next_idx == i:
                break
            nums[curr_idx] = nums[next_idx]
            curr_idx = next_idx

        nums[curr_idx] = start_elem

    return nums


def reverse(nums: list[int], start: int, end: int) -> None:
    while start < end:
        nums[start], nums[end] = nums[end], nums[start]
        start += 1
        end -= 1


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 3, 4, 5, 6],
            "k": 2,
            "expected": [3, 4, 5, 6, 1, 2], 
        },
        {
            "id": 2,
            "nums": [1, 2, 3],
            "k": 4,
            "expected": [2, 3, 1], 
        },
         {
            "id": 3,
            "nums": [],
            "k": 2,
            "expected": [], 
        },
         {
            "id": 4,
            "nums": [1, 2, 3, 4, 5, 6],
            "k": -1,
            "expected": [], 
        },
         {
            "id": 5,
            "nums": [1, 2, 3, 4, 5, 6],
            "k": 0,
            "expected": [1, 2, 3, 4, 5, 6], 
        },
         {
            "id": 6,
            "nums": "[1, 2, 3, 4, 5, 6]",
            "k": 2,
            "expected": [], 
        },
        {
            "id": 7,
            "nums": [1, 2, 3, 4, 5, 6],
            "k": "2",
            "expected": [], 
        },
    ]

    print("=================== 61. Reverse array left/anti-clockwise problem ===================")
    for name, func in [("brute force", rotate_array_left_brute_force), ("temporary array", rotate_array_left_tmp_array), ("juggling algo", rotate_array_left_juggling_algo), ("reversal algo", rotate_array_left_reversal_algo)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'], test['k'])
            print(f"Test {test['id']}: nums={test['nums']}, k={test['k']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

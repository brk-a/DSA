#!/usr/bin/env python3

from collections import defaultdict


def majority_element_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    for i in range(n):
        counter = 0
        for j in range(i, n):
            if nums[i] == nums[j]:
                counter += 1
        
        if counter > n // 2:
            return nums[i]

    return -1


def majority_element_sorting(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    nums.sort()
    candidate = nums[n // 2]
    counter = 0

    for num in nums:
        if num == candidate:
            counter += 1
    
    if counter > n // 2:
        return candidate
    else:
        return -1


def majority_element_hashing(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    count_map = defaultdict(int)

    for num in nums:
        count_map[num] += 1
        if count_map[num] > n / 2:
            return num

    return -1


def majority_element_moore_algo(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    candidate = -1
    counter = 0

    for num in nums:
        if counter == 0:
            candidate = num
            counter = 1
        elif num == candidate:
            counter += 1
        else:
            counter -= 1

    counter = 0
    for num in nums:
        if nums == candidate:
            counter += 1

    if counter > n // 2:
        return candidate
    else:
        return -1


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 1, 2, 1, 3, 5, 1],
            "expected": 1,
        },
        {
            "id": 2,
            "nums": [7],
            "expected": 7,
        },
        {
            "id": 3,
            "nums": [2, 13],
            "expected": -1
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums": [0, 1, 0, 1, 2, 3, 4, 0, 1],
            "expected": -1,
        },
    ]

    print("=================== 31. Majority element problem ===================")
    for name, func in [("brute force", majority_element_brute_force), ("sorting", majority_element_sorting), ("hashing", majority_element_hashing), ("moore's algo", majority_element_moore_algo)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

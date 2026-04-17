#!/usr/bin/env python3

import copy


def three_great_candidates_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    max_prod = -float('inf')

    for i in range(n - 2):
        for j in range(i + 1, n - 1):
            for k in range(j + 1, n):
                max_prod = max(max_prod, nums[i] * nums[j] * nums[k])

    return max_prod


def three_great_candidates_sorting(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    nums = copy.deepcopy(nums) # deep copy to preserve original list
    nums.sort()

    return max(nums[0] * nums[1] * nums[n - 1], nums[n - 1] * nums[n - 2] * nums[n - 3])


def three_great_candidates_greedy_algo(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    max_a = -float('inf')
    max_b = -float('inf')
    max_c = -float('inf')
    min_a = float('inf')
    min_b = float('inf')

    for i in range(n):
        if nums[i] > max_a:
            max_c = max_b
            max_b = max_a
            max_a = nums[i]
        elif nums[i] > max_b:
            max_c = max_b
            max_b = nums[i]
        elif nums[i] > max_c:
            max_c = nums[i]

        if nums[i] < min_a:
            min_b = min_a
            min_a = nums[i]
        elif nums[i] < min_b:
            min_b = nums[i]

    return max(min_a * min_b * max_a, max_a * max_b * max_c)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [10, 3, 5, 6, 20],
            "expected": 1200,
        },
        {
            "id": 2,
            "nums": [-10, -3, -5, -6, -20],
            "expected": -90,
        },
        {
            "id": 3,
            "nums": [1, -4, 3, -6, 7, 0],
            "expected": 168,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums": "[10, 3, 5, 6, 20]",
            "expected": -1,
        },
    ]

    print("=================== 62. Three great candidates problem ===================")
    for name, func in [("three loops", three_great_candidates_brute_force), ("sorting", three_great_candidates_sorting), ("greedy algo", three_great_candidates_greedy_algo)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

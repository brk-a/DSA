#!/usr/bin/env python3

import copy


def reverse_array_in_groups_fixed_size(nums: list[int], k: int) -> list[int]:
    if not isinstance(nums, list) or not isinstance(k, int) or k < 1:
        return []

    i = 0
    n = len(nums)
    nums = copy.deepcopy(nums) # deep copy so we can use the same array for different approaches

    while i < n:
        left = i
        right = min(i + k - 1, n - 1)

        while left < right:
            nums[left], nums[right] = nums[right], nums[left]
            left += 1
            right -= 1

        i += k

    return nums


if __name__ =="__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 3, 4, 5, 6, 7, 8],
            "k": 3,
            "expected": [3, 2, 1, 6, 5, 4, 8, 7],
        },
        {
            "id": 2,
            "nums": [1, 2, 3, 4, 5],
            "k": 3,
            "expected": [3, 2, 1, 5, 4],
        },
        {
            "id": 3,
            "nums": [5, 6, 8, 9],
            "k": 5,
            "expected": [9, 8, 6, 5],
        },
        {
            "id": 4,
            "nums": [],
            "k": 5,
            "expected": [],
        },
        {
            "id": 5,
            "nums": [1, 2, 3, 4, 5, 6, 7, 8],
            "k": -1,
            "expected": [],
        },
        {
            "id": 6,
            "nums": [1, 2, 3, 4, 5, 6, 7, 8],
            "k": 0,
            "expected": [],
        },
        {
            "id": 7,
            "nums": [1, 2, 3, 4, 5, 6, 7, 8],
            "k": "10",
            "expected": [],
        },
    ]

    print("=================== 60. Reverse array in groups problem ===================")
    for name, func in [("fixed-size group reversal", reverse_array_in_groups_fixed_size)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'], test['k'])
            print(f"Test {test['id']}: nums={test['nums']}, k={test['k']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

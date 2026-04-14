#!/usr/bin/env python3

import copy


def reverse_array_temp_array(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    tmp = [0] * n
    nums = copy.deepcopy(nums) # deep copy because of the rest of the approaches

    for i in range(n):
        tmp[i] = nums[n - i - 1]

    for i in range(n):
        nums[i] = tmp[i]
    
    return nums


def reverse_array_two_pointers(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    left, right = 0, n - 1
    nums = copy.deepcopy(nums) # deep copy because of the rest of the approaches
    while left < right:
        nums[left], nums[right] = nums[right], nums[left]
        left += 1
        right -= 1

    return nums


def reverse_array_one_pointer(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    nums = copy.deepcopy(nums) # deep copy because of the rest of the approaches
    for i in range(n // 2):
        tmp = nums[i]
        nums[i] = nums[n - i - 1]
        nums[n - i - 1] = tmp

    return nums


def reverse_array_inbuilt_methods(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    nums = copy.deepcopy(nums) # deep copy because of the rest of the approaches

    nums.reverse()

    return nums


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 4, 3, 2, 6, 5] ,
            "expected": [5, 6, 2, 3, 4, 1],
        },
        {
            "id": 2,
            "nums": [4, 5, 1, 2],
            "expected": [2, 1, 5, 4],
        },
        {
            "id": 3,
            "nums": [],
            "expected": [],
        },
        {
            "id": 4,
            "nums": "[1, 2, 3]",
            "expected": [],
        },
        {
            "id": 5,
            "nums": [9, 9, 9, 9],
            "expected": [9, 9, 9, 9],
        },
        {
            "id": 6,
            "nums": [-1, 1, -2, 2, -3, 3],
            "expected": [3, -3, 2, -2, 1, -1],
        },
    ]

    print("=================== 59. Reverse array problem ===================")
    for name, func in [("temporary array", reverse_array_temp_array), ("two pointers", reverse_array_two_pointers), ("one pointer", reverse_array_one_pointer), ("in-built methods", reverse_array_inbuilt_methods)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

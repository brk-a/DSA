#!/usr/bin/env python3

import copy


def move_all_zeroes_to_end_tmp_array(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    tmp = [0] * n
    j = 0
    nums = copy.deepcopy(nums)

    for i in range(n):
        if nums[i] != 0:
            tmp[j] = nums[i]
            j += 1

    while j < n:
        tmp[j] = 0
        j += 1

    for i in range(n):
        nums[i] = tmp[i]

    return nums


def move_all_zeroes_to_end_two_traversals(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    nums = copy.deepcopy(nums)
    count = 0

    for i in range(n):
        if nums[i] != 0:
            nums[count] = nums[i]
            count += 1

    while count < n:
        nums[count] = 0
        count += 1

    return nums 


def move_all_zeroes_to_end_one_traversal(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    nums = copy.deepcopy(nums)
    count = 0

    for i in range(n):
        if nums[i] != 0:
            nums[i], nums[count] = nums[count], nums[i]
            count += 1

    return nums


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 0, 4, 3, 0, 5, 0],
            "expected": [1, 2, 4, 3, 5, 0, 0, 0],
        },
        {
            "id": 2,
            "nums": [10, 20, 30],
            "expected": [10, 20, 30],
        },
        {
            "id": 3,
            "nums": [0, 0],
            "expected": [0, 0],
        },
        {
            "id": 4,
            "nums": [],
            "expected": [],
        },
        {
            "id": 5,
            "nums": "[1, 0, 2, 0, 3]",
            "expected": [],
        },
        {
            "id": 6,
            "nums": [1, 0, 2, 0, 3],
            "expected": [1, 2, 3, 0, 0],
        },
    ]

    print("=================== 64. Move zeroes to end problem ===================")
    for name, func in [("temporary array", move_all_zeroes_to_end_tmp_array), ("two traversals", move_all_zeroes_to_end_two_traversals), ("one traversal", move_all_zeroes_to_end_one_traversal)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

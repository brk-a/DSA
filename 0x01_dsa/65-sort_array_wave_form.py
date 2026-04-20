#!/usr/bin/env python3

import copy


def sort_array_wave_form_adjacent_pair_swap(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    nums = copy.deepcopy(nums)

    for i in range(0, n - 1, 2):
        nums[i], nums[i + 1] = nums[i + 1], nums[i]

    return nums


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums":  [1, 2, 3, 4, 5],
            "expected":  [2, 1, 4, 3, 5],
        },
        {
            "id": 2,
            "nums":  [2, 4, 7, 8, 9, 10],
            "expected":  [4, 2, 8, 7, 10, 9],
        },
        {
            "id": 3,
            "nums":  [],
            "expected":  [],
        },
        {
            "id": 4,
            "nums":  "[1, 2, 3, 4, 5]",
            "expected":  [],
        },
        {
            "id": 5,
            "nums":  [5, 4, 3, 2, 1],
            "expected":  [4, 5, 2, 3, 1],
        },
    ]

    print("=================== 65. Sort array in wave form problem ===================")
    for name, func in [("adjacent pair swap", sort_array_wave_form_adjacent_pair_swap)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

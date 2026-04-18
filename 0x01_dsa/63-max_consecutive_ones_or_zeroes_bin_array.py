#!/usr/bin/env python3


def max_consecutive_ones_or_zeroes_bin_arr_simple_traversal(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    max_count, count = 0, 1

    for i in range(1, n):
        if nums[i] == nums[i - 1]:
            count += 1
        else:
            max_count = max(max_count, count)
            count = 1

    return max(max_count, count)


def max_consecutive_ones_or_zeroes_bin_arr_bit_manip(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    max_count, count, prev = 0, 0, -1

    for num in nums:
        if (prev ^ num) == 0:
            count += 1
        else:
            max_count = max(max_count, count)
            count = 1
        prev = num

    return max(max_count, count)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [0, 1, 0, 1, 1, 1, 1],
            "expected": 4,
        },
        {
            "id": 2,
            "nums": [0, 0, 1, 0, 1, 0],
            "expected": 2,
        },
        {
            "id": 3,
            "nums": [0, 0, 0, 0],
            "expected": 4,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums": "[0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1]",
            "expected": -1,
        },
        {
            "id": 6,
            "nums": [0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1],
            "expected": 3,
        },
        {
            "id": 7,
            "nums": [1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0],
            "expected": 3,
        },
    ]

    print("=================== 63. Maximum consecutive 1s or 0s in binary array problem ===================")
    for name, func in [("simple traversal", max_consecutive_ones_or_zeroes_bin_arr_simple_traversal), ("bit manipulation", max_consecutive_ones_or_zeroes_bin_arr_bit_manip)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

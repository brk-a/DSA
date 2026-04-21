#!/usr/bin/env python3

import copy


def add_one_to_number_carry_method(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    for i in range(n):
        if nums[i] < 0:
            return -1

    nums = copy.deepcopy(nums)
    carry = 1

    for i in range(n - 1, -1, -1):
        total = nums[i] + carry
        nums[i] = total % 10
        carry = total // 10

    if carry:
        nums.insert(0, carry)

    result = ""
    for i in range(len(nums)):
        result += str(nums[i])

    return int(result)


def add_one_to_number_prepend_method(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    for i in range(n):
        if nums[i] < 0:
            return -1

    nums = copy.deepcopy(nums)
    idx = n - 1

    while idx >= 0 and nums[idx] == 9:
        nums[idx] = 0
        idx -= 1

    if idx < 0:
        nums.insert(0, 1)
    else:
        nums[idx] += 1

    result = ""
    for i in range(len(nums)):
        result += str(nums[i])

    return int(result)


def add_one_to_number_append_method(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    for i in range(n):
        if nums[i] < 0:
            return -1

    nums = copy.deepcopy(nums)
    nums.reverse()
    idx = 0

    while idx < n and nums[idx] == 9:
        nums[idx] = 0
        idx += 1

    if idx == n:
        nums.append(1)
    else:
        nums[i] += 1

    nums.reverse()

    result = ""
    for i in range(len(nums)):
        result += str(nums[i])

    return int(result)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 4],
            "expected": 125,
        },
        {
            "id": 2,
            "nums": [9, 9, 9],
            "expected": 1000,
        },
        {
            "id": 3,
            "nums": "[1, 2, 3]",
            "expected": -1,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums": [1, -2, 3],
            "expected": -1,
        },
        {
            "id": 6,
            "nums": [0, 0, 0],
            "expected": 1,
        },
    ]

    print("=================== 66. Add one to number problem ===================")
    for name, func in [("carry", add_one_to_number_carry_method), ("prepend", add_one_to_number_prepend_method), ("reverse then append", add_one_to_number_append_method)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

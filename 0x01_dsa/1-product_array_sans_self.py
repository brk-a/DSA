#!/usr/bin/env python3

'''
[2, 4, 5, 6, 7] returns [840, 420, 336, 240]
'''


def product_array_except_self_brute_force(nums) -> list[int]:
    nums = nums if nums and isinstance(nums, list) else None
    if not nums or len(nums) < 1:
        return []

    length = len(nums)
    result = [1] * length

    for i in range(length):
        for j in range(length):
            if i != j:
                result[i] *= nums[j]


    return result


def product_array_except_self_left_right(nums) -> list[int]:
    nums = nums if nums and isinstance(nums, list) else None
    if not nums or len(nums) < 1:
        return []

    length = len(nums)
    right = [1] * length
    left = [1] * length

    for i in range(1, length):
        left[i] = left[i-1] * nums[i-1]

    for i in range(length-2, -1, -1):
        right[i] = right[i+1] * nums[i+1]

    return [left[i] * right[i] for i in range(length)]


def product_array_except_self_in_place(nums) -> list[int]:
    nums = nums if nums and isinstance(nums, list) else None
    if not nums or len(nums) < 1:
        return []

    length = len(nums)
    result = [1] * length

    for i in range(1, length):
        result[i] = result[i-1] * nums[i-1]

    right_prod = 1
    for i in range(length-1, -1, -1):
        result[i] *= right_prod
        right_prod *= nums[i]

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, 4, 7, 9],
            "expected": [252, 126, 72, 56],
        },
        {
            "id": 2,
            "nums": [3, 3],
            "expected": [3, 3],
        },
        {
            "id": 3,
            "nums": [6, 2, 3, 4],
            "expected": [24, 72, 48, 36],
        },
        {
            "id": 4,
            "nums": [],
            "expected": [],
        },
        {
            "id": 5,
            "nums": [9],
            "expected": [9],
        },
    ]


    print("=================== 1. Product array except self ===================")
    for name, func in [("brute force", product_array_except_self_brute_force), ("left-right", product_array_except_self_left_right), ("in place", product_array_except_self_in_place)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

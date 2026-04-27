#!/usr/bin/env python3


def missing_interval_linear_scan(nums: list[int], lower: int, upper: int) -> list[list[int]]:
    if not isinstance(nums, list) or not isinstance(lower, int) or not isinstance(upper, int) or len(nums) == 0:
        return [[]]

    n = len(nums)
    result = []

    if lower < nums[0]:
        result.append([lower, nums[0] - 1])

    for i in range(n - 1):
        if nums[i + 1] - nums[i] > 1:
            result.append([nums[i] + 1, nums[i + 1] - 1])

    if upper > nums[-1]:
        nums.append([nums[-1] + 1, upper])

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [14, 15, 20, 30, 31, 45],
            "lower": 10,
            "upper": 50,
            "expected": [[10, 13], [16, 19], [21, 29], [32, 44], [46, 50]],
        },
        {
            "id": 2,
            "nums": [-48, -10, -6, -4, 0, 4, 17],
            "lower": -54,
            "upper": 17,
            "expected":  [[-54, -49], [-47, -11], [-9, -7], [-5, -5], [-3, -1], [1, 3], [5,16]],
        },
        {
            "id": 3,
            "nums": "[14, 15, 20, 30, 31, 45]",
            "lower": 10,
            "upper": 50,
            "expected": [[]],
        },
        {
            "id": 4,
            "nums": [14, 15, 20, 30, 31, 45],
            "lower": "10",
            "upper": 50,
            "expected": [[]],
        },
        {
            "id": 5,
            "nums": [14, 15, 20, 30, 31, 45],
            "lower": 10,
            "upper": "50",
            "expected": [[]],
        },
    ]

    print("=================== 72. Missing interval(s) problem ===================")
    for name, func in [("linear scan", missing_interval_linear_scan)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'], test['lower'], test['upper'])
            print(f"Test {test['id']}: nums={test['nums']}, lower={test['lower']}, upper={test['upper']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

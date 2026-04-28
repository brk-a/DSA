#!/usr/bin/env python3


def sum_of_all_subarrays_nested_loops(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    result = 0

    for i in range(n):
        tmp = 0
        for j in range(i, n):
            tmp += nums[j]
            result += tmp

    return result


def sum_of_all_subarrays_element_contribution(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    result = 0

    for i in range(n):
        result += nums[i] * (i + 1) * (n - i)

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 4, 5, 3, 2],
            "expected": 116,
        },
        {
            "id": 2,
            "nums":  [1, 2, 3, 4],
            "expected": 50,
        },
        {
            "id": 3,
            "nums": "[1, 4, 5, 3, 2]",
            "expected": -1,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums": [4, 5, -3, 2],
            "expected": 36,
        },
    ]

    print("=================== 73. Sum of all sub-arrays problem ===================")
    for name, func in [("nested loops", sum_of_all_subarrays_nested_loops), ("element contribution", sum_of_all_subarrays_element_contribution)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

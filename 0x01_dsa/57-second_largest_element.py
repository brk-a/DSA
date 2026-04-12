#!/usr/bin/env python3


def second_largest_element_sorting(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    nums.sort()
    for i in range(n - 2, -1, -1):
        if nums[i] != nums[n - 1]:
            return nums[i]

    return -1


def second_largest_element_two_pass(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    largest, second_largest = -1, -1

    for i in range(n):
        if nums[i] > largest:
            largest = nums[i]

    for i in range(n):
        if nums[i] > second_largest and nums[i] != largest:
            second_largest = nums[i]

    return second_largest


def second_largest_element_one_pass(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    largest, second_largest = -1, -1

    for i in range(n):
        if nums[i] > largest:
            second_largest = largest
            largest = nums[i]
        elif nums[i] < largest and nums[i] > second_largest:
            second_largest = nums[i]

    return second_largest


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [12, 35, 1, 10, 34, 1],
            "expected": 34,
        },
        {
            "id": 2,
            "nums": [10, 5, 10],
            "expected": 5,
        },
        {
            "id": 3,
            "nums": [10, 10, 10],
            "expected": -1,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums": "[12, 35, 1, 10, 34, 1]",
            "expected": -1,
        },
    ]

    print("=================== 57. Second-largest element problem ===================")
    for name, func in [("sorting", second_largest_element_sorting), ("two-pass", second_largest_element_two_pass), ("one-pass", second_largest_element_one_pass)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

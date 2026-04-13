#!/usr/bin/env python3


def third_largest_element_sorting(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    nums.sort()

    return nums[n - 3]


def third_largest_element_three_loops(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    largest, second_largest, third_largest = float('-inf'), float('-inf'), float('-inf')

    for i in range(n):
        if nums[i] > largest:
            largest = nums[i]
    
    for i in range(n):
        if nums[i] > second_largest and nums[i] < largest:
            second_largest = nums[i]

    for i in range(n):
        if nums[i] > third_largest and nums[i] < second_largest:
            third_largest = nums[i]

    return third_largest


def third_largest_element_three_variables(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    largest, second_largest, third_largest = float('-inf'), float('-inf'), float('-inf')

    for i in range(n):
        if nums[i] > largest:
            third_largest = second_largest
            second_largest = largest
            largest = nums[i]
        elif nums[i] > second_largest:
            third_largest = second_largest
            second_largest = nums[i]
        elif nums[i] > third_largest:
            third_largest = nums[i]

    return third_largest


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 14, 2, 16, 10, 20],
            "expected": 14,
        },
        {
            "id": 2,
            "nums": [19, -10, 20, 14, 2, 16, 10],
            "expected": 16,
        },
        {
            "id": 3,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 4,
            "nums": "[1, 2, 3]",
            "expected": -1,
        },
        {
            "id": 5,
            "nums": [9, 9, 9, 9, 9],
            "expected": 9,
        },
    ]

    print("=================== 58. Third-largest element problem ===================")
    for name, func in [("sorting", third_largest_element_sorting), ("three loops", third_largest_element_three_loops), ("three variables", third_largest_element_three_variables)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

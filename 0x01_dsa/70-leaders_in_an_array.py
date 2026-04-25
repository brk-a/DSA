#!/usr/bin/env python3


def leaders_in_an_array_two_pointers(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    result = []

    for i in range(n):
        for j in range(i + 1, n):
            if nums[i] < nums[j]:
                break
        else:
            result.append(nums[i])
    
    return result


def leaders_in_an_array_suffix_maximum(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    result = []
    max_right = nums[-1]
    result.append(max_right)

    for i in range(n - 2, -1, -1):
        if nums[i] >= max_right:
            max_right = nums[i]
            result.append(max_right)

    result.reverse()

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [16, 17, 4, 3, 5, 2],
            "expected": [17, 5, 2],
        },
        {
            "id": 2,
            "nums": [1, 2, 3, 4, 5, 2],
            "expected": [5, 2],
        },
        {
            "id": 3,
            "nums": "[16, 17, 4, 3, 5, 2]",
            "expected": [],
        },
        {
            "id": 4,
            "nums": [],
            "expected": [],
        },
    ]

    print("=================== 70. Leaders in an array problem ===================")
    for name, func in [("two pointers", leaders_in_an_array_two_pointers), ("suffix maximum", leaders_in_an_array_two_pointers)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

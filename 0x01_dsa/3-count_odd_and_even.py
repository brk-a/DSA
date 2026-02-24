#!/usr/bin/env python3

def count_odd_even_mod(nums: list) -> int | int:
    nums = nums if nums and isinstance(nums, list) else []
    if len(nums) == 0:
        return 0, 0

    count_odd, count_even = 0, 0
    for _, v in enumerate(nums):
        if v % 2 == 0:
            count_even += 1
        else:
            count_odd += 1

    return count_odd, count_even


def count_odd_even_and(nums: list) -> int | int:
    nums = nums if nums and isinstance(nums, list) else []
    if len(nums) == 0:
        return 0, 0

    count_odd, count_even = 0, 0
    for _, v  in enumerate(nums):
        if (v & 1):
            count_odd += 1
        else:
            count_even += 1

    return count_odd, count_even


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, 3, 4, 5],
            "expected": (2, 2),
        },
        {
            "id": 2,
            "nums": [2, 3, 4, 5, 6],
            "expected": (2, 3),
        },
        {
            "id": 3,
            "nums": [1, 2, 3, 4, 5],
            "expected": (3, 2),
        },
        {
            "id": 4,
            "nums": [],
            "expected": (0, 0),
        },
        {
            "id": 5,
            "nums": [2, 4, 6],
            "expected": (0, 3),
        },
        {
            "id": 6,
            "nums": [1, 3, 5],
            "expected": (3, 0),
        },
    ]

    print("=================== 3. Count even and odd in an array problem ===================")
    for name, func in [("using mod", count_odd_even_mod), ("using and", count_odd_even_and)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

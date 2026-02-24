#!/usr/bin/env python3

def average_iterative(nums: list) -> float:
    nums = nums if nums and isinstance(nums, list) else []
    if len(nums) == 0:
        return 0
    
    cum_sum = 0
    for _, v in enumerate(nums):
        cum_sum += v

    return cum_sum / len(nums)

def average_recursive(nums: list) -> float:
    nums = nums if nums and isinstance(nums, list) else []
    if not nums:
        return 0
    
    n = len(nums)
    return average_recursive_helper(nums, 0, 0, n)

def average_recursive_helper (nums: list, i: int, total: int, n: int) -> float:
    if i == n:
        return total / n

    return average_recursive_helper(nums, i+1, total+nums[i], n)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 3, 4, 5],
            "expected": 3,
        },
        {
            "id": 1,
            "nums": [1, 2, 3, 4, -5],
            "expected": 1,
        },
        {
            "id": 1,
            "nums": [],
            "expected": 0,
        },
        {
            "id": 1,
            "nums": [-1, -2, -3, -4, -5],
            "expected": -3,
        },
    ]

    print("=================== 4. Average of elements in an array problem ===================")
    for name, func in [("iterative", average_iterative), ("tail recursive", average_recursive)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3


def maximum_sum_among_rotations_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    result = -float('inf')

    for i in range(n):
        total = 0
        for j in range(n):
            idx = (i +  j) % n
            total += j * nums[idx]

        result = max(result, total)

    return result


def maximum_sum_among_rotations_maths(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    curr_sum = 0
    for i in range(n):
        curr_sum += nums[i]

    curr_val = 0
    for i in range(n):
        curr_val += i * nums[i]

    result = curr_val
    for i in range(1, n):
        next_val = curr_val - (curr_sum - nums[i - 1] + nums[i - 1] * (n - 1))
        curr_val = next_val
        result = max(result, next_val)

    return result


def maximum_sum_among_rotations_pivot(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    result = 0
    pivot = find_pivot(nums)
    diff = n - 1 - pivot

    for i in range(n):
        result = result + ((i + diff) % n) * nums[i]
    
    return result


def find_pivot(nums: list[int]) -> int:
    n = len(nums)
    for i in range(n):
        if nums[i] > nums[(i + 1) % n]:
            return i
    return 0


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [8, 3, 1, 2],
            "expected": 29,
        },
        {
            "id": 2,
            "nums": [1, 2, 3],
            "expected": 8,
        },
        {
            "id": 3,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 4,
            "nums": "list",
            "expected": -1,
        },
        {
            "id": 5,
            "nums": [9, 9, 9, 9],
            "expected": 54,
        },
    ]

    print("=================== 56. Maximum sum among rotations problem ===================")
    for name, func in [("brute force", maximum_sum_among_rotations_brute_force), ("mathematics", maximum_sum_among_rotations_maths), ("pivot", maximum_sum_among_rotations_pivot)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

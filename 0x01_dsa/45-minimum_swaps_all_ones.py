#!/usr/bin/env python3


def minimum_swaps_all_ones_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    ones = nums.count(1)
    if ones == 0:
        return -1
    
    min_swap = float('inf')
    ones_count, min_swap = 0, 0
    for i in range(n - ones + 1):
        ones_count = sum(nums[i : i + ones_count])
        min_swap = min(min_swap, ones_count - ones)

    return min_swap


def minimum_swaps_all_ones_sliding_window(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    number_of_ones = 0
    n = len(nums)
    for i in range(n):
        if nums[i] == 1:
            number_of_ones += 1
    
    if number_of_ones == 0:
        return -1

    x = number_of_ones
    ones_count = 0
    max_ones = 0
    for i in range(x):
        if nums[i] == 1:
            ones_count += 1
    
    max_ones = ones_count
    for i in range(1, (n - x + 1)):
        if nums[i - 1] == 1:
            ones_count -= 1
        if nums[i + x - 1] == 1:
            ones_count += 1
        if max_ones < ones_count:
            max_ones = ones_count

    number_of_zeroes = x - max_ones

    return number_of_zeroes


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 0, 1, 0, 1],
            "expected": 1,
        },
        {
            "id": 2,
            "nums": [1, 1, 0, 1, 0, 1, 1],
            "expected": 2,
        },
        {
            "id": 3,
            "nums": [0, 0, 0],
            "expected": -1,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums": "",
            "expected": -1,
        },
    ]

    print("=================== 45. Minimum swaps to group all ones together problem ===================")
    for name, func in [("brute force", minimum_swaps_all_ones_brute_force), ("sliding window", minimum_swaps_all_ones_sliding_window)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

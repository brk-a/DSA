#!/usr/bin/env python3


def stock_buy_sell_multiple_transactions_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)

    return max_profit_recursion(nums, 0, n - 1)


def stock_buy_sell_multiple_transactions_local_minima_maxima(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    left_min, left_max = nums[0], nums[0]
    result = 0
    i = 0

    while i < n - 1:

        # find local minima
        while i < n - 1 and nums[i] >= nums[i + 1]:
            i += 1
        left_min = nums[i]

        # find local maxima
        while i < n - 1 and nums[i] <= nums[i + 1]:
            i += 1
        left_max = nums[i]

        result += left_max - left_min

    return result


def stock_buy_sell_multiple_transactions_accumulating_profit(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    result = 0

    for i in range(1, n):
        if nums[i] > nums[i - 1]:
            result += nums[i] - nums[i - 1]

    return result


def max_profit_recursion(nums: list[int], start: int, end: int) -> int:
    result = 0

    for i in range(start, end):
        for j in range(i + 1, end + 1):
            if nums[j] > nums[i]:
                curr = (nums[j] - nums[i]) + (max_profit_recursion(nums, start, i - 1)) + (max_profit_recursion(nums, j + 1, end))
                result = max(result, curr)

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [100, 180, 260, 310, 40, 535, 695],
            "expected": 865,
        },
        {
            "id": 2,
            "nums": [4, 2],
            "expected": 0,
        },
        {
            "id": 3,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 4,
            "nums": "[100, 180, 260, 310, 40, 535, 695]",
            "expected": -1,
        },
        {
            "id": 5,
            "nums": [120, 810, 206, 310, 40, 553, 695, 87],
            "expected": 1449,
        },
    ]

    print("=================== 67. Buy and sell stock (multiple transactions allowed) problem ===================")
    for name, func in [("brute force", stock_buy_sell_multiple_transactions_brute_force), ("local minima and maxima", stock_buy_sell_multiple_transactions_local_minima_maxima), ("accumulating profit", stock_buy_sell_multiple_transactions_accumulating_profit)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3

import math

def best_time_to_own_stock_brute_force(nums: list) -> int:
    if not isinstance(nums, list) or len(nums) < 2:
        return 0

    max_profit = -math.inf

    for i, v in enumerate(nums):
        for j in range(i+1, len(nums)):
            profit = nums[j] - v
            max_profit = max(profit, max_profit)
    
    return max_profit if max_profit > 0 else 0


def best_time_to_own_stock_one_pass(nums: list) -> int:
    if not isinstance(nums, list) or len(nums) < 2:
        return 0

    min_price = nums[0]
    max_profit = -math.inf

    for v in nums[1:]:
        max_profit = max(max_profit, v - min_price)
        min_price = min(min_price, v)

    return max_profit if max_profit > 0 else 0


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [],
            "expected": 0,
        },
        {
            "id": 2,
            "nums": [7, 10, 1, 3, 6, 9, 2],
            "expected": 8,
        },
        {
            "id": 3,
            "nums": [7, 6, 4, 3, 1],
            "expected": 0,
        },
        {
            "id": 4,
            "nums": [1, 3, 6, 9, 11],
            "expected": 10,
        },
        {
            "id": 5,
            "nums": [11, 9, 0, 3, 6, 10],
            "expected": 10,
        },
    ]

    print("=================== 12. Best time to buy stock problem ===================")
    for name, func in [("brute force", best_time_to_own_stock_brute_force), ("one pass", best_time_to_own_stock_one_pass)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

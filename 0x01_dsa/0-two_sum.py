#!/usr/bin/env python3

def two_sum_brute_force(nums, target) -> list[int]:
    nums = nums if nums and isinstance(nums, list) else None
    target = target if target and isinstance(target, int) else None

    if not nums or not target or len(nums) < 1:
        return []

    for i in range(len(nums)):
        for j in range(i+1, len(nums)):
            if nums[i] + nums[j] == target and i != j:
                return [i, j]
    
    return []


def two_sum_two_pass(nums, target) -> list[int]:
    nums = nums if nums and isinstance(nums, list) else None
    target = target if target and isinstance(target, int) else None

    if not nums or not target or len(nums) < 1:
        return []

    mp = dict()
    for i in range(len(nums)):
        mp[nums[i]] = i

    for i in range(len(nums)):
        diff = target - nums[i]
        if diff in mp.keys() and mp[diff] != i:
            return [i, mp[diff]]
    
    return []


def two_sum_one_pass(nums, target) -> list[int]:
    nums = nums if nums and isinstance(nums, list) else None
    target = target if target and isinstance(target, int) else None

    if not nums or not target or len(nums) < 1:
        return []

    mp = dict()

    for i in range(len(nums)):
        diff = target - nums[i]
        if diff in mp.keys():
            return [mp[diff], i]
        mp[nums[i]] = i

    return []

if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, 4, 7, 9],
            "target": 9,
            "expected": [0, 2],
        },
        {
            "id": 2,
            "nums": [3, 3],
            "target": 6,
            "expected": [0, 1],
        },
        {
            "id": 3,
            "nums": [6, 2, 3, 4],
            "target": 6,
            "expected": [1, 3],
        },
        {
            "id": 4,
            "nums": [],
            "target": 9,
            "expected": [],
        },
        {
            "id": 5,
            "nums": [9],
            "target": 9,
            "expected": [],
        },
        {
            "id": 6,
            "nums": [2],
            "target": 9,
            "expected": [],
        },
    ]


    print("=================== 1. Two sum problem ===================")
    for name, func in [("brute force", two_sum_brute_force), ("two pass", two_sum_two_pass), ("one pass", two_sum_one_pass)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"], test["target"])
            print(f"Test {test['id']}: nums={test['nums']}, target={test['target']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

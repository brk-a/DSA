#!/usr/bin/env python3

def two_sum_with_target_brute_force(nums: list, target: int) -> bool:
    if not isinstance(nums, list) or not isinstance(target, int) or len(nums) < 2:
        return False

    for i, v in enumerate(nums):
        for j, w in enumerate(nums):
            if i != j and v + w == target:
                return True
    
    return False

def two_sum_with_target_hash(nums: list, target: int) -> bool:
    if not isinstance(nums, list) or not isinstance(target, int) or len(nums) == 0:
        return False

    mp = {}
    for v in nums:
        diff = target - v
        if diff in mp:
            return True
        mp[diff] = True

    return False


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, -4, 7, 9],
            "target": 3,
            "expected": True,
        },
        {
            "id": 2,
            "nums": [3, 3],
            "target": 6,
            "expected": True,
        },
        {
            "id": 3,
            "nums": [6, 2, 3, -4],
            "target": 2,
            "expected": True,
        },
        {
            "id": 4,
            "nums": [],
            "target": 9,
            "expected": False,
        },
        {
            "id": 5,
            "nums": [9],
            "target": 9,
            "expected": True,
        },
        {
            "id": 6,
            "nums": [2],
            "target": -1,
            "expected": False,
        },
    ]


    print("=================== 11. Two sum with target problem ===================")
    for name, func in [("brute force", two_sum_with_target_brute_force), ("hash", two_sum_with_target_hash)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"], test["target"])
            print(f"Test {test['id']}: nums={test['nums']}, target={test['target']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

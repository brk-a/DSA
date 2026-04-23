#!/usr/bin/env python3

import copy


def remove_duplicates_assorted_array_hash_set(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    seen = set()
    nums = copy.deepcopy(nums)

    for i in range(n):
        if nums[i] not in seen:
            seen.add(nums[i])

    result = [i for i in seen]

    return result


def remove_duplicates_assorted_array_distinct_items_search(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    nums = copy.deepcopy(nums)
    result = []
    for i in range(n):
        if nums[i] not in result:
            result.append(nums[i])

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, 2, 2, 2, 2],
            "expected": [2],
        },
        {
            "id": 2,
            "nums": [1, 2, 2, 3, 4, 4, 4, 5, 5],
            "expected": [1, 2, 3, 4, 5],
        },
        {
            "id": 3,
            "nums": [],
            "expected": [],
        },
        {
            "id": 4,
            "nums": "[2, 2, 2, 2, 2]",
            "expected": [],
        },
        {
            "id": 5,
            "nums": [1, 2, 1, 2, 1],
            "expected": [1, 2],
        },
    ]

    print("=================== 68. Remove duplicated from sorted array problem ===================")
    for name, func in [("hash set", remove_duplicates_assorted_array_hash_set), ("distinct items search", remove_duplicates_assorted_array_distinct_items_search)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

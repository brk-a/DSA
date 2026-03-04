#!/usr/bin/env python3

def merge_overlapping_intervals_brute_force(nums: list[list[int]]) -> list[list[int]]:
    if not isinstance(nums, list) or len(nums) == 0 or (len(nums) == 1 and len(nums[0]) == 0):
        return [[]]

    n = len(nums)
    nums.sort()
    result = []

    for i in range(n):
        start = nums[i][0]
        end = nums[i][1]

        if result and result[-1][1] >= end:
            continue
        for j in range(i+1, n):
            if nums[j][0] <= end:
                end = max(end, nums[j][1])
        result.append([start, end])

    return result

def merge_overlapping_intervals_last_merged_interval(nums: list[list[int]]) -> list[list[int]]:
    if not isinstance(nums, list) or len(nums) == 0 or (len(nums) == 1 and len(nums[0]) == 0):
        return [[]]
    
    nums.sort()
    result = []
    result.append(nums[0])

    for i in range(1, len(nums)):
        last = nums[-1]
        current = nums[i]

        if current[0] <= last[1]:
            last[1] = max(last[1], current[1])
        else:
            result.append(current)
    
    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [[1, 3], [2, 4], [6, 8], [9, 10]],
            "expected": [[1, 4], [6, 8], [9, 10]],
        },
        {
            "id": 2,
            "nums": [[7, 8], [1, 5], [2, 4], [4, 6]],
            "expected": [[1, 6], [7, 8]],
        },
        {
            "id": 3,
            "nums": [[]],
            "expected": [[]],
        },
        {
            "id": 4,
            "nums": [[5, 6]],
            "expected": [[5, 6]],
        },
        # {
        #     "id": 5,
        #     "nums": [[]],
        #     "expected": [[]],
        # },
    ]

    print("=================== 18. Merge overlapping intervals problem ===================")
    for name, func in [("brute force", merge_overlapping_intervals_brute_force), ("last merged interval", merge_overlapping_intervals_last_merged_interval)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

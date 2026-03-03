#!/usr/bin/env python3


def insert_merge_interval_brute_force(nums:list[list[int]], new_interval: list[int]) -> list[list[int]]:
    if len(new_interval) == 0:
        return nums[:]  # copy to avoid mutation
    if len(nums) == 0 or (len(nums) == 1 and len(nums[0]) == 0):
        return [new_interval]
    
    intervals = sorted(nums) + [new_interval]
    intervals.sort()
    
    result = [intervals[0]]
    for current in intervals[1:]:
        last = result[-1]
        if current[0] <= last[1]:
            last[1] = max(last[1], current[1])
        else:
            result.append(current)
    return result


def insert_merge_interval_contiguous_interval(nums:list[list[int]], new_interval: list[int]) -> list[list[int]]:
    if len(new_interval) == 0:
        return nums[:]  # copy to avoid mutation
    if len(nums) == 0 or (len(nums) == 1 and len(nums[0]) == 0):
        return [new_interval]

    result = []
    i = 0
    n = len(nums)

    while i < n and nums[i][1] < new_interval[0]:
        result.append(nums[i])
        i += 1

    curr = new_interval[:]
    while i < n and nums[i][0] <= curr[1]:
        curr[0] = min(curr[0], nums[i][0])
        curr[1] = max(curr[1], nums[i][1])
        i += 1
    result.append(curr)
    
    while i < n:
        result.append(nums[i])
        i += 1
    return result

if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [[1, 3], [4, 5], [6, 7], [8, 10]],
            "new interval": [5, 6],
            "expected": [[1, 3], [4, 7], [8, 10]],
        },
        {
            "id": 2,
            "nums": [[1, 2], [3, 5], [6, 7], [8, 10], [12, 16]],
            "new interval": [4, 9],
            "expected": [[1, 2], [3, 10], [12, 16]],
        },
        {
            "id": 3,
            "nums": [[]],
            "new interval": [5, 6] ,
            "expected": [[5, 6]],
        },
        {
            "id": 4,
            "nums": [[5, 6]],
            "new interval": [] ,
            "expected": [[5, 6]],
        },
        {
            "id": 5,
            "nums": [[]],
            "new interval": [] ,
            "expected": [[]],
        },
    ]

    print("=================== 17. Insert merge sort problem ===================")
    for name, func in [("brute force", insert_merge_interval_brute_force), ("contiguous interval", insert_merge_interval_contiguous_interval)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"], test["new interval"])
            print(f"Test {test['id']}: nums={test['nums']}, new interval={test['new interval']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

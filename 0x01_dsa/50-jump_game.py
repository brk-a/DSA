#!/usr/bin/env python3

import sys


def jump_game_recursion(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    answer = min_jumps_recursion(0, nums)
    return -1 if answer == sys.maxsize else answer


def jump_game_tabulation(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    tab = [sys.maxsize] * n
    tab[n - 1] = 0

    for i in range(n - 2, -1, -1):
        for j in range(i + 1, min(i + nums[i] + 1, n)):
            if tab[j] != sys.maxsize:
                tab[i] = min(tab[i], tab[j] + 1)

    return -1 if tab[0] == sys.maxsize else tab[0]


def jump_game_greedy_algo(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    if nums[0] == 0:
        return -1

    max_reach, curr_reach = 0, 0
    jump = 0
    for i in range(n):
        max_reach = max(max_reach, i + nums[i])
        if max_reach >= n - 1:
            return jump + 1

        if i == curr_reach:
            if i == max_reach:
                return -1
            else:
                jump += 1
                curr_reach = max_reach

    return -1


def min_jumps_recursion(i: int, arr: list[int]) -> int:
    if i >= len(arr) - 1:
        return 0

    answer = sys.maxsize
    for j in range(i + 1, min(i + arr[i] + 1, len(arr))):
        value = min_jumps_recursion(j, arr)
        if value != sys.maxsize:
            answer = min(answer, 1 + value)

    return answer


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums":  [1, 3, 5, 8, 9, 2, 6, 7, 6, 8, 9],
            "expected": 3,
        },
        {
            "id": 2,
            "nums":  [1, 4, 3, 2, 6, 7],
            "expected": 2,
        },
        {
            "id": 3,
            "nums":  [0, 10, 20],
            "expected": -1,
        },
        {
            "id": 4,
            "nums":  [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums":  "list",
            "expected": -1,
        },
    ]
    
    print("=================== 50. Jump game problem ===================")
    for name, func in [("recursion", jump_game_recursion), ("tabulation", jump_game_tabulation), ("greedy algo", jump_game_greedy_algo)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

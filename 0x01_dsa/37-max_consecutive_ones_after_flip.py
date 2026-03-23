#!/usr/bin/env python3


def max_consecutive_ones_after_flip_brute_force(nums: list[int], k: int) -> int:
    if not isinstance(nums, list) or not isinstance(k, int) or len(nums) == 0:
        return -1

    result = 0
    n = len(nums)

    for i in range(n):
        counter = 0
        for j in range(i, n):
            if nums[j] == 0:
                counter += 1
            if counter <= k:
                result = max(result, j - i + 1)

    return result


def max_consecutive_ones_after_flip_sliding_window(nums: list[int], k: int) -> int:
    if not isinstance(nums, list) or not isinstance(k, int) or len(nums) == 0:
        return -1

    n = len(nums)
    result = 0
    start, end = 0, 0
    counter = 0

    while end < n:
        if nums[end] == 0:
            counter += 1
        while counter > k:
            if nums[start] == 0:
                counter -= 1

            start += 1

        result = max(result, end - start + 1)
        end += 1

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1],
            "k": 2,
            "expected": 8,
        },
        {
            "id": 2,
            "nums": [1, 0, 0, 1, 0, 1, 0, 1],
            "k": 2,
            "expected": 5,
        },
        {
            "id": 3,
            "nums": [],
            "k": 2,
            "expected": -1,
        },
        {
            "id": 4,
            "nums": [1, 2, 3, 4],
            "k": "",
            "expected": -1,
        },
    ]

    print("=================== 37. Max consecutive ones after flip problem ===================")
    for name, func in [("brute force", max_consecutive_ones_after_flip_brute_force), ("sliding window", max_consecutive_ones_after_flip_sliding_window)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"], test["k"])
            print(f"Test {test['id']}: nums={test['nums']}, k={test['k']} got={got}, expected={test['expected']}, passed={got == test['expected']}")

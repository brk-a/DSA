#!/usr/bin/env python3

from collections import defaultdict


def count_subarray_k_distinct_brute_force(nums: list[int], k: int) -> int:
    if not isinstance(nums, list) or not isinstance(k, int) or len(nums) == 0 or k < 0:
        return -1

    n = len(nums)
    result = 0

    for i in range(n):
        st = set()
        for j in range(i, n):
            st.add(nums[j])

            if len(st) > k:
                break
            if len(st) == k:
                result += 1

    return result


def count_subarray_k_distinct_sliding_window(nums: list[int], k: int) -> int:
    if not isinstance(nums, list) or not isinstance(k, int) or len(nums) == 0 or k < 0:
        return -1

    return at_most_k(nums, k) - at_most_k(nums, k - 1)


def at_most_k(nums: list[int], k: int) -> int:
    n = len(nums)
    result = 0
    left, right = 0, 0
    freq = defaultdict(int)

    while right < n:
        freq[nums[right]] += 1
        if freq[nums[right]] == 1:
            k -= 1

        while k < 0:
            freq[nums[left]] -= 1
            if freq[nums[left]] == 0:
                k += 1
            left += 1

        result += right - left + 1
        right += 1

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 2, 3],
            "k": 2,
            "expected": 4,
        },
        {
            "id": 2,
            "nums": [3, 1, 2, 2, 3],
            "k": 3,
            "expected": 4,
        },
        {
            "id": 3,
            "nums": [1, 1, 1, 1],
            "k": 2,
            "expected": 0,
        },
        {
            "id": 4,
            "nums": [],
            "k": 3,
            "expected": -1,
        },
        {
            "id": 5,
            "nums": "list",
            "k": 1,
            "expected": -1,
        },
        {
            "id": 6,
            "nums": [3, 1, 2, 2, 3],
            "k": -3,
            "expected": -1,
        },
        {
            "id": 7,
            "nums": [3, 1, 2, 2, 3],
            "k": "three",
            "expected": -1,
        },
    ]

    print("=================== 54. Count sub-array w/ K distinct elements problem ===================")
    for name, func in [("brute force", count_subarray_k_distinct_brute_force), ("sliding window", count_subarray_k_distinct_sliding_window)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'], test['k'])
            print(f"Test {test['id']}: nums={test['nums']}, k={test['k']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

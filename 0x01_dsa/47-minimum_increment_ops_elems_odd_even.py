#!/usr/bin/env python3

def minimum_increment_ops_elems_odd_even_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    result = 0

    for i in range(n):
        even_sum, odd_sum = 0, 0
        idx = 0

        for j in range(n):
            if j == i:
                continue

            if idx % 2 == 0:
                even_sum += nums[j]
            else:
                odd_sum += nums[j]
            idx += 1

        if even_sum == odd_sum:
            result += 1

    return result


def minimum_increment_ops_elems_odd_even_prefix_suffix(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    result = 0
    right_odd_sum, right_even_sum = 0, 0

    for i in range(n):
        if i % 2 == 0:
            right_even_sum += nums[i]
        else:
            right_odd_sum += nums[i]

    left_odd_sum, left_even_sum = 0, 0
    for i in range(n):
        if i % 2 == 0:
            right_even_sum -= nums[i]
        else:
            right_odd_sum -= nums[i]

        if left_odd_sum + right_even_sum == left_even_sum + right_odd_sum:
            result += 1

        if i % 2 == 0:
            left_even_sum += nums[i]
        else:
            left_odd_sum += nums[i]

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, 1, 6, 4],
            "expected": 1,
        },
        {
            "id": 2,
            "nums": [1, 1, 1],
            "expected": 3,
        },
        {
            "id": 3,
            "nums": [3, 2, 7, 4],
            "expected": 1,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
    ]

    print("=================== 47. Minimum increment operations to make array elements equal even/odd problem ===================")
    for name, func in [("brute force", minimum_increment_ops_elems_odd_even_brute_force), ("prefix-suffix", minimum_increment_ops_elems_odd_even_prefix_suffix)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3


def max_circular_subarray_sum_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    result = nums[0]

    for i in range(n):
        current_sum = 0
        for j in range(n):
            idx = (i + j) % n
            current_sum += nums[idx]
            result = max(result, current_sum)

    return result


def max_circular_subarray_sum_prefix_suffix(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    suffix_sum = nums[n - 1]
    max_suffix = [0] * (n + 1)
    max_suffix[n - 1] = nums[n - 1]

    for i in range(n - 2, -1, -1):
        suffix_sum += nums[i]
        max_suffix[i] = max(max_suffix[i + 1], suffix_sum)

    circular_sum = nums[0]
    normal_sum = nums[0]
    current_sum, prefix = 0, 0

    for i in range(n):
        current_sum = max(current_sum + nums[i], nums[i])
        normal_sum = max(current_sum, normal_sum)
        prefix += nums[i]
        circular_sum = max(circular_sum, prefix + max_suffix[i + 1])

    return max(circular_sum, normal_sum)


def max_circular_subarray_sum_kadane_algo(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n, total_sum = len(nums), 0
    curr_max_sum, curr_min_sum = 0, 0
    max_sum, min_sum = nums[0], nums[0]

    for i in range(n):
        curr_max_sum = max(curr_max_sum + nums[i], nums[i])
        max_sum = max(max_sum, curr_max_sum)

        curr_min_sum = min(curr_min_sum + nums[i], nums[i])
        min_sum = min(curr_min_sum, min_sum)

        total_sum += nums[i]

    normal_sum = max_sum
    circular_sum = total_sum - min_sum

    if min_sum == total_sum:
        return normal_sum

    return max(normal_sum, circular_sum)
    


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [8, -8, 9, -9, 10, -11, 12],
            "expected": 22,
        },
        {
            "id": 2,
            "nums": [4, -1, -2, 3],
            "expected": 7,
        },
        {
            "id": 3,
            "nums": [5, -2, 3, 4],
            "expected": 12,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums": "List",
            "expected": -1,
        },
    ]

    print("=================== 48. Maximum circular sub-array sum problem ===================")
    for name, func in [("brute force", max_circular_subarray_sum_brute_force), ("prefix-suffix", max_circular_subarray_sum_prefix_suffix), ("kadane's algo", max_circular_subarray_sum_kadane_algo)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3

def max_subarray_sum_brute_force(nums: list) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return 0

    result = nums[0]

    for i in range(len(nums)):
        curr_sum = 0
        for j in range(i, len(nums)):
            curr_sum += nums[j]
            result = max(curr_sum, result)

    return result


def max_subarray_sum_kadane(nums: list) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return 0

    result = nums[0]
    max_ending = nums[0]

    for i in range(1, len(nums)):
        max_ending = max(max_ending + nums[i], nums[i])
        result = max(result, max_ending)

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, 3, -8, 7, -1, 2, 3],
            "expected": 11,
        },
        {
            "id": 2,
            "nums": [-2, -4],
            "expected": -2,
        },
        {
            "id": 3,
            "nums": [5, 4, 1, 7, 8],
            "expected": 25,
        },
        {
            "id": 4,
            "nums": [],
            "expected": 0,
        },
    ]

    print("=================== 13. Maximum sub-array sum problem ===================")
    for name, func in [("brute force", max_subarray_sum_brute_force), ("kadane algo", max_subarray_sum_kadane)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

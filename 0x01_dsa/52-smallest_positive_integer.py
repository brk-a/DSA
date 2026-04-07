#!/usr/bin/env python3

def smallest_positive_integer_memoisation(nums: list) -> int:
    if not isinstance(nums, list):
        # zero is neither negative nor positive, therefore, the
        # min possible sum of an empty array cannot be zero, however,
        # this problem assumes that zero is strictly non-negative
        return -1

    n = len(nums)
    total = 0
    for num in nums:
        total += num

    memo = [False] * (total + 1)
    memo[0] = True
    for i in range(n):
        for j in range(total - nums[i], -1, -1):
            if memo[j]:
                memo[j + nums[i]] = True # total j + nums[i] is possible if total j is possible

    for i in range(total + 1):
        if not memo[i]:
            return i # found min val for which total is impossible :@tick::

    return total + 1 # all vals are possible; return next smallest positive int :@tick::


def smallest_positive_integer_sorting(nums: list) -> int:
    if not isinstance(nums, list):
        # zero is neither negative nor positive, therefore, the
        # min possible sum of an empty array cannot be zero, however,
        # this problem assumes that zero is strictly non-negative
        return -1

    n = len(nums)
    result = 1
    nums.sort()

    for i in range(n):
        if nums[i] <= result:
            result += nums[i]
        else:
            break

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 10, 3, 11, 6, 15],
            "expected": 2,
        },
        {
            "id": 2,
            "nums": [1, 1, 1, 1],
            "expected": 5,
        },
        {
            "id": 3,
            "nums": [1, 1, 3, 4],
            "expected": 10,
        },
        {
            "id": 4,
            "nums": [],
            "expected": 1,
        },
        {
            "id": 5,
            "nums": "list",
            "expected": -1,
        },
    ]

    print("=================== 50. Jump game problem ===================")
    for name, func in [("memoisation", smallest_positive_integer_memoisation), ("sorting", smallest_positive_integer_sorting)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")


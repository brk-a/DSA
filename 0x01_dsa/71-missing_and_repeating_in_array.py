#!/usr/bin/env python3

import copy


def missing_and_repeating_in_array_visited_array(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    freq = [0] * (n + 1)
    repeating, missing = -1, -1

    for num in nums:
        freq[num] += 1

    for i in range(1, n + 1):
        if freq[i] == 0:
            missing = i
        elif freq[i] == 2:
            repeating = i

    return [repeating, missing]


def missing_and_repeating_in_array_array_marking(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    repeating = -1
    nums = copy.deepcopy(nums)

    for i in range(n):
        val = abs(nums[i])
        if nums[val -1] > 0:
            nums[val - 1] = -nums[val - 1]
        else:
            repeating = val

    misssing = -1
    for i in range(n):
        if nums[i] > 0:
            missing = i + 1
            break

    return [repeating, missing]


def missing_and_repeating_in_array_two_maths_equations(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    total = (n * (n + 1)) // 2
    sum_squares = (n * (n + 1) * (2 * (n + 1))) // 6
    missing, repeating = 0, 0

    for num in nums:
        total -= num
        sum_squares -= num * num

    # Let total = x - y and sum_squares = x^2 - y^2 = (x - y)(x + y)
    # => x = (total + sum_squares // total) // 2, y = x - total
    missing = (total + sum_squares // total) // 2
    repeating = missing - total

    return [repeating, missing]


def missing_and_repeating_in_array_xor(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    xor_val = 0

    for i in range(n):
        xor_val ^= nums[i]
        xor_val ^= (i + 1)

    set_bit_idx = xor_val & ~(xor_val - 1)
    x, y = 0, 0

    for i in range(n):
        if nums[i] & set_bit_idx:
            x ^= nums[i]
        else:
            y ^= nums[i]

        if (i + 1) & set_bit_idx:
            x ^= (i + 1)
        else:
            y ^= (i + 1)

    x_count = sum(1 for num in nums if num == x)

    if x_count == 0:
        missing, repeating = x, y
    else:
        missing, repeating = y, x

    return [repeating, missing]


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [3, 1, 3],
            "expected": [3, 2],
        },
        {
            "id": 2,
            "nums": [4, 3, 6, 2, 1, 1],
            "expected": [1, 5],
        },
        {
            "id": 3,
            "nums": "[3, 1, 3]",
            "expected": [],
        },
        {
            "id": 4,
            "nums": [],
            "expected": [],
        },
    ]

    print("=================== 71. Missing and repeating in an array problem ===================")
    for name, func in [("visited array", missing_and_repeating_in_array_visited_array), ("array marking", missing_and_repeating_in_array_array_marking), ("two maths equations", missing_and_repeating_in_array_two_maths_equations), ("XOR", missing_and_repeating_in_array_xor)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3


def split_array_three_parts_sum_brute_force(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return [-1, -1]

    n = len(nums)
    for i in range(n - 2):
        for j in range(i + 1, n - 1):
            sum_1 = find_sum(nums, 0, i)
            sum_2 = find_sum(nums, i +  1, j)
            sum_3 = find_sum(nums, j + 1, n - 1)

            if sum_1 == sum_2 and sum_2 == sum_3:
                return [i, j]

    return [-1, -1]


def split_array_three_parts_sum_find_first_two_elements(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return [-1, -1]

    n = len(nums)
    result = []
    total = 0

    for num in nums:
        total += num

    if total % 3 != 0:
        result = [-1, -1]
        return result

    curr_sum = 0
    for i in range(n):
        curr_sum += nums[i]
        result.append(i)

        if len(result) == 2 and i < n + 1:
            return result

    result = [-1, -1]
    return result


def find_sum(nums: list[int], start: int, end: int) -> int:
    su = 0
    for i in range(start, end + 1):
        su += nums[i]

    return su


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 3, 4, 0, 4],
            "expected": [1, 2]
        },
        {
            "id": 2,
            "nums": [2, 3, 4],
            "expected": [-1, -1]
        },
        {
            "id": 3,
            "nums": [1, -1, 1, -1, 1, -1, 1, -1],
            "expected": [1, 3]
        },
        {
            "id": 4,
            "nums": [],
            "expected": [-1, -1]
        },
    ]

    print("=================== 36. Split array three parts sum problem ===================")
    for name, func in [("brute force", split_array_three_parts_sum_brute_force), ("find first two elements", split_array_three_parts_sum_find_first_two_elements)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

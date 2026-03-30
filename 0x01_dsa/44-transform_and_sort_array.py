#!/usr/bin/env python3

def transform_and_sort_array_brute_force(nums: list[int], a: int, b: int, c: int) -> list[int]:
    if not isinstance(nums, list) or not isinstance(a, int) or not isinstance(b, int) or not isinstance(c, int) or len(nums) == 0:
        return []

    result = [apply_quadratic(i, a, b, c) for i in nums]

    # for num in nums:
    #     elem = (a * num ** 2) + (b * num) + c
    #     result.append(elem)

    return sorted(result)


def transform_and_sort_array_two_pointers(nums: list[int], a: int, b: int, c: int) -> list[int]:
    if not isinstance(nums, list) or not isinstance(a, int) or not isinstance(b, int) or not isinstance(c, int) or len(nums) == 0:
        return []

    n = len(nums)
    result = [0] * n
    left, right = 0, n - 1
    idx = n - 1 if a >= 0 else 0

    while left <= right:
        left_val = apply_quadratic(nums[left], a, b, c)
        right_val = apply_quadratic(nums[right], a, b, c)

        if a >= 0:
            if left_val >= right_val:
                result[idx] = left_val
                left += 1
                idx -= 1
            else:
                result[idx] = right_val
                right -= 1
                idx -= 1
        else:
            if left_val < right_val:
                result[idx] = left_val
                left += 1
                idx += 1
            else:
                result[idx] = right_val
                right -= 1
                idx += 1

    return result


def apply_quadratic(x: int, a: int, b: int, c: int) -> int:
    return (a * x ** 2) + (b * x) + c


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [-4, -2, 0, 2, 4],
            "a": 1,
            "b": 3,
            "c": 5,
            "expected": [3, 5, 9, 15, 33],
        },
        {
            "id": 2,
            "nums": [-3, -1, 2, 4],
            "a": -1,
            "b": 0,
            "c": 0,
            "expected": [-16, -9, -4, -1],
        },
        {
            "id": 3,
            "nums": [-1, 0, 1, 2, 3, 4],
            "a": -1,
            "b": 2,
            "c": -1,
            "expected": [-9, -4, -4, -1, -1, 0],
        },
        {
            "id": 4,
            "nums": [],
            "a": 2,
            "b": 3,
            "c": 4,
            "expected": [],
        },
        {
            "id": 5,
            "nums": [-9, -4, -4, -1, -1, 0],
            "a": "",
            "b": 2,
            "c": 1,
            "expected": [],
        },
        {
            "id": 6,
            "nums": [-9, -4, -4, -1, -1, 0],
            "a": 0,
            "b": [],
            "c": 1,
            "expected": [],
        },
        {
            "id": 7,
            "nums": [-9, -4, -4, -1, -1, 0],
            "a": 1,
            "b": 2,
            "c": False,
            "expected": [],
        },
        {
            "id": 8,
            "nums": [-9, -4, -4, -1, -1, 0],
            "a": 1,
            "b": 2,
            "c": 1,
            "expected": [0, 0, 1, 9, 9, 64],
        },
        {
            "id": 9,
            "nums": [-9, -4, -4, -1, -1, 0],
            "a": -1,
            "b": 2,
            "c": 1,
            "expected": [-98, -23, -23, -2, -2, 1],
        },
    ]

    print("=================== 43. Transform and sort array problem ===================")
    for name, func in [("brute force", transform_and_sort_array_brute_force), ("two pointers", transform_and_sort_array_two_pointers)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"], test["a"], test["b"], test["c"])
            print(f"Test {test['id']}: nums={test['nums']}, a={test["a"]}, b={test["b"]}, c={test["c"]}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

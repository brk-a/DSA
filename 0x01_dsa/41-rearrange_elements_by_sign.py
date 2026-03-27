#!/usr/bin/env python3

def rearrange_elements_by_sign_rotation(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    for i in range(n):
        if nums[i] >= 0 and i % 2 == 1:
            for j in range(i + 1, n):
                if nums[j] < 0:
                    right_rotate(nums, i, j)
        elif nums[i] < 0 and i % 2 == 0:
            for j in range(i + 1, n):
                if nums[j] >= 0:
                    right_rotate(nums, i, j)
                    break

    return nums


def rearrange_elements_by_sign_two_pointers(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    positive = []
    negative = []

    for num in nums:
        if num >= 0:
            positive.append(num)
        else:
            negative.append(num)

    positive_idx, negative_idx = 0, 0
    i = 0
    while positive_idx < len(positive) and negative_idx < len(negative):
        if i % 2 == 0:
            nums[i] = positive[positive_idx]
            positive_idx += 1
        else:
            nums[i] = negative[negative_idx]
            negative_idx += 1
        i += 1

    while positive_idx < len(positive):
        nums[i] = positive[positive_idx]
        positive_idx += 1
        i += 1

    while negative_idx < len(negative):
        nums[i] = negative[negative_idx]
        negative_idx += 1
        i += 1

    return nums 


def right_rotate(nums: list[int], start: int, end: int) -> None:
    temp = nums[end]
    for i in range(end, start, -1):
        nums[i] = nums[i - 1]

    nums[start] = temp


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 3, -4, -1, 4],
            "expected": [1, -4, 2, -1, 3, 4],
        },
        {
            "id": 2,
            "nums": [-5, -2, 5, 2, 4, 7, 1, 8, 0, -8],
            "expected": [5, -5, 2, -2, 4, -8, 7, 1, 8, 0],
        },
        {
            "id": 3,
            "nums": [],
            "expected": [],
        },
    ]

    print("=================== 41. Re-arrange elements by sign problem ===================")
    for name, func in [("rotation", rearrange_elements_by_sign_rotation), ("two pointer", rearrange_elements_by_sign_two_pointers)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

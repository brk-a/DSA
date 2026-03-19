#!/usr/bin/env python3

def minimise_max_height_brute_force(nums: list[int], k: int) -> int:
    if not isinstance(nums, list) or not isinstance(k ,int) or len(nums) == 0:
        return -1

    n = len(nums)
    nums.sort()
    result = nums[n - 1] - nums[0]

    for i in range(n):
        if nums[i] - k < 0:
            continue

        min_h = min(nums[0] + k, nums[i] - k)
        max_h = max(nums[i - 1] + k, nums[n - 1] - k)
        result = min(result, max_h - min_h)

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 5, 8, 10],
            "k": 2,
            "expected": 5,
        },
        {
            "id": 2,
            "nums": [3, 9, 12, 16, 20],
            "k": 3,
            "expected": 11,
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

    print("=================== 33. Minimise maximum height problem ===================")
    for name, func in [("brute force", minimise_max_height_brute_force)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"], test["k"])
            print(f"Test {test['id']}: nums={test['nums']}, k={test['k']} got={got}, expected={test['expected']}, passed={got == test['expected']}")

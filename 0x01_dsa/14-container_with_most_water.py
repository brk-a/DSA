#!/usr/bin/env python3

def container_with_most_water_brute_force(nums: list) -> int:
    if not isinstance(nums, list) and len(nums) < 2:
        return 0

    n = len(nums)
    result = 0

    for i in range(n):
        for j in range(i+1, n):
            water = min(nums[i], nums[j]) * (j - i)
            result =  max(water, result)
    
    return result

def container_with_most_water_two_pointer(nums: list) -> int:
    if not isinstance(nums, list) and len(nums) < 2:
        return 0

    left, right = 0, len(nums) - 1
    result = 0

    while left < right:
        water = min(nums[left], nums[right]) * (right - left)
        result = max(result, water)

        if nums[left] < nums[right]:
            left += 1
        else:
            right -= 1

    return result

if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 5, 4, 3],
            "expected": 6,
        },
        {
            "id": 2,
            "nums": [3, 1, 2, 4, 5],
            "expected": 12
        },
        {
            "id": 3,
            "nums": [2, 1, 8, 6, 4, 6, 5, 5],
            "expected": 25,
        },
        {
            "id": 4,
            "nums": [],
            "expected": 0,
        },
    ]

    print("=================== 14. Container with most water problem ===================")
    for name, func in [("brute force", container_with_most_water_brute_force), ("two-pointer", container_with_most_water_two_pointer)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

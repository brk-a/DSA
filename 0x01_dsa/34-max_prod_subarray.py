#!/usr/bin/env python3

def max_prod_subarray_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    max_prod = nums[0]

    for i in range(n):
        mul = 1
        for j in range(i, n):
            mul *= nums[j]
            max_prod = max(max_prod, mul)

    return max_prod



def max_prod_subarray_greedy_min_max(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    curr_max = nums[0]
    curr_min = nums[0]
    max_prod = nums[0]

    for i in range(1, n):
        temp = max(nums[i], nums[i] * curr_max, nums[i] * curr_min)
        curr_min = min(nums[i], nums[i] * curr_max, nums[i] * curr_min)
        curr_max = temp
        max_prod = max(max_prod, curr_max)

    return max_prod



def max_prod_subarray_traverse_both_directions(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    left_to_right = 1
    right_to_left = 1

    for in in range(n):
        if left_to_right == 0:
            left_to_right = 1
        if right_to_left == 0:
            right_to_left = 1
        
        left_to_right *= nums[i]
        j = n - i - 1
        right_to_left *= nums[j]
        max_prod = max(left_to_right, right_to_left, max_prod)

    return max_prod



if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [-2, 6, -3, -10, 0, 2],
            "expected": 180,
        },
        {
            "id": 2,
            "nums": [-1, -3, -10, 0, 6],
            "expected": 30,
        },
        {
            "id": 3,
            "nums": [2, 3, 4],
            "expected": 24,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
    ]

       print("=================== 33. Maximum sub-array product problem ===================")
    for name, func in [("brute force", max_prod_subarray_brute_force), ("greedy min-max", max_prod_subarray_greedy_min_max), ("traverse both directions", max_prod_subarray_traverse_both_directions)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3

def trapping_rainwater_brute_force(nums: list) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return 0

    result = 0

    for i in range(1, len(nums)-1):
         # find max left
        left = nums[i]
        for j in range(i):
            left = max(left, nums[j])

        # find max right
        right = nums[i]
        for j in range(i+1, len(nums)):
            right = max(right, nums[j])

        # update water held
        result += min(left, right) - nums[i]

    return result

def trapping_rainwater_prefix_suffix(nums: list) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return 0

    n = len(nums)
    right = [0] * n
    left = [0] * n
    result = 0

    # fill left
    left[0] = nums[0]
    for i in range(1, n):
        left[i] = max(left[i-1], nums[i])
    
    # fill right
    right[n-1] = nums[n-1]
    for i in range(n-2, -1, -1):
        right[i] = max(right[i+1], nums[i])

    # calc water accumulated
    for i in range(1, n-1):
        min_of_two = min(left[i], right[i])
        result += min_of_two - nums[i]

    return result 

def trapping_rainwater_two_pointers(nums: list) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return 0

    left, right = 1, len(nums) - 2
    left_max, right_max = nums[left - 1], nums[right +1]
    result = 0

    while left <= right:
        if right_max < left_max:
            # decide amount of water based on right_max
            result += max(0, right_max - nums[right])
            right_max = max(right_max, nums[right])
            right -= 1
        else:
            # decide amount of water based on left_max
            result += max(0, left_max - nums[left])
            left_max = max(left_max, nums[left])
            left += 1

    return result

def trapping_rainwater_stack(nums: list) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return 0

    stack = []
    result = 0

    for i in range(len(nums)):
        # pop all  items smaller than nums[i]
        while stack and nums[stack[-1]] < nums[i]:
            pop_height = nums[stack.pop()]

            if not stack:
                break
                
            # nums[i] is the next greater for removed item
            # and new stack top is previous greater
            distance = i - stack[-1] - 1

            #take min of next and prev greater
            water = min(nums[stack[-1]], nums[i])

            #find amount of water
            water -= pop_height
            result += distance * water

        stack.append(i)

    return result



if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [3, 0, 1, 0, 4, 0, 2],
            "expected": 10,
        },
        {
            "id": 2,
            "nums": [3, 0, 2, 0, 4],
            "expected": 7,
        },
        {
            "id": 3,
            "nums": [1, 2, 3, 4],
            "expected": 0,
        },
        {
            "id": 4,
            "nums": [],
            "expected": 0,
        },
    ]

    print("=================== 16. Trapping rainwater problem ===================")
    for name, func in [("brute force", trapping_rainwater_brute_force), ("prefix-suffix", trapping_rainwater_prefix_suffix), ("two pointers", trapping_rainwater_two_pointers), ("stack", trapping_rainwater_stack)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

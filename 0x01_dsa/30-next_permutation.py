#!/usr/bin/env python3


def next_permutation_brute_force(nums: list[int]) -> list[int]:
    if not nums:
        return []
    
    result = []
    n = len(nums)
    generate_permutations(result, nums[:], 0)  # Use copy to avoid modifying original
    
    result.sort()
    
    # Find current permutation's index
    for i in range(n):
        if result[i] == nums:
            if i < n - 1:
                nums[:] = result[i + 1]  # Modify input in-place
                return nums
            else:
                nums[:] = result[0]  # Wrap to first permutation
                return nums
    return nums  # If not found (shouldn't happen)

def next_permutation_generate_next(nums: list[int]) -> list[int]:
    if not nums:
        return nums
    
    n = len(nums)
    
    # Step 1: Find pivot (rightmost index i where nums[i] < nums[i+1])
    pivot = -1
    for i in range(n - 2, -1, -1):
        if nums[i] < nums[i + 1]:
            pivot = i
            break
    
    # Step 2: If no pivot (reverse sorted), reverse entire array
    if pivot == -1:
        nums.reverse()
        return nums
    
    # Step 3: Find rightmost successor > nums[pivot]
    successor = -1
    for i in range(n - 1, pivot, -1):
        if nums[i] > nums[pivot]:
            successor = i
            break
    
    # Step 4: Swap pivot with successor
    nums[pivot], nums[successor] = nums[successor], nums[pivot]
    
    # Step 5: Reverse suffix after pivot
    left, right = pivot + 1, n - 1
    while left < right:
        nums[left], nums[right] = nums[right], nums[left]
        left += 1
        right -= 1
    
    return nums

def generate_permutations(result: list, nums: list, idx: int) -> None:
    n = len(nums)
    if idx == n - 1:
        result.append(nums[:])
        return
    
    for i in range(idx, n):
        nums[idx], nums[i] = nums[i], nums[idx]
        generate_permutations(result, nums, idx + 1)
        nums[idx], nums[i] = nums[i], nums[idx]  # Backtrack


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, 4, 1, 7, 5, 0],
            "expected": [2, 4, 5, 0, 1, 7],
        },
        {
            "id": 2,
            "nums": [3, 2, 1],
            "expected": [1, 2, 3],
        },
        {
            "id": 3,
            "nums": [1, 3, 5, 4, 2],
            "expected": [1, 4, 2, 3, 5],
        },
        {
            "id": 4,
            "nums": [1, 2, 3, 4, 5],
            "expected": [1, 2, 3, 5, 4],
        },
        {
            "id": 5,
            "nums": [],
            "expected": [],
        },
        {
            "id": 6,
            "nums": [1, 2, 3, 5, 4],
            "expected": [1, 2, 4, 3, 5],
        },
        {
            "id": 7,
            "nums": [1, 2, 3, 6, 5, 4],
            "expected": [1, 2, 4, 3, 5, 6],
        },
    ]
    print("=================== 30. Next permutation problem ===================")
    for name, func in [("brute force", next_permutation_brute_force), ("generate next", next_permutation_generate_next)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

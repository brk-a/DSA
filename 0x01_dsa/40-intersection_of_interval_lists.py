#!/usr/bin/env python3

def intersection_of_interval_lists_brute_force(nums1: list[list[int]], nums2: list[list[int]]) -> list[list[int]]:
    # Filter invalid empty intervals
    nums1 = [iv for iv in nums1 if len(iv) == 2]
    nums2 = [iv for iv in nums2 if len(iv) == 2]
    
    if not nums1 or not nums2:  # No valid intervals to intersect
        return [[]]
    
    i, j = 0, 0
    n, m = len(nums1), len(nums2)
    result = []
    
    while i < n and j < m:
        left = max(nums1[i][0], nums2[j][0])
        right = min(nums1[i][1], nums2[j][1])
        
        if left <= right:
            result.append([left, right])  # Fixed: nest as list
        
        if nums1[i][1] < nums2[j][1]:
            i += 1
        else:
            j += 1
    
    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums1": [
                [0, 4], [5, 10], [13, 20], [24, 25],
            ],
            "nums2": [
                [1, 5], [8, 12], [15, 24], [25, 26],
            ],
            "expected": [
                [1, 4], [5, 5], [8, 10], [15, 20], [24, 24], [25, 25],
            ],
        },
        {
            "id": 2,
            "nums1": [
                [0, 2], [5, 10], [12, 22], [24, 25],
            ],
            "nums2": [
                [1, 4], [9, 12], [15, 24], [25, 26],
            ],
            "expected": [
                [1, 2], [9, 10], [12, 12], [15, 22], [24, 24], [25, 25],
            ],
        },
        {
            "id": 3,
            "nums1": [[]],
            "nums2": [
                [1, 5], [8, 12], [15, 24], [25, 26],
            ],
            "expected": [[]],
        },
        {
            "id": 4,
            "nums1": [
                [0, 4], [5, 10], [13, 20], [24, 25],
            ],
            "nums2": [[]],
            "expected": [[]],
        },
        {
            "id": 5,
            "nums1": [],
            "nums2": [],
            "expected": [[]],
        },
        {
            "id": 6,
            "nums1": [],
            "nums2": [
                [1, 4], [9, 12], [15, 24], [25, 26],
            ],
            "expected": [[]],
        },
        {
            "id": 7,
            "nums1": [
                [1, 4], [9, 12], [15, 24], [25, 26],
            ],
            "nums2": [],
            "expected": [[]],
        },
    ]

    print("=================== 40. Intersection of interval lists problem ===================")
    for name, func in [("brute force", intersection_of_interval_lists_brute_force)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums1"], test["nums2"])
            print(f"Test {test['id']}: nums1={test['nums1']}, nums2={test['nums2']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

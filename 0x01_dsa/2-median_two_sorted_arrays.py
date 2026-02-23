#!/usr/bin/env python3

'''
[1, 2, 3] and [1, 3, 4] return  2
'''

def median_two_sorted_arrays_brute_force(nums1, nums2) -> float:
    nums1 = nums1 if nums1 and isinstance(nums1, list) else []
    nums2 = nums2 if nums2 and isinstance(nums2, list) else []

    arr = sorted(nums1 + nums2)
    n = len(arr)

    if n > 1:
        if n % 2 == 0:
            return (arr[n//2 - 1] + arr[n//2]) / 2
        return float(arr[n // 2])
    elif n == 1:
        return float(arr[0])
    else:
        return float(-1)


def median_two_sorted_arrays_merge_sort(nums1, nums2) -> float:
    nums1 = nums1 if nums1 and isinstance(nums1, list) else []
    nums2 = nums2 if nums2 and isinstance(nums2, list) else []

    arr, i, j, m, n = [], 0, 0, len(nums1), len(nums2)
    while i < m and j < n:
        if nums1[i] < nums2[j]:
            arr.append(nums1[i])
            i += 1
        else:
            arr.append(nums2[j])
            j += 1

    arr.extend(nums1[i:])
    arr.extend(nums2[j:])

    length = len(arr)
    if n > 1:
        if n % 2 == 0:
            return (arr[n//2 - 1] + arr[n//2]) / 2
        return float(arr[n // 2])
    elif n == 1:
        return float(arr[0])
    else:
        return float(-1)

def median_two_sorted_arrays_one_pass(nums1, nums2) -> int:
    nums1 = nums1 if nums1 and isinstance(nums1, list) else []
    nums2 = nums2 if nums2 and isinstance(nums2, list) else []

    if len(nums1) > len(nums2):
        nums1, nums2 = nums2, nums1
    
    m, n = len(nums1), len(nums2)
    left, right = 0, m

    while left <= right:
        i = (left + right ) // 2
        j = (m + n + 1) // 2 - 1

        left1 = float('-inf') if i == 0 else nums1[i-1]
        right1 = float('inf') if i == m else nums1[i]
        left2 = float('-inf') if j == 0 else nums2[j-1]
        right2 = float('inf') if j == n else nums2[j]

        # Valid partition found
        if left1 <= right2 and left2 <= right1:
            # Odd total: single median
            if (m + n) % 2 == 1:
                return max(left1, left2)
            # Even total: average of two medians
            return (max(left1, left2) + min(right1, right2)) / 2.0
        
        # Adjust binary search
        elif left1 > right2:
            right = i - 1
        else:
            left = i + 1
    
    raise ValueError("Input arrays are not sorted")


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums1": [2, 4, 7, 9], 
            "nums2": [1, 3, 8],
            "expected": 4,
        },
        {
            "id": 2,
            "nums1": [3, 3],
            "nums2": [3, 4, 5], # 3, 3, 3, 4, 5
            "expected": 3,
        },
        {
            "id": 3,
            "nums1": [2, 3, 4, 6],
            "nums2": [],
            "expected": 3.5,
        },
        {
            "id": 4,
            "nums1": [],
            "nums2": [],
            "expected": -1 ,
        },
        {
            "id": 5,
            "nums1": [9],
            "nums2": [],
            "expected": 9 ,
        },
        {
            "id": 6,
            "nums1": [],
            "nums2": [9],
            "expected": 9, 
        },
    ]


    print("=================== 1. Median of two sorted arrays problem ===================")
    for name, func in [("brute force", median_two_sorted_arrays_brute_force), ("two pass", median_two_sorted_arrays_merge_sort), ("one pass", median_two_sorted_arrays_one_pass)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums1"], test["nums2"])
            print(f"Test {test['id']}: nums1={test['nums1']}, nums2={test['nums2']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

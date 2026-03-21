#!/usr/bin/env python3

def subarray_less_than_k_brute_force(nums: list[int], k: int) -> int:
    if not isinstance(nums, list) or not isinstance(k, int) or len(nums) == 0:
        return -1

    n = len(nums)
    counter = 0

    for i in range(n):
        if nums[i] < k:
            counter += 1

        mul = nums[i]
        for j in range(i + 1, n):
            mul *= nums[j]
            if mul < k:
                counter += 1
            else:
                break
    
    return counter



def subarray_less_than_k_sliding_window(nums: list[int], k: int) -> int:
    if not isinstance(nums, list) or not isinstance(k, int) or len(nums) == 0:
        return -1

    n = len(nums)
    p = 1
    result = 0
    start, end = 0, 0

    while end < n:
        p *= nums[end]
        while (start < end) and (p >= k):
            p = int(p // nums[start])
            start += 1

        if p < k:
            l = end - start + 1
            result += l

        end += 1

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 3, 4],
            "k": 10,
            "expected": 7,
        },
        {
            "id": 2,
            "nums": [1, 9, 2, 8, 6, 4, 3] ,
            "k": 100,
            "expected": 16,
        },
        {
            "id": 3,
            "nums": [10, 5, 2, 6] ,
            "k": 100,
            "expected": 8,
        },
        {
            "id": 4,
            "nums": [] ,
            "k": 100,
            "expected": -1,
        },
        {
            "id": 5,
            "nums": [10, 5, 2, 6] ,
            "k": "100",
            "expected": -1,
        },
    ]

    print("=================== 34. Sub-array less than k problem ===================")
    for name, func in [("brute force", subarray_less_than_k_brute_force), ("sliding window", subarray_less_than_k_sliding_window)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"], test["k"])
            print(f"Test {test['id']}: nums={test['nums']}, k={test["k"]} got={got}, expected={test['expected']}, passed={got == test['expected']}")

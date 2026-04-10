#!/usr/bin/env python3

from copy import deepcopy


def next_smallest_palindrome_brute_force(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    result = deepcopy(nums)

    while True:
        # step 1: add 1 to the number (always do this first)
        carry = 1
        for i in range(n - 1, -1, -1):
            result[i] += carry
            if result[i] == 10:
                result[i] = 0
                carry = 1
            else:
                carry = 0
                break

        # step 2: insert 1 at beginning of `result` if carry remains
        if carry == 1:
            result.insert(0, 1)
            n += 1

        # step 3: check if palindrome
        if check_palindrome(result):
            break

    return result

def next_smallest_palindrome_mirror_carry_propagation(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    result = deepcopy(nums)

    if are_all_nines(result):
        return [1] + [0] * (n - 1) + [1]

    next_smallest_palindrome_util(result)
    return result


def check_palindrome(nums: list[int]) -> bool:
    n = len(nums)
    for i in range(n):
        if nums[i] != nums[n - 1 - i]:
            return False
    return True


def are_all_nines(nums: list[int]) -> bool:
    for num in nums:
        if num != 9:
            return False
    return True


def next_smallest_palindrome_util(nums: list[int]) -> None:
    n = len(nums)
    mid = n // 2
    left_smaller = False
    i = mid - 1
    j = mid + 1 if n % 2 else mid

    # step 1: compare left and right sides
    while i >= 0 and nums[i] == nums[j]:
        i -= 1
        j += 1

    # step 2: check whether we need to increment the middle digit(s)
    if i < 0 or nums[i] < nums[j]:
        left_smaller = True
    
    # step 3: copy the left half to the right half
    while i >= 0:
        nums[j] = nums[i]
        i -= 1
        j += 1

    # step 4: for when middle digits need to be incremented
    if left_smaller:
        carry = 1
        i = mid - 1

        if n % 2:
            nums[mid] += carry
            carry = nums[mid] // 10
            nums[mid] %= 10
            j = mid + 1
        else:
            j = mid

        while i >= 0:
            nums[i] += carry
            carry = nums[i] // 10
            nums[i] %= 10
            nums[j] = nums[i]
            i -= 1
            j += 1


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums":  [9, 4, 1, 8, 7, 9, 7, 8, 3, 2, 2],
            "expected": [9, 4, 1, 8, 8, 0, 8, 8, 1, 4, 9],
        },
        {
            "id": 2,
            "nums": [2, 3, 5, 4, 5],
            "expected": [2, 3, 6, 3, 2],
        },
        {
            "id": 3,
            "nums": [],
            "expected": [],
        },
        {
            "id": 4,
            "nums": "list",
            "expected": [],
        },
        {
            "id": 5,
            "nums": [9, 9, 9, 9, 9],
            "expected": [1, 0, 0, 0, 0, 1],
        },
    ]

    print("=================== 55. Next smallest palindrome problem ===================")
    for name, func in [("brute force", next_smallest_palindrome_brute_force), ("mirror-carry propagation", next_smallest_palindrome_mirror_carry_propagation)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

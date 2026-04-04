#!/usr/bin/env python3


def smallest_missing_positive_number_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    nums.sort()
    result = 1

    for num in nums:
        if num == result:
            result += 1
        elif num > result:
            break

    return result


def smallest_missing_positive_number_visited_array(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    visited = [False] * n

    for i in range(n):
        if 0 <= nums[i] < n:
            visited[nums[i] - 1] = True

    for i in range(1, n + 1):
        if not visited[i - 1]:
            return i

    return n + 1


def smallest_missing_positive_number_cycle_sort(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    for i in range(n):
        while 1 <= nums[i] < n and nums[i] != nums[nums[i] - 1]:
            tmp = nums[i]
            nums[i] = nums[nums[i] - 1]
            nums[tmp - 1] = tmp

    for i in range(1, n + 1):
        if i != nums[i - 1]:
            return i

    return n + 1


def smallest_missing_positive_number_negate_arr_elems(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    k = partition(nums)
    for i in range(k):
        value = abs(nums[i])

        if value - 1 < k and nums[value - 1] > 0:
            nums[value - 1] = -nums[value - 1]

    for i in range(k):
        if nums[i] > 0:
            return i + 1

    return k + 1


def smallest_missing_positive_number_mark_indices(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    flag = False

    for i in range(n):
        if nums[i] == 1:
            flag = True
            break

    if not flag:
        return 1

    for i in range(n):
        if nums[i] <= 0 or nums[i] > n:
            nums[i] = 1

    for i in range(n):
        nums[(nums[i] - 1) % n] += n

    for i in range(n):
        if nums[i] <= n:
            return i + 1

    return n + 1 


def partition(nums: list) -> int:
    pivot_idx = 0
    n = len(nums)

    for i in range(n):
        if nums[i] > 0:
            nums[i], nums[pivot_idx] = nums[pivot_idx], nums[i]
            pivot_idx += 1

    return pivot_idx


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [2, -3, 4, 1, 1, 7],
            "expected": 3,
        },
        {
            "id": 2,
            "nums": [5, 3, 2, 5, 1],
            "expected": 4,
        },
        {
            "id": 3,
            "nums": [-8, 0, -1, -4, -3],
            "expected": 1,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
        {
            "id": 5,
            "nums": "list",
            "expected": -1,
        },
    ]

    print("=================== 49. Smallest missing positive number problem ===================")
    for name, func in [("brute force", smallest_missing_positive_number_brute_force), ("visited array", smallest_missing_positive_number_visited_array), ("cycle sort", smallest_missing_positive_number_cycle_sort), ("negate array elements", smallest_missing_positive_number_negate_arr_elems), ("mark indices", smallest_missing_positive_number_mark_indices)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

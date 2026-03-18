#!/usr/bin/env python3


def majority_element_two_brute_force(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    result = []

    for i in range(n):
        counter = 0
        for j in range(i, n):
            if nums[j] == nums[i]:
                counter += 1
        
        if counter > (n // 3):
            if len(result) == 0 or nums[i] != result[0]:
                result.append(nums[i])

        if len(result) == 2:
            if result[0] > result[1]:
                result[0], result[1] = result[1], result[0]
            break
    
    return result


def majority_element_two_hash_map(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    count_map = {}
    result = []

    for num in nums:
        count_map[num] = count_map.get(num, 0) + 1

    for num, counter in count_map.items():
        if counter > n // 3:
            result.append(num)

    if len(result) == 2 and result[0] > result[1]:
        result[0], result[1] = result[1], result[0]

    return result


def majority_element_two_boyer_moore_algo(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    elem_1, elem_2 = -1, -1
    counter_1, counter_2 = 0, 0

    for num in nums:
        if elem_1 == num:
            counter_1 += 1
        elif elem_2 == num:
            counter_2 += 1
        elif counter_1 == 0:
            elem_1 = num
            counter_1 += 1
        elif counter_2 == 0:
            elem_2 = num
            counter_2 += 1
        else:
            counter_1 -= 1
            counter_2 -= 1

    result = []
    counter_1, counter_2 = 0, 0

    for num in nums:
        if elem_1 == num:
            counter_1 += 1
        if elem_2 == num:
            counter_2 += 1

    if counter_1 > n / 3:
        result.append(elem_1)
    if counter_2 > n / 3 and elem_1 != elem_2:
        result.append(elem_2)

    if len(result) == 2 and result[0] > result[1]:
        result[0], result[1] = result[1], result[0]

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums":  [2, 2, 3, 1, 3, 2, 1, 1],
            "expected": [1, 2],
        },
        {
            "id": 2,
            "nums": [-5, 3, -5],
            "expected": [-5],
        },
        {
            "id": 3,
            "nums":  [3, 2, 2, 4, 1, 4],
            "expected": []
        },
        {
            "id": 4,
            "nums": [],
            "expected": [],
        },
        {
            "id": 5,
            "nums": [0, 1, 0, 1, 0, 1, 4, 0, 1],
            "expected": [0, 1],
        },
    ]

    print("=================== 32. Majority element problem ===================")
    for name, func in [("brute force", majority_element_two_brute_force), ("hashing", majority_element_two_hash_map), ("boyer-moore's algo", majority_element_two_boyer_moore_algo)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

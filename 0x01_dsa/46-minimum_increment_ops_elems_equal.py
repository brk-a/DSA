#!/usr/bin/env python3

def minimum_increment_operations_make_elems_equal_brute_force(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    count = 0
    nums.sort()

    while nums[0] != nums[n - 1]:
        for i in range(n - 1):
            nums[i] += 1

        count += 1
        nums.sort()

    return count


def minimum_increment_operations_make_elems_equal_direct_formula(nums: list[int]) -> int:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    total = sum(nums)
    min_value = min(nums)

    return total - n * min_value



if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 3],
            "expected": 3,
        },
        {
            "id": 2,
            "nums": [4, 3, 4],
            "expected": 2,
        },
        {
            "id": 3,
            "nums": [],
            "expected": -1,
        },
        # {
        #     "id": 4,
        #     "nums": [],
        #     "expected": ,
        # },
        # {
        #     "id": 5,
        #     "nums": [],
        #     "expected": ,
        # },
    ]

    print("=================== 46. Minimum increment operations to make array elements equal problem ===================")
    for name, func in [("brute force", minimum_increment_operations_make_elems_equal_brute_force), ("direct formula", minimum_increment_operations_make_elems_equal_direct_formula)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["nums"])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

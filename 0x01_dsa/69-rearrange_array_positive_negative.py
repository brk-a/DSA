#!/usr/bin/env python3


def rearrange_array_positive_negative_two_pointers(nums: list[int]) -> list[int]:
    if not isinstance(nums, list) or len(nums) == 0:
        return []

    n = len(nums)
    pos, neg = [], []

    # separate positive and negative numbers
    for num in nums:
        if num >= 0:
            pos.append(num)
        else:
            neg.append(num)

    pos_idx, neg_idx = 0, 0
    len_pos, len_neg = len(pos), len(neg)
    i = 0
    result = []

    # populate result array
    while pos_idx < len_pos and neg_idx < len_neg:
        if i % 2 == 0:
            result.append(pos[pos_idx])
            pos_idx += 1
        else:
            result.append(neg[neg_idx])
            neg_idx += 1

        i += 1

    # append any remaining positive numbers if any
    while pos_idx < len_pos:
        result.append(pos[pos_idx])
        pos_idx += 1
        i += 1

    # append any remaining negative numbers if any
    while neg_idx < len_neg:
        result.append(neg[neg_idx])
        neg_idx += 1
        i += 1

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [1, 2, 3, -4, -1, 4],
            "expected": [1, -4, 2, -1, 3, 4],
        },
        {
            "id": 2,
            "nums": [-5, -2, 5, 2, 4, 7, 1, 8, 0, -8],
            "expected":  [5, -5, 2, -2, 4, -8, 7, 1, 8, 0],
        },
        {
            "id": 3,
            "nums": " [5, -5, 2, -2, 4, -8, 7, 1, 8, 0]",
            "expected": [],
        },
        {
            "id": 4,
            "nums": [],
            "expected": [],
        },
    ]

    print("=================== 69. Re-arrange array positive-negative alternating problem ===================")
    for name, func in [("two pointers", rearrange_array_positive_negative_two_pointers)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

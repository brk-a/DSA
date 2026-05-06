#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


class Data:
    def __init__(self, node, start, end):
        self.node = node
        self.start = start
        self.end = end


def array_to_bst_recursion(nums: list[int]) -> int | None:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    return array_to_bst(nums, 0, len(nums) - 1)


def array_to_bst_queue(nums: list[int]) -> int | None:
    if not isinstance(nums, list) or len(nums) == 0:
        return -1

    n = len(nums)
    if n == 0:
        return None

    mid = (n - 1) // 2
    root = Node(nums[mid])
    dq = deque([Data(root, 0, n - 1)])

    while dq:
        data = dq.popleft()
        current = data.node
        st, en = data.start, data.end
        mid = (st + en) // 2

        if st < mid:
            left_val = (st + mid -1) // 2
            left = Node(nums[left_val])
            current.left = left
            dq.append(Data(left, st, mid - 1))

        if en > mid:
            right_val = (mid + 1 + en) // 2
            right = Node(nums[right_val])
            current.right = right
            dq.append(Data(right, mid + 1, en))

    return root.val


def array_to_bst(nums: list[int], start: int, end: int) -> int | None:
    if start > end:
        return None

    mid = start + (end - start) // 2
    root = Node(nums[mid])

    root.left = array_to_bst(nums, start, mid - 1)
    root.right = array_to_bst(nums, mid + 1, end)

    return root.val


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "nums": [10, 20, 30],
            "expected": 20,
        },
        {
            "id": 2,
            "nums":  [1, 5, 9, 14, 23, 27],
            "expected": 9,
        },
        {
            "id": 3,
            "nums": "[1, 4, 5, 3, 2]",
            "expected": -1,
        },
        {
            "id": 4,
            "nums": [],
            "expected": -1,
        },
    ]

    print("=================== 81. Array to BST problem ===================")
    for name, func in [("DFS: recursion", array_to_bst_recursion), ("Queue", array_to_bst_queue)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['nums'])
            print(f"Test {test['id']}: nums={test['nums']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

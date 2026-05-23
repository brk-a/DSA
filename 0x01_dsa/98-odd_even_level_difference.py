#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def odd_even_level_difference_recursion(root: Node) -> None | int:
    if not isinstance(root, Node):
        return None

    odd_sum, even_sum = [0], [0]
    get_level_diff_recursion(root, 1, odd_sum, even_sum)

    return odd_sum[0] - even_sum[0]


def odd_even_level_difference_level_order_traversal(root: Node) -> None | int:
    if not isinstance(root, Node):
        return None

    if root is None:
        return 0

    dq = deque()
    dq.append(root)
    odd_sum, even_sum = 0, 0
    is_odd = True

    while dq:
        size = len(dq)
        for i in range(size):
            current = dq.popleft()

            if is_odd:
                odd_sum += current.val
            else:
                even_sum += current.val

            if current.left:
                dq.append(current.left)
            if current.right:
                dq.append(current.right)
        is_odd = not is_odd

    return odd_sum - even_sum


def get_level_diff_recursion(root: Node, level: int, odd_sum: list[int], even_sum: list[int]) -> None:
    if root is None:
        return

    if level % 2 != 0:
        odd_sum[0] += root.val
    else:
        even_sum[0] += root.val

    get_level_diff_recursion(root.left, level + 1, odd_sum, even_sum)
    get_level_diff_recursion(root.right, level + 1, odd_sum, even_sum)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(10),
            "expected": 60,
        },
        {
            "id": 2,
            "root": Node(5),
            "expected": 11,
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": 2,
        },
    ]

    print("=================== 98. odd-even level difference problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(20)
            test['root'].right = Node(30)
            test['root'].left.left = Node(40)
            test['root'].left.right = Node(60)
            # test['root'].right.left = Node(6)
            # test['root'].right.right = Node(7)
            # test['root'].left.right.left = Node(8)
            # test['root'].left.right.right = Node(9)
        if test['id'] == 2:
            test['root'].left = Node(4)
            test['root'].right = Node(-5)
            test['root'].left.left = Node(-4)
            test['root'].left.right = Node(4)
            test['root'].right.right = Node(5)
        if test['id'] == 3:
            test['root'].left = Node(8)
            test['root'].right = Node(18)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(11)

    for name, func in [("recursion", odd_even_level_difference_recursion), ("level-order traversal", odd_even_level_difference_level_order_traversal)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

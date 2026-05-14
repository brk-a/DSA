#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def single_valued_subtree_top_down(root: Node) -> None | int:
    if not isinstance(root, Node):
        return None

    if root is None:
        return 0

    count = 0
    dq = deque([root])

    while dq:
        current = dq.popleft()
        if is_uni_value(current, current.val):
            count += 1

        if current.left:
            dq.append(current.left)
        if current.right:
            dq.append(current.right)

    return count


def single_valued_subtree_bottom_up(root: Node) -> None | int:
    if not isinstance(root, Node):
        return None

    count = [0]
    count_single(root, count)

    return count[0]


def is_uni_value(root: Node, value: int) -> bool:
    if root is None:
        return True

    if root.val != value:
        return False

    return (is_uni_value(root.left, value) and is_uni_value(root.right, value))


def count_single(root: Node, count: list[int]) -> bool:
    if root is None:
        return True

    left = count_single(root.left, count)
    right = count_single(root.right, count)

    if not left or not right:
        return False

    if root.left is not None and root.val != root.left.val:
        return False

    if root.right is not None and root.val != root.right.val:
        return False

    count[0] += 1

    return True


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(10),
            "expected": 3,
        },
        {
            "id": 2,
            "root": Node(5),
            "expected": 5,
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": 3,
        },
    ]

    print("=================== 89. Single-valued sub-tree problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(20)
            test['root'].right = Node(30)
            test['root'].left.left = Node(40)
            test['root'].left.right = Node(50)
            test['root'].right.left = Node(60)
        if test['id'] == 2:
            test['root'].left = Node(4)
            test['root'].right = Node(5)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(4)
            test['root'].right.right = Node(5)
        if test['id'] == 3:
            test['root'].left = Node(8)
            test['root'].right = Node(18)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(11)

    for name, func in [("top=down", single_valued_subtree_top_down), ("Bottom-up", single_valued_subtree_bottom_up)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

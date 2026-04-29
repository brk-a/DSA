#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def height_of_binary_tree_recursion(root: Node) -> int:
    if not isinstance(root, Node) or not root:
        return -1

    left_height = height_of_binary_tree_recursion(root.left)
    right_height = height_of_binary_tree_recursion(root.right)

    height = max(left_height, right_height) + 1

    return height


def height_of_binary_tree_level_order_traversal(root: Node) -> int:
    if not isinstance(root, Node) or not root:
        return -1

    dq = deque([root])
    depth = 0

    while dq:
        level_size = len(dq)

        for _ in range(level_size):
            current = dq.popleft()
            if current.left:
                dq.append(current.left)
            if current.right:
                dq.append(current.right)

        depth += 1

    height = depth - 1

    return height


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(12),
            "expected": 2,
        },
        {
            "id": 2,
            "root": Node(1),
            "expected": 2,
        },
        {
            "id": 3,
            "root": Node(120),
            "expected": 2,
        },
    ]

    print("=================== 74. Height of binary tree problem ===================")
    for name, func in [("recursion", height_of_binary_tree_recursion), ("level-order traversal", height_of_binary_tree_level_order_traversal)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            test['root'].left = Node(8)
            test['root'].right = Node(18)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(11)
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

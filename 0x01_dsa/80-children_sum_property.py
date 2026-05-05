#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def children_sum_property_recursion(root: Node) -> int | None:
    if not isinstance(root, Node) or root.val == None:
        return None

    if root is None or (root.left is None and root.right is None):
        return -1

    total = 0
    if root.left is not None:
        total += root.left.val
    if root.right is not None:
        total += root.right.val

    return 1 if (root.val == total and children_sum_property_recursion(root.left) and children_sum_property_recursion(root.right)) else 0


def children_sum_property_queue(root: Node) -> int | None:
    if not isinstance(root, Node) or root.val == None:
        return None

    if root is None:
        return 1

    dq = deque([root])
    while dq:
        current = dq.popleft()

        if current.left is None and current.right is None:
            continue

        total = 0
        if current.left is not None:
            total += current.left.val
        if current.right is not None:
            total += current.right.val

        if current.val != total:
            return 0

        if current.left is not None:
            dq.append(current.left)
        if current.right is not None:
            dq.append(current.right)

    return 1


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(35),
            "expected": 1,
        },
        {
            "id": 2,
            "root": Node(2),
            "expected": 0,
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": 0,
        },
    ]

    print("=================== 79. Children sum property problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(20)
            test['root'].right = Node(15)
            test['root'].left.left = Node(15)
            test['root'].left.right = Node(5)
            test['root'].right.left = Node(10)
            test['root'].right.right = Node(5)
        if test['id'] == 2:
            test['root'].left = Node(4)
            test['root'].right = Node(5)
        if test['id'] == 3:
            test['root'].left = Node(8)
            test['root'].right = Node(4)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(7)

    for name, func in [("DFS: recursion", children_sum_property_recursion), ("Queue", children_sum_property_queue)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

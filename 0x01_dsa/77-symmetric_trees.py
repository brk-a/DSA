#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def symmetric_trees_recursion(root: Node) -> bool | None:
    if not isinstance(root, Node) or root.val == None:
        return None

    if root is None:
        return True

    return is_mirror(root.left, root.right)


def symmetric_trees_stack(root: Node) -> bool | None:
    if not isinstance(root, Node) or root.val == None:
        return None

    if root is None:
        return True

    s1, s2 = [], []
    s1.append(root.left)
    s2.append(root.right)

    while s1 and s2:
        node1, node2 = s1.pop(), s2.pop()
        
        if node1 is None and node2 is None:
            continue

        if node1 is None or node2 is None or node1.val != node2.val:
            return False

        s1.append(node1.left)
        s2.append(node1.right)

        s1.append(node1.right)
        s2.append(node1.left)

    return len(s1) == 0 and len(s2) == 0


def symmetric_trees_queue(root: Node) -> bool | None:
    if not isinstance(root, Node) or root.val == None:
        return None

    if root is None:
        return True

    dq = deque()
    dq.append(root.left)
    dq.append(root.right)

    while dq:
        node1 = dq.popleft()
        node2 = dq.popleft()

        if node1 is None and node2 is None:
            continue

        if node1 is None or node2 is None or node1.val != node2.val:
            return False

        dq.append(node1.left)
        dq.append(node2.right)
        dq.append(node1.right)
        dq.append(node2.left)

    return True


def is_mirror(left_sub: Node, right_sub: Node) -> bool:
    if left_sub is None and right_sub is None:
        return True

    if left_sub is None or right_sub is None or left_sub.val != right_sub.val:
        return False

    return is_mirror(left_sub.left, right_sub.right) and is_mirror(left_sub.right, right_sub.left)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(10),
            "expected": False,
        },
        {
            "id": 2,
            "root": Node(12),
            "expected": False,
        },
        {
            "id": 3,
            "root": Node(4),
            "expected": True,
        },
    ]

    print("=================== 76. Mirror tree problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(5)
            test['root'].right = Node(5)
            test['root'].left.left = Node(2)
            test['root'].left.right = Node(2)
        if test['id'] == 2:
            test['root'].left = Node(8)
            test['root'].right = Node(18)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(11)
        if test['id'] == 3:
            test['root'].left = Node(1)
            test['root'].right = Node(1)
            test['root'].left.left = Node(-1)
            test['root'].left.right = Node(-1)

    for name, func in [("DFS: recursion", symmetric_trees_recursion), ("Stack", symmetric_trees_queue), ("Queue", symmetric_trees_stack)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

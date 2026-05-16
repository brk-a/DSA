#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def zig_zag_traversal_recursion(root: Node) -> list[int] | None:
    if not isinstance(root, Node):
        return None

    result = list()
    left_to_right = True
    height = tree_height(root)

    for i in range(1, height + 1):
        if left_to_right:
            left_to_right_traversal(root, i, result)
        else:
            right_to_left_traversal(root, i, result)

        left_to_right = not left_to_right

    return result


def zig_zag_traversal_two_stacks(root: Node) -> list[int] | None:
    if not isinstance(root, Node):
        return None

    result = list()
    if root is None:
        return result

    stack_1, stack_2 = list(), list() # current level and next level
    stack_1.append(root)

    while stack_1 or stack_2:
        while stack_1:
            current = stack_1.pop()
            result.append(current.val)

            if current.left:
                stack_2.append(current.left)
            if current.right:
                stack_2.append(current.right)

        while stack_2:
            current = stack_2.pop()
            result.append(current.val)

            if current.left:
                stack_2.append(current.left)
            if current.right:
                stack_2.append(current.right)

    return result


def zig_zag_traversal_deque(root: Node) -> list[int] | None:
    if not isinstance(root, Node):
        return None

    result = []
    if not root:
        return result
        
    dq = deque()
    dq.append(root)
    reverse = False

    while dq:
        n = len(dq)
        for _ in range(n):
            if reverse:
                current = dq.pop()
                result.append(current.val)

                if current.right:
                    dq.appendleft(current.right)
                if current.left:
                    dq.appendleft(current.left)
            else:
                current = dq.popleft()
                result.append(current.val)

                if current.left:
                    dq.appendleft(current.left)
                if current.right:
                    dq.appendleft(current.right)

        reverse = not reverse

    return result


def tree_height(root: Node) -> int:
    if root is None:
        return 0

    left_height = tree_height(root.left)
    right_height = tree_height(root.right)

    return max(left_height, right_height) + 1


def left_to_right_traversal(root: Node, level: int, result: list[int]) -> None:
    if root is None:
        return

    if level == 1:
        result.append(root.val)
    else:
        left_to_right_traversal(root.right, level - 1, result)
        left_to_right_traversal(root.left, level -1, result)


def right_to_left_traversal(root: Node, level: int, result: list[int]) -> None:
    if root is None:
        return

    if level == 1:
        result.append(root.val)
    else:
        right_to_left_traversal(root.right, level - 1, result)
        right_to_left_traversal(root.left, level -1, result)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(20),
            "expected": [20, 22, 8, 11, 12, 4, 14, 10],
        },
        {
            "id": 2,
            "root": Node(5),
            "expected": [5, 5, 4, 5, 4, 4],
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": [12, 18, 8, 11, 5],
        },
    ]

    print("=================== 91. Zig-zag traversal of a BST problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(8)
            test['root'].right = Node(22)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(12)
            test['root'].right.right = Node(11)
            test['root'].left.right.left = Node(10)
            test['root'].left.right.right = Node(14)
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

    for name, func in [("recursion", zig_zag_traversal_recursion), ("Two stacks", zig_zag_traversal_two_stacks), ("Deque", zig_zag_traversal_deque)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

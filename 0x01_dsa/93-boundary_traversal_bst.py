#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def boundary_traversal_bst_recursion(root: Node) -> None | list[int]:
    if not isinstance(root, Node):
        return None

    result = list()
    if not root:
        return []

    if not is_leaf(root):
        result.append(root.val)

    collect_left_recursion(root.left, result)
    collect_leaves_recursion(root, result)
    collect_right_recursion(root.right, result)

    return result


def boundary_traversal_bst_iteration(root: Node) -> None | list[int]:
    if not isinstance(root, Node):
        return None

    result = list()
    if root is None:
        return result

    if not is_leaf(root):
        result.append(root.val)

    collect_left_iteration(root.left, result)
    collect_leaves_iteration(root, result)
    collect_right_iteration(root.right, result)

    return result


def is_leaf(root: Node) -> bool:
    return root.left is None and root.right is None


def collect_left_recursion(root: Node, result: list) -> None:
    if root is None or is_leaf(root):
        return

    result.append(root.val)
    if root.left:
        collect_left_recursion(root.left, result)
    elif root.right:
        collect_left_recursion(root.right, result)


def collect_leaves_recursion(root: Node, result: list) -> None:
    if root is None:
        return

    if is_leaf(root):
        result.append(root.val)
        return

    collect_leaves_recursion(root.left, result)
    collect_leaves_recursion(root.right, result)


def collect_right_recursion(root: Node, result: list) -> None:
    if root is None or is_leaf(root):
        return

    if root.right:
        collect_right_recursion(root.right, result)
    elif root.left:
        collect_right_recursion(root.left, result)

    result.append(root.val)


def collect_left_iteration(root: Node, result: list) -> None:
    if root is None:
        return

    current = root
    while current is not None and not is_leaf(current):
        result.append(current.val)
        if current.left is not None:
            current = current.left
        else:
            current = current.right

def collect_leaves_iteration(root: Node, result: list) -> None:
    current = root
    while current:
        if current.left is None:
            if current.right is None:
                result.append(current.val)
            current = current.right
        else:
            predecessor = current.left
            while predecessor.right and predecessor.right != current:
                predecessor = predecessor.right

            if predecessor.right is None:
                predecessor.right = current
                current = current.left
            else:
                if predecessor.left is None:
                    result.append(predecessor.val)

                predecessor.right = None
                current = current.right


def collect_right_iteration(root: Node, result: list) -> None:
    if root is None:
        return

    current = root
    tmp = list()
    while not is_leaf(current):
        tmp.append(current.val)

        if current.right is not None:
            current = current.right
        else:
            current = current.left

    for i in reversed(tmp):
        result.append(i)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(1),
            "expected": [1, 2, 4, 8, 9, 6, 7, 3],
        },
        {
            "id": 2,
            "root": Node(5),
            "expected": [5, 4, -4, 4, 5, -5],
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": [12, 8, 5, 11, 18],
        },
    ]

    print("=================== 92. Vertical traversal of a BST problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(2)
            test['root'].right = Node(3)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(5)
            test['root'].right.left = Node(6)
            test['root'].right.right = Node(7)
            test['root'].left.right.left = Node(8)
            test['root'].left.right.right = Node(9)
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

    for name, func in [("recursion", boundary_traversal_bst_recursion), ("iteration w. morris traversal", boundary_traversal_bst_iteration)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

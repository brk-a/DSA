#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def tree_is_balanced_depth_first(root: Node) -> bool | None:
    if not isinstance(root, Node) or root.val == None:
        return None

    if root is None:
        return True

    left_height = get_height(root.left)
    right_height = get_height(root.right)

    if abs(left_height - right_height) > 1:
        return False

    return tree_is_balanced_depth_first(root.left) and tree_is_balanced_depth_first(root.right)


def tree_is_balanced_one_pass(root: Node) -> bool | None:
    if not isinstance(root, Node) or root.val == None:
        return None

    return is_balanced(root) > 0


def get_height(root: Node) -> int:
    if root is None:
        return 0

    return 1 + max(get_height(root.left), get_height(root.right))


def is_balanced(root: Node) -> int:
    if root is None:
        return 0

    left_height = is_balanced(root.left)
    right_height = is_balanced(root.right)

    if left_height == -1 or right_height == -1 or abs(left_height - right_height) > 1:
        return -1

    return max(left_height, right_height) + 1


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(10),
            "expected": False,
        },
        {
            "id": 2,
            "root": Node(2),
            "expected": True,
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": False,
        },
    ]

    print("=================== 78. Tree is balanced problem ===================")
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
        if test['id'] == 3:
            test['root'].left = Node(8)
            test['root'].right = Node(18)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(11)

    for name, func in [("DFS: recursion", tree_is_balanced_depth_first), ("One pass", tree_is_balanced_one_pass)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

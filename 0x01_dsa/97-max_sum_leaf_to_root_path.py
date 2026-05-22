#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def max_sum_leaf_to_root_path_brute_force(root: Node) -> None | int:
    if not isinstance(root, Node):
        return None

    parent = dict()
    mx = [0]
    calculate_sum(root, mx, parent)

    return mx[0]


def max_sum_leaf_to_root_path_track_max_sum(root: Node) -> None | int:
    if not isinstance(root, Node):
        return None

    if root is None:
        return 0

    max_sum = [-float('inf')]
    find_max_sum(root, 0, max_sum)

    return max_sum[0]


def calculate_sum(root: Node, mx: list, parent: dict) -> None:
    if root.left is None and root.right is None:
        mx[0] = max(mx[0], get_sum_of_path(root, parent))
        return

    if root.left:
        parent[root.left] = root
        calculate_sum(root.left, mx, parent)

    if root.right:
        parent[root.right] = root
        calculate_sum(root.right, mx, parent)


def get_sum_of_path(root: Node, parent: dict) -> int:
    if root not in parent:
        return root.val

    return root.val + get_sum_of_path(parent[root], parent)


def find_max_sum(root: Node, curr_sum: int, max_sum: list) -> None:
    if root is None:
        return

    curr_sum += root.val
    if root.left is None and root.right is None:
        if curr_sum > max_sum[0]:
            max_sum[0] = curr_sum

    find_max_sum(root.left, curr_sum, max_sum)
    find_max_sum(root.right, curr_sum, max_sum)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(10),
            "expected": 17,
        },
        {
            "id": 2,
            "root": Node(5),
            "expected": 13,
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": 31,
        },
    ]

    print("=================== 97. Maximum sum of leaf to root path of a BST problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(-2)
            test['root'].right = Node(7)
            test['root'].left.left = Node(8)
            test['root'].left.right = Node(-4)
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

    for name, func in [("recursion: brute force", max_sum_leaf_to_root_path_brute_force), ("recursion: track max sum using Kadane algo", max_sum_leaf_to_root_path_track_max_sum)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

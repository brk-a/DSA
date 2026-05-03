#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def flip_binary_tree_recursion(root: Node) -> int | None:
    """Flip the binary tree (invert children) and return new root's value."""
    if not isinstance(root, Node) or root.val is None:
        return None

    def invert(node: Node) -> Node:
        if node is None:
            return None

        # swap left and right
        node.left, node.right = node.right, node.left

        # recurse on both subtrees
        invert(node.left)
        invert(node.right)

        return node

    new_root = invert(root)
    return new_root.val if new_root.val else None


def flip_binary_tree_iteration(root: Node) -> int | None:
    """Flip the binary tree using BFS/iteration and return new root's value."""
    if not isinstance(root, Node) or root.val is None:
        return None

    stack = [root]
    while stack:
        node = stack.pop()

        # swap children
        node.left, node.right = node.right, node.left

        # push non‑None children to continue
        if node.left:
            stack.append(node.left)
        if node.right:
            stack.append(node.right)

    return root.val


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(1),
            "expected": 4,
        },
        {
            "id": 2,
            "root": Node(2),
            "expected": 4,
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": 5,
        },
    ]

    print("=================== 77. Flip binary tree problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(2)
            test['root'].right = Node(3)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(5)
            test['root'].right.left = Node(6)
            test['root'].right.right = Node(7)
        if test['id'] == 2:
            test['root'].left = Node(4)
            test['root'].right = Node(5)
        if test['id'] == 3:
            test['root'].left = Node(8)
            test['root'].right = Node(18)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(11)

    for name, func in [("DFS: recursion", flip_binary_tree_recursion), ("BFS: iteration", flip_binary_tree_iteration)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

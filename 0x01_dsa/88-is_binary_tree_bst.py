#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def is_binary_tree_bst_min_max(root: Node) -> None | bool:
    if not isinstance(root, Node):
        return None

    return is_bst_min_max(root, float('-inf'), float('inf'))


def is_binary_tree_bst_in_order_traversal(root: Node) -> None | bool:
    if not isinstance(root, Node):
        return None

    prev = [float('-inf')]

    return is_bst_inorder(root, prev)


def is_binary_tree_bst_morris_traversal(root: Node) -> None | bool:
    if not isinstance(root, Node):
        return None

    current = root
    prev = float('-inf')

    while current:
        if current.left is None:
            if current.val <= prev:
                return False
            prev = current.val
            current = current.right
        else:
            pred = current.left
            while pred.right and pred.right != current:
                pred = pred.right

            if pred.right is None:
                pred.right = current
                current = current.left
            else:
                pred.right = None
                if current.val <= prev:
                    return False
                prev = current.val
                current = current.right

    return True


def is_bst_min_max(root: Node, min_val: float, max_val: float) -> bool:
    if root is None:
        return True

    # If the current node's data 
    # is not in the valid range, return false
    if root.val < min_val or root.val > max_val:
        return False

    # Recursively check the left and 
    # right subtrees with updated ranges
    return (is_bst_min_max(root.left, min_val, root.val - 1) and is_bst_min_max(root.right, root.val + 1, max_val))


def is_bst_inorder(root: Node, prev: list) -> bool:
    if root is None:
        return True

    if not is_bst_inorder(root.left, prev):
        return False

    if prev[0] > root.val:
        return False

    prev[0] = root.val

    return is_bst_inorder(root.right, prev)


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
            "expected": False,
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": True,
        },
    ]

    print("=================== 88. Is binary tree a BST problem ===================")
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

    for name, func in [("Min-max specified range", is_binary_tree_bst_min_max), ("In-order traversal", is_binary_tree_bst_in_order_traversal), ("Morris traversal", is_binary_tree_bst_morris_traversal)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def remove_bst_keys_outside_the_given_range_postorder_traversal(root: Node, left: int, right: int) -> None | Node:
    if not isinstance(root, Node) or not isinstance(left, int) or not isinstance(right, int):
        return None

    if root is None:
        return None

    le = remove_bst_keys_outside_the_given_range_postorder_traversal(root.left, left, right)
    ri = remove_bst_keys_outside_the_given_range_postorder_traversal(root.right, left, right)

    if left <= root.val <= right:
        root.left = le
        root.right = ri
        
        return root

    elif root.val < left:
        return ri
    else:
        return le


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(1),
            "left": -10,
            "right": 17,
            "expected": 1,
        },
        {
            "id": 2,
            "root": Node(5),
            "left": -5,
            "right": -4,
            "expected": -4,
        },
        {
            "id": 3,
            "root": Node(12),
            "left": 11,
            "right": 18,
            "expected": 12,
        },
    ]

    print("=================== 101. Remove BST keys outside the given range problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(-13)
            test['root'].right = Node(14)
            # test['root'].left.left = Node(4)
            test['root'].left.right = Node(-8)
            test['root'].right.left = Node(13)
            test['root'].right.right = Node(15)
            # test['root'].left.right.left = Node(8)
            # test['root'].left.right.right = Node(9)
            test['root'].right.left.left = Node(7)
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

    for name, func in [("recursion: post-order", remove_bst_keys_outside_the_given_range_postorder_traversal)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'], test['left'], test['right'])
            print(f"Test {test['id']}: root={test['root'].val}, left={test['left']}, right={test['right']}, got={got.val if got is not None else None}, expected={test['expected']}, passed={got.val == test['expected'] if got is not None else False}")

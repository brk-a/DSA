#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def in_order_successor_bst_property(root: Node, k: Node) -> None | int:
    if not isinstance(root, Node) or not isinstance(k, Node):
        return None

    successor = None
    while root:
        if root.val <= k.val:
            root = root.right
        else:
            successor = root
            root = root.left

    if successor is None:
        return -1

    return successor.val


def in_order_successor_reverse_inorder(root: Node, k: Node, last: list = [None]) -> None | int:
    if not isinstance(root, Node) or not root.val or not isinstance(k, int):
        return None

    if not root:
        return None

    successor = in_order_successor_reverse_inorder(root.right, k, last)
    if successor:
        return successor.val

    if root.val == k.val:
        return last[0].val
    
    last[0] = root

    return in_order_successor_reverse_inorder(root.left, k, last)
    


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(20),
            "k": Node(8),
            "expected": 10,
        },
        {
            "id": 2,
            "root": Node(1),
            "k": Node(4),
            "expected": 2,
        },
        {
            "id": 3,
            "root": Node(20),
            "k": Node(24),
            "expected": 1,
        },
    ]

    print("=================== 83. In-order successor of node on BST problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(8)
            test['root'].right = Node(22)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(12)
            test['root'].left.right.left = Node(10)
            test['root'].left.right.right = Node(14)
        if test['id'] == 2:
            test['root'].left = Node(2)
            test['root'].right = Node(3)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(5)
            test['root'].right.left = Node(6)
            test['root'].right.right = Node(7)
        if test['id'] == 3:
            test['root'].left = Node(7)
            test['root'].right = Node(24)
            test['root'].left.left = Node(4)
            test['root'].right.left = Node(1)

    for name, func in [("DFS: recursion", in_order_successor_bst_property), ("check BST property", in_order_successor_bst_property)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'], test['k'])
            print(f"Test {test['id']}: root={test['root'].val}, k={test['k'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

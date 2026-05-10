#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def store_in_order(root: Node, nodes: list[Node]) -> None:
    if root is None:
        return None

    store_in_order(root.left, nodes)
    nodes.append(root.val)
    store_in_order(root.right, nodes)


def balance_a_tree(nodes: list[Node], start: int, end: int) -> None | Node:
    if start > end:
        return None

    mid = (start + end) // 2
    root = Node(nodes[mid])
    root.left = balance_a_tree(nodes, start, mid - 1)
    root.right = balance_a_tree(nodes, mid + 1, end)

    return root


def balance_a_bst_inorder_traversal(root: Node) -> None | Node:
    if not isinstance(root, Node):
        return None

    nodes = []
    store_in_order(root, nodes)
    n = len(nodes)

    return balance_a_tree(nodes, 0, n - 1)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(35),
            "expected": 3,
        },
        {
            "id": 2,
            "root": Node(2),
            "expected": 2,
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": 7,
        },
    ]

    print("=================== 85. Balance a BST problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(20)
            test['root'].right = Node(16)
            test['root'].left.left = Node(15)
            test['root'].left.right = Node(3)
            test['root'].right.left = Node(5)
        if test['id'] == 2:
            test['root'].left = Node(4)
            test['root'].right = Node(5)
            test['root'].right.left = Node(10)
        if test['id'] == 3:
            test['root'].left = Node(8)
            test['root'].right = Node(4)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(7)

    for name, func in [("In-order traversal into an array", balance_a_bst_inorder_traversal)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got.val}, expected={test['expected']}, passed={got.val == test['expected']}")

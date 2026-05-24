#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def lca_of_bst_brute_force(root: Node, node_1: Node, node_2: Node) -> None | int:
    if not isinstance(root, Node) or not isinstance(node_1, Node) or not isinstance(node_2, Node):
        return None

    if root is None:
        return None

    if has_node(root.left, node_1) and has_node(root.right, node_2):
        return root.val
    if has_node(root.left, node_2) and has_node(root.right, node_1):
        return root.val

    lca_left = lca_of_bst_brute_force(root.left, node_1, node_2)
    lca_right = lca_of_bst_brute_force(root.right, node_1, node_2)

    if lca_left is not None:
        return lca_left
    elif lca_right is not None:
        return lca_right

    return None


def lca_of_bst_sort_from_root(root: Node, node_1: Node, node_2: Node) -> None | int:
    if not isinstance(root, Node) or not isinstance(node_1, Node) or not isinstance(node_2, Node):
        return None

    path_1, path_2 = list(), list()
    if not find_path(root, path_1, node_1) or not find_path(root, path_2, node_2):
        return None

    i = 0
    m, n = len(path_1), len(path_2)
    while i < m and i < n:
        if path_1[i] != path_2[i]:
            return path_1[i - 1]
        i += 1

    return path_1[i - 1]


def lca_of_bst_single_traversal(root: Node, node_1: Node, node_2: Node) -> None | int:
    if not isinstance(root, Node) or not isinstance(node_1, Node) or not isinstance(node_2, Node):
        return None

    if not root:
        return None

    if root == node_1 or root == node_2:
        return root

    lca_left = lca_of_bst_single_traversal(root.left, node_1, node_2)
    lca_right = lca_of_bst_single_traversal(root.right, node_1, node_2)

    if lca_left == lca_right:
        return root

    if lca_left is not None:
        return lca_left
    elif lca_right is not None:
        return lca_right

    return None


def has_node(root: Node, node: Node) -> bool:
    if root is None:
        return False

    return root == node or has_node(root.left, node) or has_node(root.right, node)


def find_path(root: Node, path: list[Node], node: Node) -> bool:
    if root is None:
        return False
    
    path.append(root)
    if root == node or find_path(root.left, path, node) or find_path(root.right, path, node):
        return True

    path.pop()

    return False


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(1),
            "node_1": Node(7),
            "node_2": Node(8),
            "expected": 3,
        },
        {
            "id": 2,
            "root": Node(5),
            "node_1": Node(-5),
            "node_2": Node(-4),
            "expected": 5,
        },
        {
            "id": 3,
            "root": Node(12),
            "node_1": Node(11),
            "node_2": Node(18),
            "expected": 12,
        },
    ]

    print("=================== 99. Least common ancestor of two nodes in BST problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(2)
            test['root'].right = Node(3)
            # test['root'].left.left = Node(4)
            # test['root'].left.right = Node(5)
            test['root'].right.left = Node(6)
            test['root'].right.right = Node(7)
            # test['root'].left.right.left = Node(8)
            # test['root'].left.right.right = Node(9)
            test['root'].right.left.left = Node(8)
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

    for name, func in [("recursion: brute force", lca_of_bst_brute_force), ("recursion: store paths from root", lca_of_bst_sort_from_root), ("recursion: one pass", lca_of_bst_single_traversal)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'], test['node_1'], test['node_2'])
            print(f"Test {test['id']}: root={test['root'].val}, node 1={test['node_1'].val}, node 2={test['node_2'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

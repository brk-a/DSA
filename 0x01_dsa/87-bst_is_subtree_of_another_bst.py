#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def bst_is_subtree_of_another_bst_preorder_traversal(root1: Node, root2: Node) -> None | bool:
    if not isinstance(root1, Node) or not isinstance(root2, Node):
        return None

    # empty sub-tree always returns True
    if root2 is None:
        return True

    # main tree is empty: return False
    if root1 is None:
        return False

    # return Truw if match found at current node
    if is_identical(root1, root2):
        return True

    # search left and right otherwise
    return (bst_is_subtree_of_another_bst_preorder_traversal(root1.left, root2) or bst_is_subtree_of_another_bst_preorder_traversal(root1.right, root2))


def bst_is_subtree_of_another_bst_substring_matching(root1: Node, root2: Node) -> None | bool:
    if not isinstance(root1, Node) or not isinstance(root2, Node):
        return None

    s1, s2 = list(), list()

    # serialise trees
    serialise_tree(root1, s1)
    serialise_tree(root2, s2)

    # convert to string
    string_1 = "".join(s1)
    string_2 = "".join(s2)

    return string_2 in string_1


def bst_is_subtree_of_another_bst_kmp_algo(root1: Node, root2: Node) -> None | bool:
    if not isinstance(root1, Node) or not isinstance(root2, Node):
        return None

    s1, s2 = list(), list()

    # serialise trees
    serialise_tree(root1, s1)
    serialise_tree(root2, s2)

    # convert to string
    string_1 = "".join(s1)
    string_2 = "".join(s2)

    return kmp_search(string_1, string_2)


def is_identical(root1: Node, root2: Node) -> bool:
    # Both nodes are null → identical
    if root1 is None and root2 is None:
        return True

    # One is null → not identical
    if root1 is None or root2 is None:
        return False

    # Check current node and recurse on children
    return root1.val == root2.val and (is_identical(root1.left, root2.left) and is_identical(root1.right, root2.right))


def serialise_tree(root: Node, s: list) -> None:
    # Null node; add marker, `#`
    if root is None:
        s.append(" #")
        return

    # Add current node
    s.append(" " + str(root.val))

    # Recurse on left and right
    serialise_tree(root.left, s)
    serialise_tree(root.right, s)


def kmp_search(text: str, pattern: str) -> bool:
    lps = build_lps(pattern)

    i, j = 0, 0
    m, n = len(text), len(pattern)

    while i < m:
        # Match found: move both
        if text[i] == pattern[j]:
            i += 1
            j += 1

        # Full match found
        if j == n:
            return True
        # Mismatch after match
        elif i < m and text[i] != pattern[j]:
            if j != 0:
                j = lps[j -1]
            else:
                i += 1

    return False


def build_lps(pattern: str) -> list:
    n = len(pattern)
    lps = [0] * n
    length = 0
    i = 1

    while i < n:
        if pattern[i] == pattern[length]:
            length += 1
            lps[i] = length
            i += 1
        else:
            if length != 0:
                length = lps[length - 1]
            else:
                i += 1

    return lps


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root1": Node(26),
            "root2": Node(10),
            "expected": True,
        },
        {
            "id": 2,
            "root1": Node(12),
            "root2": Node(5),
            "expected": False,
        },
        {
            "id": 3,
            "root1": Node(120),
            "root2": Node(0),
            "expected": False,
        },
    ]

    print("=================== 87. BST is sub-tree of another BST problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root1'].left = Node(10)
            test['root1'].right = Node(3)
            test['root1'].left.left = Node(4)
            test['root1'].left.right = Node(6)
            test['root1'].right.right = Node(3)
            test['root1'].left.left.right = Node(30)
            test['root2'].left = Node(4)
            test['root2'].right = Node(6)
            test['root2'].left.right = Node(30)
        if test['id'] == 2:
            test['root1'].left = Node(8)
            test['root1'].right = Node(18)
            test['root1'].left.left = Node(5)
            test['root1'].left.right = Node(11)
            test['root1'].left.left.left = Node(20)
            test['root1'].left.left.right = Node(25)
            test['root1'].left.left.right.right = Node(35)
            test['root2'].left = Node(20)
            test['root2'].right = Node(25)
            test['root2'].left.right = Node(35)
        if test['id'] == 3:
            test['root1'].left = Node(8)
            test['root1'].right = Node(18)
            test['root1'].left.left = Node(5)
            test['root1'].left.right = Node(11)
            test['root2'].left = Node(8)
            test['root2'].right = Node(18)
            test['root2'].left.left = Node(5)
            test['root2'].left.right = Node(11)

    for name, func in [("Pre-order traversal", bst_is_subtree_of_another_bst_preorder_traversal), ("Sub-string matching", bst_is_subtree_of_another_bst_substring_matching), ("KMP algo", bst_is_subtree_of_another_bst_kmp_algo)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root1'], test['root2'])
            print(f"Test {test['id']}: root1={test['root1'].val}, root2={test['root2'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

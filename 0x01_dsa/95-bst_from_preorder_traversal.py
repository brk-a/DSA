#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def bst_from_preorder_traversal_one_by_one_insert(preorder: list[int]) -> None | list:
    if not isinstance(preorder, list):
        return []

    root = None
    result = list()
    for x in preorder:
        root = insert_bst_one_by_one(root, x)
        result.append(post_order(root))

    return result


def bst_from_preorder_traversal_first_greater_than_root(preorder: list[int]) -> None | list:
    if not isinstance(preorder, list):
        return []

    n = len(preorder)

    construct_bst(preorder, 0, n - 1)
    result = list()

    # TODO: append vals to list

    return result


def bst_from_preorder_traversal_one_pass_recursion(preorder: list[int]) -> None | list:
    if not isinstance(preorder, list):
        return []

    # TODO: create this


def bst_from_preorder_traversal_stack(preorder: list[int]) -> None | list:
    if not isinstance(preorder, list):
        return []

    # TODO: create this


def post_order(root: Node) -> int:
    if root is None:
        return -1

    post_order(root.left)
    post_order(root.right)

    return root.val


def insert_bst_one_by_one(root: Node, key: int) -> Node:
    new_node = Node(key)
    if root is None:
        return new_node

    current = root
    parent = None
    while current is not None:
        parent = current
        if key < current.val:
            current = current.left
        else:
            current = current.right

    if key < parent.val:
        parent.left = new_node
    else:
        parent.right = new_node

    return root


def construct_bst(preorder: list, low: int, high: int) -> None | Node:
    if low > high:
        return None

    root = Node(preorder[low])
    if low == high:
        return root

    i = low + 1
    while i <= high and preorder[i] <= root.val:
        i += 1

    root.left = construct_bst(preorder, low + 1, i - 1)
    root.right = construct_bst(preorder, i, high)

    return root

# TODO: write tests
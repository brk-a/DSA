#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def bt_from_inorder_preorder_traversals_preorder(inorder: list[int], preorder: list[int]) -> list[list[int]]:
    if not isinstance(inorder, list) or not isinstance(preorder, list):
        return [[]]

    pre_index = [0]
    n = len(preorder)
    return build_tree_recursion(inorder, preorder, pre_index, 0, n - 1)


def bt_from_inorder_preorder_traversals_preorder_hashmap(inorder: list[int], preorder: list[int]) -> list[list[int]]:
    if not isinstance(inorder, list) or not isinstance(preorder, list):
        return [[]]

    mp = {value: idx for idx, value in enumerate(inorder)}
    preIndex = [0]
    m = len(inorder)
    
    return build_tree_recursion_map(mp, preorder, preIndex, 0, m - 1)


def build_tree_recursion(inorder: list, preorder: list, pre_index: list, left: int, right: int) -> None | Node:
    if left > right:
        return None

    root_val = preorder[pre_index[0]]
    pre_index[0] += 1
    root = Node(root_val)
    idx = search(inorder, root_val, left, right)
    root.left = build_tree_recursion(inorder, preorder, pre_index, left, idx - 1)
    root.right = build_tree_recursion(inorder, preorder, pre_index, idx + 1, right)

    return root


def search(inorder: list, value: int, left: int, right: int) -> int:
    for i in range(left, right + 1):
        if inorder[i] == value:
            return i

    return -1


def get_height(root: Node, h: int) -> int:
    if root is None:
        return h - 1

    return max(get_height(root.left, h + 1), get_height(root.right, h + 1))


def level_order(root: Node) -> list[int]:
    dq = deque(([root, 0]))
    last_level = 0
    height = get_height(root, 0)

    while dq:
        rt, lvl = dq.popleft()
        if lvl > last_level:
            print()
            last_level = lvl
        if lvl > height:
            break

        print("N" if rt.val is -1 else rt.val, end=" ")

        if rt.val == -1:
            continue
        if rt.left is None:
            dq.append([Node(-1), lvl + 1])
        else:
            dq.append([rt.left, lvl + 1])
        if rt.right is None:
            dq.append([Node(-1), lvl + 1])
        else:
            dq.append([rt.right, lvl + 1])


def build_tree_recursion_map(mp: dict, preorder: list, pre_index: list, left: int, right: int) -> None | Node:
    if left > right:
        return None

    root_val = preorder[pre_index[0]]
    pre_index[0] += 1
    root = Node(root_val)
    idx = mp[root_val]

    root.left = build_tree_recursion_map(mp, preorder, preIndex, left, index - 1)
    root.right = build_tree_recursion_map(mp, preorder, preIndex, index + 1, right)

    return root


# TODO: TESTS HERE

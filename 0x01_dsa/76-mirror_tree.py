#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def mirror_tree_recursion(root: Node) -> int | None:
    if not isinstance(root, Node) or not root.val:
        return None

    mirror_tree_recursion(root.left)
    mirror_tree_recursion(root.right)

    root.left, root.right = root.right, root.left

    return root.val



def mirror_tree_iteration(root: Node) -> int | None:
    if not isinstance(root, Node) or not root.val:
        return None

    dq = deque([root])

    # Traverse the tree, level by level
    while dq:
        curr = dq.popleft()

        # Swap the left and right subtree
        curr.left, curr.right = curr.right, curr.left

        # Push the left and right node to the queue
        if curr.left:
            dq.append(curr.left)
        if curr.right:
            dq.append(curr.right)

    return root.val


def get_height(root: Node, h: int) -> int:
    if root is None:
        return h - 1
    return max(get_height_recursion(root.left, h + 1), get_height_recursion(root.right, h + 1))


def level_order(root: Node) -> None:
    dq = deque([[root, 0]])
    last_level = 0

    # function to get the height of tree
    height = get_height(root, 0)

    # printing the level order of tree
    while dq:
        node, lvl = dq.popleft()

        if lvl > last_level:
            print()
            last_level = lvl

        # all levels are printed
        if lvl > height:
            break

        # printing null node
        print("N" if node.data == -1 else node.data, end=" ")

        # null node has no children
        if node.data == -1:
            continue

        if node.left is None:
            dq.append([Node(-1), lvl + 1])
        else:
            dq.append([node.left, lvl + 1])

        if node.right is None:
            dq.append([Node(-1), lvl + 1])
        else:
            dq.append([node.right, lvl + 1])


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(1),
            "expected": 1,
        },
        {
            "id": 2,
            "root": Node(12),
            "expected": 12,
        },
        {
            "id": 3,
            "root": Node(4),
            "expected": 4,
        },
    ]

    print("=================== 76. Mirror tree problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(2)
            test['root'].right = Node(3)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(5)
        if test['id'] == 2:
            test['root'].left = Node(8)
            test['root'].right = Node(18)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(11)
        if test['id'] == 3:
            test['root'].left = Node(5)
            test['root'].right = Node(4)
            test['root'].left.left = Node(3)
            test['root'].left.right = Node(2)

    for name, func in [("DFS: recursion", mirror_tree_recursion), ("BFS: iteration", mirror_tree_iteration)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

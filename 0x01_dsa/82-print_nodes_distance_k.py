#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def print_nodes_distance_k_recursion(root: Node, target: Node, k: int) -> None | list:
    if not isinstance(root, Node) or not isinstance(target, Node) or not isinstance(parent, Node):
        return None

    result = []
    nodes_distance_k_recursion(root, target, k, ans)

    result.sort()

    return result


def print_nodes_distance_k_parent_pointers(root: Node, target: int, k: int) -> None | list:
    if not isinstance(root, Node) or not isinstance(target, Node) or not isinstance(parent, Node):
        return None

    result = []
    if not root:
        return result

    parent = dict()
    parent[root] = None
    tar = find_target_node(root, target, parent)
    dfs(tar, None, k, parent, result)

    result.sort()

    return result


def nodes_distance_k_recursion(root: Node, target: int, k: int, result: list) -> int:
    if root is not None:
        return -1

    if root.val == target:
        find_nodes(root, k, result)
        return 1

    left = nodes_distance_k_recursion(root.left, target, k, result)
    if left != -1:
        if k - left == 0:
            result.append(root.val)
        else:
            nodes_distance_k_recursion(root.right, k - left - 1, result)
        return left + 1

    right = nodes_distance_k_recursion(root.right, target, k, result)
    if right != -1:
        if k - right == 0:
            result.append(root.val)
        else:
            nodes_distance_k_recursion(root.left, target, k - right - 1, result)
        return right + 1

    return -1


def find_nodes(root: Node, dis: int, result: list) -> None:
    if root is None:
        return

    if dis == 0:
        result.append(root.val)
        return

    find_nodes(root.left, dis - 1, result)
    find_nodes(root.right, dis - 1, result)


def find_target_node(root: Node, target: int, parent: dict) -> Node:
    left = right = None
    if root.left is not None:
        parent[root.left] = root
        left = find_target_node(root.left, target, parent)

    if root.right is not None:
        parent[root.right] = root
        right = find_target_node(root.right, target, parent)

    if root.val == target:
        return root
    elif left:
        return left

    return right


def dfs(root: Node, prev: Node, k: int, parent: dict, result: list) -> None:
    if not root:
        return

    if root.left != prev:
        dfs(root.left, root, k - 1, parent, result)
    if root.right != prev:
        dfs(root.right, root, k - 1, parent, result)
    if parent.get(root) != prev:
        dfs(parent[root], root, k - 1, parent, result)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(20),
            "target": 2,
            "k": 1,
            "expected": [1, 24],
        },
        {
            "id": 2,
            "root": Node(12),
            "target": 4,
            "k": 1,
            "expected": [1, 24],
        },
        {
            "id": 3,
            "root": Node(20),
            "target": 2,
            "k": 1,
            "expected": [1, 24],
        },
    ]

    print("=================== 81. Print nodes k distance from target problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(7)
            test['root'].right = Node(24)
            test['root'].left.left = Node(4)
            test['root'].right.left = Node(1)
        if test['id'] == 2:
            test['root'].left = Node(20)
            test['root'].right = Node(15)
            test['root'].left.left = Node(15)
            test['root'].left.right = Node(5)
            test['root'].right.left = Node(10)
            test['root'].right.right = Node(5)
        if test['id'] == 3:
            test['root'].left = Node(7)
            test['root'].right = Node(24)
            test['root'].left.left = Node(4)
            test['root'].right.left = Node(1)

    for name, func in [("DFS: recursion", print_nodes_distance_k_recursion), ("DFS: parent pointers", print_nodes_distance_k_parent_pointers)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'], test['target'], test['k'])
            print(f"Test {test['id']}: root={test['root'].val},target={test['target']}, k={test['k']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3

from collections import defaultdict
from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def vertical_traversal_bst_brute_force(root: Node) -> None | list[list[int]]:
    if not isinstance(root, Node):
        return None

    result = []
    min_max = {
        "min": 0,
        "max": 0,
    }
    find_min_max(root, min_max, 0)

    for line in range(min_max["min"], min_max["max"] + 1):
        vertical_nodes = []
        collect_vertical_line(root, line, vertical_nodes)
        result.append(vertical_nodes)

    return result


def vertical_traversal_bst_dfs_sorting(root: Node) -> None | list[list[int]]:
    if not isinstance(root, Node):
        return None

    vertical_lines = defaultdict(list)
    perform_dfs(root, 0, 0, vertical_lines)
    result = list()

    for hd in sorted(vertical_lines.keys()):
        lines = vertical_lines[hd]
        lines.sort(key = lambda x: x[1])
        lin = []
        for rt in lines:
            lin.append(rt[0])
        result.append(lin)

    return result


def vertical_traversal_bst_bfs(root: Node) -> None | list[list[int]]:
    if not isinstance(root, Node):
        return None

    if root is None:
        return [[]]

    lst = defaultdict(list)
    dq = deque([(root, 0)])
    mn, mx = 0, 0

    while dq:
        rt, lvl = dq.popleft()
        mn = min(mn, lvl)
        mx = max(mx, lvl)

        lst[lvl].append(rt.val)
        if rt.left:
            dq.append((rt.left, lvl - 1))
        if rt.right:
            dq.append((rt.right, lvl + 1))

    result = list()
    for i in range(mn, mx + 1):
        result.append(lst[i])

    return result


def find_min_max(root: Node, min_max: dict, hd: int) -> None:
    if root is None:
        return

    if hd < min_max["min"]:
        min_max["min"] = hd
    elif hd > min_max["max"]:
        min_max["max"] = hd

    find_min_max(root.left, min_max, hd - 1)
    find_min_max(root.right, min_max, hd + 1)


def collect_vertical_line(root: Node, dist: int, line: list) -> None:
    hd = 0
    dq = deque()
    dq.append((root, 0))

    while dq:
        root, hd = dq.popleft()
        if hd == dist:
            line.append(root.val)
        
        if root.left:
            dq.append((root.left, hd - 1))
        if root.right:
            dq.append((root.right, hd + 1))


def perform_dfs(root: Node, hd: int, lvl: int, vertical_lines: list) -> None:
    if root is None:
        return

    vertical_lines[hd].append((root.val, lvl))

    perform_dfs(root.left, hd - 1, lvl + 1, vertical_lines)
    perform_dfs(root.right, hd + 1, lvl + 1, vertical_lines)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(1),
            "expected": [[4], [2], [1, 5, 6, 11], [3, 8, 9], [7], [10]],
        },
        {
            "id": 2,
            "root": Node(5),
            "expected": [[4], [4], [5, 4], [5], [5]],
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": [[5], [8], [12, 11], [18]],
        },
    ]

    print("=================== 92. Vertical traversal of a BST problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(2)
            test['root'].right = Node(3)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(5)
            test['root'].right.left = Node(6)
            test['root'].right.right = Node(7)
            test['root'].left.right.right = Node(8)
            test['root'].right.left.right = Node(9)
            test['root'].right.right.right = Node(10)
            test['root'].left.right.right.left = Node(11)
        if test['id'] == 2:
            test['root'].left = Node(4)
            test['root'].right = Node(5)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(4)
            test['root'].right.right = Node(5)
        if test['id'] == 3:
            test['root'].left = Node(8)
            test['root'].right = Node(18)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(11)

    for name, func in [("brute force: vertical line traversal", vertical_traversal_bst_brute_force), ("DFS w. sorting", vertical_traversal_bst_dfs_sorting), ("BFS", vertical_traversal_bst_bfs)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def min_dist_between_two_nodes_lca_path_length(root: Node, val_a: int, val_b: int) -> int:
    if not isinstance(root, Node) or not isinstance(val_a, int) or not isinstance(val_b, int):
        return -1

    d_1, d_2 = [-1], [-1]
    dist = [0]

    #find LCA and calc distance
    lca = find_lca_and_dist(root, val_a, val_b, d_1, d_2, dist, 1)

    if d_1[0] != -1:
        dist[0] = find_level(lca, val_b, 0)
        return dist[0]

    if d_2[0] != -1:
        dist[0] = find_level(lca, val_a, 0)
        return dist[0]

    return -1


def min_dist_between_two_nodes_lca_brute_force(root: Node, val_a: int, val_b: int) -> int:
    if not isinstance(root, Node) or not isinstance(val_a, int) or not isinstance(val_b, int):
        return -1

    lca = find_lca(root, val_a, val_b)
    d_1 = find_level_returns_int(lca, val_a, 0)
    d_2 = find_level_returns_int(lca, val_b, 0)

    return d_1 + d_2


def min_dist_between_two_nodes_lca_one_pass(root: Node, val_a: int, val_b: int) -> int:
    if not isinstance(root, Node) or not isinstance(val_a, int) or not isinstance(val_b, int):
        return -1

    dist = [0]
    find_distance(root, val_a, val_b, dist)

    return dist[0]


def find_lca_and_dist(root: Node, val_a: int, val_b: int, d_1: list[int], d_2: list[int], dist: list[int], lvl: int) -> None | Node:
    if root is None:
        return None
    
    if root.val == val_a:
        d_1[0] = lvl
        return root

    if root.val == val_b:
        d_2[0] = lvl
        return root

    left = find_lca_and_dist(root.left, val_a, val_b, d_1, d_2, dist, lvl + 1)
    right = find_lca_and_dist(root.right, val_a, val_b, d_1, d_2, dist, lvl + 1)
    if left is not None and right is not None:
        dist[0] = d_1[0] + d_2[0] - 2 * lvl

    if left is not None:
        return left
    else:
        return right


def find_level(root: Node, k: int, lvl: int) -> int:
    if root is None:
        return -1
    if root.val == k:
        return lvl

    left_lvl = find_level(root.left, k, lvl + 1)
    if left_lvl != -1:
        return left_lvl
    else:
        return find_level(root.right, k, lvl + 1)


def find_lca(root: Node, val_a: int, val_b: int) -> None | Node:
    if root is None:
        return root

    if root.val == val_a or root.val == val_b:  
        return root

    left = find_lca(root.left, val_a, val_b)
    right = find_lca(root.right, val_a, val_b)

    if left is not None and right is not None:
        return root

    if left is None and right is None:
        return None

    return left if left is not None else right


def find_level_returns_int(root: Node, k: int, lvl: int) -> int:
    if root is None:
        return -1
    if root.val == k:
        return lvl

    left_lvl = find_level(root.left, k, lvl + 1)
    if left_lvl == -1:
        return find_level(root.right, k, lvl + 1)
        
    return left_lvl


def find_distance(root: Node, val_a: int, val_b: int, dist: list[int]) -> tuple:
    if not root:
        return (False, 0)

    left = find_distance(root.left, val_a, val_b, dist)
    right = find_distance(root.right, val_a, val_b, dist)

    current = (root.val == val_a or root.val == val_b)

    if current and (left[0] or right[0]):
        dist[0] = max(left[1], right[1])
        return (False, 0)

    if left[0] and right[0]:
        dist[0] = left[1] + right[1]
        return (False, 0)

    if left[0] or right[0] or current:
        return (True, max(left[1], right[1]) + 1)

    return (False, 0)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(1),
            "val_a": 4,
            "val_b": 7,
            "expected": 4,
        },
        {
            "id": 2,
            "root": Node(5),
            "val_a": -5,
            "val_b": 5,
            "expected": 1,
        },
        {
            "id": 3,
            "root": Node(12),
            "val_a": 4,
            "val_b": 7,
            "expected": -1,
        },
    ]

    print("=================== 95. Minimum distance between two nodes problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(2)
            test['root'].right = Node(3)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(5)
            test['root'].right.left = Node(6)
            test['root'].right.right = Node(7)
            # test['root'].left.right.left = Node(8)
            # test['root'].left.right.right = Node(9)
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

    for name, func in [("LCA w. path length", min_dist_between_two_nodes_lca_path_length), ("LCA brute force", min_dist_between_two_nodes_lca_brute_force), ("LCA one pass", min_dist_between_two_nodes_lca_one_pass)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'], test['val_a'], test['val_b'])
            print(f"Test {test['id']}: root={test['root'].val}, val a={test['val_a']}, val b={test['val_b']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3

from collections import deque


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def trees_are_identical_dfs(root1: Node, root2: Node) -> bool:
    if not isinstance(root1, Node) or isinstance(root2, Node):
        return False

    if root1 is None and root2 is None:
        return True

    if root1 is None or root2 is None:
        return False

    return (root1.val == root2.val and trees_are_identical_dfs(root1.left,  root2.left) and trees_are_identical_dfs(root1.right, root2.right))


def trees_are_identical_bfs(root1: Node, root2: Node) -> bool:
    if not isinstance(root1, Node) or isinstance(root2, Node):
        return False

    if root1 is None and root2 is None:
        return True

    if root1 is None or root2 is None:
        return False

    dq_1, dq_2 = deque(), deque()
    dq_1.append(root1)
    dq_2.append(root2)

    while dq_1 and dq_2:
        node_1 = dq_1.popleft()
        node_2 = dq_2.popleft()

        if node_1.val != node_2.val:
            return False

        if node_1.left and node_2.left:
            dq_1.append(node_1.left)
            dq_2.append(node_2.left)
        elif node_1.left or node_2.left:
            return False

        if node_1.right and node_2.right:
            dq_1.append(node_1.right)
            dq_2.append(node_2.right)
        elif node_1.right or node_2.right:
            return False

    return not dq_1 and not dq_2


def trees_are_identical_morris(root1: Node, root2: Node) -> bool:
    if not isinstance(root1, Node) or isinstance(root2, Node):
        return False

    if root1 is None and root2 is None:
        return True

    if root1 is None or root2 is None:
        return False

    while root1 is not None and root2 is not None:
        if root1.val != root2.val:
            return False

        if root1.left is None:
            root1 = root1.right
        else:
            predecesor = root1.left
            while predecesor is not None and predecesor.right != root1:
                predecesor = predecesor.right

            if predecesor.right is None:
                predecesor.right = root1
                root1 = root1.left
            else:
                predecesor.right = None
                root1 = root1.right

        if root2.left is None:
            root2 = root2.right
        else:
            predecesor = root2.left
            while predecesor.right is not None and predecesor.right != root2:
                predecesor = predecesor.right

            if predecesor.right is None:
                predecesor.right = root2
                root2 = root2.left
            else:
                predecesor.right = None
                root2 = root2.right

    return root1 is None and root2 is None


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root1": Node(12),
            "root2": Node(12),
            "expected": True,
        },
        {
            "id": 2,
            "root1": Node(12),
            "root2": Node(1),
            "expected": False,
        },
        {
            "id": 3,
            "root1": Node(120),
            "root2": Node(0),
            "expected": False,
        },
    ]

    print("=================== 75. Trees are identical problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root1'].left = Node(8)
            test['root1'].right = Node(18)
            test['root1'].left.left = Node(5)
            test['root1'].left.right = Node(11)
            test['root2'].left = Node(8)
            test['root2'].right = Node(18)
            test['root2'].left.left = Node(5)
            test['root2'].left.right = Node(11)
        if test['id'] == 2:
            test['root1'].left = Node(8)
            test['root1'].right = Node(18)
            test['root1'].left.left = Node(5)
            test['root1'].left.right = Node(11)
            test['root2'].left = Node(2)
            test['root2'].right = Node(3)
            test['root2'].left.left = Node(4)
        if test['id'] == 3:
            test['root1'].left = Node(8)
            test['root1'].right = Node(18)
            test['root1'].left.left = Node(5)
            test['root1'].left.right = Node(11)
            # test['root2'].left = Node(8)
            # test['root2'].right = Node(18)
            # test['root2'].left.left = Node(5)
            # test['root2'].left.right = Node(11)

    for name, func in [("DFS", trees_are_identical_dfs), ("BFS", trees_are_identical_bfs), ("morris traversal", trees_are_identical_morris)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root1'], test['root2'])
            print(f"Test {test['id']}: root1={test['root1'].val}, root2={test['root2'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

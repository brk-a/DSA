#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def ancestors_in_binary_tree_preorder(root: Node, key: int) -> None | list[int]:
    if not isinstance(root, Node) or not isinstance(key, int):
        return None

    result = list()
    find_ancestors_preorder(root, key, result)

    return result


def ancestors_in_binary_tree_postorder(root: Node, key: int) -> None | list[int]:
    if not isinstance(root, Node) or not isinstance(key, int):
        return None

    result = list()
    if root is None:
        return result

    stack = list()
    current = root
    previous = None
    while current is not None or len(stack) > 0:
        if current is not None:
            stack.append(current)
            current = current.left
        else:
            top_node = stack[-1]
            if top_node.right is not None and previous != top_node.right:
                current = top_node.right
            else:
                if top_node.val == key:
                    stack.pop()
                    while len(stack) > 0:
                        result.append(stack[-1].val)
                        stack.pop()
                    return result
                previous = stack[-1]
                stack.pop()

    return result


def find_ancestors_preorder(root: Node, key: int, result: list[int]) -> None | bool:
    if root is None:
        return None

    if root.val == key:
        return True

    left = find_ancestors_preorder(root.left, key, result)
    right = find_ancestors_preorder(root.right, key, result)

    if left or right:
        result.append(root.val)

    return left or right


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(1),
            "key": 7,
            "expected": [4, 2, 1],
        },
        {
            "id": 2,
            "root": Node(5),
            "key": 4,
            "expected": [5],
        },
        {
            "id": 3,
            "root": Node(12),
            "key": 11,
            "expected": [12, 8],
        },
    ]

    print("=================== 100. Ancestors in binary tree problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(2)
            test['root'].right = Node(3)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(5)
            # test['root'].right.left = Node(6)
            # test['root'].right.right = Node(7)
            test['root'].left.left.left = Node(7)
            # test['root'].left.right.right = Node(9)
            # test['root'].right.left.left = Node(8)
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

    for name, func in [("recursion: pre-order traversal", ancestors_in_binary_tree_preorder), ("recursion: post-order traversal w. stack", ancestors_in_binary_tree_postorder)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'], test['key'])
            print(f"Test {test['id']}: root={test['root'].val}, key={test['key']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

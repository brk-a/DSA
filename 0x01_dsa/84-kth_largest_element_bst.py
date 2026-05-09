#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def kth_largest_element_bst_morris_traversal(root: Node, k: int) -> None | int:
    if not isinstance(root, Node) or not isinstance(k, int):
        return None

    if root is None:
        return -1

    current, count = root, 0
    while current is not None:
        if current.right is None:
            count += 1

            if count == k:
                return current.val
            current = current.left
        else:
            successor = current.right
            while successor.left is not None and successor.left != current:
                successor = successor.left
            if successor.left is None:
                successor.left = current
                current = current.right
            else:
                count += 1
                successor.left = None

                if count == k:
                    return current.val
                
                current = current.left

    return -1


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(20),
            "k": 3,
            "expected": 14,
        },
        {
            "id": 2,
            "root": Node(1),
            "k": 2,
            "expected": 3,
        },
        {
            "id": 3,
            "root": Node(20),
            "k": 9,
            "expected": -1,
        },
    ]

    print("=================== 84. K-th largest element on BST problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(8)
            test['root'].right = Node(22)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(12)
            test['root'].left.right.left = Node(10)
            test['root'].left.right.right = Node(14)
        if test['id'] == 2:
            test['root'].left = Node(2)
            test['root'].right = Node(3)
            test['root'].left.left = Node(4)
            test['root'].left.right = Node(5)
            test['root'].right.left = Node(6)
            test['root'].right.right = Node(7)
        if test['id'] == 3:
            test['root'].left = Node(7)
            test['root'].right = Node(24)
            test['root'].left.left = Node(4)
            test['root'].right.left = Node(1)

    for name, func in [("morris traversal", kth_largest_element_bst_morris_traversal),]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'], test['k'])
            print(f"Test {test['id']}: root={test['root'].val}, k={test['k']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

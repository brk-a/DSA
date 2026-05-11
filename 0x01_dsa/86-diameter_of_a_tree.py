#!/usr/bin/env python3


class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


def diameter_of_a_tree_height_of_each_node(root: Node) -> int:
    if not isinstance(root, Node) or root.val is None:
        return -1

    if root is None:
        return 0

    left_height = get_height(root.left)
    right_height = get_height(root.right)

    left_diameter = diameter_of_a_tree_height_of_each_node(root.left)
    right_diameter = diameter_of_a_tree_height_of_each_node(root.right)

    return max(left_height + right_height, left_diameter, right_diameter)


def diameter_of_a_tree_single_traversal(root: Node) -> int:
    if not isinstance(root, Node) or root.val is None:
        return -1

    global max_diameter
    max_diameter = 0

    get_diameter(root)

    return max_diameter


def get_height(root: Node) -> int:
    if root is None:
        return 0

    return 1 + max(get_height(root.left), get_height(root.right))



def get_diameter(root: Node) -> int:
    global max_diameter
    if root is None:
        return 0

    left_height = get_diameter(root.left)
    right_height = get_diameter(root.right)

    max_diameter = max(max_diameter, left_height + right_height)

    return 1 + max(left_height, right_height)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "root": Node(35),
            "expected": 4,
        },
        {
            "id": 2,
            "root": Node(2),
            "expected": 2,
        },
        {
            "id": 3,
            "root": Node(12),
            "expected": 3,
        },
        {
            "id": 4,
            "root": Node(None),
            "expected": -1,
        },
    ]

    print("=================== 86. Diameter of a tree problem ===================")
    for test in tests:
        if test['id'] == 1:
            test['root'].left = Node(20)
            test['root'].right = Node(15)
            test['root'].left.left = Node(15)
            test['root'].left.right = Node(5)
            test['root'].right.left = Node(10)
            test['root'].right.right = Node(5)
        if test['id'] == 2:
            test['root'].left = Node(4)
            test['root'].right = Node(5)
        if test['id'] == 3:
            test['root'].left = Node(8)
            test['root'].right = Node(4)
            test['root'].left.left = Node(5)
            test['root'].left.right = Node(7)

    for name, func in [("Height of neach node", diameter_of_a_tree_height_of_each_node), ("Single traversal", diameter_of_a_tree_single_traversal)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['root'])
            print(f"Test {test['id']}: root={test['root'].val}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

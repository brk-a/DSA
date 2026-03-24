#!/usr/bin/env python3

def last_moment_before_ants_fall_off_plank_brute_force(k: int, left: list[int], right: list[int]) -> int:
    if not isinstance(k, int) or not isinstance(left, list) or not isinstance(right, list):
        return -1

    m, n = len(left), len(right)
    result = 0

    for i in range(m):
        result = max(result, left[i])

    for i in range(n):
        result = max(result, k - right[i])

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "k": 4,
            "left": [2],
            "right": [0, 1, 3],
            "expected": 4,
        },
        {
            "id": 2,
            "k": 4,
            "left": [],
            "right": [0, 1, 2, 3, 4],
            "expected": 4,
        },
        {
            "id": 3,
            "k": 3,
            "left": [0],
            "right": [3],
            "expected": 0,
        },
        {
            "id": 4,
            "k": "4",
            "left": [2],
            "right": [0, 1, 3],
            "expected": -1,
        },
        {
            "id": 5,
            "k": 4,
            "left": "list",
            "right": [0, 1, 3],
            "expected": -1,
        },
        {
            "id": 1,
            "k": 4,
            "left": [2],
            "right": "list",
            "expected": -1,
        },
    ]

    print("=================== 38. Last moment before ants fall off plank problem ===================")
    for name, func in [("brute force",last_moment_before_ants_fall_off_plank_brute_force)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["k"], test["left"], test["right"])
            print(f"Test {test['id']}: k={test['k']}, left={test['left']}, right={test['right']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

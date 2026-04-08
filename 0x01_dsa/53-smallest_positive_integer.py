#!/usr/bin/env python3

import sys
import heapq


class Node:
    def __init__(self, value, row, col):
        self.value = value
        self.row = row
        self.col = col

    def __lt__(self, other):
        return self.value < other.value


def smallest_range_with_elems_k_pointers(matrix: list[list[int]]) -> list[int]:
    if not isinstance(matrix, list) or len(matrix[0]) == 0:
        return []

    k = len(matrix)
    n = len(matrix[0])
    ptr = [0] * k
    min_range = float('inf')
    start, end = -1, -1

    while True:
        min_val, max_val = float('inf'), float('-inf')
        min_row = -1

        for i in range(k):
            if ptr[i] == n:
                return [start, end]

            if matrix[i][ptr[i]] < min_val:
                min_val = matrix[i][ptr[i]]
                min_row = i

            if matrix[i][ptr[i]] > max_val:
                max_val = matrix[i][ptr[i]]

        if max_val - min_val < min_range:
            min_range = max_val - min_val
            start, end = min_val, max_val

        ptr[min_row] += 1

    return []


def smallest_range_with_elems_brute_force(matrix: list[list[int]]) -> list[int]:
    if not isinstance(matrix, list) or len(matrix[0]) == 0:
        return []

    pass


def smallest_range_with_elems_min_heap(matrix: list[list[int]]) -> list[int]:
    if not isinstance(matrix, list):
        return []

    N, K = len(matrix), len(matrix[0])
    if K == 0:
        return []
    
    pq = []
    max_val = -sys.maxsize

    for i in range(N):
        heapq.heappush(pq, Node(matrix[i][0], i, 0))
        max_val = max(max_val, matrix[i][0])

    min_range = sys.maxsize
    min_elem, max_elem = 0, 0
    while True:
        curr = heapq.heappop(pq)
        min_val = curr.value

        if max_val - min_val < min_range:
            min_range = max_val - min_val
            min_elem = min_val
            max_elem = max_val

        if curr.col + 1 == K:
            break

        next_val = matrix[curr.row][curr.col + 1]
        heapq.heappush(pq, Node(next_val, curr.row, curr.col + 1))
        max_val = max(max_val, next_val)

    return [min_elem, max_elem]


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "matrix": [[4, 7, 9, 12, 15], [0, 8, 10, 14, 20], [6, 12, 16, 30, 50]],
            "expected": [6, 8],
        },
        {
            "id": 2,
            "matrix": [[2, 4 ], [1, 7 ], [20, 40]],
            "expected": [4, 20],
        },
        {
            "id": 3,
            "matrix": [[], [2, 3, 4], []],
            "expected": [],
        },
        {
            "id": 4,
            "matrix": [[]],
            "expected": [],
        },
        {
            "id": 5,
            "matrix": "list",
            "expected": [],
        },
    ]

    print("=================== 53. Smallest range with elements from k sorted lists problem ===================")
    for name, func in [("k pointers", smallest_range_with_elems_k_pointers), ("minimum heap", smallest_range_with_elems_min_heap)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['matrix'])
            print(f"Test {test['id']}: matrix={test['matrix']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")


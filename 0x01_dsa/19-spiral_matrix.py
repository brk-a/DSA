#!/usr/bin/env python3

def spiral_matrix_visited_matrix(matrix: list[list[int]]) -> list[int]:
    if not isinstance(matrix, list) or len(matrix) == 0 or (len(matrix) == 1 and len(matrix[0]) == 0):
        return []

    m = len(matrix)
    n = len(matrix[0])

    result = []
    visited = [[False] * n for _ in range(m)]

    dir_row = [0, 1, 0, -1] # 1 = left; -1 = right
    dir_col = [1, 0, -1, 0] # 1 = up; -1 = down

    row, col = 0, 0 # initial position on matrix
    idx = 0 # initial direction; zero means right

    for _ in range(m * n):
        result.append(matrix[row][col])
        visited[row][col] = True

        new_row, new_col = row+dir_row[idx], col+dir_col[idx]

        if 0 <= new_row < m and 0 <= new_col < n and not visited[new_row][new_col]:
            row, col = new_row, new_col
        else:
            idx = (idx + 1) % 4
            row += dir_row[idx]
            col += dir_col[idx]

    return result


def spiral_matrix_boundary_traversal(matrix: list[list[int]]) -> list[int]:
    if not isinstance(matrix, list) or len(matrix) == 0 or (len(matrix) == 1 and len(matrix[0]) == 0):
        return []

    m, n = len(matrix), len(matrix[0])
    result = []
    top, bottom, left, right = 0, m-1, 0, n-1

    while top <= bottom and left <= right:
        # top row from left to right
        for i in range(left, right+1):
            result.append(matrix[top][i])
        top += 1

        # right column from top to bottom
        for i in range(top, bottom+1):
            result.append(matrix[i][right])
        right -= 1

        # bottom row from right to left (if exists)
        if top <= bottom:
            for i in range(right, left-1, -1):
                result.append(matrix[bottom][i])
            bottom -= 1

        # left column from bottom to top (if exists)
        if left <= right:
            for i in range(bottom, top-1, -1):
                result.append(matrix[i][left])
            left += 1

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "matrix": [
                [1,   2,   3,   4,],
                [5, 6, 7,   8,],
                [9, 10, 11, 12],
                [13,  14,  15, 16,],
            ],
            "expected": [1, 2, 3, 4, 8, 12, 16, 15, 14, 13, 9, 5, 6, 7, 11, 10],
        },
        {
            "id": 2,
            "matrix": [
                [1,   2,   3,   4,  5,   6],
                [7,   8,   9,  10,  11,  12],
                [13,  14,  15, 16,  17,  18],
            ],
            "expected": [1, 2, 3, 4, 5, 6, 12, 18, 17, 16, 15, 14, 13, 7, 8, 9, 10, 11],
        },
        {
            "id": 3,
            "matrix": [[]],
            "expected": [],
        },
        {
            "id": 4,
            "matrix": [
                [1, 2, 3],
                [4, 5, 6],
                [7, 8, 9],
            ],
            "expected": [1, 2, 3, 6, 9, 8, 7, 4, 5],
        },
    ]

    print("=================== 19. Spiral matrix problem ===================")
    for name, func in [("visited matrix", spiral_matrix_visited_matrix), ("boundary traversal", spiral_matrix_boundary_traversal)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["matrix"])
            print(f"Test {test['id']}: matrix={test['matrix']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

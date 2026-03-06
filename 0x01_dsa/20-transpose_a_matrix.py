#!/usr/bin/env python3

def transpose_matrix_brute_force(matrix: list[list[int]]) -> list[list[int]]:
    if not isinstance(matrix, list) or (len(matrix) == 1 and len(matrix[0]) == 0):
        return [[]]

    rows = len(matrix)
    cols = len(matrix[0])

    transposed_matrix = [[0 for _ in range(rows)] for _ in range(cols)]

    for i in range(rows):
        for j in range(cols):
            transposed_matrix[j][i] = matrix[i][j]

    return transposed_matrix

def transpose_matrix_constant_space_square_matrix(matrix: list[list[int]]) -> list[list[int]] | str:
    if not isinstance(matrix, list) or (len(matrix) == 1 and len(matrix[0]) == 0):
        return [[]]
    
    m, n = len(matrix), len(matrix[0])
    if m != n:
        return f"{matrix} is not a square matrix"

    for i in range(n):
        for j in range(i+1, n):
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]

    return matrix
    



if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "matrix": [
                [1,   2,   3,   4],
                [5, 6, 7,   8],
                [9, 10, 11, 12],
                [13,  14,  15, 16],
            ],
            "expected": [
                [1, 5, 9, 13,],
                [2, 6, 10, 14],
                [3, 7, 11, 15],
                [4, 8, 12, 16],
            ],
        },
        {
            "id": 2,
            "matrix": [
                [1,   2,   3,   4,  5,   6],
                [7,   8,   9,  10,  11,  12],
                [13,  14,  15, 16,  17,  18],
            ],
            "expected": [
                [1,   7,  13],
                [2,   8,  14],
                [3,   9,  15],
                [4,   10, 16],
                [5,   11, 17],
                [6,   12, 18],
            ],
        },
        {
            "id": 3,
            "matrix": [[]],
            "expected": [[]],
        },
        {
            "id": 4,
            "matrix": [
                [1, 2, 3],
                [4, 5, 6],
                [7, 8, 9],
            ],
            "expected": [
                [1, 4, 7],
                [2, 5, 8],
                [3, 6, 9],
            ],
        },
    ]

    print("=================== 20. Transpose a matrix problem ===================")
    for name, func in [("brute force", transpose_matrix_brute_force), ("constant space for square matrix", transpose_matrix_constant_space_square_matrix)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["matrix"])
            print(f"Test {test['id']}: matrix={test['matrix']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

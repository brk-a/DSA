#!/usr/bin/env python3

def matrix_word_search_recursion(matrix: list[list[str]], word: str) -> list[list[int]]:
    if not isinstance(matrix, list) or (len(matrix) == 1 and len(matrix[0]) == 0) or not isinstance(word, str) or len(word) == 0:
        return [[]]

    result = []
    n = len(matrix)
    m = len(matrix[0])

    # 8 degrees of freedom
    directions = [
        (1, 0), # down
        (-1, 0), # up
        (0, 1), # right
        (0, -1), # left
        (1, 1), # down then right
        (1, -1), # down then left
        (-1, 1), # up then right
        (-1, -1), # down then left
    ]

    for i in range(n):
        for j in range(m):
            # check if first characters matches
            if matrix[i][j] == word[0]:
                for dir_x, dir_y in directions:
                    if find_word_in_direction(matrix, n, m, word, 0, i, j, dir_x, dir_y):
                        result.append([i, j])
                        break

    return result if result else [[]]


def matrix_word_search_iteration(matrix: list[list[str]], word: str) -> list[list[int]]:
    if not isinstance(matrix, list) or (len(matrix) == 1 and len(matrix[0]) == 0) or not isinstance(word, str) or len(word) == 0:
        return [[]]

    m, n = len(matrix), len(matrix[0])
    result = []

    for i in range(m):
        for j in range(n):
            if search_2d(matrix, i, j, word):
                result.append([i, j])

    return result if result else [[]]

def find_word_in_direction(matrix: list[list[str]], n: int, m: int, word: str, idx: int, x: int, y: int, dir_x: int, dir_y: int) -> bool:
    if idx == len(word):
        return True

    if is_valid(x, y, n, m) and word[idx] == matrix[x][y]:
        return find_word_in_direction(matrix, n, m, word, idx+1, x+dir_x, y+dir_y, dir_x, dir_y)

    return False

def is_valid(x: int, y:int, size_x: int, size_y: int) -> bool:
    return 0 <= x < size_x and 0 <= y < size_y

def search_2d(matrix: list[list[int]], row: int, col: int, word: str) -> bool:
    m, n = len(matrix), len(matrix[0])

    if matrix[row][col] != word[0]:
        return False

    len_word = len(word)

    # degrees of freedom
    x = [-1, -1, -1, 0, 0, 1, 1, 1]
    y = [-1, 0, 1, -1, 1, -1, 0, 1]

    for direction in range(len(x)):
        curr_x, curr_y = row+x[direction], col+y[direction]
        k = 1

        while k < len_word:
            # break if out of bounds
            if curr_x >= m or curr_x < 0 or curr_y >= n or curr_y < 0:
                break

            # break if characters do not match
            if matrix[curr_x][curr_y] != word[k]:
                break

            # traverse the matrix
            curr_x += x[direction]
            curr_y += y[direction]
            k += 1

        # value of must be equal to length of word if all characters matched
        if k == len_word:
            return True

    return False


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "matrix": [
                ["a", "b", "a", "b"],
                ["a", "b", "e", "b"],
                ["e", "b", "e", "b"],
            ],
            "word": "abe",
            "expected": [
                [0, 0],
                [0, 2],
                [1, 0],
            ],
        },
        {
            "id": 2,
            "matrix": [
                ["w", "b", "a", "b"],
                ["a", "b", "e", "b"],
                ["e", "b", "e", "w"],
            ],
            "word": "web",
            "expected": [
                [2, 3],
            ],
        },
        {
            "id": 3,
            "matrix": [[]],
            "word": "web",
            "expected": [[]],
        },
        {
            "id": 4,
            "matrix": [
                ["w", "b", "w"],
                ["a", "o", "e"],
                ["w", "b", "w"],
            ],
            "word": "wow",
            "expected": [
                [0, 0],
                [0, 2],
                [2, 0],
                [2, 2],
            ],
        },
        {
            "id": 5,
            "matrix": [[]],
            "word": "",
            "expected": [[]],
        },
        {
            "id": 6,
            "matrix": [
                ["w", "b", "a", "b"],
                ["a", "b", "e", "b"],
                ["e", "b", "e", "w"],
            ],
            "word": "zzz",
            "expected": [[]],
        },
        {
            "id": 7,
            "matrix": [
                ["w", "b", "a", "b"],
                ["a", "b", "e", "b"],
                ["e", "b", "e", "w"],
            ],
            "word": "",
            "expected": [[]],
        },
    ]

    print("=================== 20. Transpose a matrix problem ===================")
    for name, func in [("recursion", matrix_word_search_recursion), ("iteration", matrix_word_search_iteration)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["matrix"], test["word"])
            print(f"Test {test['id']}: matrix={test['matrix']}, word={test['word']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

#!/usr/bin/env python3

def palindromic_substrings_count_brute_force(s: str) -> int:
    if not isinstance(s, str) or len(s) == 0:
        return 0
    
    n = len(s)
    result = 0
    for i in range(n):
        for j in range(i + 1, n):
            if is_palindrome(s, i, j):
                result += 1

    return result


def palindromic_substrings_count_memoisation(s: str) -> int:
    if not isinstance(s, str) or len(s) == 0:
        return 0
    
    n = len(s)
    memo = [[-1 for i in range(n)] for i in range(n)]
    result = 0
    for i in range(n):
        for j in range(i + 1, n):
            if is_palindrome_memo(i, j, s, memo):
                result += 1

    return result


def palindromic_substrings_count_tabulation(s: str) -> int:
    if not isinstance(s, str) or len(s) == 0:
        return 0
    
    n = len(s)
    result = 0
    table = [[False] * n for i in range(n)]

    # str of len 1 is a palindrome
    for i in range(n):
        table[i][i] = True

    # str of len 2 is a palindrome if both chars are the same
    for i in range(n - 1):
        if s[i] == s[i + 1]:
            table[i][i + 1] = True
            result += 1

    # handle palindromes of length 3 or more
    for gap in range(2, n):
        for i in range(n - gap):
            j = i + gap
            if s[i] == s[j] and table[i + 1][j - 1]:
                table[i][j] = True
                result += 1

    return result 


def palindromic_substrings_count_manacher_algo(s: str) -> int:
    if not isinstance(s, str) or len(s) == 0:
        return 0
    pass


def is_palindrome(s: str, start: int, end: str) -> bool:
    while start < end:
        if s[start] != s[end]:
            return False
        start += 1
        end -= 1

    return True


def is_palindrome_memo(i: int, j: int, s: str, memo: list) -> bool:
    # str of len 1 is a palindrome
    if i == j:
        return 1

    # str of len 2 and has the same chars is a palindrome
    if j == i + 1 and s[i] == s[j]:
        return 1

    # check whether current substring is already checked
    if memo[i][j] != -1:
        return memo[i][j]

    # check whether chars ai i ... j are equal and said str is palindrome
    if s[i] == s[j] and is_palindrome_memo(i + 1, j - 1, s, memo) == 1:
        memo[i][j] = 1
    else:
        memo[i][j] = 0

    return memo[i][j]


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "string": "abaab",
            "expected": 3,
        },
        {
            "id": 2,
            "string": "aaa",
            "expected": 3,
        },
        {
            "id": 3,
            "string": "",
            "expected": 0,
        },
        {
            "id": 4,
            "string": "abbaeae",
            "expected": 4,
        },
    ]

    print("=================== 28. palindromic substring counts problem ===================")
    for name, func in [("brute force", palindromic_substrings_count_brute_force), ("memoisation", palindromic_substrings_count_memoisation), ("tabulation", palindromic_substrings_count_tabulation), ("manacher's algorithm", palindromic_substrings_count_manacher_algo)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["string"])
            print(f"Test {test['id']}: s={test['string']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

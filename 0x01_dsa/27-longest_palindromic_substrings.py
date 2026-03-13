#!/usr/bin/env python3


def longest_palindromic_substring_brute_force(s: str) -> str:
    if not isinstance(s, str) or len(s) == 0:
        return ""
    
    n = len(s)
    max_len = 1
    start = 0

    for i in range(n):
        for j in range(i, n):
            if check_palindrome(s, i, j) and (j - i + 1) > max_len:
                start = i
                max_len = j - i + 1

    return s[start: start + max_len]


def longest_palindromic_substring_dynamic_programming(s: str) -> str:
    if not isinstance(s, str) or len(s) == 0:
        return ""
    
    n = len(s)
    memo = [[False] * n for _ in range(n)]
    start = 0
    max_len = 1

    # all substrings of len 1 are palindromes
    for i in range(n):
        memo[i][i] = True

    # check whether substrings of len 2 are palindromes
    for i in range(n - 1):
        if s[i] == s[i + 1]:
            memo[i][i + 1] = True
            if max_len == 1:
                start = i
                max_len = 2

    # check whether substrings of len and over are palindromes
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1

            if s[i] == s[j] and memo[i + 1][j - 1]:
                memo[i][j] = True
                if length > max_len:
                    start = i
                    max_len = length

    return s[start: start + max_len]


def longest_palindromic_substring_expand_from_centre(s: str) -> str:
    if not isinstance(s, str) or len(s) == 0:
        return ""
    
    n = len(s)
    start, max_len = 0, 1

    for i in range(n):
        for j in range(2):
            low, high = i, i + j
            while low >= 0 and high < n and s[low] == s[high]:
                curr_len = high - low + 1
                if curr_len > max_len:
                    start = low
                    max_len = curr_len
                low -= 1
                high += 1

    return s[start: start + max_len]


def longest_palindromic_substring_manacher_algo(s: str) -> str:
    if not isinstance(s, str) or len(s) == 0:
        return ""
    
    n = len(s)
    max_len = 1
    start = 0
    manacher = Manacher(s)

    for i in range(n):
        odd_len = manacher.get_longest(i, 1)
        if odd_len > max_len:
            start = i - (odd_len - 1) // 2
        even_len = manacher.get_longest(i, 0)
        if even_len > max_len:
            start = i - (even_len - 1) // 2

        max_len = max(max_len, max(odd_len, even_len))

    return s[start: start + max_len]


def check_palindrome(s: str, low: int, high: int) -> bool:
    while low < high:
        if s[low] != s[high]:
            return False

        low += 1
        high -= 1

    return True


class Manacher:
    def __init__(self, s: str):
        self.ms = "@"
        for char in s:
            self.ms += f"#{char}"
        self.ms += "#$"
        self.p = [0] * len(self.ms)
        self.run_manacher()

    def run_manacher(self):
        n = len(self.ms)
        l = r = 0

        for i in range(1, n - 1):
            mirror = l + r - 1
            if 0 <= mirror < n:
                self.p[i] = max(0, min(r - i, self.p[mirror]))
            else:
                self.p[i] = 0

            while (i + 1 + self.p[i] < n) and (i - 1 - self.p[i] > 0) and (self.ms[i + 1 + self.p[i]] == self.ms[i - 1 - self.p[i]]):
                self.p[i] += 1

            if i + self.p[i] > r:
                l = i - self.p[i]
                r = i + self.p[i]

    def get_longest(self, cen: int, odd: int) -> int:
        pos = 2 * cen + 2 + (0 if odd else 1)
        return self.p[pos]

    def check(self, left: int, right: int) -> bool:
        length = right - left + 1
        return length <= self.get_longest((left + right) // 2, length % 2)       


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "string": "laughkookaburalaughgayyourlifemustbe",
            "expected": "kook",
        },
        {
            "id": 2,
            "string": "bookskoob",
            "expected": "bookskoob",
        },
        {
            "id": 3,
            "string": "",
            "expected": "",
        },
        {
            "id": 4,
            "string": "abc",
            "expected": "a",
        },
    ]

    print("=================== 27. longest palindromic substring problem ===================")
    for name, func in [("brute force", longest_palindromic_substring_brute_force), ("dynamic programming", longest_palindromic_substring_dynamic_programming), ("expand from centre", longest_palindromic_substring_expand_from_centre), ("manacher's algo", longest_palindromic_substring_manacher_algo)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["string"])
            print(f"Test {test['id']}: s={test['string']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

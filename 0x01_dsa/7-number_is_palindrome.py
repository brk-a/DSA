#!/usr/bin/env python3

import hashlib

def number_is_palindrome_brute_force(num: int) -> bool:
    if not isinstance(num, int):
        return False

    reversed = [i for i in str(abs(num))][::-1]
    
    return [i for i in str(abs(num))] == reversed


def number_is_palindrome_hash(num: int) -> bool:
    if not isinstance(num, int):
        return False

    num_str = "".join([i for i in str(abs(num))])
    rev = "".join([i for i in str(abs(num))][::-1])

    num_hash = hashlib.sha256(num_str.encode()).hexdigest()
    rev_hash = hashlib.sha256(rev.encode()).hexdigest()

    return num_hash == rev_hash


def number_is_palindrome_half_reverse(num: int) -> bool:
    if not isinstance(num, int):
        return False
    
    # if num < 0:
    #     return False

    if num < 0 or (num % 10 == 0 and num != 0):
        return False

    org = abs(num)
    rev = 0
    while org > rev:
        digit = org % 10
        rev = rev * 10 + digit
        org //= 10

    return org == rev or org == rev // 10 



if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "num": 1234321,
            "expected": True,
        },
        {
            "id": 2,
            "num": 123432,
            "expected": False,
        },
        {
            "id": 3,
            "num": -1234321,
            "expected": True,
        },
        {
            "id": 4,
            "num": "1234321",
            "expected": False,
        },
        {
            "id": 5,
            "num": 0,
            "expected": True,
        },
        {
            "id": 6,
            "num": -0,
            "expected": True,
        },
    ]

    print("=================== 7. Number is palindrome problem ===================")
    for name, func in [("brute force", number_is_palindrome_brute_force), ("hash", number_is_palindrome_hash), ("half reverse", number_is_palindrome_half_reverse)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["num"])
            print(f"Test {test['id']}: num={test['num']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

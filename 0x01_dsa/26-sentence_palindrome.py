#!/usr/bin/env python3


def sentence_palindrome_brute_force(s: str) -> bool:
    if not isinstance(s, str):
        return False

    if len(s) == 0:
        return True
    
    s1 = []
    for char in s:
        if char.isalnum():
            s1.append(char.lower())

    s1 = ''.join(s1)

    reverse = s1[::-1]

    return s1 == reverse


def sentence_palindrome_two_pointers(s: str) -> bool:
    if not isinstance(s, str):
        return False

    if len(s) == 0:
        return True
    
    i, j = 0, len(s) - 1
    while i < j:
        if not s[i].isalnum():
            i += 1
        elif not s[j].isalnum():
            j -= 1
        elif s[i].lower() == s[j].lower():
            i += 1
            j -= 1
        else:
            return False
    
    return True


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "sentence": "Too hot to hoot.",
            "expected": True,
        },
        {
            "id": 2,
            "sentence": "Abc 012..##  10cbA",
            "expected": True,
        },
        {
            "id": 3,
            "sentence": "ABC $. def01ASDF..",
            "expected": False,
        },
        {
            "id": 4,
            "sentence": "",
            "expected": True,
        },
    ]

    print("=================== 26. sentence palindrome problem ===================")
    for name, func in [("brute force", sentence_palindrome_brute_force), ("two pointers", sentence_palindrome_two_pointers)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["sentence"])
            print(f"Test {test['id']}: s={test['sentence']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

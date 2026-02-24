#!/usr/bin/env python3

def count_vowel_iterative(word: str) -> int:
    if not isinstance(word, str):
        return -1

    VOWELS = "aeiou"
    count = 0

    for _, v in enumerate(word.lower()):
        if v in VOWELS:
            count += 1

    return count

def count_vowel_recursive(word: str) -> int:
    if not isinstance(word, str):
        return -1

    VOWELS = "aeiou"
    n = len(word)
    count = 0

    # TODO: recurse
    
    return -1 # TODO: change when recursion is done

if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "word": "the quick, brown fox jumps over the lazy dog",
            "expected": 11,
        },
        {
            "id": 2,
            "word": "the quick, brown fox jumps over the lazy dog. THE QUICK, BROWN FOX JUMPS OVER THE LAZY DOG.",
            "expected": 22,
        },
    ]

    print("=================== 8. Count vowels problem ===================")
    for name, func in [("iterative", count_vowel_iterative), ("recursive", count_vowel_recursive)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["word"])
            print(f"Test {test['id']}: word={test['word']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

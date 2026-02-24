#!/usr/bin/env python3

from collections import Counter

def count_vowel_iterative(word: str) -> list[tuple[str, int]]:
    if not isinstance(word, str):
         return [("Arg must be type string", -1)]

    VOWELS = "aeiou"
    
    count = Counter(word.lower())
    final = sorted([(k, v) for k, v in count.items() if k in VOWELS])

    return final

def count_vowel_recursive(word: str) -> list[tuple[str, int]]:
    if not isinstance(word, str):
        return [("Arg must be type string", -1)]

    VOWELS = "aeiou"
    n = len(word)
    
    return [("Working on it", -1)]

if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "word": "the quick, brown fox jumps over the lazy dog",
            "expected": [('a', 1), ('e', 3), ('i', 1), ('o', 4), ('u', 2)],
        },
        {
            "id": 2,
            "word": "the quick, brown fox jumps over the lazy dog. THE QUICK, BROWN FOX JUMPS OVER THE LAZY DOG.",
            "expected": [('a', 2), ('e', 6), ('i', 2), ('o', 8), ('u', 4)],
        },
    ]

    print("=================== 8. Count vowels problem ===================")
    for name, func in [("iterative", count_vowel_iterative), ("recursive", count_vowel_recursive)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["word"])
            print(f"Test {test['id']}: word={test['word']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

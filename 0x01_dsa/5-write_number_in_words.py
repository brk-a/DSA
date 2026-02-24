#!/usr/bin/env python3

def write_in_words(num: int) -> str:
    num = num if num and isinstance(num, int) else None
    if not num:
        return "Arg is not an int"

    result = ""

    for _, v in enumerate(str(num)):
        match v:
            case "0":
                result += f"zero "
            case "1":
                result += f"one "
            case "2":
                result += f"two "
            case "3":
                result += f"three "
            case "4":
                result += f"four "
            case "5":
                result += f"five "
            case "6":
                result += f"six " 
            case "7":
                result += f"seven " 
            case "8":
                result += f"eight " 
            case "9":
                result += f"nine "
            case _:
                return f"Invalid character"

    return result.lstrip().rstrip()


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "num": 122,
            "expected": "one two two",
        },
        {
            "id": 2,
            "num": "",
            "expected": "Arg is not an int",
        },
        {
            "id": 2,
            "num": 900,
            "expected": "nine zero zero",
        },
        {
            "id": 3,
            "num": 112345,
            "expected": "one one two three four five",
        },
        {
            "id": 4,
            "num": 543212345,
            "expected": "five four three two one two three four five",
        },
        {
            "id": 5,
            "num": 6009,
            "expected": "six zero zero nine",
        },
        {
            "id": 6,
            "num": 9_000,
            "expected": "nine zero zero zero",
        },
    ]

    print("=================== 5. Write a number in words problem ===================")
    for name, func in [("iterative", write_in_words)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["num"])
            print(f"Test {test['id']}: num={test['num']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

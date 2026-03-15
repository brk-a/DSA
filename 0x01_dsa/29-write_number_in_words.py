#!/usr/bin/env python3

one = [
    "", "one", "two", "three", "four",
    "five", "six", "seven", "eight", "nine",
    "ten", "eleven", "twelve", "thirteen", "fourteen",
    "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
    ]

ten = [
    "", "", "twenty", "thirty", "fourty",
    "fifty", "sixty", "seventy", "eighty",
    "ninety"
]

def number_to_words(n: int, s: str) -> str:
    if n == 0:
        return ""
    
    # ADD THIS: Handle hundreds first
    if n >= 100:
        hundreds = n // 100
        remainder = n % 100
        result = f"{one[hundreds]} hundred"
        if remainder:
            result += f" and {number_to_words(remainder, '')}"
        result += f" {s}" if s else ""
        return result.strip()
    
    # Existing logic for 0-99
    if n > 19:
        tens_part = ten[n // 10]
        ones_part = one[n % 10]
        result = f"{tens_part} {ones_part}".strip() if ones_part else tens_part
    else:
        result = one[n]
    return f"{result} {s}".strip() if s else result


def write_in_words(num: int) -> str:
    if not isinstance(num, int):
        return "Arg is not an int"
    
    result = []
    temp_num = num
    
    # STANDARD grouping - each handles its FULL range
    for divisor, name in [(100000000, "hundred million"), 
                         (1000000, "million"), 
                         (1000, "thousand"), 
                         (100, "hundred")]:
        quotient = temp_num // divisor
        if quotient:
            result.append(number_to_words(quotient, name))
        temp_num %= divisor
    
    # Handle final remainder
    remainder = temp_num % 100
    if remainder:
        if result:
            result.append("and")
        result.append(number_to_words(remainder, ""))
    
    return " ".join(result)


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "num": 122,
            "expected": "one hundred and twenty two",
        },
        {
            "id": 2,
            "num": "",
            "expected": "Arg is not an int",
        },
        {
            "id": 2,
            "num": 900,
            "expected": "nine hundred",
        },
        {
            "id": 3,
            "num": 112345,
            "expected": "one hundred and twelve thousand three hundred and fourty five",
        },
        {
            "id": 4,
            "num": 543212345,
            "expected": "five hundred and fourty three million two hundred and twelve thousand three hundred and fourty five",
        },
        {
            "id": 5,
            "num": 6009,
            "expected": "six thousand and nine",
        },
        {
            "id": 6,
            "num": 9_000,
            "expected": "nine thousand",
        },
        {
            "id": 7,
            "num": 438237764,
            "expected": "four hundred thirty eight million two hundred and thirty seven thousand seven hundred and sixty four"
        }
    ]

    print("=================== 28. Write a number in words problem ===================")
    for name, func in [("iterative", write_in_words)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["num"])
            print(f"Test {test['id']}: num={test['num']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

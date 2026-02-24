#!/usr/bin/env python3

def divisible_by_six_mod(num: int) -> bool:
    num = num if num and isinstance(num, int) else None
    if not num:
        return False

    if num % 6 == 0:
        return True

    return False


def divisible_by_six_two_three(num: int) -> bool:
    num = num if num and isinstance(num, int) else None
    if not num:
        return False
    
    num_arr = [int(i) for i in str(num)]
    if num_arr[-1] % 2 != 0:
        return False
    
    cum_sum = 0
    for _, v in enumerate(num_arr):
        cum_sum += v
    if cum_sum % 3 == 0:
        return True
    
    return False


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "num": 1234567890,
            "expected": True,
        },
        {
            "id": 2,
            "num": 123456789,
            "expected": False,
        },
        {
            "id": 3,
            "num": "",
            "expected": False,
        },
        {
            "id": 4,
            "num": 363588395960667043875487,
            "expected": False,
        },
    ]

    print("=================== 6. Divisibile by six problem ===================")
    for name, func in [("modulus by six", divisible_by_six_mod), ("two and three divisibility test", divisible_by_six_two_three)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["num"])
            print(f"Test {test['id']}: num={test['num']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")
    

#!/usr/bin/env python3


def unique_bst_with_n_keys_dp(n: int) -> int:
    if not isinstance(n, int) or n == 0:
        return -1

    # calculate 2nCn
    c = bin_coeff(2 * n, n)

    # return nth Catalan number
    return c // (n + 1)


def bin_coeff(n: int, k: int) -> int:
    # nCk is the same as nC(n-k)
    if k > n - k:
        k = n - k

    result = 1

    # calculate the value of n! / (k! * (n-k)!)
    for i in range(k):
        result *= (n - i)
        result //= (i + 1)

    return result


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "n": 3,
            "expected": 5,
        },
        {
            "id": 2,
            "n": 2,
            "expected": 2,
        },
        {
            "id": 3,
            "n": "3",
            "expected": -1,
        },
        {
            "id": 4,
            "n": 5,
            "expected": 42,
        },
    ]

    print("=================== 90. Number of unique BSTs given N keys problem ===================")
    for name, func in [("Dynamic programming", unique_bst_with_n_keys_dp)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test['n'])
            print(f"Test {test['id']}: n={test['n']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

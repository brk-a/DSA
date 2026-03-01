#!/usr/bin/env python3


class Node:
    def __init__(self, digit: int):
        self.digit = digit
        self.next = None


def factorial_of_a_large_number_carry_method(num: int) -> int:
    result = [0]*500
    result[0] = 1
    result_size = 1

    # apply simple factorial formula: n! = n * (n-1) * ... * 2 * 1
    x = 2
    while x <= num:
        result_size = multiply(x, result, result_size)
        x += 1

    return int(''.join(str(result[i]) for i in range(result_size - 1, -1, -1)))


def factorial_of_a_large_number_tail_recursion(num: int) -> int:
    def helper(n: int, accum: int) ->  int:
        if n <= 1:
            return accum
        return helper( n - 1, accum * n)
    return helper(num, 1)


def factorial_of_a_large_number_linked_list(num: int) -> int:
    if num <= 1:
        return 1
    
    head = Node(1)
    current_size = 1
    
    for x in range(2, num + 1):
        head = multiply_linked_list(head, x)
    
    result = []
    current = head
    while current:
        result.append(str(current.digit))
        current = current.next
    
    return int(''.join(reversed(result)))


def factorial_of_a_large_number_big_int(num: int) -> int:
    f = 1
    for i in range(2, num+1):
        f *= i
    
    return f


def multiply(x: int, result: list, result_size: int) -> int:
    carry = 0

    i = 0
    while i < result_size:
        prod = result[i] * x + carry
        result[i] = prod % 10

        carry = prod // 10
        i += 1

    while carry:
        if result_size >= 500:  # Prevent overflow
            break
        result[result_size] = carry % 10
        carry //= 10

        result_size += 1

    return result_size


def multiply_linked_list(head: Node, x: int) -> Node:
    if not head:
        return None
    
    carry = 0
    current = head
    
    # Multiply each digit + carry
    while current:
        prod = current.digit * x + carry
        current.digit = prod % 10
        carry = prod // 10
        current = current.next
    
    # Append new digits for remaining carry
    while carry:
        new_node = Node(carry % 10)
        new_node.next = head  # Prepend to list
        head = new_node
        carry //= 10
    
    return head


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "num": 100,
            "expected": 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000,
        },
        {
            "id": 2,
            "num": 50,
            "expected": 30414093201713378043612608166064768844377641568960512000000000000,
        },
        # {
        #     "id": 3,
        #     "num": 100,
        #     "expected": 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000,
        # },
        # {
        #     "id": 4,
        #     "num": 100,
        #     "expected": 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000,
        # },
    ]

    print("=================== 15. Factorial of a large number problem ===================")
    for name, func in [("carry", factorial_of_a_large_number_carry_method), ("tail recursion", factorial_of_a_large_number_tail_recursion), ("linked list", factorial_of_a_large_number_linked_list), ("big int", factorial_of_a_large_number_big_int)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["num"])
            print(f"Test {test['id']}: num={test['num']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

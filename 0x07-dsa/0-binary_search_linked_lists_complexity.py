#!/usr/bin/python3


def locate_card_linear(cards: list, query: int) -> int:
    cards = cards if cards and isinstance(cards, list) else []
    query = query if query and isinstance(query, int) else 0

    if len(cards) == 0:
        return -1

    for i in range(len(cards)):
        if cards[i] == query:
            return i
    return -1


def locate_card_binary(cards: list, query: int) -> int:
    cards = cards if cards and isinstance(cards, list) else []
    query = query if query and isinstance(query, int) else 0

    if len(cards) == 0:
        return -1
    
    low, high = 0, len(cards) - 1
    while low <= high:
        mid: int = low + (high - low) // 2
        if cards[mid] == query:
            return mid
        elif cards[mid] > query:
            low = mid + 1
        else:
            high = mid - 1
    
    return -1


if __name__ == "__main__":
    tests = {
        "middle_element": {
            "input": {"cards": [13, 11, 10, 7, 4, 3, 1, 0, -2], "query": 7},
            "output": 3,
        },
        "first_element": {
            "input": {"cards": [7, 6, 5, 4, 3], "query": 7},
            "output": 0,
        },
        "last_element": {
            "input": {"cards": [10, 9, 8, 7], "query": 7},
            "output": 3,
        },
        "single_element_found": {
            "input": {"cards": [7], "query": 7},
            "output": 0,
        },
        "single_element_not_found": {
            "input": {"cards": [9], "query": 7},
            "output": -1,
        },
        "not_found": {
            "input": {"cards": [13, 11, 10, 4, 3, 1, 0, -2], "query": 7},
            "output": -1,
        },
        "empty_cards": {
            "input": {"cards": [], "query": 7},
            "output": -1,
        },
        "duplicate_query": {
            "input": {"cards": [13, 11, 10, 7, 7, 4, 3, 1, 0, -2], "query": 7},
            "output": [3, 4],  # accept either index
        },
        "duplicate_non_query_elements": {
            "input": {
                "cards": [13, 13, 13, 11, 10, 7, 7, 4, 3, 3, 3, 3, 3, 3, 1, 0, -2],
                "query": 7,
            },
            "output": [5, 6],  # accept either index
        },
    }

    for name, test in tests.items():
        result_linear = locate_card_linear(**test["input"])
        result_binary = locate_card_binary(**test["input"])
        expected = test["output"]
        if isinstance(expected, list):
            match = result_linear in expected
        else:
            match = result_linear == expected
        print(f"======== results for linear algo ==========")
        print(
            f"Test '{name}': {'Match' if match else 'No match'} (Got: {result_linear}, Expected: {expected})"
        )
        if isinstance(expected, list):
            match = result_binary in expected
        else:
            match = result_binary == expected
        print(f"======== results for binary algo ==========")
        print(
            f"Test '{name}': {'Match' if match else 'No match'} (Got: {result_binary}, Expected: {expected})"
        )

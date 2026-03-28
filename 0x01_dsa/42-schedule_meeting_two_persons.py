#!/usr/bin/env python3

def schedule_meeting_two_persons_brute_force(slots1: list[int], slots2: list[int], duration: int) -> list[int]:
    if not isinstance(slots1, list) or not isinstance(slots2, list) or not isinstance(duration, int):
        return []

    # sort slots based on start times
    slots1.sort(key = lambda x: x[0])
    slots2.sort(key = lambda x: x[0])

    i, j = 0, 0
    n, m = len(slots1), len(slots2)
    while i < n and j < m:
        left = max(slots1[i][0], slots2[j][0])
        right = min(slots1[i][1], slots2[j][1])

        if right - left >= duration:
            return [left, left + duration]

        # always move the pointer of the slot that ends earlier
        if slots1[i][1] < slots2[j][1]:
            i += 1
        else:
            j += 1

    # return an empty list if no common time slot found
    return []


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "slots1": [
                [10, 50], [60, 120], [140, 210],
                ],
            "slots2": [
                [0, 15], [60, 70],
                ],
            "duration": 8,
            "expected": [60, 68],
        },
        {
            "id": 2,
            "slots1": [
                [10, 50], [60, 120], [140, 210],
                ],
            "slots2":  [
                [0, 15], [60, 70],
                ],
            "duration": 12,
            "expected": [],
        },
        {
            "id": 3,
            "slots1": [],
            "slots2": [
                [10, 50], [60, 120],
            ],
            "duration": 30,
            "expected": [],
        },
        {
            "id": 4,
            "slots1": [
                [10, 50], [60, 120],
            ],
            "slots2": [],
            "duration": 30,
            "expected": [],
        },
        {
            "id": 5,
            "slots1": [
                [10, 50], [60, 120], [140, 210],
                ],
            "slots2":  [
                [0, 15], [60, 70],
                ],
            "duration": "",
            "expected": [],
        },
        {
            "id": 6,
            "slots1": [
                [10, 50], [60, 120], [140, 210],
                ],
            "slots2":  [
                [0, 15], [60, 85], [75, 105]
                ],
            "duration": 20,
            "expected": [60, 80],
        },
    ]


print("=================== 41. Schedule meeting between two people problem ===================")
for name, func in [("brute force", schedule_meeting_two_persons_brute_force)]:
    print(f"=================== method: {name} ===================")
    for test in tests:
        got = func(test["slots1"], test["slots2"], test["duration"])
        print(f"Test {test['id']}: slots 1 = {test["slots1"]},  slots 2 = {test["slots2"]}, duration = {test["duration"]}, got = {got}, expected = {test['expected']}, passed = {got == test['expected']}")

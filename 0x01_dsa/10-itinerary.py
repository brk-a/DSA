#!/usr/bin/env python3

def create_itinerary_brute_force(passes: list) -> list[dict]:
    if not isinstance(passes, list) or len(passes) == 0:
        return []

    currs = [i["current"] for i in passes]
    nexts = [i["next"] for i in passes]
    n = len(passes)

    # Find start pass index
    start_idx = None
    for i, p in enumerate(passes):
        if p["current"] in currs and p["current"] not in nexts:
            start_idx = i
            break
    if start_idx is None:
        return []

    itinerary = [passes[start_idx]]
    passes_copy = passes[:start_idx] + passes[start_idx + 1:]

    while len(itinerary) < n:
        found = False
        for j, v in enumerate(passes_copy):
            if v["current"] == itinerary[-1]["next"]:
                itinerary.append(v)
                del passes_copy[j]
                found = True
                break
        if not found:
            return []  # Incomplete chain

    return itinerary


if __name__ == "__main__":
    tests = [
        {
            "id": 1,
            "passes": [
                {
                    "current": "LAX",
                    "next": "HNL",
                },
                {
                    "current": "SFO",
                    "next": "LAX",
                },
                {
                    "current": "HNL",
                    "next": "NRT",
                },
            ],
            "expected": [
                {
                    "current": "SFO",
                    "next": "LAX",
                },
                {
                    "current": "LAX",
                    "next": "HNL",
                },
                {
                    "current": "HNL",
                    "next": "NRT",
                },
            ],
        },
        {
            "id": 2,
            "passes": [
                {
                    "current": "NBO",
                    "next": "DXB",
                },
                {
                    "current": "GAT",
                    "next": "BCN",
                },
                {
                    "current": "DXB",
                    "next": "GAT",
                },
            ],
            "expected": [
                {
                    "current": "NBO",
                    "next": "DXB",
                },
                {
                    "current": "DXB",
                    "next": "GAT",
                },
                {
                    "current": "GAT",
                    "next": "BCN",
                },
            ],
        },
        {
            "id": 3,
            "passes": [],
            "expected": [],
        },
        {
            "id": 4,
            "passes": [
                {
                    "current": "",
                    "next": "NBO",
                },
                {
                    "current": "NBO",
                    "next": "DXB",
                },
                {
                    "current": "NBO",
                    "next": "JHB",
                },
            ],
            "expected": [],
        },
    ]

    print("=================== 10. Itinerary problem ===================")
    for name, func in [("brute force", create_itinerary_brute_force)]:
        print(f"=================== method: {name} ===================")
        for test in tests:
            got = func(test["passes"])
            print(f"Test {test['id']}: passes={test['passes']}, got={got}, expected={test['expected']}, passed={got == test['expected']}")

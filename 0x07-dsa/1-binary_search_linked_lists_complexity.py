def count_rotations(nums):
    """
    Given a sorted list that has been rotated, find the number of times it
    has been rotated. Rotation is defined as taking the last element and
    moving it to the front. The rotation count corresponds to the index of
    the smallest element in the rotated list.

    Args:
        nums (list[int]): A rotated sorted list of unique numbers.

    Returns:
        int: Number of rotations.
    """
    if not nums:
        return 0  # empty list, no rotations

    left, right = 0, len(nums) - 1

    # If the list is already sorted (not rotated)
    if nums[left] < nums[right]:
        return 0

    while left <= right:
        mid = (left + right) // 2
        next_idx = (mid + 1) % len(nums)
        prev_idx = (mid - 1 + len(nums)) % len(nums)

        # Check if mid is the minimum element
        if (nums[mid] <= nums[next_idx] and nums[mid] <= nums[prev_idx]):
            return mid

        # Decide where to go next
        if nums[mid] >= nums[left]:
            # Left part is sorted, move right
            left = mid + 1
        else:
            # Right part is sorted, move left
            right = mid - 1

    return 0  # fallback, should never reach here


if __name__ == "__main__":
    tests = {
        "empty list": {"input": {"nums": []}, "output": 0},
        "single element": {"input": {"nums": [10]}, "output": 0},
        "no rotation": {"input": {"nums": [1, 2, 3, 4, 5]}, "output": 0},
        "rotated once": {"input": {"nums": [5, 1, 2, 3, 4]}, "output": 1},
        "rotated multiple times": {"input": {"nums": [4, 5, 1, 2, 3]}, "output": 2},
        "rotated n-1 times": {"input": {"nums": [2, 3, 4, 5, 1]}, "output": 4},
        "rotated n times (same as zero rotations)": {
            "input": {"nums": [1, 2, 3, 4, 5]},
            "output": 0,
        },
        "rotated n+1 times (equivalent to 1 rotation)": {
            "input": {"nums": [5, 1, 2, 3, 4]},
            "output": 1,
        },
        "larger list rotated 4 times": {
            "input": {"nums": [7, 8, 9, 10, 1, 2, 3, 4, 5, 6]},
            "output": 4,
        },
        "larger list rotated 0 times": {
            "input": {"nums": list(range(1, 21))},
            "output": 0,
        },
        "rotated with negative numbers": {
            "input": {"nums": [3, 4, 5, -3, -2, -1, 0, 1, 2]},
            "output": 3,
        },
        "two elements rotated once": {"input": {"nums": [2, 1]}, "output": 1},
        "two elements no rotation": {"input": {"nums": [1, 2]}, "output": 0},
    }

    for name, test in tests.items():
        result = count_rotations(**test["input"])
        assert (
            result == test["output"]
        ), f"Test '{name}' failed: got {result}, expected {test['output']}"
    print("All test cases passed!")

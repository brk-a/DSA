# binary search, linked lists and complexity

## problem
- A has cards w. numbers written on them
- said cards, arranged in decreasing order, are laid face-down in a sequence
- B must pick a card that has a given number by turning over as few cards as possible
- write a function to help B achieve said goal

## approach
### the problem in simple language
- the problem described involves a sequence of cards arranged face-down in decreasing order and the task is to help someone find a card with a given number by turning over as few cards as possible
- the numbers on the cards are pre-sorted; we are spared the time and space to sort
### get a wee bit technical
- think of the numbers on the cards as a sorted list, *l*, of length *n* and *i* is a position on the list such that 0 &le; *i* &lt; *n*: we want to return the position of the value *i* where *l[i]* is the number on the card we are interested in
- the act of turning over a card in real life is accomplished by fetching the value at index *i* on the list, that is, *l[i]*
- this must be accomplished using as few fetches as possbile
- **input**:
    - `cards` &rarr; a list of ints sorted in descending order, say, [13, 11, 10, 7, 4, 3, 1, 0, -2]
    - `query` &rarr; the number on the card we are interested in, say, 7
- **output**:
    - `position` &rarr; the position of `query` in the list `card` (in this case 3)
### edge cases
- what happens during any of the following scenarios?
    - `query` appears somewhere in the middle of the list `cards` &rarr; [..., 7, ...]
    - the length of `cards` is infinitely large, say, 1 lakh crore &rarr; len(cards) = 1,000,000,000,000
    - `query` is the first element of `cards` &rarr; [7, ...]
    - `query` is the last element of `cards` &rarr; [..., 7]
    - `cards` contains one element and said element is `query` &rarr; \[7]
    - `cards` contains one element and said element is not `query` &rarr; \[9]
    - `cards` does not contain `query` &rarr; [13, 11, 10, 4, 3, 1, 0, -2]
    - `cards` is empty &rarr; []
    - `query` appears more than once in `cards` &rarr; [13, 11, 10, 7, 7, 4, 3, 1, 0, -2]
    - `cards` has duplicate elements which may or may not be `query` &rarr; [13, 13, 13, 11, 10, 7, 7, 4, 3, 3, 3, 3, 3, 3, 1, 0, -2]
    - `cards` has multiple occurrences of `query` &rarr; [13, 13, 7, 7, 7, 7, 7, 7, 7, 3, 3, 3, 3, 3, 1, 0, -2]
    - ...
### assumptions
- returns -1 when `query` is not in `cards`
- returns the index of the first occurrence of `query` when there are duplicates/ multiple occurrences of `query`
    - first occurrence is the leftmost one
- limit of length of `cards` is finitely large
## method
### method 1: turn over the cards one by one until the desired result is found
* B simply turns the cards L-R or R-L (prefer L-R)
    - is the number on the card the same as the desired one? if yes, stop and return the position of said card, else, carry on
    - this continues until the card is found or the search space is empty
* this method is called **linear search**
### method 2: check the middle, compare with desired value, discard one half and repeat
* B repeatedly checks the middle card of the current search range
    - if the number on the middle card matches the target number, the search is done
    - if the target number is smaller than the middle card's number (considering decreasing order), proceed to the right half; otherwise, proceed to the left half
    - this continues until the card is found or the search space is empty
* this method is called **binary search**

## solution
### possible solutions
1. **linear Search**: simply flip cards from one end to the other to find the target card which is inefficient with *O(n)* time complexity and many cards turned over
2. **binary Search Implementation**: this is the optimal approach with time complexity *O(log n)*, where *n* is the number of cards. the number of cards turned over is minimal compared to a linear search

### implemented solution
[0-binary_search_linked_lists_complexity.py][def]

## discussion
* say B can only turn one card per second and that there are N cards where N &ge; 0 and  that the number of interest is at the rightmost position (recall: B moves L-R; this is the worst-case scenario) 
    - using the **first method**, B will take N seconds to achieve the objective
    - this is not efficient from a time perspective: what happens when N = 1 lakh crore!?
    - this is efficient from a space perspective: B only has to track the position of the card being checked against `query`
* say that the worst-case scenario above holds
    - using the **second method**, the number of cards B has to choose from halves with each unsuccessful turn: N, N/2, N/4, N/8 ... N/2<sup>k</sup> for *k* iterations (the sum of which is 2N, by the way)
    - the `cards` array will always reduce to one element ofter *k* iterations (*k* &gt; 0), therefore, N/2<sup>k</sup> = 1
    - another way of saying this: N = 2<sup>k</sup> or *log <sub>2</sub> N = k* 
    - B takes log<sub>2</sub>(N) seconds to achieve the goal in the worst-case scenario
    - B only has to track the position of the card being checked against `query` and the boundary of the search area
    - let the starting index of `cards`, index 0, be `low` and the ending index, index N-1, be `high`
    - let the middle index/position in said array be `mid`
    - the boundary will be the first and last index of the sub-array being considered
        - first pass: boundary will be index **0** and index **N-1** (python is a zero-indexed language)
        - second pass: boundary will be index **0** and index **`mid` less 1** if `query` &gt; `cards[i]` else index **`mid` + 1** and **N-1**
        - N<sup>th</sup> pass (general case): boundary will be index **0** and index **`mid` less 1** if `query` &gt; `cards[i]` else index **`mid` + 1** and **N-1** IFF the `low` &le; `high`
* the **first method** has a time complexity of *O(N)* and space complexity of *O(1)* in big O notation terms
    - time complexity is the time taken to achieve the objective
    - space complexity is, in this case, the amount of memory space required to hold the variables
* the **second method** has a time complexity of *O(log N)* and space complexity of *O(1)*
    - the base of the logarithm is irrelevant; all that matters is that it takes logarithmic time or space
* binary search is an efficient search algorithm for sorted arrays or lists; it reduces the search space by half with each card turned over
* the best method is to use a binary search algorithm given that the cards are arranged in a sorted (decreasing) order
* since the sequence is sorted in decreasing order, the binary search should be adapted to compare and move either left or right accordingly
### generalise the solution
* the problem's input is an array; what happens if the input is, say, a dictionary? how about a string?
    - glad you asked...
    - in this case, we will have another parameter in our function; said parameter implements conditional logic
    - this will most likely be a helper function
* flow

    ```python
        def binary_search(lo, hi, condition):
            """ add docstring here """
            while lo <= hi:
                mid = (lo + hi) // 2
                result = condition(mid)
                if result == "found":
                    return mid
                elif result == "left":
                    hi = mid - 1
                else:
                    lo = mid + 1
            
            return -1


        def locate(cards, query):
            """ insert docstring here """

            def condition(mid):
                """ add docstring here """
                if cards[mid] == query:
                    if mid > 0 and cards[mid - 1] == query:
                        return "left"
                    else:
                        return "found"
                elif cards[mid] < query:
                    return "left"
                else:
                    return "right"
            
            return binary_search(0, len(cards) - 1, condition)
    ```

* this flow allows us to re-use most of the logic when the problem changes
    - examples:
        - `cards` is in ascending rather than descending order
        - `query` is a list
        - `cards` is, say, a dictionary
* say we changed the problem *viz*:
    - elements of `cards` are in ascending order
    - return the first and last occurrence of `query`
* logic does not change much

    ```python
        def binary_search(lo, hi, condition):
            """ add docstring here """
            while lo <= hi:
                mid = (lo + hi) // 2
                result = condition(mid)
                if result == "found":
                    return mid
                elif result == "left":
                    hi = mid - 1
                else:
                    lo = mid + 1
            
            return -1


        def first_position(cards, query):
            """ insert docstring here """

            def condition(mid):
                """ add docstring here """
                if cards[mid] == query:
                    if mid > 0 and cards[mid - 1] == query:
                        return "left"
                    else:
                        return "found"
                elif cards[mid] < query:
                    return "left"
                else:
                    return "right"
            
            return binary_search(0, len(cards) - 1, condition)
        

        def last_position(cards, query):
            """ insert docstring here """

            def condition(mid):
                """ add docstring here """
                if cards[mid] == query:
                    if mid < len(cards) - 1 and cards[mid + 1] == query:
                        return "right"
                    else:
                        return "found"
                elif cards[mid] < query:
                    return "right"
                else:
                    return "left"
            
            return binary_search(0, len(cards) - 1, condition)


        def locate(cards, target):
            """insert docstring here"""
            return first_position(cards, target), last_position(cards, target)
    ```

[def]: ./0-binary_search_linked_lists_complexity.py
# binary search, linked lists and complexity

## problem
- A has a list of numbers numbers obtained by rotating a sorted list an unknown number of times
- write a function to determine the minimum number of times the original sorted list was rotated to obtain the given list
- said function must, at the worst-case scenario, have *O(log N)* complexity where *N* is the length of the list
### assumptions
* assume that the numbers in said list are unique
### example
* list `[5, 6, 9, 0, 2, 3, 4]` is obtained by rotating the list `[0, 2, 3, 4, 5, 6, 9]` three times
### definitions
* **rotating a list** &rarr; to remove the last element of a list and adding it to the first element
    - i.e. popping an element from a list then pre-pending it
    - example: `[3, 2, 4, 1]` rotated once is `[1, 3, 2, 4]`
* **sorted list** &rarr; a list where elements are arranged in increasing order
    - example: `[0, 2, 3, 4, 5, 6, 9]` or `[-1, 2, 3, 4]`
* 1:50:11

## approach
### the problem in simple language
### get a wee bit technical
### assumptions
## method
## solution
### possible solutions
### implemented solution
* [1-binary_search_linked_lists_complexity.py][def]
## discussion
### generalise the solution

[def]: ./1-binary_search_linked_lists_complexity.py
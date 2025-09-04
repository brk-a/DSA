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
## approach
### the problem in simple language
* there exists a sorted list
    - list of ints sorted in ascending order
* said list is rotated x number of times
    - to rotate is to pop an element and pre-pend it to said list
* idea is to find x
    - the minimum number of times said rotations have been made
### get a wee bit technical
* **input**: `nums` &rarr; a sorted, rotated list e.g. `[6, 9, 0, 2, 3, 4, 5]`
* **output**: `rotations` &rarr; number of times said list has been rotated e.g. `2`
* **function signature**:

    ```python
        def count_rotations(nums):
            """ docstring here """

            ...

            return rotations
    ```
### edge cases *et al*
* two variables to account for: size of list and number of rotations
* size and rotations
    * a list of size 10 rotated 4 times
    * a list of size 5 rotated 3 times
    * a list of size `n` rotated `n-1` times
    * a list of size `n` rotated `n` times
    * a list of size `n` rotated `n+1` times
> **note** <br/> a list of size `n` rotated `n` times is the same as rotating said list zero times <br/> a list of size `n` rotated `n+1` times is the same as rotating said list once <br/> there is a pattern here: *a list of size `n` rotated `n+i` times, where i &ge; 0, is the same as rotating said list `i` times and `i` can never be more than `n-1`*
* rotations
    * a list rotated zero times
    * a list rotated once
* size
    * a list of size 0
    * a list of size 1
## method
* 2:04:43
## solution
### possible solutions
### implemented solution
* [1-binary_search_linked_lists_complexity.py][def]
## discussion
### generalise the solution

[def]: ./1-binary_search_linked_lists_complexity.py
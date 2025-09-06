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
> **observation(s)** <br/> - a list of size `n` rotated `n` times is the same as rotating said list zero times <br/> - a list of size `n` rotated `n+1` times is the same as rotating said list once <br/> - there is a pattern here: *a list of size `n` rotated `n+i` times is the same as rotating said list `i` times* <br/> - say we have a list: `[1, 2, 3, 4, 5]`; said list is rotated zero times <br/> - said list rotated once is `[5, 1, 2, 3, 4]`: notice how you can easily tell this by observing that the element at index 1 is smaller than the element to its left <br/> - rotated the original list twice: `[4, 5, 1, 2, 3]`; the element at position 2 is smaller than that on its left <br/> - there is a pattern here: *a list of size `n` is rotated `k` times where `k` is the index of the first element smaller than the element on its left (list of size `n` is rotated `k` times if the value of element at index `k` is smaller than the element at index `k-1` where 0 &le; k &le; n-1)*
* rotations
    * a list rotated zero times
    * a list rotated once
* size
    * a list of size 0
    * a list of size 1
### patterns (and rules)
* a list of size `n` rotated `n` times is the same as rotating said list zero times
* a list of size `n` rotated `n+i` times is the same as rotating said list `i` times where 0 &le; k &le; n-1
* list of size `n` is rotated `k` times if the value of element at index `k` is smaller than the element at index `k-1` where 0 &le; k &le; n-1
* a list of size zero cannot be rotated
* a list of size 1 cannot be rotated
* the first three patterns hold IFF n &gt; 1
> **rule(s)** <br/> - find the length of the list, `li`: if length is zero or 1 return 0, else, compare the value of `li[k+1]` to `li[k]` <br/> - if `li[k+1]` &le; `li[k]` return `k`, else, add one to `k` and compare again <br/> - return zero if the previous condition has not been satisfied when `k` &equals; `n`
## method
* say we have a list, `li`, *viz*: [2, 3, 4, 5, -1]
### **method 1: check the numbers sequentially L-R**
* this is the trivial solution
    - the list is of size 5, that is, `n` &equals; 5
    - have a variable `k` initialised to zero
    - first pass: `k` is zero and `k+1` is 1, therefore, compare `li[1]` to `li[0]`
        - 3 &gt; 2, therefore add 1 to `k`
    - second pass:  `k` is 1 and `k+1` is 2, therefore, compare `li[2]` to `li[1]`
        - 4 &gt; 3, therefore add 1 to `k`
    - third pass: `k` is 2 and `k+1` is 3, therefore, compare `li[3]` to `li[2]`
        - 5 &gt; 4, therefore add 1 to `k`
    - fourth pass: `k` is 3 and `k+1` is 4, therefore, compare `li[4]` to `li[3]`
        - -1 &lt; 5, therefore return 4
* 2:17:33
### **method 2: break the list into half. check one list then the other**
* the list is of size 5, that is, `n` &equals; 5
* have a variable `k` initialised to zero
* have a variable `mid` that will be the mid-point of the list in question
* first pass: `k` is zero and `mid` is 5 // 2 &equals; 3

## solution
### possible solutions
### implemented solution
* [1-binary_search_linked_lists_complexity.py][def]
## discussion
### generalise the solution

[def]: ./1-binary_search_linked_lists_complexity.py
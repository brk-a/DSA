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
## method
* say we have a list, `li`, *viz*: [2, 3, 4, 5, -1]
### **method 1: check the numbers sequentially L-R**
> **rule(s)** <br/> - find the length of the list, `li`: if length is zero or 1 return 0, else, compare the value of `li[k+1]` to `li[k]` <br/> - if `li[k+1]` &le; `li[k]` return `k`, else, add one to `k` and compare again <br/> - return zero if the previous condition has not been satisfied or when `k` &equals; `n`
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
* the code

    ```python
        def count_rotations(nums: list) -> int:
            nums = nums if nums and isinstance(nums, list) else []

            if len(nums) > 1:
                k = 0
                max_len = len(nums)
                while k + 1 < max_len:
                    if nums[k+1] < nums[i]:
                        return k + 1
                    k += 1

            return 0
    ```
### **method 2: break the list into half; check one list then the other**
* *but how do it know...* when to check what sub-list?
    - glad you asked...
    - say we have two lists: list A, `[7, 8, 1, 3, 4, 5, 6]`, and list B `[1, 2, 3, 4, 5, -1, 0]`
    - the number of rotations of list A is 2; that of B is 5
    - the answer lies to the **left** of the middle element in list A and on the **right** of the middle element in list B
    - consider list A: the middle element is 3
        - is 3 greater than the element, if any, to its right or less than the element, if any on its left? if yes, check the sub-list to its right, else, check the one to its left
        - sub-lists of A are: `[7, 8, 1, 3]` and `[4, 5, 6]`
        - 3 &lt; 4, therefore, check the sub-list to 3's left, that is, `[7, 8, 1, 3]`
        - repeat the steps with the sub-list: mid element is 1 and sub-lists are `[7, 8, 1]` and `[3]`
            - 1 &lt; 7, therefore, stop
        - what index is element `1` in list A? *Ans:* 2. this is the correct answer
        - **recall:** input list has no duplicates, therefore, no need to account for those
    - consider list B: the middle element is 4
        - is 4 greater than the element, if any, to its right or less than the element, if any on its left? if yes, check the sub-list to its right, else, check the one to its left
        - sub-lists of B are: `[1, 2, 3, 4]` and `[5, -1, 0]`
        - 4 &lt; 5, therefore, check the sub-list to 4's left, that is, `[1, 2, 3, 4]`
        - ***we hebben een serieus probleem*** because we know that the algo should choose thelist to element `4`'s right, that is, `[5, -1, 0]`
            - this algo does not work when the answer lies to thr right of the middle element
    - **new algo:** see if the middle element is smaller than the rightmost element
        - if yes, answer lies in the left sub-list, else, right sub-list
        - repeat until the rightmost element is the smallest
    - consider list B again: the middle element is 4
        - is 4 greater than the rightmost element, if any? if yes, check the sub-list to its left, else, check the one to its right
        - sub-lists of B are: `[1, 2, 3, 4]` and `[5, -1, 0]`
        - 4 &gt; 0, therefore, check the sub-list to 4's right, that is, `[5, -1, 0]`
        - repeat the steps with the sub-list: mid element is -1 and sub-lists are `[5, -1]` and `[0]`
            - -1 &lt; 0, therefore, check the sub-list to `-1`'s left, that is, `[5, -1]`
            - mid element is -1 again; sub-list to consider is `[5, -1]`
                - -1 is the rightmost element, therefore, stop
        - what index is element `-1` in list B? *Ans:* 5. this is the correct answer
    - verify new algo using list A: the middle element is 3
        - is 3 greater than the rightmost element, if any? if yes, check the sub-list to its left, else, check the one to its right
        - sub-lists of A are: `[7, 8, 1, 3]` and `[4, 5, 6]`
        - 3 &lt; 6, therefore, check the sub-list to 3's left, that is, `[7, 8, 1, 3]`
        - mid element is 1 and sub-lists to consider is `[7, 8, 1]`
            - 1 is the rightmost element, therefore, stop
        - what index is element `1` in list A? *Ans:* 2. this is the correct answer
    - it appears that the new algo works in both scenarios
* **why the new algo works...**
    - the input list, `nums`, of length `n` is a sorted, rotated list, therefore, all elements, if any, to the right of the element whose index we want to return are in increasing order and the ones to its, if any, left are in decreasing order
    - as such, given a middle element in said list, `mid`, the answer must lie to the left of said midpoint if `mid` is lesser than the element in the n<sup>th</sup>-1 position (`mid` &lt; `nums[n-1]`). `nums[n-1]` by the way is the same as `nums[-1]` in python
    - it follows that the answer must lie to the right of said midpoint if `mid` is greater than the element in the n<sup>th</sup>-1 position (`mid` &gt; `nums[n-1]`)
* **the rule(s):** <br/>- have a list, `nums`, of size `n` <br/> - have a variable `mid` that will be the mid-point of said list (assume that `mid` is at index `k`) <br/> - repeat the first two steps using the sub-list `nums[:k]`if `mid` &lt; `nums[-1]`,  else, use `nums[k+1:]`
* recall the list, `li`, *viz*: [2, 3, 4, 5, -1]
    - initial state: `n` is 5, `mid` is 4 and `k` is 2
    - first pass: compare `mid` to `li[-1]`, therefore, compare 4 to -1
        - 4 &gt; -1, therefore, the answer must lie on the right of 4
    - second pass: repeat steps on `li[3:]`; `n` is 2, `mid` is 5 and `k` is 3
        - compare `mid` to `li[-1]`, therefore, compare 5 to -1
        - 5 &gt; -1, therefore, the answer must lie on the right of 5
    - third pass: repeat steps on `li[4:]`; `n` is 1, `mid` is -1 and `k` is 4
        - compare `mid` to `li[-1]`, therefore compare -1 to -1
        - -1 &equals; -1, therefore, the answer must be the index of -1 aka `k`
    - return `k`
* the code

    ```python
        def count_rotations(nums: list) -> int:
            """ insert docstring here """
            nums = nums if nums and isinstance(nums, list) else []

            if len(nums) > 1:
              pass
            # 2:23:26

            return 0
    ```

## solution
### possible solutions
### implemented solution
* [1-binary_search_linked_lists_complexity.py][def]
## discussion
### *method 1: linear search**
* time complexity of *O(N)*; space complexity of *O(1)*
### *method 2: binary search**
### generalise the solution

[def]: ./1-binary_search_linked_lists_complexity.py
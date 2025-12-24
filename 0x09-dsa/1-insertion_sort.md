# insertion sort

## 0. intro
### 0.1. pseudo-code of insertion sort algo

```plaintext
    Algorithm Insertion-sort(A):
        for i = 1 to A.length-1:  .................. line 1
            key = A[i]  ............................ line 2
            // insert key into sorted section  ..... line 3
            j = i - 1  ............................. line 4
            while j >= 0 and A[j] > key:  .......... line 5
                A[j+1] = A[j]  ..................... line 6
                j = j - 1  ......................... line 7
            A[j+1] = key  .......................... line 8
```

### 1. how it works
* algo takes in a linear data structure such as an array and sorts it in place
> **idea:** use a *moving wall* to separate the input array into two parts: the sorted part on the left and the unsorted part on the right. focus on the element on the right that is nearest to the wall. figure out where to place said element on the left, move the wall one step to the right and repeat process until there are no elements on the right of said wall 
* how TF do you figure out where to place the element in question?
    * glad you asked ...
    * say the element in question is at index `i`
    * start with the element on its immediate left, index `i-1`
        * swap the elements if the element at `i` is less than the element at `i-1`, else, move the wall one step to the right
        * repeat previous step until the element in question is &ge; the one on its left or it is at the leftmost position
* variable `key` is the element in question
* there are two loops
    * a `for` loop that traverses the linear data structure L-R
    * a `while` loop that figures out where `key` should be on the left of the wall
* the outer loop starts at index one and not zero because the sortrd list must, by definition, have one element to start with
    * also, starting at index one (or pointing to the element at `i+1` generally) is how it moves the wall 
### 2. time complexity
* $T(n) = c_1n + c_2(n-1) + c_4(n-1) + c_5\sum_{j=2}^nt_j + c_6\sum_{j=2}^n(t_j - 1) + c_7\sum_{j=2}^n(t_j - 1) + c_8(n - 1)$
    * $c_n$ is the constant at line $n$ in the pseudo-code above
        * example: constant `key` on line 2 is $c_2$
        * example: the `while` loop is represented by $c_5\sum_{j=2}^nt_j + c_6\sum_{j=2}^n(t_j - 1) + c_7\sum_{j=2}^n(t_j - 1)$
        * example: the `for` loop is represented by $c_1n$: a `for` loop runs $n-1$ times, hoewever, the pointer must also move from zero to 1 in this case because we start at inded one, not zero. $(n-1) + 1 = n$
    * $j$ is the index of the *moving window*
    * $t_j$ is the time taken to achieve the j<sup>th</sup> iteration of swaps
    * $n$ is the input array size
* best case scenario: input array is sorted in ascending order
    * the `while` loop does not execute
    * no swapping required
    * $T(n) \approx cn$ : time complexity is linear
* worst case scenario: input array is sorted in descending order
    * `while` loop is triggered n-1 times per iteration
    * swapping must happen at every iteration
    * $T(n) \approx c\frac{n}{2} + c\frac{n^2}{2}$: time complexity is polynomial in general and quadratic in particular
* $T(n)$ can be simplified *viz*: $T(n) \approx \frac{c}{2}(n + n^2) + d$
    * the time complexity of is quadratic

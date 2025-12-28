# divide and conquer (plus merge sort algo and master theorem)

## 0. intro
### 0.1. context
* given a recurrence relation $T(n) = aT(\frac{n}{b}) + f(n)$ where
    * $a$ &rarr; number of sub-problems in the recursion
    * $b$ &rarr; factor by which the problem size is divided
    * $f(n)$ &rarr; time required at current step outside the recursion
* if $f(n) \in \Omicron(n^{log_b{a}})$, then $T(n) = \Theta(n^{log_b{a}})$
### 0.2. merge sort algo pseudo-code  using recursion
```plaintext
MergeSort(A):
    if A.length > 1:
    /* divide phase */
        mid = A.length // 2
        // clone two different sub-arrays
        leftHalf = clone(A[0:mid])
        rightHalf = clone(A[mid:A.length])
    /* conquer phase */
        // recursive call
        MergeSort(leftHalf)
        MergeSort(rightHalf)
    /* combine phase */
        // 3 pointers
        i, j, k = 0, 0, 0
        while i < leftHalf.length && j < rightHalf.length:
            if leftHalf[i] < rightHalf[i]:
                A[k] = leftHalf[i]
                i++
            else:
                A[k] = rightHalf[j]
                j++
            k++
        while i < leftHalf.length:
            A[k] = leftHalf[i]
            i++
            k++
        while j < rightHalf.length:
            A[k] = rightHalf[j]
            j++
            k++
```

## 1. merge sort algo
* a classic divide-and-conquer style algo
    * **divide phase:** unsorted array is broken down into n sub-arrays of length one
    * **combine phase:** intermediate step. sorted sub-arrays are combined to form a sorted array (ascending order by default)
    * **conquer phase:** neighbouring sub-arrays are sorted pairwise
* requires memory to store sub-arrays each of which is a deep clone (deep copy of the sub-array)
### 1.1. recurrence relations
* time complexity, $T(n)$, depends on the following
    * time it takes to perform the divide ops
    * time it takes to perform recursive calls in the conquer phase
    * time it takes to perform combination ops
* we know that
    * there will be $n-1$ recursion calls where $n$ is `A.length`
        * two recursive calls on half the previous input size $n$ times 
        * $2 \times T(\frac{n}{2})$
    * it takes constant time to perform one divide op
        * $c$ time; $n$ times
        * $c_1 \times n$
    * it takes constant time to perform  one combination op
        * $c$ time; $n$ times
        * $c_2 \times n$
* the time complexity is $T(n) = c_1n + 2T(\frac{n}{2}) + c_2n$
    * this can be simplified *viz:*
        * let $c_1 + c_2 = c$
        * $T(n) = 2T(\frac{n}{2}) + cn$
        * where $2T(\frac{n}{2})$ is work done in the current step and $cn$ is the work done during the recursive call

|sub-problem size|number of sub-problems|total merging time for all problems of this size|
|:---:|:---:|:---:|
|$n$|1|$c \times n = cn$|
|$\frac{n}{2}$|2|$2 \times \frac{cn}{2} = cn$|
|$\frac{n}{4}$|4|$4 \times \frac{cn}{4} = cn$|
|$\frac{n}{8}$|8|$8 \times \frac{cn}{8} = cn$|
|...|...|...|
|$1$|$n$|$n \times c = cn$|
|**Total**||$ \Sigma_{k} 2^k cn = cn \times log(n)$|

> **pattern:** the total merging time at any step, regardless of the sub-problem size, is $cn$

* time complexity function, $T(n)$, is, therefore, $cn \times log(n)$
    * $c$ is insignificant as far as the upper bound is concerned, therefore, $T(n) \in \Omicron(n\thinspace log\thinspace n)$
### 1.2. master theorem
#### 1.2.1. definition
* given a recurrence relation $T(n) = aT(\frac{n}{b}) + f(n)$ where
    * $a$ &rarr; number of sub-problems in the recursion
    * $b$ &rarr; factor by which the problem size is divided
    * $f(n)$ &rarr; time required at current step outside the recursion
* if $f(n) \in \Omicron(n^{log_b{a}})$,  then  $T(n) = \Theta(n^{log_b{a}})$
* if $f(n) \in \Theta(n^{log_b{a}})$,  then  $T(n) = \Theta(n^{log_b{a}}log(n))$
* if $f(n) \in \Omega(n^{log_b{a}})$ and  if $a \times \frac{n}{b} \leq k \times f(n)$ for some $k \lt 1$ and sufficiently large $n$, then  $T(n) = \Theta(f(n))$
#### 1.2.2. apply it to merge sort recurrence relation
* recurrence relation for merge sort is $T(n) = 2T(\frac{n}{2}) + cn$
    * a = 2, b = 2, f(n) = cn
* compare f(n) to n<sup>log<sub>2</sub>2</sup>
    * cn grows at the same rate as n<sup>log<sub>2</sub>2</sup>
    * f(n) fulfills the second condition
* therefore, $T(n) \in \Theta(n^{log_2{2}}log(n)) = \Theta(n\thinspace log\thinspace n)$
#### 1.2.2. apply it to binary search recurrence relation
* recurrence relation for binary search is $T(n) = T(\frac{n}{2}) + c$
    * a = 1, b = 2, f(n) = c
* compare f(n) to n<sup>log<sub>2</sub>1</sup>
    * c grows at the same rate as n<sup>log<sub>2</sub>1</sup>
    * f(n) fulfills the second condition
* therefore, $T(n) \in \Theta(n^{log_2{1}}log(n)) = \Theta(log\thinspace n)$
# divide and conquer (plus quicksort algo)

<div style="text-align: justify;">

## 0. pseudo-code for quick sort algo

```plaintext
    algorithm QuickSort(A, first, last):
        if first < last:
            splitPoint = partition(A, first, last)
            QuickSort(A, first, splitPoint-1)
            QuickSort(A, splitPoint+1, last)
    
    partition(A, first, last):
        pivotValue = A[first]
        left = first+1
        right = last

        done = False
        while not done:
            while left <= right && A[left] <= pivotValue:
                left += 1
            while A[right] >= pivotValue && right >= left:
                right -= 1
            
            if right < left:
                done = True
            else:
                temp = A[left]
                A[left] = A[right]
                A[right] = temp
        temp = A[first]
        A[first] = A[right]
        A[right] = temp

        return right
```

## 1. quick sort algo
* sorts input in place; no deep copies
### 1.1. time complexity analysis
#### 1.1.1. time complexity analysis of the the `partition` bit

```plaintext

    --- snip ---

    partition(A, first, last):
        pivotValue = A[first] ................................. line 8 ..... 1 step
        left = first+1 ........................................ line 9 ..... 1 step
        right = last .......................................... line 10 .... 1 step

        done = False .......................................... line 11 .... 1 step
        while not done:
            while left <= right && A[left] <= pivotValue: ..... line 13 .... n steps
                left += 1 ..................................... line 14 .... 1 step
            while A[right] >= pivotValue && right >= left:
                right -= 1 .................................... line 16 .... 1 step
            
            if right < left:
                done = True ................................... line 19 .... 1 step
            else:
                temp = A[left] ................................ line 21 .... 1 step
                A[left] = A[right] ............................ line 22 .... 1 step
                A[right] = temp ............................... line 23 .... 1 step
        temp = A[first] ....................................... line 24 .... 1 step
        A[first] = A[right] ................................... line 25 .... 1 step
        A[right] = temp ....................................... line 26 .... 1 step

        return right
```
> **idea:** <br/> move two pointers, `left` and `right` such that the elements less than `pivotValue` are on its left and those greater are on its right <br/> stop said movement when `left` &gt; `right` and swap `A[right]` and `pivotValue` 
* at worst, `left` will traverse the entirety of `A`
    * time function *viz*: $T(n) = cn$
#### 1.1.2. time complexity analysis of the recursive bit

```plaintext
    algorithm QuickSort(A, first, last):
        if first < last:
            splitPoint = partition(A, first, last) ..... line 3
            QuickSort(A, first, splitPoint-1) .......... line 4
            QuickSort(A, splitPoint+1, last) ........... line 5

            --- snip ---
```

> **idea:** <br/> make recursive calls as on partitions of `A` as long as `first` &lt; `last`
* at worst, the pivot is either the smallest or largest always
    * example: pivot value is always the smallest in the window
        * A = [1, 3, 4, 6, 7, 10, 11]. `A.length` = 7, `start` = 0 and `last` = 6
    
    ```plaintext
        QuickSort(A, 0, 6)
               ⬇
        QuickSort(A, 1, 6)
               ⬇
        QuickSort(A, 2, 6)
               ⬇
        QuickSort(A, 3, 6)
               ⬇
            ...
               ⬇
        QuickSort(A, 6, 6)
    ```

    * there will be $n-1$ recursive calls; the size of the input array reduces by one in each call
    * recurrence relation *viz*: $T(n) = T(n-1) + cn$
* at best, the pivot is always the middle element
    * example: A = [4, 2, 6, 1, 3, 5, 7]. `A.length` = 7, `start` = 0 and `last` = 6
    
        ```plaintext
                                            QuickSort(A, 0, 6)
                                                    ⬇
                                QuickSort(A, 0, 2) and QuickSort(A, 4, 6)
                                                    ⬇
            QuickSort(A, 0, 0), QuickSort(A, 2, 2), QuickSort(A, 4, 4) and QuickSort(A, 6, 6)
        ```

    * the input size (problem size) is halved at each recursive call
    * recurrence relation *viz*: $T(n) = 2T(\frac{n}{2}) + cn$
#### 1.1.3. combine both bits and come up with bounds
##### 1.1.3.1. upper bound of worst-case scenario
* $T(n) = T(n-1) + cn \ \text{where} \ c \ \text{is a constant}$
    * `n-1` shows that the sub-array size is reduced by one in each successive all
    * `cn` represents the work done by the `partition` function at the current step

|sub-problem size|number of sub-problems|total sorting time for all problems of this size|
|:---:|:---:|:---:|
|$n$|1|$c \times n = cn$|
|$n-1$|1|$c \times (n-1) = c(n-1)$|
|$n-2$|1|$c \times (n-2) = c(n-2)$|
|$n-3$|1|$c \times (n-3) = c(n-3)$|
|...|...|...|
|$2$|1|$c \times (n-(n-2)) = 2c$|
|$1$|1|$c \times (n-(n-1)) = c$|
|**Total**||$\Sigma_{k=1}^n c = \frac{n^2 + n}{2} \times c$|

> **recall:** <br/> $\Sigma_{k=1}^n c =  c + 2c + ... + c(n-1) + cn \\[1em]$ <br/> $(1 + 2 + ... + (n-1) + n) \times c \\[1em]$ <br/> $((1+n) \frac{n}{2}) \times c \\[1em]$ <br/> $\frac{n^2 + n}{2} \times c \\[1em]$ <br/>
* **upper bound** of time complexity function, $T(n)$, is, therefore, $\frac{n^2 + n}{2} \times c$
    * $c$ and the non-leading polynomials are insignificant as far as the upper bound is concerned, therefore, $T(n) \in \Omicron(n^2)$
##### 1.1.3.2. upper bound of best-case scenario
* $T(n) = 2T(\frac{n}{2}) + cn$
    * the scalar, `2`, tells us thet there are two recursive problems spawned by each parent
    * the scalar, `1/2`, tells us that the input size, `n`, is halved in each recursive call
    * `cn` represents the work done at current step outside the recursion

|sub-problem size|number of sub-problems|total sorting time for all problems of this size|
|:---:|:---:|:---:|
|$n$|1|$c \times n = cn$|
|$\frac{n}{2}$|2|$2 \times \frac{cn}{2} = cn$|
|$\frac{n}{4}$|4|$4 \times \frac{cn}{4} = cn$|
|$\frac{n}{8}$|8|$8 \times \frac{cn}{8} = cn$|
|...|...|...|
|$1$|$n$|$n \times c = cn$|
|**Total**||$\Sigma_{k} 2^k cn = cn \times log(n)$|

> **pattern:**<br/> the total merging time at any step, regardless of the sub-problem size, is $cn$

* **upper bound** of time complexity function, $T(n)$, is, therefore, $cn \times log(n)$
    * $c$ is insignificant as far as the upper bound is concerned, therefore, $T(n) \in \Omicron(n \ log \ n)$

</div>
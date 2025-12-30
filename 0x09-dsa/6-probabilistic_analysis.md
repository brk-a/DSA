# probabilistic analysis (with quicksort)

<div style="text-align: justify;">

## 0. intro
### 0.1. pseudo-code for quick sort algo

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

### 0.2. best and worst case for quick sort
* recall from [5-divide_and_conquer][def]
    * upper bound of best case is $T(n) \in \Omicron(n \ log \ n)$
    * upper bound of worst case is $T(n) \in \Omicron(n^2)$
* the upper bound of average case, therefore, lies somewhere in between
    * given an array as input, it is not likely that said array has the characteristics that fit the best or worst-case; there is an amount of randomness involved
    * to account for said randomness, bring out the stochastic analysis tool-kit

### 0.3. regular vs probabilistic time analysis

||regular|probabilistic|
|:---|:---|:---|
|focus|best, worst and average-case for fixed input|expected performance over all possible randon outcomes|
|randomness|assumes no randomness in input|accounts for randomness (this is the point of the approach)|
|scope|examines specific scenarios based on input characteristics|analyses expected behaviour across all random executions|

### 0.4. pre-requisites from elementary probability
* uniform distribution $U(i, \ j)$
* expectation of a random variable $\mathbb{E}[\cdot]$
* linearity of expectation

## 1. uniform dist, expectation of a random var and linearity of expectation
### 1.1. uniform dist
* a random variable, `X`, follows a uniform distribution if all outcomes in a specified range are equally likely
* assume `X` takes `n` discrete outcomes each with equal probability: the PMF (probability mass function) *viz*:<br/><br/>
$P(X=x) = \frac{1}{n}, \ x \in \{x_1, x_2, ..., x_n\}$ <br/>
### 1.2. expectation of a random variable
* the expected value of a random variable, `X`, is the measure of central tendancy said variable takes
    * *measure of central tendancy* is a fancy way of implying mean (average), median or mode: in this case the expected value is, simply, the mean (do you see how we get to the average-case scenario?)
* in other words, the expected value of a random variable is the weighted average of all possible outcomes where said weights are probabilities
* the expected value of a discrete random variable, `X`, denoted as $E[X]$ *viz*: <br/><br/>
$\mathbb{E}[X] = \sum_x x \cdot P(X=x)$ <br/>
### 1.3. linearity of expectation
* the expectation of a sum of random variables is the sum of the expectation of said variables
    * let $X_1$ and $X_2$ be random variables <br/><br/>
    $\mathbb{E}[X_1 + X_2] = \mathbb{E}[X_1] + \mathbb{E}[X_2] \\[1em]$ <br/>
* scalars (aka constants or coefficients) of a random variable scale its expectation
    * let $c$ be a scalar and $X_1$ and random var<br/><br/>
    $\mathbb{E}[cX_1] = c \mathbb{E}[X_1]$ <br/>

## 2. average case for quick sort
* the average case recurrence relation *viz*: <br/><br/>
$T(n) = (n-1) + \frac{1}{n} \sum_{k=0}^{n-1}(T(k) + T(n-k-1))$ <br/>

### 2.1. WTF is this?
* glad you asked...
* there are two parts
    * work done at current step: $(n-1)$
        * the `partition` method traverses the arry `n-1` times
    * work done in the recursive step: $\frac{1}{n} \sum_{k=0}^{n-1}(T(k) + T(n-k-1))$
* the value of the work done in the recursive step is the **expected value** of cost of recursive calls
* i hear you asking WTF $k$ is and from where TF it came...
    * ea...sy! say the input array is randomly ordered
    * the pivot is equally likely to partition the array at any position, `k`, where $0 \leq k \leq (n-1)$
    * each of these $n$ positions is equally likely to occur
        * in other words, $P(k) = \frac{1}{n}$
* "alright", i hear you say, "explain bloody $T(k)$ and $T(n-k-1)$"
    * in the context of the quick-sort algo
        * $k$ is the random var that represents the split point
        * $T(k) + T(n-k-1)$ is the cost of recursive calls given a split at position $k$
        * $P(k) = \frac{1}{n}$ is the probability of the pivot splitting the array at position $k$
    * $\mathbb{E}[cost] =  \sum_{k=0}^{n-1} P(k) \cdot (T(k) + T(n-k-1))$
    * recall $P(k) = \frac{1}{n}$
    * this is how we end up with $\frac{1}{n} \sum_{k=0}^{n-1}(T(k) + T(n-k-1))$
### 2.2. substitute method
* we have seen how the recurrence tree method and master theorem are used to find the upper bound; introducing the substitute method...
* 
* **steps**
    * make an educated guess
    * prove (or disprove) said guess using induction
* WTF is induction?
    * glad you asked ...
    * say we have the following: prove that $S(n) = 1 + 2 + ... + n = \frac{n(n+1)}{2}$
    * how TF would we prove that?
    * it is not that difficult, actually
        * say you work out: 10 burpees followed by nine, all the way to one. the sum of your burpees will be 10 + 9 + 8 + ... + 1 = 55
        * we can express the sum this way: take the sum of the highest and lowest number in the series followed by the second-highest and second-lowest etc 
            * (10 + 1), (9 + 2), ..., (6 + 5)
        * notice how there each pair adds up to 11
            * 10+1=11, 9+2=11, ..., 6+5=11
        * there are 5 pairs; their sum is 55
            * 11 * 5 = 55
        * notice how 11 = 10 + 1, 11*10 = 110 and 110/2 = 55; we may have a pattern here
            * the sum of $n$ consecutive whole numbers starting from 1 ending at $n$ is $\frac{n(n+1)}{2}$
        * let us test this with n = 20
            $$
                S(20) = 1 + 2+ 3 + ... + 20 \\[1em]
                S(20) = (20+1) + (19+2) + ... + (11+10) \\[1em]
                S(20) = 21 \times 10 \\[1em]
                S(20) = 210 \\[1em]
                
                \text{test using the pattern we noticed} \\[1em]

                S(20) = \frac{20(21)}{2}\\[1em]
                S(20) = \frac{420}{2}\\[1em]
                S(20) = 210\\[1em]

                \text{the pattern appears to work}
            $$
         * let us test this with n = 21
            $$
                S(21) = 1 + 2+ 3 + ... + 21 \\[1em]
                S(21) = (21+1) + (20+2) + ... + (12+10) + 11 \\[1em]
                \text{notice how the last term has no partner; we hebben een serieus probleem}\\[1em]
                \text{actually not; we do not hebben een serieus probleem. calma, povo; calma}\\[1em]
                S(21) = (22 \times 10) + 11 \\[1em]
                S(21) = 231 \\[1em]
                
                \text{ test using the pattern we noticed} \\[1em]

                S(21) = \frac{21(22)}{2}\\[1em]
                S(21) = \frac{462}{2}\\[1em]
                S(21) = 231\\[1em]

                \text{the pattern appears to work even for odd-numbered terms}
            $$
        * we can conclude that generally, $S(n) = 1 + 2 + ... + n = \frac{n(n+1)}{2}$ ✅
    * induction, therefore, is a **reasoning process where one moves from the specific to the general case**
    * there are three steps during induction
        * test the statement/proposition in the trivial/base case
        * test said statement/proposition when $n$ is an arbitrary number $k$
        * test said statement/proposition when $n$ is an arbitrary number $k+1$
    * the statement/proposal holds IFF it passes all tests
    * in our case
        * the trivial test is $n=1$
            * $S(1) = \frac{1(1+1)}{2} = \frac{2}{2} = 1$ ✅
        * the case where $n=k$
            * $S(k) = 1 + 2 + ... + k = \frac{k(k+1)}{2}$ ✅
        * the case where $n=(k+1)$
            * $S(k+1) = S(k) + (k+1) = 1 + 2 + ... + k + (k+1) = \frac{k(k+1)}{2} + (k+1) = \frac{(k+1)(k+2)}{2}$ ✅

#### 2.2.1 use substitute method to find the upper bound of the average case for quick sort
* given the recurrence relation <br/><br/>
$T(n) = (n-1) + \frac{1}{n} \sum_{k=0}^{n-1}(T(k) + T(n-k-1))$ <br/>
<br/>prove that in the average case <br/><br/>
$T(n) \leq cn \, log \, n$ <br/>

* step one: trivial case
    * set n to 1
    * $T(1) \leq 1 \cdot log(1)$ ✅
* step two: n = k
    * assume that `n-k-1` tends to `n` in the worst case $T(n-k-1) \leq T(n)\, \forall \, k \in n \, \text{that is, the worst-case recurrence time for a sub-problem is} T(n)$
        * the recurrence relation can be simplified to $T(n) = (n-1) + \frac{2}{n} \sum_{k=0}^{n-1}T(k)$
    * it follows that $T(k) \leq c \cdot k \, log(k)$ ✅
* step three: n = k+1
    * the recurrence relation has been simplified to $T(n) = (n-1) + \frac{2}{n} \sum_{k=0}^{n-1}T(k)$
    * it follows that $T(n) \leq (n-1) + \frac{2}{n} \sum_{k=0}^{n-1} ck \, log \, k$ ✅
* 2:54:15

* **what we have established**
    * at n=1, quicksort does not need to run; an array of length one is already sorted
    * at n=k



</div>

[def]: ./5-divide_and_conquer.md
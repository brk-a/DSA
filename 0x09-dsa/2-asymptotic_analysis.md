# asymptotic analysis

* aka **Big-O Notation**

<div style="text-align: justify;">

## 0. time complexity analysis vs asymptotic analysis
### 0.1. time complexity analysis
* describes the input-dependent behaviour of an algo
    * **worst case** &rarr; input that cases the algo to perform the most work
    * **average case** &rarr; expected performance across all possible inputs often weighted by probability of occurrence
    * **best case** &rarr; input that cases the algo to perform the least work
### 0.2. asymptotic analysis
* describes the theoretical guarantees on the performance of the algo
    * measures how an algo performs as its input increases
    * allows for comparison of relative performance between different algorithms
    * describes an algo's time complexity or growth rate using asymptotic notation(s)
* is agnostic (does not GAF) to the characteristics of the input
## 1. growth of functions
### 1.1. notation
* $O$ represents upper bound of a time complexity function
* $\Omega$ represents lower bound of a time complexity function
* $\Theta$ represents both upper and lower bounds of a time complexity function
### 1.2. formal definitions
#### 1.2.1. upper bound
* $O(g(n)) = \{f(n):\medspace  \ni c, n_0 > 0 \medspace such \medspace that\medspace f(n) \leq cg(n)\: \forall n \geq n_0\}$
* $O(\cdot)$ is used to asymptotically *upper-bound* a function
* think of $f(n) \in O(g(n))$ as corresponding to $f(n) \leq g(n)$
> **for the americans at the back:** given a time complexity function, `f(n)`, and another function, `g(n)`, the latter will be the upper bound of the former provided that there are two constants to sustain a relationship between said functions. this applies in a time vs input graph
* $c$ and $n_0$ are the constants that sustain said relationship: the latter is the specific size of the input
* the relationship *viz*: for input of size $n_0$ and above, as said input &rarr; &infin;,  $f(n) \lt cg(n)$
* **random quiz:** given f(n) = 2n + 2 and g(n) = n<sup>2</sup>, is g(n) the upper bound of f(n)?
    * both time and input size cannot be negative, therefore, we are dealing with the first quadrant of the cartesian plane
    * at what point is g(n) = f(n)?

    $$
        n^2 = 2n + 2 \\[1em]
        n^2 -2n -2 = 0 \\[1em]
        n = \frac{2 \pm \sqrt{12}}{2} \\[1em]
        n = 1 \pm \sqrt{3} \\[1em]
        we\medspace are\medspace only\medspace interested\medspace in\medspace the\medspace solution\medspace where\medspace n \geq 0 \\[1em]
        \therefore n = 1+\sqrt{3} \approx 2.7\medspace (1 \medspace d.p.)\medspace or\medspace 3\medspace (0 \medspace d.p.)
    $$

    * at what point, if any, is g(n) &gt; f(n)?
        * both functions are non-negative for n &ge; zero
        * g(n) &lt; f(n) for 0 &le; n &lt; 2.7
        * g(n) &gt; f(n) for n &gt; 2.7
        * $\therefore g(n) \gt f(n) \: \forall n \geq 3$
    * is there a constant we can multiply g(n) by to make it larger than f(n)?
        * yes: any real number greater than zero (recall: we just saw that g(n) = f(n) at n &approx; 2.7)
    * we have fulfilled both conditions, therefore, g(n) is an upper bound of f(n) ✅ 
        * $f(n) \in O(n^2)$
* **another random quiz:** given f(n) = 2n + 2 and g(n) = n, is g(n) the upper bound of f(n)?
    * both time and input size cannot be negative, therefore, we are dealing with the first quadrant of the cartesian plane
    * at what point is g(n) = f(n)?

    $$
        n = 2n + 2 \\[1em]
        -n = 2 \\[1em]
        n = -2 \\[1em]
        we\medspace are\medspace only\medspace interested\medspace in\medspace the\medspace solution\medspace where\medspace n \geq 0 \\[1em]
        \therefore n \medspace has \medspace no \medspace viable \medspace solution
    $$

    * at what point, if any, is g(n) &gt; f(n)?
        * at any point where n &lt; -2
        * the slope of g(n) &lt; that of f(n) and the constant in f(n) makes sure that f(n) &gt; zero for all n &ge; zero
    * is there a constant we can multiply g(n) by to make it larger than f(n)?
        * yes: any real number greater than two
        * example: set c to 5 and n<sub>0</sub> to 1
            * $cg(n)$ becomes $5n$
            * $cg(n) = f(n)$ at n &approx; 0.6 (1 \medspace d.p.) or 1 (0 \medspace d.p.)$
            * for any input size greater than one, $cg(n) \gt f(n)$
            * **conslusion:** $cg(n) \gt f(n) \medspace \forall c > 2\medspace and\medspace n_0 \geq 1$
    * we have fulfilled both conditions, therefore, g(n) is an upper bound of f(n) ✅ 
        * $f(n) \in O(n)$
> the result of the second quiz is contrarian/counter-intuitive: 2n+2 &gt; n as long as n &gt; -2, however, the *c* and *n<sub>0</sub>* are not fixed <br/> as long as you can find a suitable *c* and *n<sub>0</sub>* within the bounds of *n* and *t*, you can have as many bounds as you like
* **random quiz 3:** given f(n) = n<sup>2</sup> + 3n + 9 and g(n) = n<sup>2</sup>, is g(n) the upper bound of f(n)?
    * both time and input size cannot be negative, therefore, we are dealing with the first quadrant of the cartesian plane
    * at what point is g(n) = f(n)?

    $$
        n^2 = n^2 + 3n + 9 \\[1em]
        -3n = 9 \\[1em]
        n = -3 \\[1em]
        we\medspace are\medspace only\medspace interested\medspace in\medspace the\medspace solution\medspace where\medspace n \geq 0 \\[1em]
        \therefore n \medspace has \medspace no \medspace viable \medspace solution
    $$

    * at what point, if any, is g(n) &gt; f(n)?
       * at any point where n &lt; -3
        * the slope of g(n) &equals; that of f(n) and the other terms in f(n) makes sure that f(n) &lt; g(n) for all n &lt; -3
    * is there a constant we can multiply g(n) by to make it larger than f(n)?
        * yes: any real number greater than one
        * example: set c to 2
            * $cg(n)$ becomes $2g(n)$
            * consider these terms in f(n): `3n + 9`: at what point is `cg(n)` = `f(n)`?

                $$
                    2n^2 = n^2 + 3n + 9 \\[1em]
                    n^2 -3n - 9 = 0 \\[1em]
                    n = \frac{3 \pm \sqrt{45}}{2} \\[1em]
                    n = \frac{3 \pm 3 \sqrt{5}}{2} \\[1em]
                    we\medspace are\medspace only\medspace interested\medspace in\medspace the\medspace solution\medspace where\medspace n \geq 0 \\[1em]
                    \therefore n = \frac{3 + 3 \sqrt{5}}{2} \approx 4.9\medspace (1 \medspace d.p.)\medspace or\medspace 5\medspace (0 \medspace d.p.) 
                $$

            * **conclusion:** $cg(n) \gt f(n) \medspace \forall c > 1\medspace and\medspace n_0 \geq 5$
    * we have fulfilled both conditions, therefore, g(n) is an upper bound of f(n) ✅ 
        * $f(n) \in O(n^2)$
> **handy shortcut:** only look at the leading terms (terms with the highest power) when the terms of the functions you are dealing with have no negative coefficients
* **random quiz 4:** prove that the time complexity function, $T(n) = 8n + 5$ is $\Omicron(n)$
    * strategy: find a constant c &gt; 0 and int n<sub>0</sub> &ge; 1 such that for every integer n &ge; n<sub>0</sub>, 8n + 5 &le; c*n
    * we are dealing with a linear function: the slope of said func determines how quickly it grows
        * set c &gt; 8 to make g(n) grow faster than f(n)
        * set c to 9
    * where does c*g(n) equal f(n)?

    $$
        9n = 8n + 5 \\[1em]
        n = 5
    $$

    * at what point does c*g(n) become greater than f(n)?
        * at n = 5

        $$
            9n - (8n + 5) \geq 0 \\[1em]
            n - 5 \geq 0 \\[1em]
            n \geq 5 \\[1em]
        $$

        |n|8n+5|9n|
        |:---:|:---:|:---:|
        |1|13|9|
        |2|21|18|
        |3|29|27|
        |4|37|36|
        |5|45|45|
        |6|53|54|
        |7|61|63|
        |...|...|...|

        * therefore, n<sub>0</sub> = 5
    * **conclusion:** $cg(n) \gt f(n) \medspace \forall c > 8\medspace and\medspace n_0 \geq 5$
    * we have fulfilled both conditions, therefore, g(n) is an upper bound of f(n) ✅ 
        * $f(n) = 8n + 5 \in \Omicron(n)$
#### 1.2.2. lower bound
* $\Omega(g(n)) = \{f(n):\medspace  \ni c, n_0 > 0 \medspace such \medspace that\medspace cg(n) \leq f(n)\: \forall n \geq n_0\}$
* $\Omega(\cdot)$ is used to asymptotically *lower-bound* a function
* think of $f(n) \in \Omega(g(n))$ as corresponding to $f(n) \geq g(n)$
> **for the americans at the back:** given a time complexity function, `f(n)`, and another function, `g(n)`, the latter will be the lower bound of the former provided that there are two constants to sustain a relationship between said functions. this applies in a time vs input graph
* $c$ and $n_0$ are the constants that sustain said relationship: the latter is the specific size of the input
* the relationship *viz*: for input of size $n_0$ and above, as said input &rarr; &infin;,  $f(n) \gt cg(n)$
* **random quiz:** given f(n) = n<sup>2</sup> + 2n + 3 and g(n) = 10n, is g(n) the lower bound of f(n)?
    * both time and input size cannot be negative, therefore, we are dealing with the first quadrant of the cartesian plane
    * at what point is g(n) = f(n)?

    $$
        10n = n^2 + 2n + 3 \\[1em]
        0 = n^2 -8n + 3 \\[1em]
        n = \frac{8 \pm \sqrt{52}}{2} \\[1em]
        n = 4 \pm \sqrt{13} \\[1em]
        we\medspace are\medspace only\medspace interested\medspace in\medspace the\medspace solution\medspace where\medspace n \geq 0 \\[1em]
        n = 4 + \sqrt{13} \medspace or \medspace 4 - \sqrt{13} \approx 7.6 (1 \medspace d.p.) \medspace or \medspace 0.4 (1 \medspace d.p.) \\[1em]
        n = 8 (0 \medspace d.p.) \medspace or \medspace 0 (0 \medspace d.p.)
    $$

    * at what point, if any, is g(n) &lt; f(n)?
       * two points: at any point where n &lt; 0 and at any point where n &ge; 8
       * t and n are &lt; zero at n &lt; 0, therefore, the region of interest is where n &ge; 8
    * is there a constant we can multiply g(n) by to make it smaller than f(n)?
        * yes: any real number &ge; 1
        * example: set c to 1
            * $cg(n)$ becomes $g(n)$
            * consider these terms in f(n): `2n + 3`: at what point is `cg(n)` = `f(n)`?
                * we know that n = 0 or 4 from our work above
            * **conclusion:** $cg(n) \lt f(n) \medspace \forall c \gt 0\medspace and\medspace n_0 \geq 8$
    * we have fulfilled both conditions, therefore, g(n) is a lower bound of f(n) ✅ 
        * $f(n) \in \Omega(n)$
#### 1.2.3. tight bound
* aka lower and upper bounds
* $\Theta(g(n)) = \{f(n):\medspace  \ni c_1, c_2, n_0 > 0 \medspace such \medspace that\medspace c_1g(n) \leq f(n) \leq c_2g(n)\: \forall n \geq n_0\}$
* $\Theta(\cdot)$ is used to asymptotically *tight-bound* a function
* think of $f(n) \in \Theta(g(n))$ as corresponding to $f(n) = g(n)$
> **for the americans at the back:** given a time complexity function, `f(n)`, and another function, `g(n)`, the latter will be the tight bound of the former provided that there are three constants to sustain a relationship between said functions. this applies in a time vs input graph
* $c_1$, $c_2$ and $n_0$ are the constants that sustain said relationship; $n_0$ is the specific size of the input
* the relationship *viz*: for input of size $n_0$ and above, as said input &rarr; &infin;,  $f(n) = cg(n)$
* **random quiz:** f(n) = $2n^2 + 2n + 3$. g(n) = $n^2$. is g(n) the tight bound of f(n)?
    * both time and input size cannot be negative, therefore, we are dealing with the first quadrant of the cartesian plane
    * both f(n) and g(n) are order 2 polynomial functions: the leading co-efficients can tell us how each function grows
        * set c<sub>2</sub> to 3 or higher to make g(n) grow faster than f(n)
        * set c<sub>1</sub> to 2 or lower to make g(n) grow slower than f(n)
        * **conclusion:** $c_1 \leq 2$ and $c_2 \gt 2$
    * at what point is g(n) = f(n)?

    $$
        n^2 = 2n^2 + 2n + 3 \\[1em]
        0 = n^2 + 2n + 3 \\[1em]
        n = \frac{-2 \pm \sqrt{-8}}{2} \\[1em]
        n = \frac{-2 \pm i\sqrt{8}}{2} \\[1em]
        n = \frac{-2 \pm 2i\sqrt{2}}{2} \\[1em]
        n = -1\pm i\sqrt{2} \\[1em]
        we\medspace are\medspace only\medspace interested\medspace in\medspace the\medspace solution\medspace where\medspace n \geq 0 \\[1em]
        \therefore n \medspace has \medspace no \medspace viable \medspace solution
    $$

    
    * at what point is c<sub>2</sub>g(n) = f(n)?
        * set c<sub>2</sub> to 3 so that c<sub>2</sub>g(n) = 3n<sup>2</sup>

    $$
        3n^2 = 2n^2 + 2n + 3 \\[1em]
        n^2 - 2n - 3 = 0 \\[1em]
        n^2 + n - 3n -3 = 0 \\[1em]
        n(n+1) -3(n+1) = 0 \\[1em]
        (n - 3) (n + 1) = 0 \\[1em]
        n = 3 \medspace or -1 \\[1em]
        we\medspace are\medspace only\medspace interested\medspace in\medspace the\medspace solution\medspace where\medspace n \geq 0 \\[1em]
        \therefore n = 3
    $$

    * at what point, if any, is c<sub>1</sub>g(n) = f(n)?
        * set c<sub>1</sub> to 2 so that c<sub>1</sub>g(n) = 2n<sup>2</sup>

    $$
        2n^2 = 2n^2 + 2n + 3 \\[1em]
        0 = 2n + 3 \\[1em]
        -2n = 3 \\[1em]
        n = -\frac{3}{2} \\[1em]

        we\medspace are\medspace only\medspace interested\medspace in\medspace the\medspace solution\medspace where\medspace n \geq 0 \\[1em]
       \therefore n \medspace has \medspace no \medspace viable \medspace solution \medspace at \medspace c_1 = 2
    $$

    * so far we have the following
        * g(n) = f(n) IFF $n = -1\pm i\sqrt{2}$
        * $c_2 = 3$ and $c_1 \geq 2$
        * $n_0 \geq 3$
        * $c_2g(n) \gt f(n) \medspace \forall c > 2\medspace and\medspace n_0 \geq 3$
        * $c_1g(n) \lt f(n) \medspace \forall c < 2\medspace and\medspace n_0 \geq 3$
        * **conslusion:** $c_1g(n) \leq f(n) \leq c_2g(n) \medspace \forall c_1 < 2, c_2 > 2\medspace and\medspace n_0 \geq 3$
    * we have fulfilled all conditions, therefore, g(n) is a tight bound of f(n) ✅ 
        * $f(n) \in \Theta(n^2)$

> **handy shortcut:** if the leading terms in each functions are of the same degree, said functions will be each other's tight bound <br/> the other terms (lesser degree terms) have a negligible effect on the *"bound-ness"* of the functions
## 2. asymptotic analysis
### 2.1. pseudo-code of find max algo

```plaintext
    Algorithm Maximum;
    input: list x = {a_1, a_2, ..., a_n};
        y = a_1 ........................................ line 1 ...... 1 step
        for a in x ..................................... line 2 ...... n steps
            if a > y: y = a ............................ line 3, 4 ... 1 or 2 steps
    output: y, the maximum of {a_1, a_2, ..., a_n}; .... line 5 ...... 1 step
```

* $c_n$ is the constant at line $n$ in the pseudo-code above
    * example: constant `y` on line 1 is $c_1$
* best case
    $$
    T(n) = c_1 + c_2n + c_4 \\[1em]
    T(n) = c_an + d
    $$

    * where *c<sub>a</sub>* and *d* are constants
* worst case
    $$
    T(n) = c_1 + (c_2+c_3)n + c_4 \\[1em]
    T(n) = c_bn + d
    $$

    * where *c<sub>b</sub>* and *d* are constants
#### 2.1.1. find the upper, lower and tight bounds
* $T(n)$ is a linear function, therefore, we expect the bounds to be in the order 1 polynomial functions
* **upper bound:** $T(n) \in \Omicron(n)$
* **lower bound:** $T(n) \in \Omega(n)$
* **tight bound:** $T(n) \in \Theta(n)$

### 2.2. pseudo-code of insertion sort algo

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

* $c_n$ is the constant at line $n$ in the pseudo-code above
    * example: constant `key` on line 2 is $c_2$
* best case
    $$
        T(n) = cn
    $$

    *  where *c* is a constant
* worst case
    $$
        T(n) = \frac{cn^2 - cn}{2}\\[1em]
    $$

    * where *c* is a constant
#### 2.2.1. find the upper, lower and tight bounds
* $T(n)$ worst case is a quadratic function, therefore, we expect the bounds to be in the order 2 polynomial functions
* $T(n)$ best case is a linear function, therefore, we expect the bounds to be in the order 1 polynomial functions
* **upper bound:** $T(n) \in \Omicron(n^2)$ because the upper bound of the worst case is the most inefficient an algo can be
* **lower bound:** $T(n) \in \Omega(n)$ because the lower bound of the best case is the most efficient an algo can be
* **tight bound:** $T(n) \in \Theta(n^2)$

|| $\Omicron$ | $\Theta$ | $\Omega$ |
|:---:|:---:|:---:|:---:|
|best case| $\Omicron(n)$ |$\Theta(n)$|$\Omega(n)$|
|average case| $\Omicron(n^2)$ |$\Theta(n^2)$|$\Omega(n^2)$|
|worst case| $\Omicron(n^2)$ |$\Theta(n^2)$|$\Omega(n^2)$|

> **handy shortcut:** pick the worst case of the upper bound when looking for the ~~ultimate~~ upper bound; pick the best case of the lower bound when looking for the ~~ultimate~~ lower bound <br/> **in other words:** upper bound of the worst case is the worst of the worst; lower bound of the best case is the best of the best

### 2.3. commonly used upper bound functions
* in practice, we care about the upper bound of an algo
    * example: how well will my server perform when the load spikes to 1,000,000 from 1,000 in two seconds?
* commonly used functions are
    * $\Omicron(log_2n)$ &rarr; logarithmic time
    * $\Omicron(n)$ &rarr; linear time
    * $\Omicron(n * log_2n)$ &rarr; linearithmic time
    * $\Omicron(n^2)$ &rarr; quadratic time
    * $\Omicron(n^3)$ &rarr; cubic time
    * $\Omicron(2^n)$ &rarr; exponential time
    * $\Omicron(1)$ &rarr; constant time

</div>
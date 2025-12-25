# asymptotic analysis

* aka **Big-O Notation**
### 0. time complexity analysis vs asymptotic analysis
#### 0.1. time complexity analysis
* describes the input-dependent behaviour of an algo
    * **worst case** &rarr; input that cases the algo to perform the most work
    * **average case** &rarr; expected performance across all possible inputs often weighted by probability of occurrence
    * **best case** &rarr; input that cases the algo to perform the least work
#### 0.2. asymptotic analysis
* describes the theoretical guarantees on the performance of the algo
    * measures how an algo performs as its input increases
    * allows for comparison of relative performance between different algorithms
    * describes an algo's time complexity or growth rate using asymptotic notation(s)
* is agnostic (does not GAF) to the characteristics of the input
### 1. growth functions
#### 1.1. notation
* $O$ represents upper bound of a time complexity function
* $\Omega$ represents lower bound of a time complexity function
* $\Theta$ represents both upper and lower bounds of a time complexity function
#### 1.2. formal definitions
##### 1.2.1. upper bound
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
            * $cg(n) = f(n)$ at n &approx; 0.6 (1 \medspace d.p.) or 1 (0 \medspace d.p.)
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
* **random quiz 4:** prove that the time complexity function, $T(n) = 8n + 5$ is O(n)
    * strategy: find a constant c &gt; 0 and inte n<sub>0</sub> &ge; 1 such that for every integer n &ge; n<sub>0</sub>, 8n + 5 &le; c*n
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
        * $f(n) = 8n + 5 \in O(n)$
##### 1.2.2. lower bound
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
##### 1.2.3. tight bound
* aka lower and upper bounds
* $\Theta(g(n)) = \{f(n):\medspace  \ni c_1, c_2, n_0 > 0 \medspace such \medspace that\medspace c_1g(n) \leq f(n) \leq c_2g(n)\: \forall n \geq n_0\}$
* $\Theta(\cdot)$ is used to asymptotically *tight-bound* a function
* think of $f(n) \in \Theta(g(n))$ as corresponding to $f(n) = g(n)$
> **for the americans at the back:** given a time complexity function, `f(n)`, and another function, `g(n)`, the latter will be the tight bound of the former provided that there are three constants to sustain a relationship between said functions. this applies in a time vs input graph
* $c_1$, $c_2$ and $n_0$ are the constants that sustain said relationship; $n_0$ is the specific size of the input
* the relationship *viz*: for input of size $n_0$ and above, as said input &rarr; &infin;,  $f(n) = cg(n)$
* 1:03:50
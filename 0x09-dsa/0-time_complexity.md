# time complexity

<div style="text-align: justify;">

## 0. intro
## 0.1. why TF do we need to study algos?
* limited time and space resources for computational tasks
    * multiply the time and space requirements for a single google query; multiply that by 5.9 million (as of 20-Dec-2025)
    <img alt="internet-minute-2025-viz" src="./Internet-Minute-2025.webp" style="width: 500px; height: auto; padding: 5px; margin: 5px;"/> <br/>
    
    
    * picture what requirements would be needed for the surface web alone
    * the margins of error must be tight; in some cases, there is simply no margin of error
## 1. how TF do we measure the efficiency of code?
* another way to ask the question: how TF do we measure the speed of a function/method?
* depends on the measuring rod ...
    * time it takes to initialise, execute and clean up a function/method and its variables
        * affected by the hardware said func/meth runs on: a func/meth runs quicker on a cluster of super computers than on my puny machine
    * number of steps it takes to execute a func/meth
        * example: find max element of a given array

        ```plaintext
            // arr: an array of size n
            findMax(arr):
                result = arr[0]
                for i in arr:
                    if i > result: result = 1
                return result
        ```

        * say `arr` has one element, `{5}`
            * the number of theps is approx. four: 1 + 2 + 1
                * assign the element to `result`
                * loop through the array
                * find the result of the condition
                * return `result`
        * it will take eight steps (1 + 2\*3 + 1) when `arr` is of size 3
        * for N elements, the number of steps is 2 + 2\*N (1 + 2\*N + 1)
        * this leads to another question: how about the steps it takes to execute the func/meth relative to input size?
            * same example as above
            * let T(n) be a function that measures the speed of the func/meth; `n` is the size of input (0 &le; n &le; &infin;)
                * T(n) is the number of primitive operations executed
            * T(n) = 2n + 2 as we saw above
## 2. formal definitions
1. **algorithm**
* a well-defined, finite procedure that takes input and produces an output
    * aka an effective way to solve a problem
2. **random access machine (RAM)**
* a thoeretical framework to analyse the time complexity of an algorithm
* assumptions of RAM
    * a computer has infinite amount of memory
    * access to memory location takes a constant amount of time
    * basic ops: arithmetic, logical and comparison take a constant amount of time
    * executing a single instruction such as assignment, branching or looping take constant time
    * the time complexity of an algo is determined by counting the number of basic ops it performs
* example: `findMax` from above; how many steps does it take?
    * pseudo-code

    ```plaintext
        input: list x = {a_1, a_2, ..., a_n};
            y = a_1 ........................................ line 1 ...... 1 step
            for a in X ..................................... line 2 ...... n steps
                if a > y: y = a ............................ line 3, 4 ... 1 or 2 steps
        output: y, the maximum of {a_1, a_2, ..., a_n}; .... line 5 ...... 1 step
    ```

    * the key to determining the time complexity depends on whether the step on line 4 executes
        * best case scenario: elements are in descending order; $a_1$ is the max
        * worst case scenario: elements are in ascending order; $a_n$ is the max
        * T(n), therefore, is n + 2 in the best case and 2n + 2 worst case
            * the average case is anything in between n+2 and 2n+2
* **formal definition of T(n):** the number of primitive operations executed
* **formal definition of best-case scenario:** minimum number of steps an algo takes for any possible input
* **formal definition of worst-case scenario:** maximum number of steps an algo takes for any possible input
* **formal definition of average-case scenario:** average number of steps an algo takes for all possible inputs

</div>

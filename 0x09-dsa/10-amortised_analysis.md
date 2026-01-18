# amortised analysis

<div style="text-align: justify">

## 0. WTF is amortised analysis?
* looks at the average time (or space) complexity of algorithm operations over a sequence rather than per operation in isolation
* this approach smooths out occasional expensive operations by spreading their cost across many cheaper ones providing a tighter bound than worst-case analysis alone
    * overall cost remains low even if, ocassionally, some ops are expensive
* examines the total cost of a series of operations and divides by the number to find the amortised cost per operation
    * **example:** in dynamic arrays, insertions are usually O(1), however, resizing doubles the array size occasionally at O(n) cost; over `m` insertions, the average remains O(1)
> **TL;DR:** <br/><br/> - amortised analysis is a way to measure the average cost of a sequence of operations <br/> - *cost* means the time it takes to execute  said ops <br/> - *operation(s)* means functions/methods <br/><br/>
### 0.1. methods
* three techniques exist for calculating amortised costs
    * aggregate method: Sums total costs over a sequence and divides by operation count
    * accounting method: Credits "tokens" to cheap operations to pay for future costly ones
    * potential method: Uses a potential function to track "stored work" in the data structure's state
### 0.2. when TF do we need amortised analysis?
* amortised analysis applies to data structures, say, hash tables (resizing), disjoint sets (union-find) and splay trees where worst-case per-operation bounds are loose but sequences perform efficiently

|type of analysis|goal|
|:---|:---|
|worst-case analysis|find the **max running time** of an algo of **any** input size, `n`; provides an upper bound|
|average-case analysis|find the **expected running time** of an algo of a **randomly chosen** input size, `n`, assuming a known input distribution|
|probabilistic analysis|find the **expected running time** of an algo **that involves randomness**; it averages over **different random choices** made by said algo|
|amortised analysis|find the **average time complexity** per operation over a sequence of ops|

## 1. aggregate method
* takes a sequence of ops, computes total cost of all ops and finds aggregate cost per op by dividing said cost by number of said ops
### 1.1. steps
* have a sequence of `n` ops
* find out the cost of each op
* compute the total cost
* divide total cost by `n`
### 1.2. example: incrementing a binary counter
* say we have an n-bit binary counter initialised to all zeroes
* said counter supports `Increment()` op that adds one to the counter *viz*: flip the rightmost bit to one if it is zero &rarr; O(1), else, flip said bit to zero and propagate the carry to the left by flipping more bits
    * each flip is a cost of the `Increment()` op
* say n = 4; `Increment()` would work *viz*
    * bit 0 represents the rightmost bit and bit 3 represents the leftmost

|decimal|binary|bit 0|bit 1|bit 2| bit 3|
|:---|:---|:---|:---|:---|:---|
|0|0000|-|-|-|-|
|1|0001|flip||||
|2|0010|flip|flip|||
|3|0011|flip||||
|4|0100|flip|flip|flip||
|5|0101|flip||||
|6|0110|flip|flip|||
|7|0111|flip||||
|8|1000|flip|flip|flip|flip|

* worst-case scenario is when all bits are flipped
    * in this case, time complexity is proportional to the number of flips, therefore, <br/><br/> $T(n) \in O(n)$ <br/><br/>
* best-case scenario is when only one bit is flipped
    * in this case, time complexity is constant, therefore, <br/><br/> $T(n) \in O(1)$ <br/><br/>
* for variable input, these analyses do not present a reliable picture of the time complexity; let us see whether the aggregate method will...
    * **step one:** have a sequence of `n` ops
        * `n` = 8 (the number of steps it takes to flip `0000` to `1111` sequentially)
        > **for the americans at the back:** <br/><br/> - the `n` in *n-bit* and the `n` in *number of ops* are not the same; I could have used, say, $i$ and $j$ to differentiate both, however, that is too much work and you are the only ones that do not get it
    * **step two:** find out the cost of each op
        * flipping takes contant time
    * **step three:** compute the total cost
        * the rightmost bit, `bit 0`, flips eight times
        * `bit 1`, flips four times
        * `bit 2`, flips twice
        * the leftmost bit, `bit 3`, flips once
        > **pattern** <br/>- number of bits flipped halves as you move from the rightmost to the leftmost bit until the number of bits flipped equals one <br/><br/>
        * for `n` that is an arbitrarily large number, the rightmost bit flips $n$ times, the second-rightmost flips $\frac{n}{2}$ times and so on until the leftmost flips once <br/><br/> $n, \frac{n}{2}, \frac{n}{4}, ..., 1$ <br/><br/>
        * total number of flips *viz* <br/><br/> $n + \frac{n}{2} + \frac{n}{4} + ... + 1$ <br><br/> $n \times (1 + \frac{1}{2} + \frac{1}{4} + ... + \frac{1}{n})$ <br/><br/> $n \times (1 + \frac{1}{2} + \frac{1}{4} + ... + \frac{1}{2^k})$ where $k$ is the highest bit position in an n-bit counter <br/><br/> we have seen the term in brackets before: geometric series where |r| &lt; 1 (see sub-section 2.3 of [7-heaps_and_heapsort][def]) <br/> we know that $S_{\infin} \approx 2$ where $S$ represents the sum of the geometric series<br/> use the approximation of $S_{\infin}$ in our computations <br/><br/> $n \times S_{\infin} \approx 2n$ <br/><br/> **conclusion: the total cost is approximately twice the number of ops, that is,** <br/><br/> $n \times S_{\infin} \approx 2n \in O(n)$ <br/><br/>
    * **step four:** divide total cost by `n`
        * total cost is approximately $2n \in O(n)$
        * there are $n$ ops
        * divide the cost by the number of ops <br/><br> $\frac{O(n)}{n} = O(1)$ <br/><br/>
        * **conclusion: the amortised time complexity is constant** <br/><br/> $\text{amortised time complexity} \in O(1)$ <br/><br/>
## 2. accounting method
### 2.0. pre-amble
* some ops appear to steal time from the algo; others appear to be doing nothing
* what if we could assign a *"fair"* cost to each op?
* as it happens, we can; enter the *accounting method*...
### 2.1. WTF if the accounting method?
* an approach to amortised analysis where we analyse the overall cost of a sequence of ops by assigning **"credits"** to each op
* the idea is to spread out the cost of the expensive ops to multiple cheaper one; the end result is that the costs are "fairly" distributed
### 2.2. steps
* **step one:** define a cost scheme
    * define an amortised cost (a fixed cost) to each type of op
    * maintain an *"account"* or *"credit"*
    * save extra cost as credit when an op is overcharged
    * use said credit when an expensive op occurs
* **step two:** check validity of said scheme
    * charge said amortised cost to each op
    * if cost<sub>amortised</sub> &gt; cost<sub>actual</sub>, op is overcharged; store the difference as credit
    * if cost<sub>amortised</sub> &lt; cost<sub>actual</sub>, op is expensive; withdraw stored credits to make up for the difference
    * make sure $\sum \text{amortised cost} \geq \sum \text{actual cost}$ always, else, go back to step one
* **step three:** calculate average cost per op
    * $\text{cost}_{average} = \frac{\sum \text{cost}_{amortised}}{\text{no. of ops}}$
### 2.3. example: stack ops
* say you have a stack, `S`, of length `n` that has the following (in first-to-last order): 1, 4, 3, 8, 6, 2
    * here, `n` = 6
* there are two main ops in a stack: `push` and `pop`
    * `push` adds one element to the top of the stack
    * `pop` removes one element; the one at the top of the stack (LIFO; this is the defining feature of a stack)
* there is one other op relevant to thei example: `multipop`
    * `multipop` simply pops at most `k` elements while the stack is not empty

    ```plaintext
        // assume a stack, `Stack`, with method `Pop` exists 
        Function MultiPop(S, k):
            while S is not empty && k > 0:
                Pop(S)
                k -= 1
    ```

* apply the steps...
    * **step one:** define a cost scheme
        * this is the cost schedule of each op

        |op type|purpose|cost|
        |:---|:---|:---|
        |Push(S, x)|add a single element, `x`, to S|O(1)|
        |Pop(S)|remove a single element, `x`, from the top of S|O(1)|
        |MultiPop(S, k)|pop at most `k` elements from the top of S|O(min(\|S\|, k))|

        * we can tell that the worst-case scenario will be $T(n) \in O(n)$ because of $max(|S|) = n$
            * however, this only occurs when `MultiPop(S, n)` is called when `S` is full
            * a full stack means that `Push(S, x)` was called at least `n` times 
        * define a cost scheme

            |op type|actual cost|amortised cost|reason for amortised cost|
            |:---|:---:|:---:|:---|
            |Push(S, x)|1|2|`Pop` and `MultiPop` will have zero cost on an empty stack|
            |Pop(S)|1|0|*ex nihilo nihil fit*|
            |MultiPop(S, k)|min(\|S\|, k)|0|*ditto*|
    * **step two:** check validity of said scheme
        * charge/credit table

            |op type|charge|credit|total credit|
            |:---|:---:|:---:|:---:|
            |Push(S, 1)|2|1|1|
            |Push(S, 4)|2|1|2|
            |Push(S, 3)|2|1|3|
            |Push(S, 8)|2|1|4|
            |Push(S, 6)|2|1|5|
            |Push(S, 2)|2|1|6|
            |Pop(S)|0|-1|5|
            |MultiPop(S, 4)|0|-4|1|
            |MultiPop(S, 3)|0|-1|0|

        * the actual-amortised cost table *viz*

            |op type|total actual cost|total amortised cost|
            |:---|:---:|:---:|
            |Push(S, x)|n|2n|
            |Pop(S) / MultiPop(S, k)|n|0|
            |Total|2n|2n|

        * we see clearly that $\sum \text{amortised cost} \geq \sum \text{actual cost}$ because 2n &ge; 2n
        
        * **conslusion: charging 2 to `Push` and zero to `Pop` and `MultiPop` works because the total credit never goes below zero and the sum of amortised cost is &ge; that of actual cost; cost scheme is valid** ✅
    * **step three:** calculate average cost per op
        * $\text{cost}_{average} = \frac{\sum \text{cost}_{amortised}}{\text{no. of ops}}$ <br/><br/> we know that $(n+1) \leq \ \text{total no. of ops} \ \leq 2n$ <br/>calculate avg cost for both extremes <br/><br/> 1. $\frac{2n}{n+1} \approx 2$ <br/><br/> 2. $\frac{2n}{2n} = 1$ <br/><br/> the average cost in both scenarios is a constant (a number independent of $n$), therefore, average cost is in $O(1)$ <br/><br/>
        * **conclusion: the amortised time complexity is constant** <br/><br/> $\text{amortised time complexity} \in O(1)$ <br/><br/>
## 3. potential method
* 6:08:43

</div>

[def]: 7-heaps_and_heapsort.md
# amortised analysis
## 0. WTF is amortised analysis?
* looks at the average time (or space) complexity of algorithm operations over a sequence rather than per operation in isolation
* this approach smooths out occasional expensive operations by spreading their cost across many cheaper ones providing a tighter bound than worst-case analysis alone
* examines the total cost of a series of operations and divides by the number to find the amortised cost per operation
    * **example:** in dynamic arrays, insertions are usually O(1), however, resizing doubles the array size occasionally at O(n) cost; over `m` insertions, the average remains O(1)
### 0.1. methods
* three techniques exist for calculating amortised costs
    * aggregate method: Sums total costs over a sequence and divides by operation count
    * accounting method: Credits "tokens" to cheap operations to pay for future costly ones
    * potential method: Uses a potential function to track "stored work" in the data structure's state
### 0.2. when TF do we need amortised analysis?
* amortised analysis applies to data structures, say, hash tables (resizing), disjoint sets (union-find) and splay trees where worst-case per-operation bounds are loose but sequences perform efficiently

## 1. aggregate method

## 2. accounting method

## 3. potential method
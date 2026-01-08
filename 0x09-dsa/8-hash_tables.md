# hash tables

<div style="text-align: justify;">

## 0. WTF is a hash table?
* a data structure used to store key-value pairs efficiently; enables fast insertion, deletion and lookup operations
* works by using a hash function to convert a key into an index within an array where the corresponding value is stored
    * this allows for average-case constant time complexity, O(1), for these operations under reasonable assumptions
* hash function maps each key to a specific index in the array aiming to distribute keys uniformly across the available slots to minimise collisions—situations where different keys produce the same index
* hash collisions are common, especially as the number of keys increases relative to the number of slots, and they must be handled using strategies like separate chaining (where each slot contains a list of entries) or open addressing (where alternative slots are probed until an empty one is found)
> **TL;DR:** <br/> - hash table stores key-value pairs for fast access <br/> - uses a special function called a *hash function* to convert keys to numerical indices; said indices determine where values are stored <br/> - searching, deleting and inserting may be done in constant time under the right conditions
### 0.1. where TF are hash tables useful?
* situations/scenarios that have any of the following requirements
    * frequent, efficient searching
    * fast insertions and/or deletions
    * unique key-value mappings
    * data deduplication and uniqueness verification
* widely used in the following
    * computing for applications such as implementing associative arrays in programming languages (e.g. Python dictionaries, Java HashMaps), database indexing, caching and sets
    * compilers, spell checkers and network routers
    * etc
### 0.2. WTF is a hash function?
*  a mathematical function that takes an input of any size such as a file, message or password and converts it into a fixed-length string of characters known as a *hash value*, *hash code*, *digest* or simply a *hash*
    * this process is often described as compressing large data into a compact fingerprint
* regardless of the input size, the output is always the same fixed length; even a small change in the input—like adding a single character—results in a completely different hash value due to the avalanche effect
* hash functions are designed to be fast to compute and deterministic; the same input will always produce the same output
* they are one-way functions making it practically impossible to reverse the process and retrieve the original input from the hash
### 0.3. how TF does a hash function relate to a hash table?
* hash function decides where data lies on the hash table
* hash table may be represented *viz*:

    * keys

        |null|null|null|null|null|null|null|null|null|...|null|
        |:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
        |0|1|2|3|4|5|6|7|8|...|n|

     * values

        |null|null|null|null|null|null|null|null|null|...|null|
        |:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
        |0|1|2|3|4|5|6|7|8|...|n|

* two arrays: one hold the keys; the other holds the values
* indices in the `keys` array correspond to those of the `values` array: `keys[i]` &rarr; `values[i]` where 0 &le; `i` &le; n and n = `keys.length` = `values.length`
    * the hash function returns an index, $i$, in the range $[0, \ \text{keys.length})$; data is stored at said index in the `values` array
* say we want to insert the following into the hash table:

    ```plaintext
        {
            Cat, 5
            Dog, 2
            Fox, 3
        }
    ```

    * data before the comma are the keys; the ones after are the values (three cats, two dogs and three foxes on a farm ...) and commas are delimiters
    * assume the hash function *viz*: `index = sum_of_digits % n`, that is, the index is given by the sum of ASCII digits of the characters of the key mod `n` where `n` is `keys.length` 
    * input `Cat, 5`
        * "C" &rarr; 67, "a" &rarr; 97 and "t" &rarr; 116  and n = 11<br/><br/> $index = \text{ sum of ASCII digits} \ mod \ \text{n}$ <br/><br/> $index = (67 + 97 + 116) \ mod \ 11$ <br/><br/> $index = 280 \ mod \ 11$ <br/><br/> $index = 5$
        * insert the key, `Cat` into `keys[5]` and the value, `5`, into `values[5]`

            |val|null|null|null|null|null|Cat|null|null|null|null|null|
            |:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
            |idx|0|1|2|3|4|5|6|7|8|9|10|


            |val|null|null|null|null|null|5|null|null|null|null|null|
            |:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
            |idx|0|1|2|3|4|5|6|7|8|9|10|
        
        * we now know what to do with the rest
            * `Dog, 2` &rarr; $index = 282 \ mod \ 11 = 7$
            * `Fox, 3` &rarr; $index = 301 \ mod \ 11 = 4$
        * the hash table *viz*:

            |val|null|null|null|null|Fox|Cat|null|Dog|null|null|null|
            |:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
            |idx|0|1|2|3|4|5|6|7|8|9|10|


            |val|null|null|null|null|3|5|null|2|null|null|null|
            |:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
            |idx|0|1|2|3|4|5|6|7|8|9|10|

* you start to see how efficient the search operation will be: simply put the target key through a hash function and look up the `values` array at the index returned after making sure said index exists in the `keys` array (another look-up op)
#### 0.3.1. hash collisions
* everything is great, no?
    * too good... no way this is *that* good
    * enter *la tormenta en el paraiso* ... hash collisions
    * say we want to add `Ant, 1000` to the hash table above
        * ea...sy! use the hash function to find the index and insert the relevant values at said index in the relevant tables. too easy!
        * well... carry on... <br/> <br/>$index = 291 \ mod \ 11 = 5$ <br/><br/> wait... WTF!? index `5` is already occupied! *wa-TF-gwaan*?<br/><br/>
        * see, *mũrata*, a good thing is simply the beginning of a very bad thing; *bata ndũbatabataga*
    * it is not all gloom and doom; we can change the things we can and accept those we cannot
* examine our situation
    * hash function fails because it relies on `n`, the length of the hash table
    * we could make the hash function less reliant on `n`
        * introduce more contrivances (not recommended)
        * find a better function (best option)
* how TF do we create a better function?
    * glad you asked...
    * each word has the following attributes: length, characters, case (upper/lower)
    * we can use the length attribute...
    * say we treat a word as a 1-based array, that is, character count starts at one, not zero *viz*

        |val|C|a|t|
        |:---:|:---:|:---:|:---:|
        |idx|1|2|3|

    * the new hash function could be <br/><br/> $index = (\text{sum of ASCII digits} \times i ) \ mod \ \text{n}$ where $i$ is a 1-based index if a word is treated as an array <br/><br/>
    * based on the *new & improved* hash function, we observe the following

        |input|index|
        |:---:|:---:|
        |Cat|5|
        |Dog|5|
        |Fox|3|
        |Ant|6|

        * **collisions. collisions everywhere...**
* we clearly met our destiny on the road we took to avoid it; time to get serious...

<div style="display: flex; width:100%; align-items: center; justify-content: center; padding: 2em; margin: 5px;">
    <img alt="collisions. collisions everywhere." src="https://i.imgflip.com/2syhd9.jpg" style="width: 350px; height: auto;"/>
</div>

## 1. resolving hash collisions
### 1.1. use open addressing to solve the hash collision problem
* collisions are solved by finding another open slot in the hash table
    * process of finding is called *probing*
* common probing methods
    * **linear:** look for the next open slot on the hash table sequentially
    * **quadratic:** look for the next open slot using a quadratic function
    * **double hashing:** use a second hash function to determine the probe sequence
* **example: look at our problem again**
    * the problem: we have three items on the hash table and the hash of `Ant, 1000` collides with that of `Cat, 5`
        * hash of `Ant, 1000` is <br/><br/> $index = 291 \ mod \ 11 = 5$ <br/><br/>
        * and hash table *viz*

        |val|null|null|null|null|Fox|Cat|null|Dog|null|null|null|
        |:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
        |idx|0|1|2|3|4|5|6|7|8|9|10|


        |val|null|null|null|null|3|5|null|2|null|null|null|
        |:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
        |idx|0|1|2|3|4|5|6|7|8|9|10|

    * say we have a linear probing function *viz* <br/><br/> $index = (\text{original index} + \text{step}) \ mod \ n$  <br/><br/> where $step$ increases by one every time the probing function is called <br/> 
    * the new hash *viz* <br/><br/> $index = (\text{original index} + \text{step}) \ mod \ n$  <br/><br/> $index = (5 + 1) \ mod \ 11$  <br/><br/> $index = 6 \ mod \ 11$  <br/><br/> $index = 6$  <br/><br/>
    * index six happens to be empty, therefore, job done; insert the key, `Ant` into `keys[6]` and the value, `1000`, into `values[6]`

        |val|null|null|null|null|Fox|Cat|Ant|Dog|null|null|null|
        |:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
        |idx|0|1|2|3|4|5|6|7|8|9|10|


        |val|null|null|null|null|3|5|1000|2|null|null|null|
        |:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
        |idx|0|1|2|3|4|5|6|7|8|9|10|

> **important!** <br/> - feel free to replace the linear function with a quadratic or another hash function to achieve the same effect using the other probing methods
### 1.2. use chaining to solve the hash collision problem
*  ---- COMING SOON ----
## 2. implement a hash table
* we will use open addressing to resolve collisions
### 2.1. pseudo-code of a hash-table (w. open addressing)
```plaintext
    class HashTable:
        Define hashtable size in a variable `n`
        Create two arrays, `keys` and `vals`, each of size `n`

        Function hash_function(key):
            ...
        Function linear_probe(index):
            ...
        
        Function Insert(key, value):
            index = hash_function(key)
            while keys[index] is not NULL && keys[index] != key:
                index = linear_probe(index)
            keys[index] = key
            vals[index] = value

        Function Search(key):
            index = hash_function(key)
            while keys[index] is not NULL:
                if keys[index] == key:
                    return TRUE
                index = linear_probe(index)
            return FALSE

        Function Delete(key):
            index = hash_function(key)
            while keys[index] is not NULL:
                if keys[index] == key:
                    keys[index] = NULL
                    vals[index] = NULL
                    return TRUE
                linear_probe(index)
            return FALSE

        Function Resize():
            Double hash table size `n`
            Prepare two arrays, `new_keys` and `new_vals`, with update size
            Copy everything from `keys` to `new_keys`
            Copy everything from `vals` to `new_vals`
            keys = new_keys
            vals = new_vals
```

### 2.2. time complexity of `insert` method
* **best case** is when the probing function does not run at all, that is, index computed by hash function is available
    * in this case, insertion takes constant time <br/><br/> $T(n) \in O(1)$ <br/><br/>
* **worst case** occurs when hash table is full, that is, all indices have a non-null value
    * the `while` loop in `insert` will be proportional to the length of the hash table before resizing
    * in this case, the time complexity is proportional to the size of the hash table <br/><br/> $T(n) \in O(n)$ <br/><br/>
* **average case** ~~, where the fun is,~~ time complexity is proportional to the expected number of probes <br/><br/> $T(n) \propto \mathbb{E}[X]$ <br/><br/>
    * random variable `X` represents the number of probes
        * let $i$ be the number of probes required to find an available spot where 0 &le; i &lt; n
    * assumptions
        * `n` is the size of the hash table and `m` is the number of elements in said hash table
        * load factor, $\alpha$, measures how full the table is and is given by <br/><br/> $\alpha = \frac{m}{n}$ <br/><br/> and $0 \leq \alpha \leq 1$ because the upper bound of $m$ is $n$ <br/><br/>
            > **for the americans in the back:** <br/> - there are `m` elements in the hash table of size `n` <br/> - `m` increases as you add elements in the hash table until it is equal to `n`; at that point, the table if full <br/> - you cannot fill a a cup that is already full
    * probability is proportional to the load factor
        * probability of an unsuccessful probe is $\alpha$; trivially, $(1 - \alpha)$ is the probability of a successful probe
            * why TF is this the case?
                * glad you asked...
                * the load factor is the proportion of occupied/full spots/indices in a hash table
                * probing occurs when an index is already occupied
                * an arbitrary element may or may not cause a hash collision and the likelihood of causing said collision is in porportion to how full the hash table is
                * *"how full the hash table is"* is simply the load factor ~~brings a different meaning to "circle back", no?~~
                > **for the americans at the back:** <br/> - searching $i$ times to find a spot means you were unsuccessful $(i - 1)$ times
        * **conclusion:** <br/><br/> $P(X=i) = (1 - \alpha) \alpha^{i-1}$ <br/><br/>
    * we have everything we need to find the average-case time complexity <br/><br/> $\mathbb{E}[X] = \sum_{i=1}^n i(1 - \alpha) \alpha^{i-1}$ <br/><br/> $\mathbb{E}[X] = (1 - \alpha) \sum_{i=1}^n i\alpha^{i-1}$ <br/> we have seen this before; geometric progression: &nbsp; $\sum_{k=1}^{\infin} ar^k \ \text{converges to} \ \frac{a}{1-r} \ \text{for |r|} \lt 1$ <br/> we can relax the limit of $i$ so we can approximate the solution: $\sum_{i=1}^{\infin} i\alpha^{i-1} = \frac{1}{(1 - \alpha) ^ 2}$ <br/><br/> $\mathbb{E}[X] \approx (1 - \alpha) \cdot \frac{1}{(1 - \alpha) ^ 2}$ <br/><br/> $\therefore \ \mathbb{E}[X] \approx \frac{1}{1 - \alpha}$ <br/><br/>
    > **important!** <br/><br/> - $\alpha = \frac{m}{n}$ &nbsp; that is, $\alpha$ has nothing to do with the random variable <br/> - $\alpha$ is a constant as far as $\mathbb{E}[X]$ is concerned <br/><br/>
    * **conclusion: the time complexity in the average case is propotional to the load factor, that is,** <br/><br/> $T(n) \in O(\frac{1}{1 - \alpha})$ 

### 2.3. time complexity of `search` method
* 4:36:29


</div>
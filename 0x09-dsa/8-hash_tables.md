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
        * "C" &rarr; 67, "a" &rarr; 97 and "t" &rarr; 116  and n = 11<br/><br/> $index = \text{ sum of ASCII digits} \ mod \ \text{n}$ <br/><br/> $ index = (67 + 97 + 116) \ mod \ 11$ <br/><br/> $index = 280 \ mod \ 11$ <br/><br/> $index = 5$
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

## 1. collisions
* 4:20:09


</div>
# arrays

* a collection of elements of the same data type stored contiguously in memory
* each element will be accessed using an index
    * said index is an int value representing its position on the array
* example: [10, 20, 30, 40, 50]
    * same data type: int
    * the entire collection is saved in  sequential slots in memory (say element `10` is at address 0x0ab2d; element `20` will be at address 0x0ab2e etc)
    * array is of size 5; there are five elements on said array
    * Go implements zero-based indexing, therefore, the indices (positions) of the elements, L-R, start at zero and end at 4
* element-wise operations
    * access &rarr; use a valid index; takes O(1) time
    * insert &rarr; use a valid index; takes O(n) time
    * delete &rarr; use a valid index; takes O(1) time
    * $n$ is the length of the array
* stored in memory
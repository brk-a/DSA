# elementary maths problem
### the problem
* E is a maths teacher who is preparing a question bank of  *n* (1 <= n <= 2500) questions for a maths test. in each question, students will add (+), subtract (-) or multiply (*) a pair of numbers
* E has already chosen the *n* pairs of numbers; all that remains is to decide, for each pair, which of the three possible operators (+, -, *) the students should perform
* the students are easy to bore, therefore, E wants to make sure that the *n* correct answers to the exam are all different
* for eachpair of numbers, *(a, b)*, in the same order as the input, output aline containing a valid equation. each equation must have the following
    - *a*
    - one of the three operators
    - *b*
    - equals sign
    - result of expression
* output any valid answer if there are multiple ones; output *impossible* if there is no valid answer
### example

    ```text
        1 ◻ 1 = ◻ 
        -1 ◻ 6 = ◻ 
        4 ◻ -5 = ◻ 
        3 ◻ 2 = ◻ 
    ```
* constraints
    - one of the following ops: _, -, *
    - answers must be unique
### approach
* is there a way to reduce this problem to a bipartite graph?
* how does one detect *impossible* sets of pairs?
* how will multiple, repeated pairs be handled?
> 💡 there will be, at most, three unique solutions for every pair<br /> 
> example: -1 ◻ 6 = ◻<br />  
>> -1 + 6 = 5<br /> 
>> -1 - 6 = -7<br /> 
>> -1 * 6 = -6<br /> 
> this makes it easy to formulate the flow graph as a bipartite graph: input pairs on one side and solutions on the other
### method
1. have a bipartite graph that has *(a, b)* pairs on one side and possible answers on the other
2. add an edge from a *(a, b)* pair to a valid answer
3. have a source and sink node; source before the *(a, b)* pairs, sink after valid answers
4. add an edge from:
    - the source to every *(a, b)* pair
    - every valid answer node to sink
5. add zero flow and unit capacity, `0/1`, to every edge from valid answer node to sink
    - unit capacity because every answer must be unique
6. add zero flow and unit capacity, `0/1`, to every edge from *(a, b)* pairs to valid answer node
    - unit capacity because only one of the operators can be matched with an answer (collisions result in one edge and one edge only)
7. add zero flow and N capacity, `0/N`, to every edge from source to *(a, b)* pairs
    - N capacity because N is the frequency of the input pair (N = 1 means said pair occurs only once, N = 7 means said pair occurs 7 times etc)
8. use any max-flow algo to get max flow

* using the example above this is the network flow diagram:

    ```mermaid
    graph LR
        ((s))--0/1-->((1, 1));
        ((s))--0/1-->((-1, 6));
        ((s))--0/1-->((4, -5));
        ((s))--0/1-->((3, 2));
        ((1, 1))--0/1-->((2));
        ((1, 1))--0/1-->((0));
        ((1, 1))--0/1-->((1));
        ((-1, 6))--0/1-->((5));
        ((-1, 6))--0/1-->((-7));
        ((-1, 6))--0/1-->((-6));
        ((4, -5))--0/1-->((-1));
        ((4, -5))--0/1-->((9));
        ((4, -5))--0/1-->((-20));
        ((3, 2))--0/1-->((5));
        ((3, 2))--0/1-->((1));
        ((3, 2))--0/1-->((6));
        ((2))--0/1-->((t));
        ((0))--0/1-->((t));
        ((1))--0/1-->((t));
        ((5))--0/1-->((t));
        ((-7))--0/1-->((t));
        ((-6))--0/1-->((t));
        ((-1))--0/1-->((t));
        ((9))--0/1-->((t));
        ((-20))--0/1-->((t));
        ((1))--0/1-->((t));
        ((6))--0/1-->((t));
    ```

* edge case: *(a, b)* = (0, 0): there will be one, and only one valid answer: zero, therefore, there will be one, and only one edge to that answer

    ```mermaid
    graph LR
        ((s))-->((0, 0));
        ((0, 0))-->((0));
        ((0))-->((t));
    ```

* edge case: *(a, b)* = (2, 2): there will be two, and only two valid answers: zero and 4, therefore, there will be two, and only two edges

    ```mermaid
    graph LR
        ((s))-->((2, 2));
        ((2, 2))-->((0));
        ((2, 2))-->((4));
        ((0))-->((t));
        ((4))-->((t));
    ```


# edmonds-karp algorith
* from a network flow perspective
* is a max-flow algo that repeatedly finds the shortest augmenting paths from the source, *s*, to the sink, *t* in terms of the #edges used each iteration
    - instead of repeatedly finding augmenting paths from *s* to *t* in the flow graph and augmenting the flow until no more paths exist as in the ford-fulkerson algo
    - EK algo finds the shortest path from *s* to *t* during every iteration
> 💡 ford-fulkerson method does not specify how to find the augmenting paths; this is where optimisations come in<br /> 
> FF algo uses a DFS and takes O(Ef) time where *E* &rarr; #edges and *f* &rarr; max flow<br /> 
> EK uses a BFS to find augmenting paths an, arguably, better complexity of O(VE<sup>2</sup>) where *V* &rarr; #vertices<br /> 
>> notice the time complexity is not affected by capacity value (flow) of any edge, therefore, the EK algo is a *strongly polynomial algo*<br /> 
* **strongly polynomial algo** &rarr; an algo whose time and/or space complexity is not affected by the input values

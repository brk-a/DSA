# shortest path

### use bfs to find shortest path on a grid
* many problems in graph theory can be represented using a grid
* a grid is an *implicit graph* because one can determine a node's neighbours based on current position w/i the grid
* common approach when solving grid problems is to convert said grid to an adjacency list or matrix, whichever fits the problem
    * assumption: grid is unweighted. cells are connected left, right, up and down 
    1. have an empty m by n grid (m rows, n cols)

        |||
        |:---:|:---:|
        |||
        |||

    2. label the cells in the grid w. the indices 1 <= i < N where N = m * n

        |0|1|
        |:---:|:---:|
        |2|3|
        |4|5|

    3. initialise an adjacency list or a N by N adjacency matrix

        ```text
            empty adjacency list
        ```

        ||0|1|2|3|4|5|
        |:---:|:---:|:---:|:---:|:---:|:---:|:---:|
        |0|||||||
        |1|||||||
        |2|||||||
        |3|||||||
        |4|||||||
        |5|||||||

    4. put down the connections in said list and/or matrix

         ```text
            0 -> [1, 2]
            1 -> [0, 3]
            2 -> [0, 3, 4]
            3 -> [1, 2, 5]
            4 -> [2, 5]
            5 -> [3, 4]
        ```

        |:---:|:---:|:---:|:---:|:---:|:---:|
        |0|1|1|0|0|0|
        |1|0|0|1|0|0|
        |1|0|0|1|1|0|
        |0|1|1|0|0|1|
        |0|0|1|0|0|1|
        |0|0|0|1|1|0|

    5. run whatever graph algo is required to solve the problem

    > - tranformations between representations can be avoided
    > consider a position, (r, c), on a grid. the only ways to move are left, right, up or down
    > that is: left: (r, c-1), right: (r, c+1), up: (r+1, c) or down: (r-1, c)
    > each of the said movements is represented by the vectors [-1, 0], [1, 0], [0, 1] and [0, -1] respectively
    > therefore, to move, one only needs add the necessary vector to [r, c]
    > say the problem allows one to move diagonally. the following vectors will be used: [-1, -1], [-1, 1], [1, 1] and [1, -1]
    > these vectors are called **direction vectors**. they allow easy access to neighbouring cells given a starting point

* example: traverse a grid using the four cardinal directions

    ```text
        //direction vectors for N,S, E and W
        dr = [-1, +1, 0, 0]
        dc = [0, 0, +1, -1]

        for(i=0; i<4; i++):
            rr = r + dr[i]
            cc = c + dc[i]

            //skip invalid cells. assume R -> #rows and C -> #cols
            if rr<0 or cc<0: continue
            if rr>=R or cc>=C: continue

            //(rr, cc) is a neighbour of (r, c)

    ```

### dungeon problem
* you are trapped in a 2D dungeon. find the quickest way out.
    * dungeon is composed of unit cubes which may or may not be filled with rocks
    * it takes one minutes to move in any of the four cardinal directions; you cannot move diagonally
    * the walls of said dungeon are solid rock on all sides
    * is an escape possible? if yes, how long will it take?
* dungeon is of size R by C. start at cell `S`. aim is to find the exit, `E`. cells full of rocks are represented by `#`. clear cells are represented by `-`

|S|-|-|...|-|
|:---:|:---:|:---:|:---:|:---:|
|-|#|-|...|'|
|#|-|-|...|#|
|...|...|...|...|...|
|-|-|E|...|-|

* pseudo-code

    ```text
        //global/class scope vars
        R, C = ... //R->#rows, C->#cols
        m = ... //input character matrix of dims R * C
        sr, sc = ... //`S` symbol row and col vals respectively
        rq, cq = ... //empy row queue (RQ) and col (RC) respectively

        //variables to track #steps taken
        move_count = 0
        nodes_left_in_layer = 1
        nodes_in_next_layer = 0

        //var used to track whether `E` character has been reached during bfs
        reahed_end = false

        //R by C matrix of false vals used to track whether node @ (i, j) has been visited
        visited = ...

        //cardinal directions
        dr = [-1, +1, 0, 0]
        dc = [0, 0, +1, -1]

        function solve():
            rq.enqueue(sr)
            cq.enqueue(sc)
            visited[sr][sc] = true
            while rq.size()>0: //or cq.size()>0
                r = rq.dequeue()
                c = cq.dequeue()
                if m[r][c]=="E":
                    reached_end = true
                    break
                explore_neighbours(r, c)
                nodes_left_in_layer--
                if nodes_left_in_layer==0:
                    nodes_left_in_layer = nodes_in_next_layer
                    nodes_in_next_layer = 0
                    move_count++
            if reached_end:
                return move_count
            return -1

        function explore_neigbours(r, c):
            for(i=0; i<4; i++):
                rr = r + dr[i]
                cc = c + dc[i]

                #skip out-of-bounds locations
                if rr<0 or cc<0: continue
                if rr>=R or cc>=C: continue

                #skip visited locations or blocked cells
                if visited[rr][cc]: continue
                if m[rr][cc]=='#': continue

                rq.enqueue(rr)
                cq.enqueue(cc)
                visited[rr][cc] = true
                nodes_in_next_layer++
    ```
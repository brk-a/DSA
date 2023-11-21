const graph = {
    a: ["b", "c"],
    b: ["d"],
    c: ["e"],
    d: ["f"],
    e: [],
    f: [],
}

const depthFirstLoop = (graph, startNode) => {
    const stack = [startNode]
    const result = []

    while(stack.length>0){
        const current = stack.pop()
        result.push(current)
        for(let neighbour of graph[current]){
            stack.push(neighbour)
        }
    }

    return result

}

const depthFirstRecursive = (graph, startNode) => {
    //implicit base case: empty array
    // no recursive call is made on an empty array
    const result = []
    result.push(startNode)
    
    for( let neighbour of graph[startNode]){
        result.push(...depthFirstRecursive(graph, neighbour))
    }

    return result
}

/**breadth-firt is possible iteratively, not recursively
 * because it implements a queue, not a stack
*/
const breadthFirstLoop = (graph, startNode) => {
    const queue = [startNode]
    const result = []

    while(queue.length>0){
        const current = queue.shift()
        result.push(current)
        for(let neighbour of graph[current]){
            queue.push(neighbour)
        }
    }

    return result
}

console.info("depth-first, iterative", depthFirstLoop(graph, 'a'))
console.info("depth-first, recursive", depthFirstRecursive(graph, 'a'))
console.info("breadth-first, iterative", breadthFirstLoop(graph, "a"))

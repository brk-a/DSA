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
    const result = []
    result.push(startNode)
    
    for( let neighbour of graph[startNode]){
        depthFirstRecursive(graph, neighbour)
        result.push(...neighbour)
    }

    return result

}

console.info(depthFirstLoop(graph, 'a'))
console.info(depthFirstRecursive(graph, 'a'))

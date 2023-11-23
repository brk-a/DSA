// const graph = {
//     w: ["x", "v"],
//     x: ["w", "y"],
//     y: ["x", "z"],
//     z: ["y", "v"],
//     v: ["z", "w"],
// }
const edges = [
    ["w", "x"],
    ["x", "y"],
    ["z", "y"],
    ["z", "v"],
    ["w", "v"],
]

const shortestPath = (graph, nodeA, nodeB) => {
    const graph = buildGraph(edges)
    const visited = new Set([nodeA])
    const queue = [[nodeA, 0]]

    while(queue.length>0){
        const [currentNode, distance] = queue.shift()
        if(currentNode===nodeB) return distance

        for(let neighbour of graph[currentNode]){
            if(!visited.has(neighbour)){
                visited.add(neighbour)
                queue.push([neighbour, distance+1])
            }
        }
    }

    return -1
}

const buildGraph = (edges) => {
    const graph = {}

    for(let edge of edges){
        const [a, b] = edge
        if(!(a in graph))graph[a] = []
        if(!(b in graph))graph[b] = []
        graph[a].push(b)
        graph[b].push(a)
    }

    return graph
}
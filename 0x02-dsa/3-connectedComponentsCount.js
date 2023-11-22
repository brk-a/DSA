const graph = {
    3: [],
    4: ["6"],
    6: ["4", "5", "7", "8"],
    8: ["6"],
    7: ["6"],
    5: ["6"],
    1: ["2"],
    2: ["1"],
}

const visited = new Set()
let count = 0

const connectedComponentCount = (graph) => {
    for(let node in graph){
        if(explore(graph, node, visited)===true) count++
    }

    return count
}

const explore = (graph, node, visited) => {
    if(visited.has(node)) return false
    visited.add(node)

    for(neighbour of graph[node]){
        explore(graph, neighbour, visited)
    }
    return true
}

console.info(connectedComponentCount(graph))

const edges = [
    ["i", "j"],
    ["k", "i"],
    ["m", "k"],
    ["k", "l"],
    ["o", "n"],
]
/**
 * buildGraph(edges) should return the following
 * {
    i: ["j", "k"],
    j: ["i"],
    k: ["i", "m", "l"],
    m: ["k"],
    l: ["k"],
    o: ["n"],
    n: ["o"],
* }
*/

const undirectedPath = (edges, nodeA, nodeB) => {
    const graph = buildGraph(edges)
    const result = hasPath(graph, nodeA, nodeB, new Set())
    return result
}

const buildGraph = (edges) => {
    const graph = {}

    for(let edge of edges){
        const [a, b] = edge
        if(!(a in graph)) graph[a] = []
        if(!(b in graph)) graph[b] = []
        graph[a].push(b)
        graph[b].push(a)
    }

    return graph
}

const hasPath = (graph, src, dst, visited) => {
    if(visited.has(src)) return false
    if(src===dst) return true

    visited.add(src)

    for(let neighbour in graph[src]){
        if(hasPath(graph, neighbour, dst, visited)===true) return true
    }

    return false
}

console.info("undirected path", undirectedPath(edges, "i", "n"))
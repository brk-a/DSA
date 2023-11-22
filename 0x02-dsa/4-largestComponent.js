const graph = {
    0: ["8", "1", "5"],
    1: ["0"],
    5: ["0", "8"],
    8: ["0", "5"],
    2: ["3", "4"],
    3: ["2", "4"],
    4: ["3", "2"],
}

const largestComponent = (graph) => {
    let longest = 0
    const visited = new Set()

    for(let node in graph){
        const size = exploreCount(graph, node, visited)
        if(size>longest) longest = size
    }

    return longest
}

const exploreCount = (graph, node, visited) => {
    if(visited.has(node)) return 0
    visited.add(node)

    let size = 1
    for(neighbour of graph[node]){
        size += exploreCount(graph, neighbour, visited)
    }

    return size
}

console.info("largest component", largestComponent(graph))
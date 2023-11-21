/**
 * write a fn, hasPath(), that takes in an object representing an adjacency
 * list of a directed, acyclic graph. said fn has two other params, src and dst,
 * the source and destination. it should return a boolean that indicates
 * whether or not there exists a directed path between the src and dst nodes
 */

const graph =  {
    f: ["g", "i"],
    g: ["h"],
    h: [],
    i: ["g", "k"],
    j: ["i"],
    k: [],
}

const hasPathIterative = (graph, src, dst) => {
    const queue = [src]

    while(queue.length>0){
        const current = queue.shift()

        if(current===dst) return true

        for(let neighbour of graph[current]){
            queue.push(neighbour)
        }
    }

    return false
}

const hasPathRecursive = (graph, src, dst) => {
    if(src===dst) return true

    for(let neighbour of graph[src]){
        if(hasPathRecursive(graph, neighbour, dst)) return true
    }

    return false
}

console.info("hasPathIterative", hasPathIterative(graph, 'f', 'k'))
console.info("hasPathRecursive", hasPathRecursive(graph, 'f', 'k'))
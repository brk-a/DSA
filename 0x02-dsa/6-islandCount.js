const grid = [
    ["W", "L", "W", "W", "W"],
    ["W", "L", "W", "W", "W"],
    ["W", "W", "W", "L", "W"],
    ["W", "W", "L", "L", "W"],
    ["L", "W", "W", "L", "L"],
    ["L", "L", "W", "W", "W"],
]

const islandCount = (grid) =>  {
    const visited = new Set()
    let count = 0

    for(r=0; r<grid.length; r++){
        for(c=0; c<grid[0].length; c++){
            if(explore(grid, r, c, visited)===true) count++
        }
    }

    return count
}

const explore = (grid, r, c, visited) => {
    const rowInBounds = 0 <= r && r < grid.length
    const colInBounds = 0 <= c && c < grid[0].length
    if(!rowInBounds || !colInBounds) return false

    if(grid[r][c]==="W") return false

    const pos = `${r}, ${c}`
    if(visited.has(pos)) return false
    visited.add(pos)

    explore(grid, r-1, c, visited)
    explore(grid, r+1, c, visited)
    explore(grid, r, c-1, visited)
    explore(grid, r, c+1, visited)

    return true
}

console.info("island count", islandCount(grid))
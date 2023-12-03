class Node{
    constructor(val){
        this.val = val
        this.left = null
        this.right = null
    }
}

const a = new Node(5)
const b = new Node(11)
const c = new Node(3)
const d = new Node(4)
const e = new Node(15)
const f = new Node(12)

a.left = b
a.right = c
b.left = d
b.right = e
c.right = f

const treeMinValueIterativeQueue = root => {
    if(root.val===null) return Infinity

    let minValue = Infinity
    const queue = [root]

    while(queue.length>0){
        const current = queue.shift()

        if(current.val<minValue) minValue = current.val
        if(current.left!==null) queue.push(current.left)
        if(current.right!==null) queue.push(current.right)
    }

    return minValue
}

const treeMinValueIterativeStack = root => {
    if(root.val===null) return Infinity

    let minValue = Infinity
    const stack = [root]

    while(stack.length>0){
        const current = stack.pop()

        if(current.val<minValue) minValue = current.val
        if(current.right!==null) stack.push(current.right)
        if(current.left!==null) stack.push(current.left)
    }

    return minValue
}

const treeMinValueRecursive = root => {
    if(root===null) return Infinity

    const leftMin = treeMinValueRecursive(root.left)
    const rightMin = treeMinValueRecursive(root.right)

    return Math.min(root.val, leftMin, rightMin)
}

console.info("treeMinValue iterative queue", treeMinValueIterativeQueue(a))
console.info("treeMinValue iterative stack", treeMinValueIterativeStack(a))
console.info("treeMinValue recursive", treeMinValueRecursive(a))
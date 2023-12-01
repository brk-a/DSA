class Node{
    constructor(val){
        this.val = val
        this.left = null
        this.right = null
    }
}

const a = new Node(3)
const b = new Node(11)
const c = new Node(4)
const d = new Node(4)
const e = new Node(2)
const f = new Node(1)

a.left = b
a.right = c
b.left = d
b.right = e
c.right = f


const treeSumIterative = (root) => {
    if(root===null) return 0

    let total = 0
    const queue = [root]

    while(queue.length>0){
        const current = queue.shift()
        total += current.val

        if(current.left!==null) queue.push(current.left)
        if(current.right!==null) queue.push(current.right)
    }

    return total
}

const treeSumRecursive = (root) => {
    if(root===null) return 0

    return root.val + treeSumRecursive(root.left) + treeSumRecursive(root.right)
}

console.info("treeSum iterative", treeSumIterative(a))
console.info("treeSum recursive", treeSumRecursive(a))
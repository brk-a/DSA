class Node{
    constructor(val){
        this.val = val
        this.left = null
        this.right = null
    }
}

const a = new Node("a")
const b = new Node("b")
const c = new Node("c")
const d = new Node("d")
const e = new Node("e")
const f = new Node("f")

a.left = b
a.right = c
b.left = d
b.right = e
c.right = f

const treeIncludesIterative = (root, target) => {
    if(root===null) return false

    const queue = [root]

    while(queue.length>0){
        const current = queue.shift()

        if(current===target) return true

        if(current.left!==null) queue.push(current.left)
        if(current.right!==null) queue.push(current.right)
    }

    return false
}

const treeIncludesRecursive = (root, target) => {
    if(root===null) return false
    if(root===target) return true

    return treeIncludesRecursive(root.left, target) || treeIncludesRecursive(root.right, target)
}

console.info("treeIncludes iterative", treeIncludesIterative(a, e))
console.info("treeIncludes recursive", treeIncludesRecursive(a, e))
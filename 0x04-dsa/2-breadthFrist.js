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

const breadthFirstIterative = root => {
    if(root===null) return []

    const queue = [root]
    const result = []

    while(queue.length>0){
        const current = queue.shift()
        result.push(current)

        if(current.left!=null) queue.push(current.left)
        if(current.right!=null) queue.push(current.right)
    }

    return result.map(obj => (obj.val))
}

console.info("breadth-first", breadthFirstIterative(a))
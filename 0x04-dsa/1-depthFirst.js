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

const depthFirstIterative = root => {
    if(root===null) return []

    const stack = [root]
    const result = []

    while(stack.length>0){
        const current = stack.pop()
        result.push(current)

        if(current.right) stack.push(current.right)
        if(current.left) stack.push(current.left)
    }

    return result.map(obj => (obj.val))
}

const depthFirstRecursive = root => {}

console.info(depthFirstIterative("depth-first iterative", a))
console.info(depthFirstRecursive("depth-first recursive", a))

import {Node} from './0-nodeClass'

const nodeArray = ["a", "b", "c", "d", "e", "f"]
for(i of nodeArray){
    const i = new Node(i)
    console.info(i)
}

const depthFirstIterative = root => {
    const stack = [root]
    const result = []

    while(stack.length>0){
        const current = stack.pop()
        result.push(current)

        if(current.left) stack.push(current.left)
        if(current.right) stack.push(current.right)
    }
}


module.exports = {
    depthFirstIterative,
}
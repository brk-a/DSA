class Node {
    constructor(val) {
        this.val = val
        this.next = null
    }
}

const a = new Node("a")
const b = new Node("b")
const c = new Node("c")
const d = new Node("d")
const e = new Node("e")
const f = new Node("f")

a.next = b
b.next = c
c.next = d
d.next = e
e.next = f

const traverseList = head => {
    const result = []
    let current = head

    while (current !== null) {
        result.push(current.val)
        current = current.next
    }

    return result
}

const traverseListRecursive = head => {
    const result = []

    traverseListRecursiveHelper(head, result)

    return result
}

const traverseListRecursiveHelper = (head, result) => {
    if(head===null) return

    result.push(head.val)
    traverseListRecursiveHelper(head.next, result)
}

console.info("traverseList", traverseList(a))
console.info("traverseListRecursive", traverseListRecursive(a))
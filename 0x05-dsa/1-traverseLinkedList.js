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
    if (head === null) return
    if(head.next===null) return head.val

    const result = []
    result.push(head.val)

    traverseListRecursive(head.next)

    return result
}

console.info("traverseList", traverseList(a))
console.info("traverseListRecursive", traverseListRecursive(a))
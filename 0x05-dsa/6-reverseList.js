class Node{
    constructor(val){
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

const reverseListIterative = head => {
    let prev = null
    let current = head

    while(current!==null){
        const next = current.next

        current.next = prev
        prev = current
        current = next
    }

    return prev
}

const reverseListRecursive = head => {
    return reverseListRecursiveHelper(head, null)
}

const reverseListRecursiveHelper = (head, prev) => {
    if(head===null) return prev

    const next = head.next
    head.next = prev

    return reverseListRecursiveHelper(next, head)
}

console.info("reverseListIterative", reverseListIterative(a))
console.info("reverseListRecursive", reverseListRecursive(a))
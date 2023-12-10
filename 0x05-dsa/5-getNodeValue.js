class Node{
    constructor(val){
        this.val = val
        this.next = null
    }
}

const a = new Node(5)
const b = new Node(8)
const c = new Node(13)
const d = new Node(21)
const e = new Node(34)
const f = new Node(55)

a.next = b
b.next = c
c.next = d
d.next = e
e.next = f

const getNodeValueIterative = (head, idx) => {
    let current = head
    let count = 0

    while(current!==null){
        if(count===idx) return current.val

        count++
        current = current.next
    }

    return null
}

const getNodeValueRecursive = (head, idx) => {
    if(head===null) return null
    if(idx===0) return head.val

    return getNodeValueRecursive(head.next, idx-1)
}

console.info("getNodeValueIterative", getNodeValueIterative(a, 4))
console.info("getNodeValueRecursive", getNodeValueRecursive(a, 4))
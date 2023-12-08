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

const sumListIterative = head => {
    let sum = 0
    let current = head

    while(current!==null){
        sum += current.val
        current = current.next
    }

    return sum
}

const sumListRecursive = head => {
    if(head===null) return 0
    return head.val + sumListRecursive(head.next)
}


console.info("sumListIterative", sumListIterative(a))
console.info("sumListRecursive", sumListRecursive(a))
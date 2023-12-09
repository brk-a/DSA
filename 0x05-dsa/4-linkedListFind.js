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

const linkedListFindIterative = (head, target) => {
    let current = head

    while(current!==null){ 
        if(current.val===target) return true 
        current = current.next
    }

    return false
}

const linkedListFindRecursive = (head, target) => {
    if(head===null) return false
    if(head.val===target) return true

    return linkedListFindRecursive(head.next, target)
}

console.info("linkedListFindIterative", linkedListFindIterative(a, 55))
console.info("linkedListFindRecursive", linkedListFindRecursive(a, 55))
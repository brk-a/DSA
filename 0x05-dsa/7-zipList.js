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

const g = new Node(5)
const h = new Node(8)
const i = new Node(13)
const j = new Node(21)
const k = new Node(34)
const l = new Node(55)

g.next = h
h.next = i
i.next = j
j.next = k
k.next = l

const zipListIterative = (head1, head2) => {
    let tail = head1
    let current1 = head1.next
    let current2 = head2
    let count = 0

    while(current1!==null && current2!==null){
        if(count%2==0){
            tail.next = current2
            current2 = current2.next
        }else{
            tail.next = current1
            current1 = current1.next
        }

        tail = tail.next
        count++
    }

    if(current1!==null) tail.next = current1
    if(current2!=null) tail.next = current2

    return head1
}

const zipListRecursive = (head1, head2) => {
    if(head1===null && head2===null) return null
    if(head1===null) return head2
    if(head2===null) return head1

    const next1 = head1.next
    const next2 = head2.next
    
    head1.next = head2
    head2.next = zipListRecursive(next1, next2)


    return head1
}

console.info("zipListIterative", zipListIterative(a, g))
console.info("zipListRecursive", zipListRecursive(a, g))
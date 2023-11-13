public class DoublyLinkedList <T> implements Iterable <T>{
    private int size = 0;
    private Node <T> head = null;
    private Node <T> tail = null;

    //internal node class to re data
    T data;
    Node <T> prev, next;
    public Node(T data, Node <T> prev, Node <T> next){
        this.data = data;
        this.prev = prev;
        this.next = next;
    }
    @Override public String toString(){
        return data.toString();
    }

    //empty linked list O(n)
    public void clear(){
        Node <T> trav = head;
        while(trav!=null){
            Node <T> next = trav.next;
            trav.prev = trav.next = null;
            trav.data = null;
            trav = next;
        }
        head = tail = trav = null;
        size = 0;
    }

    //return size of linked list
    public int size(){
        return size;
    }

    //is the ll empty?
    public boolean isEmpty(){
        return size() == 0;
    }

    //add an elem to tail of ll
    public void add(T elem){
        addLast(elem);
    }

    //add an elem to beginning of ll
    public void addFirst(T elem){
        if(isEmpty()){
            head = tail = new Node <T> (elem, null, null);
        } else {
            head.prev = new Node <T> (elem, null, head);
            head = head.prev;
        }
        size++;
    }

    //add a node to tail of ll
    public void addLast(T elem){
        if(isEmpty()){
            head = tail = new Node <T> (elem, null, null);
        } else {
            tail.next = new Node <T> (elem, tail, null);
            tail = tail.next;
        }
        size++;
    }

    //check if value of first node exists O(1)
    public T peekFirst(){
        if(isEmpty()) throw new RuntimeException("empty list");
        return head.data;
    }

    //check if value of last node exists O(1)
    public T peekLast(){
        if(isEmpty()) throw new RuntimeException("empty list");
        return tail.data;
    }

    //remove value/node @ head of ll
    public T removeFirst(){
        if(isEmpty()) throw new RuntimeException("empty list");
        
        T data = head.data;
        head = head.next;
        --size;

        if(isEmpty()) tail = null;
        else head.prev = null;

        return data;

    //remove value/node @ tail of ll
    public T removeLast(){
        if(isEmpty()) throw new RuntimeException("empty list");
        
        T data = tail.data;
        head = tail.next;
        --size;

        if(isEmpty()) head = null;
        else tail.next = null;

        return data;
    }

    //remove an arbitrary node from ll
    private T remove(Node <T> node){
        if(node.prev==null) return removeFirst();
        if(node.next==null) return removeLast();

        node.next.prev = node.prev;
        node.prev.next = node.next;

        T data = node.data;
        node.data = null;
        node = node.prev = node.next = null;

        --size;

        return data;
    }

    //remove a node and index n
    public T removeAt(int index){
        if(index<0 || index>=size) throw new IllegalArgumentException();

        int i;
        Node <T> trav;

        if(index<size/2){
            for(i=0; trav=head; i!=index; i++)
                trav = trav.next;
        } else {
            for(i=size-1, trav=tail; i!=index; i--)
                trav = trav.prev;
        }
        return remove(trav);
    }

    //remove a particular value in the ll
    public boolean remove(Object obj){
        Node <T> trav = head;

        if(obj==null){
            for(trav=head; trav!=null; trav=trav.next){
                if(trav.data==null){
                    remove(trav);
                    return true;
                }
            }
        } else {
            for(trav=head; trav!=null; trav=trav.next){
                if(obj.equals(trav.data)){
                    remove(trav);
                    return true;
                }
            }
        }
        return false;
    }

    //return index of a valuein ll O(n)
    public int indexOf(Object obj){
        int index = 0;
        Node <T> trav = head;

        if(obj==null){
            for(trav=head; trav!=null; trav=trav.next, index++)
                if(trav.data==null)
                    return index;
        } else{
            for(trav=head; trav!=null; trav=trav.next, index++)
                if(obj.equals(trav.data))
                    return index;
        }

        return -1;
    }

    //is a value in ll?
    public boolean contains(Object obj){
        return indexOf(obj) != -1;
    }

    @Override public java.util.Iterator <T> iterator(){
        return new java.util.Iterator <T> (){
            private Node <T> trav = head;
            @Override public boolean hasNext(){
                return trav != null;
            }
            @Override public T next(){
                T data = trav.data;
                trav = trav.next;
                return data;
            }
        };
    }

    @Override public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node <T> trav = head;
        while(trav!=null){
            sb.append(trav.data + ", ");
            trav = trav.next;
        }
        sb.append("]");
        return sb.toString();
    }

}
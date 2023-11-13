public class Queue <T> implements Iterable <T>{
    private java.util.LinkedList <T> list = new java.util.LinkedList <T> ();
    
    public Queue() {}

    public Queue(T firstElem){
        offer(firstElem);
    }

    //return size of queue
    public int size(){
        return list.size();
    }

    //is queue empty?
    public boolean isEmpty(){
        return size() == 0;
    }

    //peek @ an elem @ head/front of queue
    public T peek(){
        if(isEmpty())
            throw new RuntimeException("Queue empty");
        return list.peekFirst();
    }

    //poll/dequeue elem @ head/front of queue
    public T poll(){
        if(isEmpty())
            throw new RuntimeException("Queue empty");
        return list.removeFirst();
    }

    //add elem to back of queue
    public void offer(T elem){
        list.addLast(elem);
    }

    //return iterator to allow users to traverse the queue
    @Override public java.util.iterator <T> iterator(){
        return list.iterator();
    }
}
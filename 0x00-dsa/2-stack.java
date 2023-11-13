public class Stack <T> implements Iterable <T>{
    private java.util.LinkedList <T> list = new java.util.LinkedList <T>();

    //empty stack
    public Stack(){};

    //stack w. an initial elem
    public Stack(T firstElem){
        push(firstElem);
    }

    //return #elems in stack
    public int size(){
        return list.size();
    }

    //is stack empty?
    public boolean isEmpty(){
        return size() == 0;
    }

    //push an elem to stack
    public void push(T elem){
        list.addLast(elem);
    }

    //pop an element from stack
    public T pop(){
        if(isEmpty())
            throw new java.util.EmptyStackException();
        return list.removeLast();
    }

    //peek at the top of stack w/o removing elem
    public T peek(){
        if(isEmpty())
            throw new java.util.    EmptyStackException();
        return list.peekLast();
    }

    //allow users to iterate the stack using an iterator
    @Override public java.util.iterator <T> iterator(){
        return list.iterator();
    }
}
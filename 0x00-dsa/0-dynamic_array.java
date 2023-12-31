@SuppressWarnings('unchecked')
public class Array <T> implements Iterable <T>{
    private T [] arr;
    private int len = 0; //user thinks this is array length
    private int capacity = 0; // actual length of array

    public Array() {this(16);}

    public Array(int capacity){
        if(capacity<0) throw new IllegalArgumentException("illegal capacity"+capacity);
        this.capacity = capacity;
        arr = (T[]) new Object[capacity];
    }

    public int size() {return len;}
    public boolean isEmpty() {return size() == 0;}

    public T get(int index) {return arr[index];}
    public void set(int index, T elem) {arr[index] = elem;}

    public void clear() {
        for(int i=0; i<capacity; i++)
            arr[i] = null;
        len = 0;
    }

    public void add(T elem){
        //resize array
        if(len+1>=capacity){
            if(capacity==0) capacity = 1;
            else capacity *= 2; //double size of array
            T[] new_arr = (T[]) new Object[capacity];
            for(int i=0; i<len; i++)
                new_arr[i] = arr[i];
            arr = new_arr; //arr has extra nulls
        }
        arr[len++] = elem;
    }

    public T removeAt(int rm_index){
        //rm @ specified index
        if(rm_index>=len && rm_index<0) throw new IndexOutOfBoundsException();
        T data = arr[rm_index];
        T[] new_arr = (T[]) new Object[len-1];
        for(int i=0, j=0; i<len; i++, j++)
            if(i==rm_index) j--; //skip over rm_index by fixing j temporarily
            else new_arr[j] = arr[i];
        arr = new_arr;
        capacity = --len;
        return data;
    }

    public boolean remove(Object obj){
        for(int i=0; i<len; i++){
            if(arr[i].equals(obj)){
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    public indexOf(Object obj){
        for(int i=0; i<len; i++)
            if(arr[i].equals(obj))
                return i;
        return -1;
    }

    public boolean contains(Object obj){
        return indexOf(obj) != -1;
    }

    //iterator is still fast but not as fast as iterative `for` loop
    @Override public java.util.Iterator <T> Iterator(){
        return new java.util.Iterator <T> (){
            int index = 0;
            public boolean hasNext(){ return index < len;}
            public T next() {return arr[index++];}
        };
    }

    @Override public String toString(){
        if(len==0) return "[]";
        else {
            for(int i=0; i<len-1; i++)
                sb.append(arr[i] + ", ");
            return sb.append(arr[len-1] + "]").toString();
        }
    }
}
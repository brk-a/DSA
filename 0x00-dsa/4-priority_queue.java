import java.util.*;

public class PriorityQueue <T extends Comparable <T>>{
    // #elems curently in heap
    private int heapSize = 0;

    //internal capacity of heap
    private int heapCapacity = 0;

    //dynamic list to track elems in heap
    private List <T> heap = null;

    // hashmap
    private Map <T, TreeSet<Integer>> map = new HashMap<>();

    //initially-empty priority queue
    public PriorityQueue(){
        this(1);
    }

    //pq with an intial capacity
    public PriorityQueue(int sz){
        heap = new ArrayList<>(sz);
    }

    //pq using heapify O(n)
    public PriorityQueue(T[] elems){
        heapSize = heapCapacity = elems.length;
        heap = new ArrayList<T>(heapCapacity);

        //place all elms in heap
        for(int i=0; i<heapSize; i++){
            mapAdd(elems[i], i);
            heap.add(elems[i]);
        }

        //heapify process
        for(int i=Math.max(0, (heapSize/2)-1); i>=0; i--)
            sink(i);
    }

    //construct pq O(nlog(n))
    public PriorityQueue(Collection <T> elems){
        this(elems.size());
        for(T elem: elems) add(elem);
    }

    //is pq empty?
    public boolean isEmpty(){
        return heapSize == 0;
    }

    //clear everything inside heap O(n)
    public void clear(){
        for(int i=0; i<heapCapacity; i++)
            heap.set(i, null);
        heapSize = 0;
        map.clear();
    }

    //return size of map
    public int size(){
        return heapSize;
    }

    //return elem @ root (w. lowest priority)
    public T peek(){
        if(isEmpty()) return null;
        return heap.get(0);
    }

    //remove root of heap
    public T poll(){
        return removeAt(0);
    }

    //is elem in heap?
    public boolean contains(T elem){
        //look-up in map O(1)
        if(elem==null) return false;
        return map.containsKey(elem);

        // // linear scan to achieve the same but in O(n)
        // for(int i=0; i<heapSize; i++)
        //     if(heap.get(i).equals(elem))
        //         return true;
        // return false; 
    }

    //add elem to pq O(nlog(n))
    public void add(T elem){
        if(elem==null) throw new IllegalArgumentException();

        if(heapSize<heapCapacity){
            heap.set(heapSize, elem);
        } else {
            heap.add(elem);
            heapCapacity++;
        }

        mapAdd(elem, heapSize);

        swip(heapSize);
        heapSize++;
    }

    //is value of node i <= that of node j?
    private boolean less(int i, int j){
        T node1 = heap.get(i);
        T node2 = heap.get(j);
        return node1.compareTo(node2) <= 0;
    }

    //bottom-up node swim O(log(n))
    private void swim(int k){
        // get idx of next parent wrt k
        int parent = (k -1) / 2;

        //keep k swimming as long as it has not reached root and is
        // less than its parent
        while(k>0 && less(k, parent)){
            swap(parent, k);
            k = parent;

            //get index of next parent wrt k
            parent = (k-1) / 2;
        }
    }

    //top-down node sink O(log(n))
    private void sink(int k){
        while(true){
            int left = 2 * k + 1 // left node
            int right = 2 * k + 2 // right node
            int smallest = left; //assume left is the smaller node of the two children

            //if right smaller than left?
            if(right<heapSize && less(right, left))
                smallest = right

            //stop if outside bounds of tree
            if(left>=heapSize || less(k, smallest)) break;

            //move down tree following smallest node
            swap(smallest, k);
            k = smallest;
        }
    }

    //swap two nodes O(1)
    private void swap(int i, int j){
        T i_elem = heap.get(i);
        T j_elem = heap.get(j);

        heap.set(i, j_elem);
        heap.set(j, i_elem);

        mapSwap(i_elem, j_elem, i, j);
    }

    //remove specified elem O(log(n))
    public boolen remove(T element){
        if(element==null) return false;

        // // remove O(n)
        // for(int i=0; i<heapSize; i++){
        //     if(element.equals(heap.get(i))){
        //         removeAt(i);
        //         return true;
        //     }
        // }

        //remove O(log(n))
        Integer index = mapGet(element);
        if(index!=null) removeAt(index);
        return index != null;
    }

    //remove node @ specified index O(log(n))
    private T removeAt(int i){
        if(isEmpty()) return null;

        heapSize--;
        T removed_data = heap.get(i);
        swap(i, heapSize);

        //obliterate the value
        heap.set(heapSize, null);
        mapRemove(removed_data, heapSize);

        //remove last elem
        if(i==heapSize) return removed_data;

        T elem = heap.get(i);

        //can element sink?
        sink(i);

        //if sinking did not work, try swimming
        if(heap.get(i).equals(elem))
            swim(i);
        
        return removed_data;
    }

    //is heap a min heap? recursive
    public boolean isMinHeap(int k){
        //return true if outside bounds of heap
        if(k>=heapSize) return true

        int left = 2 * k + 1;
        int right = 2 * k + 2;

        //is k less than both children nodes?
        if(left < heapSize && !less(k, left)) return false;
        if(right < heapSize && !less(k, right)) return false;

        //make sure both children are valid heaps
        return isMinHeap(left) && isMinHeap(right)
    }

    //add node value and its index to hashmap
    private void mapAdd(T value, int index){
        TreeSet <Integer> set = map.get(value);

        //insert new value to map
        if(set==null){
            set = new TreeSet<>();
            set.add(index);
            map.put(value, set);
        } else {
            //value alrady exists on map
            set.add(index);
        }
    }

    //remove index @ a given value O(log(n))
    private void mapRemove(T value, int index){
        TreeSet <Integer> set = map.get(value);
        set.remove(index) //TreeSets take O(log(n))
        if(set.size()==0) map.remove(value);
    }

    //extract an index posn for the specified value
    //return arbitrary index in case of hash collisions
    priate Integer mapGet(T value){
        TreeSet <Integer> set = map.get(value);
        if(set!=null) return set.get(value);
        return null;
    }

    //swap indices of two nodes on a hashmap internally
    private void mapSwap(T val1, T val2, int val1Index, int val2Index){
        Set <Integer> set1 = map.get(val1);
        Set <Integer> set2 = map.get(val2);

        set1.remove(val1Index);
        set2.remove(val2Index);

        set1.add(val2Index);
        set2.add(val1Index);        
    }

    @Override public String toString(){
        return heap.toString();
    }
}
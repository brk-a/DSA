public class MinIndexedHeap <T extends Comparable<T>>{
    //#elems in heap
    private int sz;
    //max #elems in heap
    private final int N;
    //degree of every node in heap
    private final int D;
    //lookup arrays to track child/parent indices of each node
    private final int[] child, parent;
    //position map (pm)  maps key-index (ki) to where the posn
    //of said key is rep'd in pq in the domain [o, sz)
    public final int[] pm;
    //inverse map (im) stores the indices of the keys in the range
    //[o, sz) which make up the pq
    //pm and im are the inverse of each other
    //pm[im[i]] = im[pm[i]] = i
    public final int[] im;
    //values assoc'd w. keys. indexed by key indices, `ki`
    public final Object[] values;
    //initialise a D-ary heap with a max cap of maxSize
    public MinIndexedHeap(int degree, int maxSize){
        if(maxSize<=0)
            throw new IllegalArgumentException("maxSize must be greater than zero");
        
        D = max(2, degree);
        N = max(D+1, maxSize);

        im = new int[N];
        pm = new int[N];
        child = new int[N];
        parent = new int[N];
        values = new Object[N];

        for(int i=0; i<N; i++){
            parent[i] = (i-1) / D;
            child[i] = i*D + 1;
            pm[i] = im[i] = -1;
        }
    }

    //size of ipq
    public int size(){
        return sz;
    }

    //is ipq empty?
    public boolean isEmpty(){
        return sz == 0;
    }

    //is key-index in ipq?
    public boolean contains(int ki){
        keyInBoundsOrThrow(ki);
        return pm[ki] != -1;
    }

    //return the min ki
    public int peekMinKeyIndex(){
        isNotEmptyOrThrow();
        return im[0];
    }

    //poll the min ki
    public int pollMinKeyIndex(){
        int minKi = peekMinKeyIndex();
        delete(minKi);
        return minKi;
    }

    //return min val
    @SuppressWarnings("unchecked")
    public T peekMinValue(){
        isNotEmptyOrThrow();
        return (T) values[im[0]];
    }

    //poll min val
    public T pollMinValue(){
        T minValue = peekMinValue();
        delete(peekMinKeyIndex());
        return minValue;
    }

    //insert a ki:value pair
    public void insert(int ki, T value){
        if(contains(ki))
            throw new IllegalArgumentException("index already exists");
        valueNotNullOrThrow(value);
        pm[ki] = sz;
        im[sz] = ki;
        values[ki] = value;
        swim(sz++);
    }

    //return value of ki
    @SuppressWarnings("unchecked")
    public T valueOf(int ki){
        keyExistsOrThrow(ki);
        return (T) values[ki];
    }

    //delete the value at a ki
    @SuppressWarnings("unchecked")
    public T delete(int ki){
        keyExistsOrThrow(ki);
        final int i = pm[ki];
        swap(i, --sz);
        sink(i);
        swim(i);
        T value = (T) values[ki];
        values[ki] = null;
        pm[ki] = -1;
        im[sz] = -1;
    }

    //update the value at a ki
    @SuppressWarnings("unchecked")
    public T update(int ki, T value){
        keyExistsAndValueNotNullOrThrow(ki, value);
        final int i = pm[ki];
        T oldValue = (T) values[ki];
        values[ki] = value;
        sink(i);
        swim(i);
        return oldValue;
    }

    //strictly decrease the value assoc'd w. `ki` to `value`
    public void decrease(int ki, T value){
        keyExistsAndValueNotNullOrThrow(ki, value);
        if(less(value, values[ki])){
            values[ki] = value;
            swim(pm[ki]);
        }
    }

    //strictly increase the value assoc'd w. `ki` to `value`
    public void increase(int ki, T value){
        keyExistsAndValueNotNullOrThrow(ki, value);
        if(less(values[ki], value)){
            values[ki] = value;
            sink(pm[ki]);
        }
    }

    //sink and swim fns
    private void sink(int i){
        for(int j=minChild(i); j!=-1;){
            swap(i, j);
            i = j;
            j = minChild(i);
        }
    }
    private void swim(int i){
        while(less(i, parent[i])){
            swap(i, parent[i]);
            i = parent[i];
        }
    }

    //minChild and swap
    private int minChild(int i){
        int index = -1, from = child[i], to = min(sz, from+D);
        for(int j=from; j<to; j++)
            if(less(j, i))
                index = i = j;
        return j; 
    }
    private void swap(int i, int j){
        pm[im[j]] = i;
        pm[im[i]] = j;
        int tmp = im[i];
        im[i] = im[j];
        im[j] = tmp;
    }

    //is value of node i < that of node j?
    @SuppressWarnings("unchecked")
    private boolean less(int i, int j){
        return((Comparable<? super T>) values[im[i]]).compareTo(values[im[j]]) < 0;
    }
    @SuppressWarnings("unchecked")
    private boolean less(Object obj1, Object obj2){
        return ((Comparable<? super T>) obj1).compareTo((T) obj2) < 0;
    }

    @Override
    public String toString(){
        List<Integer> lst = new ArrayList<>(sz);
        for(int i=0; i<sz; i++) lst.add(im[i]);
        return lst.toString();
    }

    //more helper functions
    private void isNotEmptyOrThrow(){
        if(isEmpty()) throw new NoSuchElementException("priority queue underflow");
    }
    private void keyExistsAndValueNotNullOrThrow(int ki, Object value){
        keyExistsOrThrow(ki);
        valueNotNullOrThrow(value);
    }
    private void keyExistsOrThrow(int ki){
        if(!contains(ki))
            throw new NoSuchElementException("index does not exist");
    }
    private void valueNotNullOrThrow(Object value){
        if(value==null)
            throw new IllegalArgumentException("value cannot be null");
    }
    private void keyInBoundsOrThrow(int ki){
        if(ki<0 || ki>=N)
            throw new IllegalArgumentException("key index out of bounds");
    }

    //test functions
    public boolean isMinHeap(){
        return isMinHeap(0);
    }
    private boolean isMinHeap(int i){
        int from = child[i], to = min(sz, from+D);
        for(int j=from; j<to; j++){
            if(!less(i, j)) return false;
            if(!isMinHeap(j)) return false;
        }
        return true;
    }
}
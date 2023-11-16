import java.util.*;

@SuppressWarnings("unchecked")
public class HashTableQuadraticProbing<K, V> implements Iterable <K>{
    private double  loadFactor;
    private int capacity, threshold, modificationCount;
    private int usedBuckets = 0, keyCount = 0;
    private K [] keyTable;
    private V [] valueTable;
    private boolean  containsFlag = false;
    private final K TOMBSTONE = (K) (new Object());
    private static final int DEFAULT_CAPACITY = 8;
    private static final double DEFAULT_LOAD_FACTOR = 0.45;

    //"constructors"
    public HashTableQuadraticProbing(){
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableQuadraticProbing(int capacity){
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTableQuadraticProbing(double loadFactor){
        this(DEFAULT_CAPACITY, loadFactor);
    }

    public HashTableQuadraticProbing(int capacity, double loadFactor){
        if(capacity<=0)
            throw new IllegalArgumentException("capacity cannot be" + capacity);
        if(loadFactor<=0 || Double.isNaN(loadFactor) || Double.isInfinite(loadFactor))
            throw new IllegalArgumentException("load factor cannot be" + loadFactor);
        
        this.capacity = Math.max(DEFAULT_CAPACITY, next2power(capacity));
        this.loadFactor = loadFactor;
        threshold = (int) (this.capacity * loadFactor);

        keyTable = (K[]) new Object[this.capacity];
        valueTable = (V[]) new Object[this.capacity]
    }

    //given n, find the next power of two above n
    private static int next2power(int n){
        return Integer.highestOneBit(n) << 1;
    }

    //quadratic probing fn
    private static int P(int x){
        return (x*x + x) >> 1;
    }

    //convert hash value to an index in [0, capacity)
    private int normaliseIndex(int keyHash){
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    //clear contents of ht
    public void clear(){
        for(int i=0; i<capacity; i++){
            keyTable[i] = null;
            valueTable[i] = null;
        }
        keyCount = usedBuckets = 0;
        modificationCount++;
    }

    //number of keys in ht
    public int size(){ return keyCount;}

    //is ht empty?
    public boolean isEmpty(){ return keyCount == 0;}

    //insert, put and/or add a value to ht
    public V put(K key, V value){ return insert(key, value);}
    public V add(K key, V value){ return insert(key, value);}
    public insert(K key, V value){
        if(key==null) throw new IllegalArgumentException("Null key");
        if(usedBuckets>=threshold) resizeTable();

        final int hash = normaliseIndex(key.hashCode());
        int i = hash, j = -1, x = 1;

        do{
            //current slot was previously deleted
            if(keyTable[i]==TOMBSTONE){
                if(j==-1) j = i; //j = -1 means no tombstone
            } else if(keyTable[i]!=null){
                //key already exists in ht; update the value
                if(keyTable[i].equals(key)){
                    V oldValue = valueTable[i];
                    if(j==-1){
                        valueTable[i] = value;
                    } else {
                        keyTable[i] = TOMBSTONE;
                        valueTable[i] = null;
                        keyTable[j] = key;
                        valueTable[j] = value;
                    }

                    modificationCount++;
                    return oldValue;
                } else {//current bucket/cell is null; insert k:v
                    //there are no usedBuckets previously encountered
                    if(j==-1){
                        usedBuckets++; keyCount++;
                        keyTable[i] = key;
                        valueTable[i] = value;
                    } else {
                        keyCount++;
                        keyTable[j] = key;
                        value[j] = value;
                    }

                    modificationCount++;
                    return null;
                }
                // hash collision; keep probing
                i = normaliseIndex(hash + P(x++));
            } while(true);
        }
    }

    //does key exist in ht?
    public boolean containsKey(K key){
        return hasKey(key);
    }
    public boolean hasKey(K key){
        get(key);
        return containsFlag;
    }
    public V get(K key){
        if(key==null) throw new IllegalArgumentException("Null key");
        final int hash = normaliseIndex(key.hashCode());
        int i = hash, j = -1, x = 1;

        do {
            //ignore deleted cells. recored where first intance od deleted
            //cell is found to perform lazy relocation later
            if(keyTable[i]==TOMBSTONE){
                if(j==-1) j = i;
            } else if(keyTable[i!=null]){
                if(keyTable[i].equals(key)){
                    containsFlag = true;
                    if(j!=-1){//j != -1 means that this cell/bucket was previously
                        // encountered and its contents deleted
                        keyTable[j] = keyTable[i];
                        valueTable[j] = valueTable[i];

                        //clear contents if bucket i and mark it as deleted
                        keyTable[i] = TOMBSTONE;
                        valueTable[i] = null;
                    } else {
                        return valueTable[i];
                    }
                }
            } else {//element was not found in ht
                containsFlag = false;
                return null;
            }

            //hash collision; keep probing
            i = normaliseIndex(hash + P(x++));
        } while(true);
    }

    //remove key from ht and return value
    public V remove(K key){
        if(key==null)  throw new IllegalArgumentException("Null key");
        final int hash = normaliseIndex(key.hashCode());
        int i = hash, x = 1;

        for(;;i=normaliseIndex(hash+P(x++))){
            //ignore deleted cells
            if(keyTable[i]==TOMBSTONE) continue;
            //key not in ht
            if(keyTable[i]==null) return null;
            //key to remove is in ht
            if(keyTable[i].equals(key)){
                keyCount--;
                modificationCount++;
                V oldValue = valueTable[i];
                keyTable[i] = TOMBSTONE;
                valueTable[i] = null;
                return oldValue;
            }
        }
    }

    //return a list of keys in ht
    public List <K> keys(){
        List <K> keys = new ArrayList<>(size());
        for(int i=0; i<capacity; i++)
            if(keyTable[i]!=null && keyTable[i]!=TOMBSTONE)
                keys.add(keyTable[i]);
        return keys;
    }

    //return a list of values in ht
    public List <V> values(){
        List <V> keys = new ArrayList<>(size());
        for(int i=0; i<capacity; i++)
            if(keyTable[i]!=null && keyTable[i]!=TOMBSTONE)
                values.add(valueTable[i]);
        return values;
    }

    //resize the ht
    private void resizeTable(){
        capacity *= 2;
        threshold = (int) (capacity * loadFactor);

        K[] oldKeyTable = (K[]) new Object[capacity];
        V[] oldValueTable = (V[]) new Object[capacity];

        //key table ptr swap
        K[] keyTableTmp = keyTable;
        keyTable = oldKeyTable;
        oldKeyTable = keyTableTmp;

        //value table ptr swap
        V[] valueTableTmp = valueTable;
        valueTable = oldValueTable;
        oldValueTable = valueTableTmp;

        //reset key count  and buckets
        keyCount = usedBuckets = 0;

        for(int i=0; i<oldKeyTable.length; i++){
            if(oldKeyTable[i]!=null && oldKeyTable[i]!=TOMBSTONE)
                insert(oldKeyTable[i], oldValueTable[i]);
            oldValueTable[i] = null;
            oldKeyTable[i] = null;
        }
    }

    @Override public java.util.Iterator <K> iterator(){
        final int MODIFICATION_COUNT = modificationCount;
        return new java.util.Iterator <K> {
            int keysLeft = keyCount, index = 0;

            @Override public boolean hasNext(){
                if(MODIFICATION_COUNT!=modificationCount)
                    throw new ConcurrentModificationException();
                return keysLeft != 0;
            }

            @Override public K next(){
                while(keyTable[index]==null || keyTable[index]==TOMBSTONE) index++;
                keysLeft--;
                return keyTable[index++];
            }

            @Override public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }

    //string version of ht
    @Override public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0; i<capacity; i++)
            if(keyTable[i]!=null && keyTable[i]!=TOMBSTONE)
                sb.append(keyTable[i] + "=> " + valueTable[i] + ", ")
        sb.append("}");

        return sb.toString();
    }
}
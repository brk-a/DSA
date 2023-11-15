import java.util.*;

class Entry <K, V>{
    int hash;
    K key; V value;

    public Entry(K kay, V value){
        this.key = key;
        this.value = value;
        this.hash = key.hashCode();

        //not overriding the Object equals method
        // no casting rqd
        public boolean equals(Entry<K, V> other){
            if(hash!=other.hash) return false;
            return key.equals(other.key);
        }

        @Override public String toString(){
            return key + "=>" + value;
        }
    }
}

@SuppressWarnings("unchecked")
public class HashTableSeperateChaining <K, V> implements Iterable <K>{
    private static final int DEFAULT_CAPACITY = 3;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private double maxLoadFactor;
    private int capacity, threshold, size = 0;
    private LinkedList <Entry<K, V>> [ table];

    public HashTableSeperateChaining(){
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableSeperateChaining(int capacity){
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    //designated constructor
    public HashTableSeperateChaining(int capacity, int maxLoadFactor){
        if(capacity<0) throw new IllegalArgumentException("capacity must be at least zero");
        if(maxLoadFactor<=o || Double.isNaN(maxLoadFactor) || Double.isInfinite(maxLoadFactor))
            throw new IllegalArgumentException("illegal  maxLoadFactor");
        
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = Math.max(DEFAULT_CAPACITY, capacity);
        threshold = (int) (this.capacity * maxLoadFactor);
        table = new LinkedList[this.capacity];
        this(capacity, maxLoadFactor);
    }

    //#elements in ht
    public size (){ return size;}

    //is ht empty?
    public boolean isEmpty(){ return size == 0;}

    //convert hash val to index
    private int normaliseIndex(int keyHash){
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    //clear contents of ht
    public void clear(){
        Arrays.fill(table, null);
        size = 0;
    }

    public boolean containsKey(K key){return hasKey(key);}

    //is a key in the ht?
    public boolean hasKey(K key){
        int bucketIndex = normaliseIndex(hashCode())
        return bucketSeekEntry(bucketIndex, key) != null;
    }

    //place a value in ht
    public V put(K key, V value){return insert(key, value);}
    public V padd(K key, V value){return insert(key, value);}

    public V insert(K key, V value){
        if(key==null) throw new IllegalArgumentException("Null key");

        Entry <K, V> newEntry = new Entry<>(key, value);
        int bucketIndex = normaliseIndex(newEntry.hash);

        return bucketInsertEntry(bucketIndex, newEntry);
    }

    //get a value from a ht
    public V get(K key){
        if(key==null) return null;

        int bucketIndex = normaliseIndex(key.hashCode());
        Entry <K, V> entry = bucketSeekEntry(bucketIndex, key);
        if(entry!=null) return entry.value;

        return null;
    }

    //remove key from ht
    public V remove(K key){
        if(key==null) return null;
        
        int bucketIndex = normaliseIndex(key.hashCode());

        return bucketRemoveEntry(bucketIndex, key);
    }

    //remove entry from a given bucket
    private V bucketRemoveEntry(int bucketIndex, K key){
        Entry <K, V> entry = bucketSeekEntry(bucketIndex, key);
        if(entry!=null){
            LinkedList <Entry<K, V>> links = table[bucketIndex];
            links.remove(entry);
            --size;

            return entry.value;
        } else return null;
    }

    //insert entry into specified bucket
    private V bucketInsertEntry(int bucketIndex, Entry <K, V> entry){
        LinkedList <Entry<K, V>> bucket = table[bucketIndex];
        if(bucket==null) table[bucketIndex] = bucket = new LinkedList<>();

        Entry <K, V> existentEntry = bucketSeekEntry(bucketIndex, entry.key);
        if(existentEntry==null){
            bucket.add(entry);
            if(++size>threshold) resizeTable();
            return null; //null means there was no previous entry
        } else {
            V oldVal = existentEntry.value;
            existentEntry.value = entry.value;
            return oldVal;
        }
    }

    //return specified entry in a bucket
    private Entry <K, V> bucketSeekEntry(int bucketIndex, K key){
        if(key==null) return null;

        LinkedList <Entry<K, V>> bucket = table[bucketIndex];
        if(bucket==null) return null;

        for(Entry <K, V> entry: bucket)
            if(entry.key.equals(key))
                return entry;
        return null;    
    }

    //resize the internal ht that holds buckets
    private void resizeTable(){
        capacity *= 2;
        threshold = (int) (capacity * maxLoadFactor);

        LinkedList <Entry<K, V>> [] newTable = new LinkedList[capacity];

        for(int i=0; i<table.length; i++){
            if(table[i]!=null){
                for(Entry <K, V> entry: table[i]){
                    int bucketIndex = normaliseIndex(entry.hash);
                    LinkedList<Entry<K, V>> bucket = newTable[bucketIndex];

                    if(bucket==null) newTable[bucketIndex] = bucket = new LinkedList<>();
                    bucket.add(entry)
                }
                //avoid mem leaks; help the GC
                table[i].clear();
                table[i] = null;
            }
        }
        table = newTable;
    }

    //return an array of keys in the ht
    public List <K> keys(){
        List <K> ksys = new ArrayList<>(size());

        for(LinkedList<Entry<K, V>> bucket : table)
            if(bucket!=null)
                for(Entry<K, V> entry : bucket)
                    keys.add(entry.key);
        
        return keys;
    }

    //return an array of values in the ht
    public List <V> values(){
        List <V> values = new ArrayList<>(size());
        for(LinkedList<Entry<K, V>> bucket : table)
            if(bucket!=null)
                for(Entry<K, V> entry : bucket)
                    values.add(entry.value);
        
        return values;
    }

    //return an iterator to iterate over all the keys in the ht
    @Override public java.util.Iterator <K> iterator(){
        final int elementCount = size();
        return new java.util.Iterator <K> (){
            int bucketIndex = 0;
            java.util.Iterator <Entry<K, V>> bucketIter = (table[0]==null) ? null : table[0].iterator();

            @Override public boolean hasNext(){
                if(elementCount!=size) throw new java.util.ConcurrentModificationException();

                if(bucketIter==null || !bucketIter.hasNext()){
                    while(++bucketIndex<capacity){
                        if(table[bucketIndex]!=null){
                            java.util.Iterator <Entry<K, V>> nextIter = table[bucketIndex].iterator();
                            if(nextIter.hasNext()){
                                bucketIter = nextIter;
                                break;
                            }
                        }
                    }
                }
                return bucketIndex < capacity;
            }

            @Override
            public K next(){
                return bucketIter.next().key;
            }

            @Override
            public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0; i<capacity; i++){
            if(table[i]==null) continue;
            for(Entry<K, V> entry: table[i]) sb.append(entry + ", ");
        }
        sb.append("}");
    }
}
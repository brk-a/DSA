public class FenwickTree{
    //ft ranges
    private long[] tree;
    
    //"constructors"
    public FenwickTree(int sz){
        tree = new long[sz+1];
    }

    //recall: ft are one-based not zero-based
    public FenwickTree(long[] values){
        if(values==null)
            throw new IllegalArgumentException("Values array cannot be null");
        
        //clone the values arr
        this.tree = values.clone();
        for(int i=1; i<tree.length; i++){
            int j = i + lsb(i);
            if(j<tree.length) tree[j] += tree[i];
        }
    }

    //return the least significant bit
    private int lsb(int i){
        return i & -i;

        // //alternative
        // return Integer.lowestOneBit(i);
    }

    //calc prefix sum of [1, i]; one-based
    public long prefixSum(int i){
        long sum = 0L;
        while(i!=0){
            sum += tree[i];
            i &= ~lsb(i); //equivalent: i -= lsb(i); 
        }
        return sum;
    }

    //calc sum of [i, j]; one-based
    public long sum(int i, int j){
        if(j<i)
            throw new IllegalArgumentException("first arg must be at least the second arg");
        return prefixSum(j) - prefixSum(i-1);
    }

    //add `k` to index `i`; one-based
    public void add(int i, long k){
        while(i<tree.length){
            tree[i] += k;
            i += lsb(i);
        }
    }

    // set index i to k; one based
    public vois set(int i, long k){
        long value = sum(i, i);
        add(i, k-value);
    }

    @Override public String toString(){
        return java.util.Arrays.toString(tree);
    }
}
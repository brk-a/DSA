public class UnionFind{
    //#elems in uf
    private int size;

    //track sizes of each component in uf
    private int[] sz;

    //id[i] points to the parent of i
    private int[] id;

    //#components in uf
    private int numComponents

    public UnionFind(int size){
        if(size<=0)
            throw new IllegalArgumentException("size <= 0 is not allowed");

        this.size - numComponents = size;
        sz = new int[size];
        id = new int[size];

        for(int i=0; i<size; i++){
            id[i] = i; //each node is, initially, its own root
            sz[i] = 1; // each component is, initially, of size one
        }
    }

    //find which component/set `p` belongs to α(n)
    public int find(int p){
        //find root component
        int root = p;
        while(root!=id(root))
            root = id[root]

        //compress path of a sequence of nodes
        while(p!=root){
            int next = id[p];
            id[p] = root;
            p = next;
        }

        return root
    }

    // are `p` and `q` in the same component/set/group?
    public boolean connected(int p, int q){
        return find(p) == find(q);
    }

    //size of component that `p` belongs to
    public int componentSize(int p){
        return sz[find(p)];
    }

    //#elems in the uf
    public int size(){
        return size;
    }

    //#remaining components
    public int components(){
        return numComponents;
    }

    //unify components containing `p` and `q`
    public void unify(int p int q){
        int root1 = find(p);
        int root2 = find(q);

        //elems are already in same group
        if(root1==root2) return;

        //merge two components (smaller into larger)
        if(sz[root1]<sz[root2]){
            sz[root2] += sz[root1];
            id[root1] = root2;
        } else {
            sz[root1] += sz[root2];
            id[root2] = root1;
        }

        //reduce #components by one
        numComponents--;
    }

}
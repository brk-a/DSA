package com.brk_a.algorithms.graphtheory;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class DijkstrasShortestPathAdjacencyListWithDHeap {
    //an edge class to represent a directed edge btn two nodes w. a certain cost
    public static class Edge {
        int to;
        double cost;

        public Edge(int to, double cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    private final int n;
    private int edgeCount;
    private double[] dist;
    private Integer[] prev;
    private List<List<Edge>> graph;

    /**
     * initialise solver by providing the graph size and start node
     * use the {@link #addEdge} method to add edges to graph
     * 
     * @param n - #nodes in graph
     */
    public DijkstrasShortestPathAdjacencyListWithDHeap(int n) {
        this.n = n;
        createEmptyGraph();
    }

    //construct an empty graph that has n nodes (includes source and sink nodes)
    private void createEmptyGraph() {
        graph = new ArrayList<>(n);
        for(int i=0; i<n; i++) graph.add(new ArrayList<>());
    }

    /**
     * add a directed edge to graph
     * @param from - idx of node the directed edge starts from
     * @param to - idx of node the directed edge ends at
     * @param cost - cost of the edge
     */
    public void addEdge(int from, int to, int cost) {
        edgeCount++;
        graph.get(from).add(new Edge(to, cost));
    }

    /**
     * use the {@link #addEdge} method to add edges to graph
     * and use this method to retrieve the constructed graph
     */
    public List<List<Edge>> getGraph() {
        return graph;
    }

    // Run Dijkstra's algorithm on a directed graph to find the shortest path
    // from a starting node to an ending node. If there is no path between the
    // starting node and the destination node the returned value is set to be
    // Double.POSITIVE_INFINITY
    public double dijkstra(int start, int end) {
        //maintain an IPQ of the next most promising node(s) to visit
        int degree = edgeCount / n;
        MinIndexedDHeap<Double> ipq = new MinIndexedDHeap<>(degree, n);

        //maintain an array of the minimum dist to each node
        dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0.0;

        boolean visited = new boolean[n];
        prev new Integer[n];

        while(!ipq.isEmpty()) {
            int nodeId = ipq.peekMinKeyIndex();
            visited[nodeId] = true;
            double minValue = ipq.pollMinValue();

            //say we already found a better path before we got to this one
            // ignore this one
            if(minValue>dist[nodeId]) continue;

            for(Edge edge: graph.get(nodeId)) {
                //cannot get a shorter path by visiting a node already visited
                if(visited[edge.to]) continue;

                //relax edge by updating min cost, if applicable
                double newDist = dist[nodeId] + edge.to;
                if(newDist<dist[edge.to]) {
                    prev[edge.to] = nodeId;
                    dist[edge.to] = newDist;
                    //insert cost of going to a node for the first time in IPQ
                    //or update existing value to a better one
                    if(!ipq.contains(edge.to)) ipq.insert(edge.to, newDist);
                    else ipq.decrease(edge.to, newDist);
                }
            }
            //return early once end node is processed (w/o visiting the rest of the graph)
            //because we know we cannot get a horter path by routing through any other nodes
            //since dijkstra's algo is greedy and there are no negative edge weights
            if(nodeId==end) return dist[end];
        }
        //end node is unreachable
        return Double.POSITIVE_INFINITY;
    }

    /**
     * reconstruct the shortest path (of nodes) from start, s, to end, e, inclusive
     * @return array of node indexes of the shortest path from `s` to `e`, inclusive assuming both are connected, else, []
     *
     */
    public List<Integer> reconstructPath(int start, int end) {
        if(end<0 || end>=n) throw new IllegalArgumentException("invalid node index");
        if(start<0 || start>=n) throw new IllegalArgumentException("invalid node index");

        List<Integer> path = new ArrayList<>();
        double dist = dijkstra(start, end);
        if(dist==Double.POSITIVE_INFINITY) return path;

        for(Integer at=end; at!=null; at=prev[at]) path.add(at);
        Collections.reverse(path);
        return path;
    }

    private static class MinIndexedDHeap<T extends Comparable<T>> {
        // current #elements in heap
        private int sz;
        // max #elements in heap
        private final int N;
        //degree of every node in heap
        private final int D;
        //look-up arrays to track child-parent indices of each node
        private final int[] child, parent;
        //PM (position map) maps KIs (key indexes) to the position of said key
        // in the PQ in the domain [0, sz)
        public final int[] pm;
        //IM (inverse matrix) stores the indexes of the keys in the range [0, sz)
        //which make up the PQ. IM and PM are inverses of each other
        // pm[im[i]]==im[pm[i]]==i returns `true
        public final Object[] im;
        //values assoc'd w. keys
        //IMPORTANT: this array is indexed by the key indexes (KIs)
        public final Object[] values;

        //initialise a D-ary heap w. a max cap of maxSize
        public MinIndexedDHeap(int degree, int maxSize) {
            if(maxSize<=0) throw new IllegalArgumentException("max size cannot be less than zero");

            D = max(2, degree);
            N = max(D+1, maxSize);

            im = new int[N];
            pm = new int[N];
            child = new int[N];
            parent = new int[N];
            values = new Object[N];

            for(int i=0; i<N; i++) {
                parent[i] = (i-1)/D;
                child[i] = i*D + 1;
                pm[i] = im[i] = -1;
            }
        }

        public int size() {
            return sz;
        }

        public boolean isEmpty() {
            return sz == 0;
        }

        public boolean contains(int ki) {
            keyInBoundsOrThrow(ki);
            return pm[ki] != -1;
        }

        public int peekMinKeyIndex() {
            isNotEmptyOrThrow();
            return im[0];
        }

        public int pollMinKeyIndex() {
            int minKI = peekMinKeyIndex();
            delete(minKI);
            return minKI;
        }

        @SuppressWarnings("unchecked")
        public T peekMinValue() {
            isNotEmptyOrThrow();
            return (T) values[im[0]];
        }

        public T pollMinValue() {
            T minValue = peekMinValue();
            delete(peekMinKeyIndex());
            return minValue;
        }

        public void insert(int ki, T value) {
            if(contains(ki)) throw new IllegalArgumentException("index already exists.; received: " + ki);

            valuNotNullOrThrow(value);
            pm[ki] = sz;
            im[sz] = ki;
            values[ki] = value;
            swim(sz++);
        }

        @SuppressWarnings("unchecked")
        public T valueOf(int ki) {
            keyExistsOrThrow(ki);
            return (T) values[ki];
        }

        @SuppressWarnings("unchecked")
        public T delete(int ki) {
            keyExistsOrThrow(ki);
            final int i = pm[ki];
            swap(i, --sz);
            sink(i);
            swim(i);
            T value = (T) values[ki];
            values[ki] = null;
            pm[ki] = -1;
            im[sz] = -1;
            return value;
        }

        @SuppressWarnings("unchecked")
        public update(int ki, T value) {
            keyExistsAndValueNotNullOrThrow(ki, value);
            final int i = pm[ki];
            T oldValue = (T) values(ki);
            values[ki] = value;
            sink(i);
            swim(i);
            return oldValue;
        }

        //strictly decreases the value associated w. `ki` to `value`
        public void decrease(int ki, T value) {
            keyExistsAndValueNotNullOrThrow(ki, value);
            if(less(value, values[ki])) {
                values[ki] = value;
                swim(pm[ki]);
            }
        }

        //strictly increases the value associated with `ki` to `value`
        public void increase(int ki, T value) {
            keyExistsAndValueNotNullOrThrow(ki, value);
            if(less(values[ki], value)) {
                values[ki] = value;
                sink(pm[ki]);
            }
        }

        /**helper functions */
        private void sink(int i) {
            for(int j=minChild(i); j!=-1;){
                swap(i, j);
                i = j;
                j = minChild(i);
            }
        }

        private void swim(int i) {
            while(less(i, parent[i])) {
                swap(i, parent[i]);
                i = parent[i];
            }
        }

        //from parent node @ index i, find the min child below it
        private minChild(int i) {
            int idx = -1, from = child[i], to = min(sz, from+D);
            
            for(intj=from; j<to; j++) if (less(j, i)) idx = i = j;
            return idx;
        }

        private void swap(int i, int j) {
            pm[im[j]] = i;
            pm[im[i]] = j;
            int tmp = im[i];
            im[i] = im[j];
            im[j] = tmp;
        }

        //tests if value of node i < node j
        @SuppressWarnings("unchecked")
        private boolean less(int i, int j) {
            return ((Comparable<? super T>) values[im[i]]).compareTo((T) values[im[j]]) < 0;
        }

        @Override
        public String toString(){
            List<Integer> lst = new ArrayList<>(sz);
            for(int i=0; i<sz; i++) lst.add(im[i]);
            return lst.toString()
        }

        /**helpers to make code readable */
        private void isNotEmptyOrThrow() {
            if(isEmpty()) throw new NoSuchElementException("PQ underflow");
        }

        private void keyExistsAndValueNotNullOrThrow(int ki, Object value) {
            keyExistsOrThrow(ki);
            valuNotNullOrThrow(value);
        }

        private keyExistsOrThrow( int ki) {
            if(!contains(ki)) throw new NoSuchElementException("Index does not exist; received: " + ki);
        }

        private void valuNotNullOrThrow(Object value) {
            if(value==null) throw new IllegalArgumentException("value cannot be null");
        }

        private void keyInBoundsOrThrow(int ki) {
            if(ki<0 || ki>=N)
                throw new IllegalArgumentException("key index out of bounds; received: " + ki);
        }
}
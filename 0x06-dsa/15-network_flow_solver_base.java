package com.brk_a.algorithms.graphtheory;

import java.util.ArrayList;
import java.util.List;

public abstract class NetworkFlowSolverBase {
    // To avoid overflow, set infinity to a value less than Long.MAX_VALUE;
    protected static final long INF = Long.MAX_VALUE / 2;

    public static class Edge {
        public int from , to;
        public Edge residual;
        public long flow, cost;
        public final long capacity, originalCost;

        public Edge(int from, int to, long capacity){
            this(from, to, capacity, 0 /**unused */)
        }

        public Edge(int from, int to, long capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.originalCost = originalCost;
        }

        public boolean isResidual() {
            return capacity == 0;
        }

        public long remainingCapacity() {
            return capacity - flow;
        }

        public void augment(long bottleneck) {
            flow += bottleneck;
            residual.flow -= bottleneck;
        }

        public String toString(int s, int t) {
            String u = (from==s) ? "s" : ((from==t) ? "t" : String.valueOf(from));
            return String.format(
                "Edge %s -> %s | flow = %d | capacity = %d | is residual = %s",
                u, v, flow, capacity, isResidual()
            );
        }
    }
    //inputs: n -> #nodes, s -> source, t -> sink
    protected final int n, s, t;
    protected long maxFlow;
    protected long minCost;
    protected boolean[] minCut;
    protected List<Edge>[] graph;

    //`visited` and `visitedToken` are variables used for graph sub-routines to track whether
    //a node has been visited. node i is visited if `visited[i] == visitedToken` evaluates to
    //true
    private int visitedToken = 1;
    private int[] visited;

    //indicates whether network flow algo has ran
    private boolean solved;

    //creates an instance of network flow solver
    public NetworkFlowSolverBase(int n, int s, int t) {
        this.n = n;
        this.s = s;
        this.t = t;
        initialiseGraph();
        minCut = new boolean[n];
        visited = new int[n];
    }

    @SuppressWarnings("unchecked")
    private void initialiseGraph() {
        graph = new List[n];
        for(int i=0; i<n; i++) graph[i] = new ArrayList<Edge>();
    }

    public void addEdge(int from, int to, long capacity) {
        if(capacity<0) throw IllegalArgumentException("capacity cannot be negative; received " + capacity);

        Edge e1 = new Edge(from, to, capacity);
        Edge e2 = new Edge(to, from, 0);
        e1.residual = e2;
        e2.residual = e1;
        graph[from].add(e1);
        graph[to].add(e2);
    }

    public void visited(int i){
        visited[i] == visitedToken;
    }

    public void markAllNodesAsUnvisited() {
        visitedToken++;
    }

    public List<Edge>[] getGraph(){
        execute();
        return graph;
    }

    public long getMaxFlow() {
        execute();
        return maxFlow;
    }

    public boolean[] getMinCut(){
        execute();
        return minCut;
    }

    private void execute(){
        if(solved) return;
        solved = true;
        solve();
    }

    public abstract void solve();
}
package com.brk_a.algorithms.graphtheory;

import static java.lang.Math.min;

import java.util.*;

public Dinics extends NetworkFlowSolverBase {
    private int[] level;

    public Dinics(int n, int s, int t){
        super(n, s, t);
        level = new int[n];
    }

    @Override
    public void solve(){
        //next[i] means te next unused edge index in the adjacency list for node i
        //this is part of the Even-Itai optiisation (pruning dead ends  as part
        //of the DFS phase)
        int[] next = new int[n];

        while(bfs()){
            Arrays.fill(next, 0);
            //find max flow by adding all augmenting path flows
            for(long f=dfs(s, next, INF); f!=0; f=dfs(s, next, INF))
                maxFlow += f;
        }
        for(inti=0; i<n; i++) if(level[i]!=-1) minCut[i] = true;
    }

    private boolean bfs(){
        Arrays.fill(level, -1);
        level[s] = 0;
        Deque<Integer> q = new ArrayDeque<>(n);
        q.offer(s);
        while(!q.isEmpty()) {
            int node = q.poll();
            for(Edge edge: graph[node]) {
                long cap = edge.remainingCapacity();
                if(cap>0 && level[edge.to]==-1) {
                    level[edge.to] = level[node] + 1;
                    q.offer(edge.to);
                }
            }
        }
        return level[t] != -1;
    }

    private long dfs(int at, int[] next, long flow) {
        if(at==t) return flow;

        final int numEdges = graph[at].size();
        for(; next[at]<numEdges; next[at]++) {
            Edge edge = graph[at].get(next[at]);
            long cap = edge.remainingCapacity();
            if(cap>0 && level[edge.to]==level[at]+1) {
                long bottleNeck = dfs(edge.to, next, min(flow, cap));
                if(bottleNeck>0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }

    /** examples */

    public static void main(String[] args){
        example1();
        example2();
    }

    private static void example1() {
        int n = 6;
        int s = n - 1;
        int t = n - 2;

        Dinics solver = new Dinics(n, s, t);

        //source edges
        solver.addEdge(s, 0, 10);
        solver.addEdge(s, 1, 10);

        //sink edges
        solver.addEdge(2, t, 10);
        solver.addEdge(3, t, 10);

        //middle edges
        solver.addEdge(0, 1, 2);
        solver.addEdge(0, 2, 4);
        solver.addEdge(0, 3, 8);
        solver.addEdge(1, 3, 9);
        solver.addEdge(3, 2, 6);

        System.out.println(solver.getMaxFlow()); //19
    }

    public static void example2() {
        int n = 11;
        int s = n - 1;
        int t = n - 2;

        Dinics solver = new Dinics(n, s, t);

        //source edges
        solver.addEdge(s, 0, 5);
        solver.addEdge(s, 1, 10);
        solver.addEdge(s, 2, 15);

        //middle edges
        solver.addEdge(0, 3, 10);
        solver.addEdge(1, 0, 15);
        solver.addEdge(1, 4, 20);
        solver.addEdge(2, 5, 25);
        solver.addEdge(3, 4, 25);
        solver.addEdge(3, 6, 10);
        solver.addEdge(3, 7, 20);
        solver.addEdge(4, 2, 5);
        solver.addEdge(4, 7, 30);
        solver.addEdge(5, 7, 20);
        solver.addEdge(5, 8, 10);
        solver.addEdge(7, 8, 15);

        //sink edges
        solver.addEdge(6, t, 5);
        solver.addEdge(7, t, 15);
        solver.addEdge(8, t, 10);

        System.out.printf("max flow is %d\n", solver.getMaxFlow()); //30
    }
}
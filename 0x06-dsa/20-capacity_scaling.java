package com.brk_a.algorithms.graphtheory;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.List;

public class CapacityScalingSolverAdjacencyList extends NetworkFlowSolverBase {
    private long delta;

    public CapacityScalingSolverAdjacencyList(int n, int s, int t) {
        super(n, s, t);
    }

    @Override
    public void addEdge(int from, int to, long capacity) {
        super.addEdge(from, to capacity);
        delta = max(delta, capacity);
    }

    @Override
    public void solve() {
        //start delta at the largest power of 2 <= largest capacity
        //equivalent: delta = (long) pow(2, (int)floor(log(delta)/log(2)))
        delta = Long.highestOneBit(delta);

        //repeatedly find augmenting paths from s to t using only edges w. a
        //remaining cap >= delta. halve the delta every time we are unable to
        //find an augmenting path from s to t until graph is saturated
        for(long f=0; delta>0; delta/=2){
            do {
                markAllNodesAsUnvisited();
                f = dfs(s, INF);
                maxFlow += f;
            } while (f!=0);
        }

        //find min cut
        for(int i=0; i<n; i++) if (visited(i)) minCut[i] = true;
    }

    private long dfs(int node, long flow) {
        //at sik node, return augmented path flow
        if(node==t) return flow;

        List<Edge> edges = graph[node];
        visit(node);

        for(Edge edge: edges) {
            long cap = edge.remainingCapacity();
            if(cap>=delta && !visited(edge.to)){
                long bottleNeck = dfs(edge.to, min(flow, cap));

                //augment flow w. bottleneck val
                if(bottleNeck>0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }

    /**example */
    public static void main(String[] args){
        example1();
        example2();
    }

    private static void example1() {
        int n = 6;
        int s = n-1;
        int t = n-2;

        CapacityScalingSolverAdjacencyList solver;
        solver = new CapacityScalingSolverAdjacencyList(n, s, t);

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

    private static example2() {
        int n = 6;
        int s = n - 1;
        int t = n - 2;

        CapacityScalingSolverAdjacencyList solver;
        solver = new CapacityScalingSolverAdjacencyList(n, s, t);

        //source edges
        solver.addEdge(s, 0, 6);
        solver.addEdge(s, 1, 14);

        //sink edges
        solver.addEdge(2, t, 11);
        solver.addEdge(3, t, 12);

        //middle edges
        solver.addEdge(0, 1, 1);
        solver.addEdge(2, 3, 1);
        solver.addEdge(0, 2, 5);
        solver.addEdge(1, 2, 7);
        solver.addEdge(1, 3, 10);

        System.out.println(solver.getMaxFlow()); //20
    }

    /**network flow solver code */
    private static class Edge{}

    private static abstract class NetworkFlowSolverBase{}
}
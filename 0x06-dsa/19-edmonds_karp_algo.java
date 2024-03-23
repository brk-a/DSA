package com.brk_a.algorithms.graphtheory;

import static java.lang.Math.min;

import java.util.*;

public class EdmondsKArpAdjacencyList extends NetworkFlowSolverBase {
    public EdmondsKArpAdjacencyList(int n, int s, int t) {
        super(n, s, t);
    }

    //runs EK algo and finds max-flow
    @Override
    public void solve() {
        long flow;
        do{
            markAllNodesAsUnvisited();
            flow = bfs();
            maxFlow += flow;
        } while (flow!=0);

        for(int i=0; i<n; i++) if(visited(i)) minCut[i] = true;
    }

    private long bfs(){
        Edge[] prev = new Edge[n];

        //queue can be optimised to use a faster queue
        Queue<Integer> q = new ArrayDeque<>(n);
        visit(s);
        q.offer(s);

        //perform BFS from s to t
        while(!q.isEmpty()){
            int node = q.poll();
            if(node==t) break;

            for(Edge edge: graph[node]){
                long cap = edge.remainingCapacity();
                if(cap>0 && !visited(edge.to)) {
                    visit(edge.to);
                    prev[edge.to] = edge;
                    q.offer(edge.to);
                }
            }
        }

        //t not reachable
        if(prev[t]==null) return 0;

        long bottleNeck = Long.MAX_VALUE;

        //find augmented path and bottleNeck
        for(Edge edge=prev[t]; edge!=null; edge=prev[edge.from])
            bottleNeck = min(bottleNeck, edge.remainingCapacity());
        
        //retrace augmented paath and update flow status
        for(Edge edge=prev[t]; edge!=null; edge=prev[edge.from]) edge.augment(bottleNeck);

        //return bottleNeck flow
        return bottleNeck;
    }

    /**example */
    private static void main(String[] args) {
        example1();
        example2();
    }

    private static void example1(){
        int n = 6; // #nodes, including s and t
        int s = n - 1;
        int t = n - 2;

        EdmondsKArpAdjacencyList solver;
        solver = new EdmondsKArpAdjacencyList(n, s, t);

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

        //display all edges of residual graph
        List<Edge>[] resultGraph = solver.getGraph();
        for(List<Edge> edges: resultGraph)
            for(Edge e: edges)
                System.out.println(e.toString(s, t));
    }

    private static void example2(){
        int n = 11; // #nodes, including s and t
        int s = n - 1;
        int t = n - 2;

        EdmondsKArpAdjacencyList solver;
        solver = new EdmondsKArpAdjacencyList(n, s, t);

        //source edges
        solver.addEdge(s, 0, 5);
        solver.addEdge(s, 1, 10);
        solver.addEdge(s, 2, 5);

        //sink edges
        solver.addEdge(6, t, 5);
        solver.addEdge(7, t, 15);
        solver.addEdge(8, t, 10);

        //middle edges
        solver.addEdge(0, 3, 10);
        solver.addEdge(1, 0, 15);
        solver.addEdge(1, 4, 20);
        solver.addEdge(2, 5, 10);
        solver.addEdge(3, 4, 25);
        solver.addEdge(3, 6, 10);
        solver.addEdge(4, 2, 5);
        solver.addEdge(4, 7, 30);
        solver.addEdge(5, 7, 5);
        solver.addEdge(5, 8, 10);
        solver.addEdge(7, 3, 15);
        solver.addEdge(7, 8, 5);

        System.out.println(solver.getMaxFlow());

        //display all edges of residual graph
        List<Edge>[] resultGraph = solver.getGraph();
        for(List<Edge> edges: resultGraph)
            for(Edge e: edges)
                System.out.println(e.toString(s, t));
    }

    /**network flow solver code */
    private static class Edge{}

    private static abstract class NetworkFlowSolverBase{}
}
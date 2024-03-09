package com.brk_a.algorithms.graphtheory;

import static java.lang.Math.*;
import java.util;

public class EagerPrimsAdjacencyList {
    static class Edge implements Comparable<Edge> {
        int from, to, cost;

        public Edge(int from, int to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        }

        @Override
        public int compareTo(Edge other){
            return cost - other.cost;
        }
    }

    //inputs
    private final int n;
    private final List<List<Edge>> graph;

    //outputs
    private long minCostSum;
    private Edge[] mstEdges;

    public EagerPrimsAdjacencyList(List<List<Edge>> graph) {
        if(graph==null || graph.isEmpty()) throw new IllegalArgumentException();
        this.n = graph.size();
        this.graph = graph;
    }

    //returns the edges used in finding the MST or null, else
    public Edge[] getMst(){
        solve();
        return mstExists ? mstEdges : null;
    }

    public long getMstCost() {
        solve();
        return mstExists ? minCostSum : null;
    }

    public void relaxEdgesAtNode(int currentNodeIndex) {
        visited[currentNodeIndex] = true;

        //edges will never be null if the createEmptyGraph method was used to build graph
        List<Edge> edges = graph.get(currentNodeIndex);

        for(Edge edge: edges) {
            int destNodeIndex = edge.to;

            //skip edges pointing to already visited nodes
            if(visited[destNodeIndex]) continue;
            if(ipq.contains(destNodeIndex)) {
                //relax the cheapest edge at destNodeIndex w. current edge in ipq
                ipq.decrease(destNodeIndex, edge);
            } else {
                //insert edge first time
                ipq.insert(destNodeIndex, edge);
            }
        }
    }

    //computes the MST and MST cost
    private void solve() {
        if(solved) return;
        solved = true;

        int m = n - 1, edgeCount = 0;
        visited = new boolean[n];
        mstEdges = new Edge[m];

        //the degree of the d-ary heap supporting the IPQ is sensitive to graph density and affects performance
        //the base 2 logarithm of n is a decent value of degree (in many cases better than E/V)
        int degree = (int) Math.ceil(Math.log(n).Math.log(2));
        ipq = new MinIndexedDHeap<>(max(2, degree), n);

        //add initial set of edges to IPQ starting at node zero
        relaxEdgesAtNode(0);

        while(!ipq.isEmpty() && edgeCount!=m) {
            int destNodeIndex = ipq.peekMinKeyIndex(); //equivalent: edge.to
            Edge edge = ipq.pollMinValue();

            mstEdges[edgeCount++] = edge;
            minCostSum += edge.cost;

            relaxEdgesAtNode(destNodeIndex);
        } 

        //verify that MST spans entire length
        mstExists = (edgeCount = m);    
    }

    /**construct graph helpers */
    static List<List<Edge>> createEmptyGraph(int n) {
        List<List<Edge>> g = new ArrayList<>();
        for(int i=0; i<n; i++) g.add(new ArrayList<>());
        return g;
    }

    static void addDirectedEdge(List<List<Edge>> g, int from, int to, int cost) {
        g.get(from).add(new Edge(from, to, cost));
    }

    static void addUndirectedEdge(List<List<Edge>> g, int from, int to, int cost) {
        addDirectedEdge(g, from, to, cost);
        addDirectedEdge(g, to, from, cost);
    }

    /**examples */
    public static vois main(String[] args) {
        example1();
        example2();
        example3();
        example4();
        example5();
    }

    private static void example1() {
        int n = 10;
        List<List<Edge>> g = createEmptyGraph(n);

        addUndirectedEdge(g, 0, 1, 5);
        addUndirectedEdge(g, 1, 2, 4);
        addUndirectedEdge(g, 2, 9, 2);
        addUndirectedEdge(g, 0, 4, 1);
        addUndirectedEdge(g, 0, 3, 4);
        addUndirectedEdge(g, 1, 3, 2);
        addUndirectedEdge(g, 2, 7, 4);
        addUndirectedEdge(g, 2, 8, 1);
        addUndirectedEdge(g, 9, 8, 0);
        addUndirectedEdge(g, 4, 5, 1);
        addUndirectedEdge(g, 5, 6, 7);
        addUndirectedEdge(g, 6, 8, 4);
        addUndirectedEdge(g, 4, 3, 2);
        addUndirectedEdge(g, 5, 3, 5);
        addUndirectedEdge(g, 3, 6, 11);
        addUndirectedEdge(g, 6, 7, 1);
        addUndirectedEdge(g, 3, 7, 2);
        addUndirectedEdge(g, 7, 8, 6);

        EagerPrimsAdjacencyList solver = new EagerPrimsAdjacencyList(g);
        Long cost = solver.getMstCost();

        if(cost==null) {
            System.out.println("No MST exists");
        } else {
            System.out.println("MST cost: " + cost);
            for(Edge e: solver.getMst()) {
                System.out.println(String.format("from: %d to %d cost: %d", e.from, e.to, e.cost));
            }
        }
        // Output:
        // MST cost: 14
        // from: 0 to: 4 cost: 1
        // from: 4 to: 5 cost: 1
        // from: 4 to: 3 cost: 2
        // from: 3 to: 1 cost: 2
        // from: 3 to: 7 cost: 2
        // from: 7 to: 6 cost: 1
        // from: 6 to: 8 cost: 4
        // from: 8 to: 9 cost: 0
        // from: 8 to: 2 cost: 1
    }

    private static void example2() {
        int n = 7;
        List<List<Edge>> g = createEmptyGraph(n);

        addUndirectedEdge(g, 0, 1, 9);
        addUndirectedEdge(g, 0, 2, 0);
        addUndirectedEdge(g, 0, 3, 5);
        addUndirectedEdge(g, 0, 5, 7);
        addUndirectedEdge(g, 1, 3, -2);
        addUndirectedEdge(g, 1, 4, 3);
        addUndirectedEdge(g, 1, 6, 4);
        addUndirectedEdge(g, 2, 5, 6);
        addUndirectedEdge(g, 3, 5, 2);
        addUndirectedEdge(g, 3, 6, 3);
        addUndirectedEdge(g, 4, 6, 6);
        addUndirectedEdge(g, 5, 6, 1);

        EagerPrimsAdjacencyList solver = new EagerPrimsAdjacencyList(g);
        Long cost = solver.getMstCost();

        if(cost==null) {
            System.out.println("No MST exists");
        } else {
            System.out.println("MST cost: " + cost);
            for(Edge e: solver.getMst()) {
                System.out.println(String.format("from: %d to %d cost: %d", e.from, e.to, e.cost));
            }
        }
    }

    private static void example3() {
        int n = 9;
        List<List<Edge>> g = createEmptyGraph(n);

        addUndirectedEdge(g, 0, 1, 6);
        addUndirectedEdge(g, 0, 3, 3);
        addUndirectedEdge(g, 1, 2, 4);
        addUndirectedEdge(g, 1, 4, 2);
        addUndirectedEdge(g, 2, 5, 12);
        addUndirectedEdge(g, 3, 4, 1);
        addUndirectedEdge(g, 3, 6, 8);
        addUndirectedEdge(g, 4, 5, 7);
        addUndirectedEdge(g, 4, 7, 9);
        addUndirectedEdge(g, 5, 8, 10);
        addUndirectedEdge(g, 6, 7, 11);
        addUndirectedEdge(g, 7, 8, 5);

        EagerPrimsAdjacencyList solver = new EagerPrimsAdjacencyList(g);
        Long cost = solver.getMstCost();

        if(cost==null) {
            System.out.println("No MST exists");
        } else {
            System.out.println("MST cost: " + cost);
            for(Edge e: solver.getMst()) {
                System.out.println(String.format("from: %d to %d cost: %d", e.from, e.to, e.cost));
            }
        }
    }

    private static void example4() {
        int n = 4;
        List<List<Edge>> g = createEmptyGraph(n);

        // Node edges connected to zero
        addUndirectedEdge(g, 1, 2, 1);
        addUndirectedEdge(g, 2, 3, 1);
        addUndirectedEdge(g, 3, 1, 1);

        EagerPrimsAdjacencyList solver = new EagerPrimsAdjacencyList(g);
        Long cost = solver.getMstCost();

        if(cost==null) {
            System.out.println("No MST exists");
        } else {
            System.out.println("MST cost: " + cost);
            for(Edge e: solver.getMst()) {
                System.out.println(String.format("from: %d to %d cost: %d", e.from, e.to, e.cost));
            }
        }
    }

    private static void example5() {
        int n = 6;
        List<List<Edge>> g = createEmptyGraph(n);

        // Component 1
        addUndirectedEdge(g, 0, 1, 1);
        addUndirectedEdge(g, 1, 2, 1);
        addUndirectedEdge(g, 2, 0, 1);

        // Component 2
        addUndirectedEdge(g, 3, 4, 1);
        addUndirectedEdge(g, 4, 5, 1);
        addUndirectedEdge(g, 5, 3, 1);


        EagerPrimsAdjacencyList solver = new EagerPrimsAdjacencyList(g);
        Long cost = solver.getMstCost();

        if(cost==null) {
            System.out.println("No MST exists");
        } else {
            System.out.println("MST cost: " + cost);
            for(Edge e: solver.getMst()) {
                System.out.println(String.format("from: %d to %d cost: %d", e.from, e.to, e.cost));
            }
        }
    }

}
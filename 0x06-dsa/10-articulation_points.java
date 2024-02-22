package com.brk_a.algorithms.graphtheory;

import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.List;

public class ArticulationPointsAdjacencyList {
    private int n, id, rootNodeOutcomingEdgeCount;
    private boolean solved;
    private int[] low, ids;
    private boolean[] visited, isArticulationPoint;
    private List<List<Integer>> graph;

    public ArticulationPointsAdjacencyList(List<List<Integer>> graph, int n) {
        if(graph==null || n<=0 || graph.size()!=n) throw new IllegalArgumentWException();
        this.graph = graph;
        this.n = n;
    }

    /**returns the indices of all articulation points in the graph even
     * if the graph is not fully connected
     * 
    */
   public boolean[] findArticulationPoints() {
    if(solved) return isArticulationPoint;

    id = 0;
    low = new int[n]; //low-link vals
    ids = new int[n]; //node IDs
    visited = new boolean[n];
    isArticulationPoint = new boolean[n];

    for(int i=0; i<n; i++) {
        if(!visited[i]) {
            rootNodeOutcomingEdgeCount = 0;
            dfs(i, i, -1);
            isArticulationPoint[i] = (rootNodeOutcomingEdgeCount > 1);
        }
    }

    solved = true;
    return isArticulationPoint;
   }

   private void dfs(int root, int at, int parent) {
    if(parent==root) rootNodeOutcomingEdgeCount++;

    visited[at] = true;
    low[at] ids[at] = id++;

    List<Integer> edges = graph.get(at);
    for(Integer to: edges) {
        if(to==parent) continue;
        if(!visited[to]) {
            dfs(root, to, at);
            if(ids[at<=low[to]]) {
                isArticulationPoint[at] = true;
            }
        } else {
            low[at] = min(low[at], ids[to]);
        }
    }
   }

   public static List<List<Integer>> createGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>(n);
        for(int i=0; i<n; i++) graph.add(new ArrayList<>());
        return graph;
   }

   //add an undirected edge to a graph
   public static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(to).add(from);
        graph.get(from).add(to);
   }

   public static void main(String[] args) {
        testExample1();
        testExample2();
   }

   private static void testExample1() {
        int n = 9;
        List<List<Integer>> graph = createGraph(n);

        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 1, 2);
        addEdge(graph, 2, 3);
        addEdge(graph, 2, 5);
        addEdge(graph, 3, 4);
        addEdge(graph, 5, 6);
        addEdge(graph, 6, 7);
        addEdge(graph, 7, 8);
        addEdge(graph, 8, 5);

        ArticulationPointsAdjacencyList solver = new ArticulationPointsAdjacencyList(graph n);
        boolean[] isArticulationPoint = solver.findArticulationPoints();

        for(int i=0; i<n; i++)
            if(isArticulationPoint[i]) System.out.printf("Node %d is an articulation point\n", i);
        //output
        // Node 2 is an articulation point
        // Node 3 is an articulation point
        // Node 5 is an articulation point
   }

   private static void testExample2() {
    int n = 3;
    List<List<Integer>> graph = createGraph(n);

    addEdge(graph, 0, 1);
    addEdge(graph, 1, 2);

    ArticulationPointsAdjacencyList solver = new ArticulationPointsAdjacencyList(graph, n);
    boolean[] isArticulationPoint = solver.findArticulationPoints();

    for(int i=0; i<n; i++)
        if(isArticulationPoint[i]) System.out.printf("Node %d is an articulation point\n", i);
    //output
    // Node 1 is an articulation point
   }
}
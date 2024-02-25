package com.brk_a.algorithms.graphtheory;

import static java.lang.Math.min;

import java.util.*;

public class TarjanSccSolverAdjacencyList {
    private int n;
    private List<List<Integer>> graph;
    private boolean solved;
    private int sccCount;
    private boolean[] visited;
    private int[] ids, low, sccs;
    private Deque<Integer> stack;
    private static final int UNVISITED = -1;

    public TarjanSccSolverAdjacencyList(List<List<Integer>> graph) {
        if(graph==null) throw new IllegalArgumentWException("Graph cannot  be null");
        n = graph.size();
        this.graph = graph;
    }

    //returns #SCCs in graph
    public int sccCount() {
        if(!solved) solve();
        return sccCount;
    }

    //get the connected components of this graph
    //two indices are in the same SCC when they have the same value
    public int[] getSccs() {
        if(solved) return;

        ids = new int[n];
        low = new int[n];
        sccs = new int[n];
        visited = new boolean[n];

        for(int i=0; i<n; i++) {
            if(ids[i]==UNVISITED) {
                dfs(i);
            }
        }

        solved = true;
    }

    private void dfs(int at) {
        ids[at] = low[at] = id++;
        stack.push(at);
        visited[at] = true;

        for(int to: graph.get(at)) {
            if(ids[to]==UNVISITED) {
                dfs(to);
            }
            if(visited[to]) {
                low[at] = min(low[at], low[to]);
            }
            //recursive callback: if we are @ the root (start of the SCC), empty the stack invariant until root
            if(ids[at]==low[at]) {
                for(int node=stack.pop(); ;node=stack.pop()) {
                    visited[node] = false;
                    sccs[node] = sccCount;
                    if(node==at) break;
                }
            }
        }
        sccCount++;
    }

    //initialise an adjacency list w. n nodes
    public static List<List<Integer>> createGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>();
        for(int i=0; i<n; i++) graph.add(new ArrayList<>());
        return graph;
    }

    //create a directed edge from `from` to `to`
    public static addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to)
    }

    public static vois main(String[] args) {
        int n = 8;
        List<List<Integer>> graph = createGraph(n);

        addEdge(graph, 6, 0);
        addEdge(graph, 6, 2);
        addEdge(graph, 3, 4);
        addEdge(graph, 6, 4);
        addEdge(graph, 2, 0);
        addEdge(graph, 0, 1);
        addEdge(graph, 4, 5);
        addEdge(graph, 5, 6);
        addEdge(graph, 3, 7);
        addEdge(graph, 7, 5);
        addEdge(graph, 1, 2);
        addEdge(graph, 7, 3);
        addEdge(graph, 5, 0);

        TarjanSccSolverAdjacencyList solver = new TarjanSccSolverAdjacencyList(graph);

        int[] sccs = solver.getSccs();
        Map<Integer, List<Integer>> multiMap = new HashMap<>();
        for(int i=0; i<n; i++) {
            if(!multiMap.containsKey(sccs[i])) multiMap.put(sccs[i], new ArrayList<>());
            multiMap.get(sccs[i]).add(i);
        }

        System.out.printf("Number of Strongly Connected Componentrs: %d\n", solver.sccCount());
        for(List<Integer> scc: multiMap.values()) {
            System.out.println("Nodes: " +  scc : "form a Strongly Connected Component");
        }
        // Output
        // Number of Strongly Connected Components: 3
        // Nodes: [0, 1, 2] form a Strongly Connected Component
        // Nodes: [3, 7] form a Strongly Connected Component
        // Nodes: [4, 5, 6] form a Strongly Connected Component
        }
}
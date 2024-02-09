package com.brk_a.algorithms.graphtheory;

import java.util.*;

public class TopologicalSortAdjacencyList {
    //helper class Edge -> describes edges in graph
    static class Edge {
        int from, to, weight;

        public Edge(int f, int t, int w) {
            from = f;
            to = t;
            weight = w;
        }
    }

    //helper method that performs dfs on graph to provide the topological ordering required
    //for simplicity, nodes are placed in the ordering array in reverse order, therefore, no need for a stack
    private static int dfs(
        int i, int at, boolean[] visited, int[] ordering, Map<Integer, List<Edge>> graph
    ) {
        visited[at] = true;
        List<Edge> edges = graph.get(at);
        if(edges!=null)
            for(Edge edge: edges)
                if(!visited[edge.to])
                    i = dfs(i, edge.to, visited, ordering, graph);
        ordering[i] = at;
        return i-1;
    }

    //finds a topological ordering of nodes in a DAG
    //input args are: an adjacency list representing the graph and an int representing the #nodes
    //CAVEAT: 'numNodes' is not necessarily the number of nodes currently present
    // in the adjacency list because you can have singleton nodes (nodes with no edges) which
    // would not be present in the adjacency list but are still part of the graph
    public static int[] topologicalSort(Map<Integer, List<Edge>> graph, int numNodes) {
        int[] ordering = new int[numNodes];
        boolean[] visited = new boolean[numNodes];
        int i = numNodes - 1;

        for(int at=0; at<numNodes; at++)
            if(!visited[at])
                i = dfs(i, at, visited, ordering, graph);
        
        return ordering;
    }

    //return shortest path between a starting node and all other nodes in a DAG
    //CAVEAT: 'numNodes' is not necessarily the number of nodes currently present
    // in the adjacency list because you can have singleton nodes (nodes with no edges) which
    // would not be present in the adjacency list but are still part of the graph
    public static Integer[] dagShortestath(Map<Integer, List<Edge>> graph, int start, int numNodes) {
        int[] topSort = topologicalSort(graph, numNodes);
        Integer[] dist = new Integer[numNodes];
        dist[start] = 0;

        for(int i=0; i<numNodes; i++) {
            int nodeIndex = topSort[i];
            if(dist[nodeIndex]!=null) {
                List<Edge> adjacentEdges = graph.get(nodeIndex);
                if(adjacentEdges!=null) {
                    for(Edge edge: adjacentEdges) {
                        int newDist = dist[nodeIndex] + edge.weight;
                        if(dist[edge.to]==null) dist[edge.to] = newDist;
                        else dist[edge.to]=Math.min(dist[edge.to], newDist);
                    }
                }
            }
        }
        return dist;
    }

    //example
    public static void main(String[] args) {
        //set up the graph
        final int n = 7;
        Map<Integer, List<Edge>> graph = new HashMap<>();

        for(int i=0; i<N; i++) graph.put(i, new ArrayList<>());

        graph.get(0).add(new Edge(0, 1, 3));
        graph.get(0).add(new Edge(0, 2, 2));
        graph.get(0).add(new Edge(0, 5, 3));
        graph.get(1).add(new Edge(1, 3, 1));
        graph.get(1).add(new Edge(1, 2, 6));
        graph.get(1).add(new Edge(2, 3, 1));
        graph.get(1).add(new Edge(2, 4, 10));
        graph.get(3).add(new Edge(3, 4, 5));
        graph.get(5).add(new Edge(5, 4, 7));

        int[] ordering = topologicalSort(graph, N);

        //prints [6, 0, 5, 1, 2, 3, 4]
        System.out.println(java.util.Arrays.toString(ordering));

        //finds shortest paths given start node zero
        Integer[] dists = dagShortestath(graph, 0, N);

        //finds the shorest path from node zero to node 4 which is 8.0
        System.out.println(dists[4]);

        //finds the shortest path between node zero and 6 which is null because node 6 is not reachable
        System.out.println(dists[6]);
    }
}
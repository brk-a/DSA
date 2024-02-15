package com.brk_a.algorithms.graphtheory;

public class  BellmanFordEdgeList {
    //directed edge
    public static class Edge {
        double cost;
        int from, to;

        public Edge(int from, into to, double cost) {
            this.to = to;
            this.from = from;
            this.cost = cost;
        }
    }

    /**
        * BF algo. finds the shortest path between
        * a starting node and all other nodes in the graph. also detects negative cycles
        * 
        * if a node is part of a negative cycle, the minimum cost for that node is set to
        * Double.NEGATIVE_INFINITY
        *
        * @param edges - edge list containing directed edges forming the graph
        * @param V - #vertices in the graph
        * @param start - The id of the start node
     */
    public double[] bellmanFord(Edge[] edges, int V, int start) {
        double[] dist = new double[V];
        java.util.Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0;

        //has V-1 iterations in worst-case scenario
        //we've reached an optimal soln when we cannot relax an edge
        boolean relaxedAnEdge = true;

        //for each vertex, apply rick-laxation for all edges
        for(int i=0; i<V-1 && relaxedAnEdge; i++){
            relaxedAnEdge = false;
            for(Edge edge: edges) {
                if(dist[edge.from]+edge.cost<dist[edge.to]){
                    dist[edge.to] = dist[edge.from] + edge.cost;
                    relaxedAnEdge = true;
                }
            }
        }

        //run algo again to detect negative cycles
        relaxedAnEdge = true;
        for(int i=0; i<V-1 && relaxedAnEdge; i++) {
            relaxedAnEdge = true;
            for(Edge edge: edges) {
                if(dist[edge.from]+edge.to<dist[edge.to]) {
                    dist[edge.to] = Double.NEGATIVE_INFINITY;
                    relaxedAnEdge = true;
                }
            }
        }

        //return the array coontaining the shortest distance to every node
        return dist;
    }

    public static void main(String[] args) {
        int E = 10, V = 9, start = 0;
        Edge edges = new Edge[E];

        edges[0] = new Edge(0, 1, 1);
        edges[1] = new Edge(1, 2, 1);
        edges[2] = new Edge(2, 4, 1);
        edges[3] = new Edge(4, 3, -3);
        edges[4] = new Edge(3, 2, 1);
        edges[5] = new Edge(1, 5, 4);
        edges[6] = new Edge(1, 6, 4);
        edges[7] = new Edge(5, 6, 5);
        edges[8] = new Edge(6, 7, 4);
        edges[9] = new Edge(5, 7, 3);

        double[] d = bellmanFord(edges, V, start);

        for(int i=0; i<V; i++)
            System.out.printf("%d to %d costs %.2f\n", start, i, d[i]);

        // Output:
        // The cost to get from node 0 to 0 is 0.00
        // The cost to get from node 0 to 1 is 1.00
        // The cost to get from node 0 to 2 is -Infinity
        // The cost to get from node 0 to 3 is -Infinity
        // The cost to get from node 0 to 4 is -Infinity
        // The cost to get from node 0 to 5 is 5.00
        // The cost to get from node 0 to 6 is 5.00
        // The cost to get from node 0 to 7 is 8.00
        // The cost to get from node 0 to 8 is Infinity
    }
}
package com.brk_a.algorithms.graphtheory;

import static java.lang.Math.min;
import java.awt/geom.*;
import java.util.*;

public class MiceAndOwls{
    public class Mouse {
        Point2D point;
        public Mouse(int x, int y) {
            point = new Point2D.Double(x, y);
        }
    }

    public class Hole{
        int capacity;
        Point2D point;
        public Hole(int x, int y, int cap){
            point = new Point2D.Double(x, y);
            capacity = cap;
        }
    }

    public static void main(String[] args) {
        Mouse[] mice = {
            new Mouse(1, 0),
            new Mouse(0, 1),
            new Mouse(8, 1),
            new Mouse(12, 0),
            new Mouse(12, 4),
            new Mouse(15, 5),
        };
        Hole[] holes = {
            new Hole(1, 1, 1),
            new Hole(10, 2, 2),
            new Hole(14, 5, 1),
        };
        solve(mice, holes, 3);
    }

    static void solve(Mouse[] mice, Hole[], holes, int radius){
        final int M = mice.length;
        final int H = holes.length;
        //N->#nodes, S->source node, T->sink node
        final int N = M + H + 2;
        final int S = N - 1;
        final int T = N - 2;

        NetworkFlowSolverBase solver;
        solver = new FordFulkersonDfsSolver(N, S, T);

        //connect source node to mice
        for(int i=0; i<M; i++){
            solver.addEdge(S, i, 1);
        }

        //connect eachmouse w. hole(s) it can reach
        for(int i=0; i<M; i++){
            Point2D mouse = mice[i].point;
            for(int j=0; j<H; j++){
                Point2D hole = holes[j].point;
                if(mouse.distance(hole)<=radius){
                    solver.addEdge(i, M+j, 1);
                }
            }
        }

        //connect holes to sink node
        for(inti=0; i<H; i++){
            solver.addEdge(M+i, T, holes[i].capacity)
        }

        //output: number of safe mice: 4
        System.out.println("number of safe muce: " + solver.getMaxFlow())
    }

    /**network flow solver code */
    private static class Edge{}

    private static abstract class NetworkFlowSolverBase{}

    private static class FordFulkersonDfsSolver extends NetworkFlowSolverBase {}
}

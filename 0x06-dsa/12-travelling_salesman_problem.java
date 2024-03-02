package com.brk_a.algorithms.graphtheory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TspDynamicProgrammingIterative {
    private final int N, start;
    private final double[][] distance;
    private List<Integer> tour = new ArrayList<>();
    private double minTourCost = Double.POSITIVE_INFINITY;
    private boolean ranSolver = false;

    public TspDynamicProgrammingIterative(double[][] distance) {
        this(0, distance);
    }

    public TspDynamicProgrammingIterative(int start, double[][] distance) {
        N = distance.length;

        if(N<=2) throw new IllegalArgumentException("N<=2 not yet supported");
        if(N!=distance[0].length) throw new IllegalArgumentException("Matrix must be square, that is, n  by n");
        if(start<0 || start>=N) throw new IllegalArgumentException("invalid start node");
        if(N>32) throw new IllegalArgumentException(
            "matrix is too large. a matrix of that size in the DP TSP problem perpective has a time complexity of
            O(n^2*2^n); this is too much computation for any typical home computer"
        );

        this.start = start;
        this.distance = distance;
    }

    //returns the optimal tour for tsp
    public List<Integer> getTour() {
        if(!ranSolver) solve();
        return tour;
    }

    //returns minimal tour  cost
    public double getTourCost(){
        if(!ranSolver) solve();
        return minTourCost;
    }

    //solves tsp and caches solution
    public void solve(){
        if(ranSolver) return;

        final int END_STATE = (1<<N) - 1;
        Double[][] memo = new Double[N][1<<N];

        //add all outgoing edges from start node to memo table
        for(int end=0; end<N; end++){
            if(end==start) continue;
            memo[end][(1<<start) | (1<<end)] = distance[start][end];
        }

        for(int r=3; r<=N; r++) {
            for(int subset: combinations(r, N)){
                if(notIn(start, subset)) continue;
                for(int next=0; next<N; next++){
                    if(next==start || notIn(next, subset)) continue;
                    int subsetWithoutNext = subset ^ (1<<next);
                    double minDist = Double.POSITIVE_INFINITY;
                    for(int end=0; end<N; end++) {
                        if(end==start || end==next || notIn(end, subset)) continue;
                        double newDistance = memo[end][subsetWithoutNext] + distance[end][next];
                        if(newDistance<minDist) {
                            minDist = newDistance;
                        }
                    }
                    memo[next][subset] = minDist;
                }
            }
        }

        //connect tour back to starting node and minimise cost
        for(int i=0; i<N; i++){
            if(i==start) continue;
            double tourCost = memo[i][END_STATE] + distance[i][start];
            if(tourCost<minTourCost) {
                minTourCost = tourCost;
            }
        }

        int lastIndex = start;
        int state = END_STATE;
        tour.add(start);

        //reconstruct tsp path from memo table
        for(int i=1; i<N; i++){
            int bestIndex = -1;
            double bestDist = DOuble.POSITIVE_INFINITY;
            for(int j=0; j<N; j++){
                if(j==start || notIn(j, state)) continue;
                double newDist = memo[j][state] + distance[j][lastIndex];
                if(newDist<bestDist) {
                    bestDist = newDist;
                }
            }

            tour.add(bestIndex);
            state = state ^ (1<<bestIndex);
            lastIndex = bestIndex;
        }
        tour.add(start);
        Collections.reverse(tour);

        ranSolver = true;
    }

    private static boolean notIn(int elem, int subset){
        return ((1<<elem) & subset) == 0;
    }

    //generate all bit sets of size n where r bits are set to 1
    //result is returned as a list of integer masks
    //to find all combos, we must recurse untilwe have selected r elems (aka r=zero),
    //else, if r!=zero, select an element which is found after the position of our last
    //selected elem
    public static List<Integer> combinations(int r, int n){
        List<Integer> subsets = new ArrayList<>();
        combinations(0, 0, r, n, subsets);
        return subsets;
    }

    private static void combinations(int set, int at, int r, int n, List<Integer> subsets){
        //return early when there are mmore elems left to select than are available
        int elementsLeftToPick = n - at;
        if(elementsLeftToPick<r) return;

        //found a valid subset when `r` is selected
        if(r==0){
            subsets.add(set);
        } else {
            for(int i=at; i<n; i++){
                //try to include this elem
                set ^= (1<<i);
                combinations(set, i+1, r-1, n, subsets);
                //backtrack and try the instance where this elem was not included
                set ^= (1<<i);
            }
        }
    }

    public static void main(String[] args){
        //create adjacency matrix
        int n = 6;
        double[][] distanceMatrix = new double[n][n];
        for (double[] row : distanceMatrix) java.util.Arrays.fill(row, 10000);
        distanceMatrix[5][0] = 10;
        distanceMatrix[1][5] = 12;
        distanceMatrix[4][1] = 2;
        distanceMatrix[2][4] = 4;
        distanceMatrix[3][2] = 6;
        distanceMatrix[0][3] = 8;

        int startNode = 0;
        TspDynamicProgrammingIterative solver =
            new TspDynamicProgrammingIterative(startNode, distanceMatrix);

        // Prints: [0, 3, 2, 4, 1, 5, 0]
        System.out.println("Tour: " + solver.getTour());

        // Prints: 42.0
        System.out.println("Tour cost: " + solver.getTourCost());
    }
}
package com.brk_a.algorithms.graphtheory;

import java.util.ArrayList;
import java.util.List;

public abstract class NetworkFlowSolverBase {
    // To avoid overflow, set infinity to a value less than Long.MAX_VALUE;
    protected static final long INF = Long.MAX_VALUE / 2;

    public static class Edge {
        public int from , to;
        public Edge residual;
        public long flow, cost;
        public final long capacity, originalCost 
    }
}
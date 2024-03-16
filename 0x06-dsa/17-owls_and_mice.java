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
            new Mouse(1, 0);
            new Mouse(0, 1);
            new Mouse(8, 1);
            new Mouse(12, 0);
            new Mouse(12, 4);
        }
    }
}

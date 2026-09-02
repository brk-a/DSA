import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Largest Rectangular Area in a Histogram.
 *
 * Problem:
 * Given an array of non-negative bar heights (all the same width, standing
 * side by side), find the area of the largest rectangle that fits under
 * the skyline they form.
 *
 * Notes:
 * - If bars is invalid (null / empty), Result.valid() == false.
 * - Only one implementation was given in the original code, and it was
 *   the most broken file in this whole series. A second, independently
 *   written implementation (the monotonic-stack technique - adapted from
 *   the already-verified getMaxArea helper in the LargestBinarySubMatrixAllOnes
 *   file, since that solves this exact sub-problem) was added below so the
 *   segment-tree version has something genuine to be cross-checked against.
 *
 * Implementations:
 *
 * 1. Segment Tree + Range Minimum Query (divide and conquer)
 *      Build a segment tree that can answer "index of the shortest bar in
 *      range [l, r]" in O(log n). For a range, the largest rectangle
 *      either sits entirely left of that shortest bar, entirely right of
 *      it, or spans the whole range at the shortest bar's height -
 *      recurse and take the best of the three.
 *      Time: O(n log n)   Space: O(n).
 *
 * 2. Monotonic Stack
 *      Sweep left to right keeping a stack of bar indices with
 *      non-decreasing height. Whenever a shorter bar is seen, pop and
 *      "close off" every taller bar behind it, since none of them can
 *      extend any further right.
 *      Time: O(n)   Space: O(n).
 *
 * Both are cross-checked against each other for both fixed and randomised
 * test inputs, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class LargestRectangularAreaInHistogram {

    record Result(int area, boolean valid) {}

    private static int[] segmentTree;
    private static int[] histogramBars;

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validBars(int[] bars) {
        return bars != null && bars.length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result largestRectangularAreaInHistogramSegmentTree(int[] bars) {
        if (!validBars(bars)) {
            return new Result(-1, false);
        }

        histogramBars = bars;
        int n = bars.length;
        constructSegmentTree(n);
        int result = getMaxAreaRectangle(0, n - 1);

        return new Result(result, true);
    }

    static Result largestRectangularAreaInHistogramMonotonicStack(int[] bars) {
        if (!validBars(bars)) {
            return new Result(-1, false);
        }

        int n = bars.length;
        Stack<Integer> stack = new Stack<>();
        int result = 0;

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && bars[stack.peek()] >= bars[i]) {
                int tp = stack.pop();
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                result = Math.max(result, bars[tp] * width);
            }
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            int tp = stack.pop();
            int width = stack.isEmpty() ? n : n - stack.peek() - 1;
            result = Math.max(result, bars[tp] * width);
        }

        return new Result(result, true);
    }

    /* **********************************************************************
     * Segment Tree Helpers
     * **********************************************************************/

    static int getMaxAreaRectangle(int left, int right) {
        if (left > right) {
            return Integer.MIN_VALUE;
        }
        if (left == right) {
            return histogramBars[left];
        }

        int m = RMQ(left, right);

        return max(
                getMaxAreaRectangle(left, m - 1),
                getMaxAreaRectangle(m + 1, right),
                (right - left + 1) * histogramBars[m]);
    }

    static void constructSegmentTree(int n) {
        int height = (int) Math.ceil(Math.log(n) / Math.log(2));
        int maxSize = 2 * (int) Math.pow(2, height) - 1;
        segmentTree = new int[maxSize];
        constructSegmentTreeUtil(0, n - 1, 0);
    }

    static int RMQ(int qs, int qe) {
        int n = histogramBars.length;
        if (qs < 0 || qe > n - 1 || qs > qe) {
            return -1;
        }

        return RMQUtil(0, n - 1, qs, qe, 0);
    }

    /**
     * Builds the tree over range [ss, se], storing each node's result at
     * segmentTree[si]. (The original had ss/se transposed with si in its
     * own parameter list relative to how both the top-level call and its
     * own recursive calls actually invoked it, and used the same child
     * index for both children - see the write-up for the full trace.)
     */
    static int constructSegmentTreeUtil(int ss, int se, int si) {
        if (ss == se) {
            segmentTree[si] = ss;
            return segmentTree[si];
        }
        int mid = getMid(ss, se);
        segmentTree[si] = minVal(
                constructSegmentTreeUtil(ss, mid, si * 2 + 1),
                constructSegmentTreeUtil(mid + 1, se, si * 2 + 2));

        return segmentTree[si];
    }

    static int RMQUtil(int ss, int se, int qs, int qe, int idx) {
        if (qs <= ss && qe >= se) {
            return segmentTree[idx];
        }
        if (se < qs || ss > qe) {
            return -1;
        }

        int mid = getMid(ss, se);

        return minVal(RMQUtil(ss, mid, qs, qe, 2 * idx + 1), RMQUtil(mid + 1, se, qs, qe, 2 * idx + 2));
    }

    static int getMid(int s, int e) {
        return s + (e - s) / 2;
    }

    /** Returns whichever of index i or j points to the shorter bar (-1 means "no candidate"). */
    static int minVal(int i, int j) {
        if (i == -1) {
            return j;
        }
        if (j == -1) {
            return i;
        }

        return histogramBars[i] < histogramBars[j] ? i : j;
    }

    static int max(int x, int y, int z) {
        return Math.max(Math.max(x, y), z);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] bars;
        final Result expected;
        final String description;

        TestCase(
                String id,
                int[] bars,
                Result expected,
                String description) {

            this.id = id;
            this.bars = bars;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int[] bars);
    }

    static class MethodCase {

        final String name;
        final Algorithm algorithm;

        MethodCase(
                String name,
                Algorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Utilities
     * **********************************************************************/

    static boolean resultsEqual(Result a, Result b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.area() == b.area() && a.valid() == b.valid();
    }

    static void runTests(
            String algorithmName,
            Algorithm method,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");
        System.out.println(algorithmName);
        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Result actual = method.solve(test.bars == null ? null : test.bars.clone());

                if (resultsEqual(actual, test.expected)) {

                    passed++;

                    System.out.printf(
                            "✓ %s (%s)%n",
                            test.id,
                            test.description);

                } else {

                    failed++;

                    System.out.printf(
                            "✗ %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  bars      = %s%n",
                            Arrays.toString(test.bars));

                    System.out.printf(
                            "  expected  = %s%n",
                            test.expected);

                    System.out.printf(
                            "  actual    = %s%n",
                            actual);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  bars      = %s%n",
                        Arrays.toString(test.bars));

                System.out.printf(
                        "  exception = %s%n",
                        ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                tests.size());

        System.out.println();
    }

    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/

    static int[] randomBars(Random rng, int maxLen, int maxHeight) {
        int len = rng.nextInt(maxLen) + 1;
        int[] bars = new int[len];
        for (int i = 0; i < len; i++) {
            bars[i] = rng.nextInt(maxHeight + 1);
        }
        return bars;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Segment Tree vs Monotonic Stack)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260830L);

        for (int i = 1; i <= iterations; i++) {

            // Includes small and non-power-of-two lengths deliberately,
            // to stress the segment tree's sizing formula.
            int[] bars = randomBars(rng, 40, 20);

            Result segTree = largestRectangularAreaInHistogramSegmentTree(bars.clone());
            Result monoStack = largestRectangularAreaInHistogramMonotonicStack(bars.clone());

            if (!resultsEqual(segTree, monoStack)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "bars     = " + Arrays.toString(bars));

                System.out.println(
                        "segTree  = " + segTree);

                System.out.println(
                        "monoStack = " + monoStack);

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Basic Cases
         * (expected values verified with an independent Python
         * brute force before being hardcoded here)
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                new int[]{2, 1, 5, 6, 2, 3},
                new Result(10, true),
                "classic textbook example"));

        tests.add(new TestCase(
                "B2",
                new int[]{3, 3, 3, 3},
                new Result(12, true),
                "all bars the same height"));

        tests.add(new TestCase(
                "B3",
                new int[]{7},
                new Result(7, true),
                "single bar"));

        tests.add(new TestCase(
                "B4",
                new int[]{1, 2, 3, 4, 5},
                new Result(9, true),
                "strictly increasing"));

        tests.add(new TestCase(
                "B5",
                new int[]{5, 4, 3, 2, 1},
                new Result(9, true),
                "strictly decreasing"));

        tests.add(new TestCase(
                "B6",
                new int[]{0, 0, 0},
                new Result(0, true),
                "all zero-height bars"));

        tests.add(new TestCase(
                "B7",
                new int[]{2, 0, 3},
                new Result(3, true),
                "a zero bar splits the histogram in two"));

        tests.add(new TestCase(
                "B8",
                new int[]{1, 1},
                new Result(2, true),
                "two equal bars"));

        /*
         * ============================================================
         * Non-Power-of-Two Sizing
         * (stresses the segment tree's size/height formula, which the
         * original never even reached correctly due to the earlier bugs)
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[]{6, 2, 5, 4, 5, 1, 6},
                new Result(12, true),
                "7 bars, not a power of two"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new Result(-1, false),
                "null bars"));

        tests.add(new TestCase(
                "E2",
                new int[]{},
                new Result(-1, false),
                "empty bars"));

        System.out.println(
                "############################################################");
        System.out.println(
                "############  LARGEST RECTANGULAR AREA IN HISTOGRAM  #######");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Segment Tree (RMQ)",
                        LargestRectangularAreaInHistogram::largestRectangularAreaInHistogramSegmentTree),

                new MethodCase(
                        "Monotonic Stack",
                        LargestRectangularAreaInHistogram::largestRectangularAreaInHistogramMonotonicStack)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(5000);
    }
}

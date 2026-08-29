import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Minimum Cost to Fill a Bag of Given Weight.
 *
 * Problem:
 * cost[i] gives the price of a packet weighing (i + 1) units, or -1 if no
 * such packet is sold. Using as many packets of any available weight as
 * needed (each weight is available in UNLIMITED supply), find the minimum
 * total cost to fill a bag of EXACTLY the target weight.
 *
 * Notes:
 * - If cost is invalid (null / empty), or weight is negative, every
 *   method returns Result(-1, false). The weight >= 0 check is an added
 *   robustness check - the original code never validated weight at all,
 *   so a negative weight would previously have thrown
 *   NegativeArraySizeException while allocating the DP table.
 * - weight == 0 is always achievable at cost 0 (fill it with nothing).
 * - Both implementations are UNBOUNDED-knapsack style (any packet weight
 *   may be reused any number of times) - that's what this problem actually
 *   calls for, since packets are bought, not "used up" one at a time.
 *
 * Implementations:
 *
 * 1. Unbounded Knapsack, 2D table (indexed by item)
 *      minCost[i][j] = min cost to reach weight j using only the first i
 *      distinct packet weights, reusing minCost[i][...] (not minCost[i-1][...])
 *      in the "use this weight" branch specifically so a weight can be
 *      picked more than once.
 *      (Renamed from the original "BoundedKnapsack" - the recurrence it
 *      implements already allows unlimited reuse, so that name described
 *      the wrong algorithm entirely, not just a stale label.)
 *      Time: O(distinctWeights * weight)   Space: O(distinctWeights * weight).
 *
 * 2. Unbounded Knapsack, 1D array (indexed by weight, coin-change style)
 *      dp[i] = min cost to reach weight i, trying every available packet
 *      weight at each i.
 *      Time: O(n * weight)   Space: O(weight).
 *
 * Both are cross-checked against each other for both fixed and randomised
 * test inputs, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class MinimumCostToFillWeight {

    private static final int INF = (int) 1e9;
    private static final int MAX_VALUE = Integer.MAX_VALUE;

    static record Result(int cost, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validList(int[] list) {
        return list != null && list.length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result minimumCostToFillWeightUnboundedKnapsack2D(int weight, int[] cost) {
        if (!validList(cost) || weight < 0) {
            return new Result(-1, false);
        }

        int n = cost.length;
        List<Integer> val = new ArrayList<>();
        List<Integer> wt = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (cost[i] != -1) {
                val.add(cost[i]);
                wt.add(i + 1);
            }
        }

        int sz = val.size();
        int[][] minCost = new int[sz + 1][weight + 1];
        for (int i = 0; i <= sz; i++) {
            Arrays.fill(minCost[i], INF);
            // Zero weight always costs 0 regardless of how many packet
            // types are available
            minCost[i][0] = 0;
        }

        for (int i = 1; i <= sz; i++) {
            for (int j = 1; j <= weight; j++) {
                if (wt.get(i - 1) > j) {
                    minCost[i][j] = minCost[i - 1][j];
                } else {
                    minCost[i][j] = Math.min(minCost[i - 1][j], minCost[i][j - wt.get(i - 1)] + val.get(i - 1));
                }
            }
        }

        int result = minCost[sz][weight] >= INF ? -1 : minCost[sz][weight];

        return new Result(result, result != -1);
    }

    static Result minimumCostToFillWeightUnboundedKnapsack1D(int weight, int[] cost) {
        if (!validList(cost) || weight < 0) {
            return new Result(-1, false);
        }

        int n = cost.length;
        int[] dp = new int[weight + 1];
        Arrays.fill(dp, MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= weight; i++) {
            int best = MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (cost[j] != -1 && (j + 1) <= i && dp[i - (j + 1)] != MAX_VALUE) {
                    best = Math.min(best, cost[j] + dp[i - (j + 1)]);
                }
            }
            dp[i] = best;
        }

        int result = dp[weight] == MAX_VALUE ? -1 : dp[weight];

        return new Result(result, result != -1);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int weight;
        final int[] cost;
        final Result expected;
        final String description;

        TestCase(
                String id,
                int weight,
                int[] cost,
                Result expected,
                String description) {

            this.id = id;
            this.weight = weight;
            this.cost = cost;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int weight, int[] cost);
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

        return a.cost() == b.cost() && a.valid() == b.valid();
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

                Result actual = method.solve(test.weight, test.cost);

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
                            "  weight    = %d, cost = %s%n",
                            test.weight,
                            Arrays.toString(test.cost));

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
                        "  weight    = %d, cost = %s%n",
                        test.weight,
                        Arrays.toString(test.cost));

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

    static int[] randomCostArray(Random rng, int maxLen, double availableProbability, int maxCost) {
        int len = rng.nextInt(maxLen) + 1;
        int[] cost = new int[len];
        for (int i = 0; i < len; i++) {
            cost[i] = rng.nextDouble() < availableProbability ? (1 + rng.nextInt(maxCost)) : -1;
        }
        return cost;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (2D table vs 1D array)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260827L);

        for (int i = 1; i <= iterations; i++) {

            int weight = rng.nextInt(20);
            int[] cost = randomCostArray(rng, 10, 0.5, 15);

            Result twoD = minimumCostToFillWeightUnboundedKnapsack2D(weight, cost.clone());
            Result oneD = minimumCostToFillWeightUnboundedKnapsack1D(weight, cost.clone());

            if (!resultsEqual(twoD, oneD)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "weight = " + weight + ", cost = " + Arrays.toString(cost));

                System.out.println(
                        "2D     = " + twoD);

                System.out.println(
                        "1D     = " + oneD);

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
         * Base-Case Regression
         * (guards the original missing "minCost[i][0] = 0" bug, which
         * made a target reachable by exactly one packet look unreachable)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                3, new int[]{-1, -1, 5},
                new Result(5, true),
                "only weight-3 packets available, target is exactly one packet"));

        /*
         * ============================================================
         * Unbounded Reuse
         * (also guards the original "j <= sz" loop-bound bug, which left
         * most of the DP table uncomputed whenever weight exceeded the
         * number of distinct available packet weights)
         * ============================================================
         */

        tests.add(new TestCase(
                "U1",
                6, new int[]{-1, -1, 5},
                new Result(10, true),
                "same weight-3 packet reused twice (2 x 3 = 6)"));

        tests.add(new TestCase(
                "U2",
                4, new int[]{2, -1, -1},
                new Result(8, true),
                "only weight-1 packets available, reused four times"));

        tests.add(new TestCase(
                "U3",
                10, new int[]{1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                new Result(10, true),
                "single cheap weight-1 packet, reused ten times"));

        tests.add(new TestCase(
                "U4",
                7, new int[]{3, 2},
                new Result(9, true),
                "mixed packet sizes, cheaper-per-weight packet favoured"));

        /*
         * ============================================================
         * Trivial / Unreachable Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "T1",
                0, new int[]{5},
                new Result(0, true),
                "zero target weight always costs 0"));

        tests.add(new TestCase(
                "T2",
                5, new int[]{-1, -1, -1, -1, -1},
                new Result(-1, false),
                "no packets available at all: unreachable"));

        tests.add(new TestCase(
                "T3",
                5, new int[]{-1, 4, -1, 6},
                new Result(-1, false),
                "only even packet weights (2, 4) available, odd target: unreachable"));

        /*
         * ============================================================
         * Added Robustness: Negative Weight
         * ============================================================
         */

        tests.add(new TestCase(
                "V1",
                -3, new int[]{1, 2, 3},
                new Result(-1, false),
                "negative weight is rejected rather than crashing on array allocation"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                5, null,
                new Result(-1, false),
                "null cost array"));

        tests.add(new TestCase(
                "E2",
                5, new int[]{},
                new Result(-1, false),
                "empty cost array"));

        System.out.println(
                "############################################################");
        System.out.println(
                "################  MINIMUM COST TO FILL WEIGHT  #############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Unbounded Knapsack (2D table)",
                        MinimumCostToFillWeight::minimumCostToFillWeightUnboundedKnapsack2D),

                new MethodCase(
                        "Unbounded Knapsack (1D array)",
                        MinimumCostToFillWeight::minimumCostToFillWeightUnboundedKnapsack1D)
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

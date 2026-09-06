import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Chain Matrix Multiplication.
 *
 * Problem:
 * Given a chain of matrices:
 *
 *     A1 = p0 x p1
 *     A2 = p1 x p2
 *     ...
 *     An = p(n-1) x pn
 *
 * represented by:
 *
 *     int[] dimensions = {p0, p1, ..., pn}
 *
 * find the minimum number of scalar multiplications required
 * to multiply the entire chain.
 *
 * Implementations:
 *
 * 1. Plain Recursion
 *    Time:  O(2^n) approximately
 *    Space: O(n) recursion stack
 *
 * 2. Memoisation
 *    Time:  O(n^3)
 *    Space: O(n^2)
 *
 * 3. Tabulation
 *    Time:  O(n^3)
 *    Space: O(n^2)
 *
 * The test suite:
 * - tests all implementations against fixed expected results
 * - tests invalid input
 * - tests large values / overflow
 * - cross-checks all implementations using randomized input
 */
public class ChainMatrixMultiplication {

    /*
     * long is used instead of int because matrix multiplication
     * costs can exceed Integer.MAX_VALUE.
     */
    static record Result(long result, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    /**
     * A valid dimensions array must:
     *
     * - not be null
     * - contain at least two dimensions
     * - contain only positive dimensions
     *
     * Example:
     *
     *     {10, 20, 30}
     *
     * represents:
     *
     *     A1 = 10 x 20
     *     A2 = 20 x 30
     */
    static boolean validDimensions(int[] dimensions) {

        if (dimensions == null || dimensions.length < 2) {
            return false;
        }

        for (int dimension : dimensions) {

            if (dimension <= 0) {
                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * 1. Plain Recursion
     * **********************************************************************/

    static Result chainMatrixMultiplicationRecursion(
            int[] dimensions) {

        if (!validDimensions(dimensions)) {
            return new Result(-1, false);
        }

        int matrixCount = dimensions.length - 1;

        /*
         * A single matrix requires no multiplication.
         */
        if (matrixCount == 1) {
            return new Result(0, true);
        }

        long result =
                matrixMultiplicationRecursion(
                        dimensions,
                        1,
                        matrixCount);

        return new Result(result, true);
    }

    /**
     * Calculates the minimum multiplication cost for matrices i..j.
     *
     * Matrix i has dimensions:
     *
     *     dimensions[i - 1] x dimensions[i]
     *
     * Matrix j has dimensions:
     *
     *     dimensions[j - 1] x dimensions[j]
     */
    static long matrixMultiplicationRecursion(
            int[] dimensions,
            int i,
            int j) {

        /*
         * One matrix requires no multiplication.
         */
        if (i >= j) {
            return 0;
        }

        long result = Long.MAX_VALUE;

        /*
         * Try every possible split:
         *
         *     (Ai ... Ak) (A(k+1) ... Aj)
         */
        for (int k = i; k < j; k++) {

            long left =
                    matrixMultiplicationRecursion(
                            dimensions,
                            i,
                            k);

            long right =
                    matrixMultiplicationRecursion(
                            dimensions,
                            k + 1,
                            j);

            long multiplicationCost =
                    (long) dimensions[i - 1]
                            * dimensions[k]
                            * dimensions[j];

            long current =
                    left
                            + right
                            + multiplicationCost;

            result = Math.min(result, current);
        }

        return result;
    }

    /* **********************************************************************
     * 2. Memoisation
     * **********************************************************************/

    static Result chainMatrixMultiplicationMemoisation(
            int[] dimensions) {

        if (!validDimensions(dimensions)) {
            return new Result(-1, false);
        }

        int matrixCount = dimensions.length - 1;

        /*
         * A single matrix requires no multiplication.
         */
        if (matrixCount == 1) {
            return new Result(0, true);
        }

        long[][] memo =
                new long[matrixCount + 1][matrixCount + 1];

        /*
         * -1 means "not calculated yet".
         */
        for (long[] row : memo) {
            Arrays.fill(row, -1L);
        }

        long result =
                matrixMultiplicationMemoisation(
                        dimensions,
                        1,
                        matrixCount,
                        memo);

        return new Result(result, true);
    }

    static long matrixMultiplicationMemoisation(
            int[] dimensions,
            int i,
            int j,
            long[][] memo) {

        /*
         * One matrix requires no multiplication.
         */
        if (i >= j) {
            return 0;
        }

        /*
         * Return cached result.
         */
        if (memo[i][j] != -1L) {
            return memo[i][j];
        }

        long result = Long.MAX_VALUE;

        /*
         * Try every possible split.
         */
        for (int k = i; k < j; k++) {

            long left =
                    matrixMultiplicationMemoisation(
                            dimensions,
                            i,
                            k,
                            memo);

            long right =
                    matrixMultiplicationMemoisation(
                            dimensions,
                            k + 1,
                            j,
                            memo);

            long multiplicationCost =
                    (long) dimensions[i - 1]
                            * dimensions[k]
                            * dimensions[j];

            long current =
                    left
                            + right
                            + multiplicationCost;

            result = Math.min(result, current);
        }

        memo[i][j] = result;

        return result;
    }

    /* **********************************************************************
     * 3. Tabulation
     * **********************************************************************/

    static Result chainMatrixMultiplicationTabulation(
            int[] dimensions) {

        if (!validDimensions(dimensions)) {
            return new Result(-1, false);
        }

        int matrixCount = dimensions.length - 1;

        /*
         * A single matrix requires no multiplication.
         */
        if (matrixCount == 1) {
            return new Result(0, true);
        }

        long[][] tab =
                new long[matrixCount + 1][matrixCount + 1];

        /*
         * chainLength is the number of matrices in the
         * current sub-chain.
         *
         * Example:
         *
         * chainLength = 2
         *
         *     A1 A2
         *
         * chainLength = 3
         *
         *     A1 A2 A3
         */
        for (int chainLength = 2;
             chainLength <= matrixCount;
             chainLength++) {

            for (int i = 1;
                 i <= matrixCount - chainLength + 1;
                 i++) {

                int j =
                        i + chainLength - 1;

                /*
                 * Start with the largest possible value.
                 */
                tab[i][j] = Long.MAX_VALUE;

                /*
                 * Try every possible split point.
                 */
                for (int k = i; k < j; k++) {

                    long multiplicationCost =
                            (long) dimensions[i - 1]
                                    * dimensions[k]
                                    * dimensions[j];

                    long current =
                            tab[i][k]
                                    + tab[k + 1][j]
                                    + multiplicationCost;

                    if (current < tab[i][j]) {
                        tab[i][j] = current;
                    }
                }
            }
        }

        return new Result(
                tab[1][matrixCount],
                true);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] input;
        final Result expected;
        final String description;

        TestCase(
                String id,
                int[] input,
                Result expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int[] dimensions);
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

    static String formatDimensions(int[] dimensions) {

        if (dimensions == null) {
            return "null";
        }

        return Arrays.toString(dimensions);
    }

    static boolean resultsEqual(Result a, Result b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.result() == b.result()
                && a.valid() == b.valid();
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

                Result actual =
                        method.solve(test.input);

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
                            "  input    = %s%n",
                            formatDimensions(test.input));

                    System.out.printf(
                            "  expected = %s%n",
                            test.expected);

                    System.out.printf(
                            "  actual   = %s%n",
                            actual);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  input     = %s%n",
                        formatDimensions(test.input));

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

    static int[] randomDimensions(
            Random rng,
            int maxMatrices,
            int minDimension,
            int maxDimension) {

        int matrixCount =
                rng.nextInt(maxMatrices) + 1;

        int[] dimensions =
                new int[matrixCount + 1];

        int range =
                maxDimension - minDimension + 1;

        for (int i = 0;
             i < dimensions.length;
             i++) {

            dimensions[i] =
                    rng.nextInt(range)
                            + minDimension;
        }

        return dimensions;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "Recursion vs Memoisation vs Tabulation");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260905L);

        for (int i = 1; i <= iterations; i++) {

            /*
             * Keep chains small because plain recursion is
             * intentionally included.
             */
            int[] dimensions =
                    randomDimensions(
                            rng,
                            7,
                            1,
                            20);

            Result recursion =
                    chainMatrixMultiplicationRecursion(
                            dimensions);

            Result memoisation =
                    chainMatrixMultiplicationMemoisation(
                            dimensions);

            Result tabulation =
                    chainMatrixMultiplicationTabulation(
                            dimensions);

            if (!resultsEqual(
                        recursion,
                        memoisation)
                    || !resultsEqual(
                        recursion,
                        tabulation)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration   = " + i);

                System.out.println(
                        "dimensions  = "
                                + formatDimensions(
                                        dimensions));

                System.out.println(
                        "recursion   = "
                                + recursion);

                System.out.println(
                        "memoisation = "
                                + memoisation);

                System.out.println(
                        "tabulation  = "
                                + tabulation);

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main / Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Classic / Basic Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                new int[]{
                        10, 20, 30
                },
                new Result(
                        6000,
                        true),
                "two matrices"));

        tests.add(new TestCase(
                "B2",
                new int[]{
                        10, 20, 30, 40
                },
                new Result(
                        18000,
                        true),
                "classic three-matrix example"));

        tests.add(new TestCase(
                "B3",
                new int[]{
                        40, 20, 30, 10, 30
                },
                new Result(
                        26000,
                        true),
                "standard four-matrix example"));

        tests.add(new TestCase(
                "B4",
                new int[]{
                        10, 20
                },
                new Result(
                        0,
                        true),
                "single matrix requires no multiplication"));

        tests.add(new TestCase(
                "B5",
                new int[]{
                        5, 10, 3
                },
                new Result(
                        150,
                        true),
                "small dimensions"));

        tests.add(new TestCase(
                "B6",
                new int[]{
                        1, 1, 1, 1, 1
                },
                new Result(
                        3,
                        true),
                "all dimensions equal to one"));

        /*
         * ============================================================
         * Regression Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[]{
                        10, 20, 30, 40
                },
                new Result(
                        18000,
                        true),
                "guards against incorrect tabulation ranges"));

        tests.add(new TestCase(
                "R2",
                new int[]{
                        30, 35, 15, 5, 10, 20, 25
                },
                new Result(
                        15125,
                        true),
                "classic CLRS matrix-chain example"));

        tests.add(new TestCase(
                "R3",
                new int[]{
                        5, 4, 6, 2, 7
                },
                new Result(
                        158,
                        true),
                "competing split points"));

        tests.add(new TestCase(
                "R4",
                new int[]{
                        2, 3, 6, 4, 5
                },
                new Result(
                        132,
                        true),
                "non-uniform dimensions"));

        /*
         * ============================================================
         * Overflow Regression
         * ============================================================
         *
         * 1,000,000 x 1,000,000
         *
         * multiplied by
         *
         * 1,000,000 x 1,000,000
         *
         * costs:
         *
         *     1,000,000^3
         *
         * = 10^18
         *
         * which fits in long but not int.
         */

        tests.add(new TestCase(
                "O1",
                new int[]{
                        1_000_000,
                        1_000_000,
                        1_000_000
                },
                new Result(
                        1_000_000_000_000L,
                        true),
                "cost exceeds int range"));

        tests.add(new TestCase(
                "O2",
                new int[]{
                        2_000_000,
                        2_000_000
                },
                new Result(
                        0,
                        true),
                "large dimensions with one matrix"));

        /*
         * ============================================================
         * Invalid Input Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new Result(
                        -1,
                        false),
                "null dimensions"));

        tests.add(new TestCase(
                "E2",
                new int[]{},
                new Result(
                        -1,
                        false),
                "empty dimensions"));

        tests.add(new TestCase(
                "E3",
                new int[]{
                        10
                },
                new Result(
                        -1,
                        false),
                "only one dimension"));

        tests.add(new TestCase(
                "E4",
                new int[]{
                        0, 10
                },
                new Result(
                        -1,
                        false),
                "zero dimension"));

        tests.add(new TestCase(
                "E5",
                new int[]{
                        10, 0, 20
                },
                new Result(
                        -1,
                        false),
                "zero middle dimension"));

        tests.add(new TestCase(
                "E6",
                new int[]{
                        -10, 20
                },
                new Result(
                        -1,
                        false),
                "negative dimension"));

        tests.add(new TestCase(
                "E7",
                new int[]{
                        10, -20, 30
                },
                new Result(
                        -1,
                        false),
                "negative middle dimension"));

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "##########  CHAIN MATRIX MULTIPLICATION  ##################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Methods
         * ============================================================
         */

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Plain Recursion",
                                ChainMatrixMultiplication
                                        ::chainMatrixMultiplicationRecursion),

                        new MethodCase(
                                "Memoisation",
                                ChainMatrixMultiplication
                                        ::chainMatrixMultiplicationMemoisation),

                        new MethodCase(
                                "Tabulation",
                                ChainMatrixMultiplication
                                        ::chainMatrixMultiplicationTabulation)
                );

        /*
         * ============================================================
         * Deterministic Tests
         * ============================================================
         */

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * ============================================================
         * Randomised Cross Validation
         * ============================================================
         */

        runRandomisedTests(5000);
    }
}

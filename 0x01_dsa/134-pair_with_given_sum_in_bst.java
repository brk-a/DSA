import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Find all distinct value pairs in a Binary Search Tree whose sum equals
 * a given target.
 *
 * Two implementations are provided:
 *
 * 1. Complement Check
 *
 *      For every node with value x, look for:
 *
 *          target - x
 *
 *      A HashSet stores values that have already been visited.
 *
 *      Time:  O(n)
 *      Space: O(n)
 *
 * 2. In-order Traversal
 *
 *      An in-order traversal of a BST produces the values in ascending
 *      order.
 *
 *      Two pointers are then used:
 *
 *          left  -> smallest value
 *          right -> largest value
 *
 *      If their sum is too small, move left forwards.
 *      If their sum is too large, move right backwards.
 *
 *      Time:  O(n)
 *      Space: O(n)
 *
 * Duplicate values are allowed.
 *
 * The result contains DISTINCT VALUE PAIRS.
 *
 * For example, given:
 *
 *      [1, 1, 1, 3, 3]
 *
 * and target:
 *
 *      4
 *
 * the result is:
 *
 *      [[1, 3]]
 *
 * rather than returning [1, 3] multiple times.
 */
public class PairWithGivenSumBST {

    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {

        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }

        Node(
                Node left,
                Node right,
                int val) {

            this.left = left;
            this.right = right;
            this.val = val;
        }
    }

    /* **********************************************************************
     * Result
     * **********************************************************************/

    static record Result(
            ArrayList<ArrayList<Integer>> pairs,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validRoot(
            Node root) {

        return root != null;
    }

    static Node node(
            int value) {

        return new Node(value);
    }

    static Node node(
            int value,
            Node left,
            Node right) {

        return new Node(
                left,
                right,
                value);
    }

    /**
     * Create a deep copy of a tree.
     *
     * The algorithms do not modify the tree, but using a fresh tree for
     * every test keeps the test suite independent.
     */
    static Node copyTree(
            Node root) {

        if (root == null) {
            return null;
        }

        return new Node(
                copyTree(root.left),
                copyTree(root.right),
                root.val);
    }

    /**
     * Add a pair in canonical ascending order.
     *
     * This helper also prevents duplicate value pairs.
     */
    static void addPair(
            Set<String> seenPairs,
            ArrayList<ArrayList<Integer>> pairs,
            int first,
            int second) {

        int smaller =
                Math.min(
                        first,
                        second);

        int larger =
                Math.max(
                        first,
                        second);

        String key =
                smaller
                        + ":"
                        + larger;

        if (seenPairs.add(key)) {

            pairs.add(
                    new ArrayList<>(
                            List.of(
                                    smaller,
                                    larger)));
        }
    }

    /**
     * Sort pairs lexicographically.
     *
     * For example:
     *
     *      [1, 9]
     *      [2, 8]
     *      [2, 7]
     *
     * becomes:
     *
     *      [1, 9]
     *      [2, 7]
     *      [2, 8]
     */
    static void sortPairs(
            ArrayList<ArrayList<Integer>> pairs) {

        pairs.sort(
                (first, second) -> {

                    int firstValue =
                            Integer.compare(
                                    first.get(0),
                                    second.get(0));

                    if (firstValue != 0) {
                        return firstValue;
                    }

                    return Integer.compare(
                            first.get(1),
                            second.get(1));
                });
    }

    /* **********************************************************************
     * 1. Complement Check
     * **********************************************************************/

    /**
     * Find distinct value pairs using the complement technique.
     *
     * For every visited value x, the required complement is:
     *
     *      target - x
     *
     * Values already encountered are stored in a HashSet.
     *
     * If the complement is already present, the two values form a pair.
     *
     * A separate set of result keys prevents duplicate value pairs.
     *
     * This works correctly when duplicate values exist.
     *
     * For example, with:
     *
     *      5, 5
     *
     * and target:
     *
     *      10
     *
     * the second 5 finds the first 5 in the visited set.
     */
    static Result pairWithGivenSumBSTCheckCompliment(
            Node root,
            int target) {

        if (!validRoot(root)) {

            return new Result(
                    new ArrayList<>(),
                    false);
        }

        Set<Integer> seenValues =
                new HashSet<>();

        Set<String> seenPairs =
                new HashSet<>();

        ArrayList<ArrayList<Integer>> pairs =
                new ArrayList<>();

        collectPairsUsingComplement(
                root,
                target,
                seenValues,
                seenPairs,
                pairs);

        sortPairs(pairs);

        return new Result(
                pairs,
                true);
    }

    /**
     * Traverse the BST and search for complements.
     */
    static void collectPairsUsingComplement(
            Node root,
            int target,
            Set<Integer> seenValues,
            Set<String> seenPairs,
            ArrayList<ArrayList<Integer>> pairs) {

        if (root == null) {
            return;
        }

        /*
         * Search the current node's complement before adding the current
         * value to the set.
         *
         * This is important when:
         *
         *      value + value = target
         *
         * because a node must not pair with itself.
         */
        int complement =
                target - root.val;

        if (seenValues.contains(complement)) {

            addPair(
                    seenPairs,
                    pairs,
                    root.val,
                    complement);
        }

        seenValues.add(
                root.val);

        collectPairsUsingComplement(
                root.left,
                target,
                seenValues,
                seenPairs,
                pairs);

        collectPairsUsingComplement(
                root.right,
                target,
                seenValues,
                seenPairs,
                pairs);
    }

    /* **********************************************************************
     * 2. In-order Traversal
     * **********************************************************************/

    /**
     * Find distinct value pairs using in-order traversal.
     *
     * A BST's in-order traversal is sorted:
     *
     *      left -> root -> right
     *
     * Once the values are sorted, two pointers can be used:
     *
     *      left  = first value
     *      right = last value
     *
     * If:
     *
     *      values[left] + values[right] < target
     *
     * move left forwards.
     *
     * If:
     *
     *      values[left] + values[right] > target
     *
     * move right backwards.
     *
     * Otherwise a pair has been found.
     *
     * Duplicate values are skipped after a pair is found so that the
     * result contains distinct value pairs.
     */
    static Result pairWithGivenSumBSTInOrder(
            Node root,
            int target) {

        if (!validRoot(root)) {

            return new Result(
                    new ArrayList<>(),
                    false);
        }

        ArrayList<Integer> values =
                new ArrayList<>();

        inOrder(
                root,
                values);

        ArrayList<ArrayList<Integer>> pairs =
                new ArrayList<>();

        int left = 0;
        int right =
                values.size() - 1;

        while (left < right) {

            int leftValue =
                    values.get(left);

            int rightValue =
                    values.get(right);

            /*
             * Use long to avoid integer overflow when adding two
             * integer values.
             */
            long sum =
                    (long) leftValue
                            + rightValue;

            if (sum < target) {

                /*
                 * All values at or before left have already been
                 * considered and are too small with this right value.
                 */
                left++;

            } else if (sum > target) {

                /*
                 * All values at or after right are too large with
                 * this left value.
                 */
                right--;

            } else {

                addPair(
                        new HashSet<>(),
                        pairs,
                        leftValue,
                        rightValue);

                /*
                 * Move past all duplicate occurrences of both values.
                 *
                 * This ensures that the result contains distinct
                 * VALUE pairs rather than repeated node combinations.
                 */
                while (left < right
                        && values.get(left)
                        == leftValue) {

                    left++;
                }

                while (left < right
                        && values.get(right)
                        == rightValue) {

                    right--;
                }
            }
        }

        sortPairs(pairs);

        return new Result(
                pairs,
                true);
    }

    /* **********************************************************************
     * In-order Traversal
     * **********************************************************************/

    static void inOrder(
            Node root,
            ArrayList<Integer> values) {

        if (root == null) {
            return;
        }

        inOrder(
                root.left,
                values);

        values.add(
                root.val);

        inOrder(
                root.right,
                values);
    }

    /* **********************************************************************
     * Test Case
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int target;
        final ArrayList<ArrayList<Integer>> expected;
        final String description;

        TestCase(
                String id,
                Node root,
                int target,
                ArrayList<ArrayList<Integer>> expected,
                String description) {

            this.id = id;
            this.root = root;
            this.target = target;
            this.expected = expected;
            this.description = description;
        }
    }

    /* **********************************************************************
     * Algorithm
     * **********************************************************************/

    @FunctionalInterface
    interface Algorithm {

        Result solve(
                Node root,
                int target);
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
     * Test Helpers
     * **********************************************************************/

    static ArrayList<ArrayList<Integer>> pairs(
            int... values) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        /*
         * Values are supplied in groups of two:
         *
         *      pairs(1, 9, 2, 8)
         *
         * produces:
         *
         *      [[1, 9], [2, 8]]
         */
        for (int i = 0;
             i < values.length;
             i += 2) {

            result.add(
                    new ArrayList<>(
                            List.of(
                                    values[i],
                                    values[i + 1])));
        }

        sortPairs(result);

        return result;
    }

    /* **********************************************************************
     * Test Runner
     * **********************************************************************/

    static void runTests(
            MethodCase method,
            List<TestCase> tests) {

        printSeparator();

        System.out.println(
                method.name);

        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test :
                tests) {

            try {

                Result actual =
                        method.algorithm.solve(
                                copyTree(test.root),
                                test.target);

                boolean success =
                        actual.valid()
                                && actual.pairs().equals(
                                        test.expected);

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  pairs = "
                                    + actual.pairs());

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + test.expected);

                    System.out.println(
                            "  actual   = "
                                    + actual.pairs());

                    System.out.println(
                            "  valid    = "
                                    + actual.valid());
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = "
                                + ex);
            }
        }

        printResults(
                passed,
                failed,
                tests.size());
    }

    /* **********************************************************************
     * Cross-Algorithm Tests
     * **********************************************************************/

    static void runCrossCheckTests(
            List<MethodCase> methods,
            List<TestCase> tests) {

        printSeparator();

        System.out.println(
                "Cross-Algorithm Tests");

        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test :
                tests) {

            ArrayList<ArrayList<Integer>> first =
                    null;

            boolean success =
                    true;

            for (MethodCase method :
                    methods) {

                Result actual =
                        method.algorithm.solve(
                                copyTree(test.root),
                                test.target);

                if (!actual.valid()) {

                    success = false;
                }

                if (first == null) {

                    first =
                            actual.pairs();

                } else if (!first.equals(
                        actual.pairs())) {

                    success = false;

                    System.out.printf(
                            "  %s = %s%n",
                            method.name,
                            actual.pairs());
                }

                if (!actual.pairs().equals(
                        test.expected)) {

                    success = false;
                }
            }

            if (success) {

                passed++;

                System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description);

            } else {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);
            }
        }

        printResults(
                passed,
                failed,
                tests.size());
    }

    /* **********************************************************************
     * Invalid Input Tests
     * **********************************************************************/

    static void runInvalidInputTests(
            List<MethodCase> methods) {

        printSeparator();

        System.out.println(
                "Invalid Input Tests");

        printSeparator();

        int passed = 0;
        int failed = 0;

        /*
         * Null root.
         */
        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            null,
                            10);

            if (!result.valid()
                    && result.pairs().isEmpty()) {

                passed++;

                System.out.printf(
                        "PASS I1 - %s rejects null root%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I1 - %s accepts null root%n",
                        method.name);
            }
        }

        printResults(
                passed,
                failed,
                passed + failed);
    }

    /* **********************************************************************
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Basic Tree
         * ============================================================
         *
         *             20
         *           /    \
         *         10      30
         *        /  \    /  \
         *       5   15  25  35
         *
         * Values:
         *
         *      5, 10, 15, 20, 25, 30, 35
         */

        Node root =
                node(
                        20,
                        node(
                                10,
                                node(5),
                                node(15)),
                        node(
                                30,
                                node(25),
                                node(35)));

        tests.add(
                new TestCase(
                        "B1",
                        root,
                        35,
                        pairs(
                                5, 30,
                                10, 25,
                                15, 20),
                        "three distinct pairs"));

        tests.add(
                new TestCase(
                        "B2",
                        root,
                        30,
                        pairs(
                                5, 25,
                                10, 20),
                        "two pairs"));

        tests.add(
                new TestCase(
                        "B3",
                        root,
                        40,
                        pairs(),
                        "no pair exists"));

        tests.add(
                new TestCase(
                        "B4",
                        root,
                        10,
                        pairs(),
                        "no pair reaches target"));

        /*
         * ============================================================
         * Pair Containing Root
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "R1",
                        root,
                        45,
                        pairs(
                                10, 35,
                                15, 30,
                                20, 25),
                        "root participates in no pair but surrounding pairs exist"));

        tests.add(
                new TestCase(
                        "R2",
                        root,
                        25,
                        pairs(
                                5, 20,
                                10, 15),
                        "root participates in a pair"));

        /*
         * ============================================================
         * Single Node
         * ============================================================
         */

        Node single =
                node(10);

        tests.add(
                new TestCase(
                        "S1",
                        single,
                        20,
                        pairs(),
                        "a node cannot pair with itself"));

        /*
         * ============================================================
         * Two Nodes
         * ============================================================
         */

        Node twoNodes =
                node(
                        10,
                        null,
                        node(20));

        tests.add(
                new TestCase(
                        "S2",
                        twoNodes,
                        30,
                        pairs(
                                10, 20),
                        "exactly one pair"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        Node negative =
                node(
                        -10,
                        node(-20),
                        node(
                                -5,
                                node(-7),
                                node(-1)));

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        -27,
                        pairs(
                                -20, -7),
                        "negative target"));

        tests.add(
                new TestCase(
                        "N2",
                        negative,
                        -6,
                        pairs(
                                -5, -1),
                        "negative values"));

        /*
         * ============================================================
         * Mixed Negative and Positive Values
         * ============================================================
         */

        Node mixed =
                node(
                        0,
                        node(
                                -10,
                                node(-20),
                                node(-5)),
                        node(
                                10,
                                node(5),
                                node(20)));

        tests.add(
                new TestCase(
                        "M1",
                        mixed,
                        0,
                        pairs(
                                -20, 20,
                                -10, 10,
                                -5, 5),
                        "zero target"));

        tests.add(
                new TestCase(
                        "M2",
                        mixed,
                        15,
                        pairs(
                                -5, 20,
                                5, 10),
                        "mixed values"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         *
         *             5
         *           /   \
         *          3     7
         *         / \   / \
         *        3   4 7   8
         */

        Node duplicates =
                node(
                        5,
                        node(
                                3,
                                node(3),
                                node(4)),
                        node(
                                7,
                                node(7),
                                node(8)));

        tests.add(
                new TestCase(
                        "D1",
                        duplicates,
                        10,
                        pairs(
                                3, 7),
                        "duplicate values produce one distinct pair"));

        tests.add(
                new TestCase(
                        "D2",
                        duplicates,
                        6,
                        pairs(
                                3, 3),
                        "two distinct nodes with equal values"));

        tests.add(
                new TestCase(
                        "D3",
                        duplicates,
                        12,
                        pairs(
                                4, 8,
                                5, 7),
                        "duplicate values do not duplicate the result"));

        /*
         * ============================================================
         * Integer Boundary Values
         * ============================================================
         */

        Node boundaries =
                node(
                        0,
                        node(
                                Integer.MIN_VALUE),
                        node(
                                Integer.MAX_VALUE));

        tests.add(
                new TestCase(
                        "I1",
                        boundaries,
                        -1,
                        pairs(
                                Integer.MIN_VALUE,
                                Integer.MAX_VALUE),
                        "integer boundary values"));

        tests.add(
                new TestCase(
                        "I2",
                        boundaries,
                        0,
                        pairs(),
                        "integer overflow must not create a false pair"));

        return tests;
    }

    /* **********************************************************************
     * Random BST Generation
     * **********************************************************************/

    static Node insert(
            Node root,
            int value) {

        if (root == null) {
            return node(value);
        }

        if (value < root.val) {

            root.left =
                    insert(
                            root.left,
                            value);

        } else {

            root.right =
                    insert(
                            root.right,
                            value);
        }

        return root;
    }

    static Node randomBST(
            Random random,
            int size) {

        Node root = null;

        for (int i = 0;
             i < size;
             i++) {

            int value =
                    random.nextInt(101)
                            - 50;

            root =
                    insert(
                            root,
                            value);
        }

        return root;
    }

    /* **********************************************************************
     * Reference Implementation
     * **********************************************************************/

    /**
     * Produce the expected distinct value pairs independently of either
     * algorithm under test.
     *
     * This is deliberately implemented using the sorted in-order sequence
     * and a simple exhaustive comparison rather than reusing either
     * production algorithm.
     */
    static ArrayList<ArrayList<Integer>> expectedPairs(
            Node root,
            int target) {

        ArrayList<Integer> values =
                new ArrayList<>();

        inOrder(
                root,
                values);

        Set<String> seen =
                new HashSet<>();

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        for (int i = 0;
             i < values.size();
             i++) {

            for (int j = i + 1;
                 j < values.size();
                 j++) {

                long sum =
                        (long) values.get(i)
                                + values.get(j);

                if (sum == target) {

                    addPair(
                            seen,
                            result,
                            values.get(i),
                            values.get(j));
                }
            }
        }

        sortPairs(result);

        return result;
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
            List<MethodCase> methods,
            int iterations) {

        printSeparator();

        System.out.println(
                "Randomised Cross Checks");

        printSeparator();

        Random random =
                new Random(20261004L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                    1 + random.nextInt(50);

            Node root =
                    randomBST(
                            random,
                            size);

            int target =
                    random.nextInt(201)
                            - 100;

            ArrayList<ArrayList<Integer>> expected =
                    expectedPairs(
                            root,
                            target);

            for (MethodCase method :
                    methods) {

                Result result =
                        method.algorithm.solve(
                                copyTree(root),
                                target);

                if (!result.valid()
                        || !result.pairs().equals(
                                expected)) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "target = "
                                    + target);

                    System.out.println(
                            "expected = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + result.pairs());

                    System.out.println(
                            "valid = "
                                    + result.valid());

                    return;
                }
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Output Helpers
     * **********************************************************************/

    static void printSeparator() {

        System.out.println(
                "============================================================");
    }

    static void printResults(
            int passed,
            int failed,
            int total) {

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                total);

        System.out.println();
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(
            String[] args) {

        System.out.println(
                "############################################################");

        System.out.println(
                "############## PAIR WITH GIVEN SUM BST #####################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Complement Check",
                                PairWithGivenSumBST
                                        ::pairWithGivenSumBSTCheckCompliment),

                        new MethodCase(
                                "In-order Two Pointer",
                                PairWithGivenSumBST
                                        ::pairWithGivenSumBSTInOrder)
                );

        List<TestCase> tests =
                buildTests();

        /*
         * ============================================================
         * Individual Algorithm Tests
         * ============================================================
         */

        for (MethodCase method :
                methods) {

            runTests(
                    method,
                    tests);
        }

        /*
         * ============================================================
         * Cross-Algorithm Tests
         * ============================================================
         */

        runCrossCheckTests(
                methods,
                tests);

        /*
         * ============================================================
         * Invalid Input Tests
         * ============================================================
         */

        runInvalidInputTests(
                methods);

        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */

        runRandomisedTests(
                methods,
                5000);

        System.out.println(
                "All test suites completed.");
    }
}

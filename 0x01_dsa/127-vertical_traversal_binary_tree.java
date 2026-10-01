import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;

/**
 * Perform vertical traversal of a binary tree.
 *
 * Each node is assigned:
 *
 *      horizontal distance (column)
 *      depth (row)
 *
 * The root starts at column 0 and row 0.
 *
 *      left child  -> column - 1
 *      right child -> column + 1
 *      child       -> row + 1
 *
 * Nodes are returned column-by-column from left to right.
 *
 * Within a column:
 *
 *      1. Smaller row first.
 *      2. If row and column are identical, smaller value first.
 *
 * Three approaches are provided:
 *
 * 1. Brute force
 *      Find the minimum and maximum column, then scan the entire tree
 *      once for every column.
 *
 *      Time:  O(n * w), where w is the number of columns
 *      Space: O(h + n)
 *
 * 2. Depth-first search with sorting
 *      Traverse the tree once and store each node against its column.
 *      Nodes in each column are then sorted by row and value.
 *
 *      Time:  O(n log n) worst case
 *      Space: O(n)
 *
 * 3. Breadth-first search with sorting
 *      Traverse the tree level-by-level and store each node against
 *      its column and row. Each column is sorted by value where nodes
 *      occupy the same row.
 *
 *      Time:  O(n log n) worst case
 *      Space: O(n)
 *
 * The Result record contains:
 *
 *      result = vertical traversal result
 *      valid  = whether the request was valid
 */
public class VerticalTraversalBinaryTree {

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

        Node(Node left, Node right, int val) {
            this.left = left;
            this.right = right;
            this.val = val;
        }
    }

    /* **********************************************************************
     * Supporting Records
     * **********************************************************************/

    /**
     * Stores the information required to order a node vertically.
     *
     * row    = depth of the node
     * value  = node value
     */
    static record NodePosition(
            int value,
            int row) {
    }

    /**
     * Stores a node together with its column and row.
     *
     * This is useful for the BFS implementation.
     */
    static record TraversalNode(
            Node node,
            int column,
            int row) {
    }

    /**
     * Result of a traversal request.
     */
    static record Result(
            ArrayList<ArrayList<Integer>> result,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static Node node(int value) {

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
     * A vertical traversal request is valid when the tree exists.
     */
    static boolean validRequest(
            Node root) {

        return root != null;
    }

    /**
     * Create a comparator which orders nodes by:
     *
     *      1. row
     *      2. value
     */
    static Comparator<NodePosition> positionComparator() {

        return Comparator
                .comparingInt(NodePosition::row)
                .thenComparingInt(NodePosition::value);
    }

    /**
     * Convert a column map into the final nested ArrayList result.
     *
     * TreeMap guarantees that columns are processed from left to right.
     */
    static ArrayList<ArrayList<Integer>> buildResult(
            TreeMap<Integer, ArrayList<NodePosition>> columns) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        for (ArrayList<NodePosition> positions :
                columns.values()) {

            positions.sort(
                    positionComparator());

            ArrayList<Integer> column =
                    new ArrayList<>();

            for (NodePosition position :
                    positions) {

                column.add(
                        position.value());
            }

            result.add(column);
        }

        return result;
    }

    /**
     * Add a node to a column map.
     */
    static void addToColumn(
            TreeMap<Integer, ArrayList<NodePosition>> columns,
            int column,
            int row,
            int value) {

        columns
                .computeIfAbsent(
                        column,
                        ignored -> new ArrayList<>())
                .add(
                        new NodePosition(
                                value,
                                row));
    }

    /* **********************************************************************
     * 1. Brute Force
     * **********************************************************************/

    /**
     * Brute-force vertical traversal.
     *
     * First find the minimum and maximum horizontal distance.
     *
     * Then, for every possible column, scan the entire tree and collect
     * nodes belonging to that column.
     */
    static Result verticalTraversalBruteForce(
            Node root) {

        if (!validRequest(root)) {

            return new Result(
                    null,
                    false);
        }

        int[] min =
                {0};

        int[] max =
                {0};

        findMinMax(
                root,
                0,
                min,
                max);

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        for (int column = min[0];
             column <= max[0];
             column++) {

            ArrayList<NodePosition> positions =
                    new ArrayList<>();

            collectVerticalColumn(
                    root,
                    column,
                    0,
                    0,
                    positions);

            positions.sort(
                    positionComparator());

            ArrayList<Integer> values =
                    new ArrayList<>();

            for (NodePosition position :
                    positions) {

                values.add(
                        position.value());
            }

            result.add(values);
        }

        return new Result(
                result,
                true);
    }

    /**
     * Find the minimum and maximum horizontal distance in the tree.
     *
     * column:
     *
     *      root  = 0
     *      left  = column - 1
     *      right = column + 1
     */
    static void findMinMax(
            Node root,
            int column,
            int[] min,
            int[] max) {

        if (root == null) {
            return;
        }

        min[0] =
                Math.min(
                        min[0],
                        column);

        max[0] =
                Math.max(
                        max[0],
                        column);

        findMinMax(
                root.left,
                column - 1,
                min,
                max);

        findMinMax(
                root.right,
                column + 1,
                min,
                max);
    }

    /**
     * Collect all nodes belonging to a requested column.
     *
     * row is tracked because nodes in the same column must be ordered
     * from top to bottom.
     */
    static void collectVerticalColumn(
            Node root,
            int targetColumn,
            int column,
            int row,
            ArrayList<NodePosition> positions) {

        if (root == null) {
            return;
        }

        if (column == targetColumn) {

            positions.add(
                    new NodePosition(
                            root.val,
                            row));
        }

        collectVerticalColumn(
                root.left,
                targetColumn,
                column - 1,
                row + 1,
                positions);

        collectVerticalColumn(
                root.right,
                targetColumn,
                column + 1,
                row + 1,
                positions);
    }

    /* **********************************************************************
     * 2. Depth-First Search with Sorting
     * **********************************************************************/

    /**
     * DFS solution.
     *
     * Every node is stored against its horizontal distance. The row and
     * value are retained so that the final ordering can be established
     * after traversal.
     */
    static Result verticalTraversalDFSSorting(
            Node root) {

        if (!validRequest(root)) {

            return new Result(
                    null,
                    false);
        }

        TreeMap<Integer, ArrayList<NodePosition>> columns =
                new TreeMap<>();

        implementDFS(
                root,
                0,
                0,
                columns);

        ArrayList<ArrayList<Integer>> result =
                buildResult(columns);

        return new Result(
                result,
                true);
    }

    /**
     * Traverse the tree recursively.
     */
    static void implementDFS(
            Node root,
            int column,
            int row,
            TreeMap<Integer, ArrayList<NodePosition>> columns) {

        if (root == null) {
            return;
        }

        addToColumn(
                columns,
                column,
                row,
                root.val);

        implementDFS(
                root.left,
                column - 1,
                row + 1,
                columns);

        implementDFS(
                root.right,
                column + 1,
                row + 1,
                columns);
    }

    /* **********************************************************************
     * 3. Breadth-First Search
     * **********************************************************************/

    /**
     * BFS solution.
     *
     * BFS naturally visits nodes from smaller row to larger row.
     *
     * We nevertheless retain the row and sort each column explicitly.
     * This makes the ordering rule independent of queue insertion order,
     * particularly when two nodes share the same row and column.
     */
    static Result verticalTraversalBFS(
            Node root) {

        if (!validRequest(root)) {

            return new Result(
                    null,
                    false);
        }

        TreeMap<Integer, ArrayList<NodePosition>> columns =
                new TreeMap<>();

        Queue<TraversalNode> queue =
                new ArrayDeque<>();

        queue.offer(
                new TraversalNode(
                        root,
                        0,
                        0));

        while (!queue.isEmpty()) {

            TraversalNode current =
                    queue.poll();

            Node node =
                    current.node();

            int column =
                    current.column();

            int row =
                    current.row();

            addToColumn(
                    columns,
                    column,
                    row,
                    node.val);

            if (node.left != null) {

                queue.offer(
                        new TraversalNode(
                                node.left,
                                column - 1,
                                row + 1));
            }

            if (node.right != null) {

                queue.offer(
                        new TraversalNode(
                                node.right,
                                column + 1,
                                row + 1));
            }
        }

        ArrayList<ArrayList<Integer>> result =
                buildResult(columns);

        return new Result(
                result,
                true);
    }

    /* **********************************************************************
     * Tree Helpers
     * **********************************************************************/

    /**
     * Compare two trees structurally and by value.
     *
     * This helper is useful when constructing and validating test data.
     */
    static boolean treesEqual(
            Node first,
            Node second) {

        if (first == null
                && second == null) {

            return true;
        }

        if (first == null
                || second == null) {

            return false;
        }

        return first.val == second.val
                && treesEqual(
                first.left,
                second.left)
                && treesEqual(
                first.right,
                second.right);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final ArrayList<ArrayList<Integer>> expected;
        final String description;

        TestCase(
                String id,
                Node root,
                List<List<Integer>> expected,
                String description) {

            this.id = id;
            this.root = root;
            this.expected =
                    copyResult(expected);
            this.description =
                    description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(Node root);
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

    /**
     * Deep-copy a vertical traversal result.
     */
    static ArrayList<ArrayList<Integer>> copyResult(
            List<List<Integer>> result) {

        ArrayList<ArrayList<Integer>> copy =
                new ArrayList<>();

        for (List<Integer> column :
                result) {

            copy.add(
                    new ArrayList<>(column));
        }

        return copy;
    }

    /* **********************************************************************
     * Standard Tests
     * **********************************************************************/

    static void runAlgorithmTests(
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
                                test.root);

                boolean success =
                        actual.valid()
                                && actual.result() != null
                                && actual.result().equals(
                                test.expected);

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  result = "
                                    + actual.result());

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
                                    + (actual.valid()
                                    ? actual.result()
                                    : "invalid"));
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

            try {

                ArrayList<ArrayList<Integer>> expected =
                        null;

                boolean allEqual =
                        true;

                for (MethodCase method :
                        methods) {

                    Result result =
                            method.algorithm.solve(
                                    test.root);

                    if (!result.valid()) {

                        allEqual = false;

                        System.out.printf(
                                "  %s returned invalid%n",
                                method.name);

                        break;
                    }

                    if (expected == null) {

                        expected =
                                result.result();

                    } else if (!expected.equals(
                            result.result())) {

                        allEqual = false;

                        System.out.printf(
                                "  %s = %s%n",
                                method.name,
                                result.result());
                    }
                }

                if (allEqual
                        && expected != null
                        && expected.equals(
                        test.expected)) {

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

                    System.out.println(
                            "  expected = "
                                    + test.expected);
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

        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            null);

            if (!result.valid()) {

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
     * Random Tree Generation
     * **********************************************************************/

    /**
     * Generate a random binary tree.
     *
     * A small value range is deliberately used so that duplicate values
     * occur frequently.
     */
    static Node randomTree(
            Random rng,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || rng.nextDouble()
                > nodeProbability) {

            return null;
        }

        int value =
                rng.nextInt(21) - 10;

        Node root =
                new Node(value);

        root.left =
                randomTree(
                        rng,
                        depth - 1,
                        nodeProbability);

        root.right =
                randomTree(
                        rng,
                        depth - 1,
                        nodeProbability);

        return root;
    }

    /* **********************************************************************
     * Reference Implementation
     * **********************************************************************/

    /**
     * A deliberately straightforward reference implementation.
     *
     * The randomised tests use this implementation as an independent
     * source of expected results rather than simply comparing the three
     * production implementations against one another.
     */
    static ArrayList<ArrayList<Integer>> referenceTraversal(
            Node root) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        if (root == null) {
            return result;
        }

        TreeMap<Integer, ArrayList<NodePosition>> columns =
                new TreeMap<>();

        referenceTraversal(
                root,
                0,
                0,
                columns);

        return buildResult(columns);
    }

    static void referenceTraversal(
            Node root,
            int column,
            int row,
            TreeMap<Integer, ArrayList<NodePosition>> columns) {

        if (root == null) {
            return;
        }

        addToColumn(
                columns,
                column,
                row,
                root.val);

        /*
         * Visit right before left deliberately. The final result must
         * not depend on traversal order.
         */
        referenceTraversal(
                root.right,
                column + 1,
                row + 1,
                columns);

        referenceTraversal(
                root.left,
                column - 1,
                row + 1,
                columns);
    }

    /* **********************************************************************
     * Randomised Cross Checks
     * **********************************************************************/

    static void runRandomisedTests(
            List<MethodCase> methods,
            int iterations) {

        printSeparator();

        System.out.println(
                "Randomised Cross Checks");

        printSeparator();

        Random rng =
                new Random(20260930L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            Node root;

            /*
             * Generate a non-empty tree.
             */
            do {

                root =
                        randomTree(
                                rng,
                                8,
                                0.75);

            } while (root == null);

            ArrayList<ArrayList<Integer>> expected =
                    referenceTraversal(root);

            for (MethodCase method :
                    methods) {

                Result actual =
                        method.algorithm.solve(
                                root);

                if (!actual.valid()
                        || !expected.equals(
                        actual.result())) {

                    System.out.println();

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "expected = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + (actual.valid()
                                    ? actual.result()
                                    : "invalid"));

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
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Basic Cases
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "B1",
                        node(1),
                        List.of(
                                List.of(1)),
                        "single node"));

        tests.add(
                new TestCase(
                        "B2",
                        node(
                                1,
                                node(2),
                                node(3)),
                        List.of(
                                List.of(2),
                                List.of(1),
                                List.of(3)),
                        "root with two children"));

        tests.add(
                new TestCase(
                        "B3",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        node(5)),
                                node(
                                        3,
                                        node(6),
                                        node(7))),
                        List.of(
                                List.of(4),
                                List.of(2),
                                List.of(1, 5, 6),
                                List.of(3),
                                List.of(7)),
                        "complete three-level tree"));

        /*
         * ============================================================
         * Classic Vertical Traversal Example
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "V1",
                        node(
                                3,
                                node(
                                        9),
                                node(
                                        20,
                                        node(15),
                                        node(7))),
                        List.of(
                                List.of(9),
                                List.of(3, 15),
                                List.of(20),
                                List.of(7)),
                        "classic vertical traversal example"));

        /*
         * ============================================================
         * Same Row + Same Column
         * ============================================================
         *
         * Nodes 4 and 5 both occupy column 0 and row 2.
         * They must therefore be sorted by value.
         */

        tests.add(
                new TestCase(
                        "T1",
                        node(
                                1,
                                node(
                                        2,
                                        null,
                                        node(5)),
                                node(
                                        3,
                                        node(4),
                                        null)),
                        List.of(
                                List.of(2),
                                List.of(1, 4, 5),
                                List.of(3)),
                        "same row and column tie"));

        /*
         * ============================================================
         * Tie With Reversed Traversal Order
         * ============================================================
         *
         * 4 and 5 occupy the same position. The implementation must
         * return 4 before 5 regardless of traversal order.
         */

        tests.add(
                new TestCase(
                        "T2",
                        node(
                                1,
                                node(
                                        2,
                                        null,
                                        node(5)),
                                node(
                                        3,
                                        node(4),
                                        null)),
                        List.of(
                                List.of(2),
                                List.of(1, 4, 5),
                                List.of(3)),
                        "value ordering resolves tie"));

        /*
         * ============================================================
         * Left-Skewed Tree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "L1",
                        node(
                                1,
                                node(
                                        2,
                                        node(3),
                                        null),
                                null),
                        List.of(
                                List.of(3),
                                List.of(2),
                                List.of(1)),
                        "left-skewed tree"));

        /*
         * ============================================================
         * Right-Skewed Tree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "R1",
                        node(
                                1,
                                null,
                                node(
                                        2,
                                        null,
                                        node(3))),
                        List.of(
                                List.of(1),
                                List.of(2),
                                List.of(3)),
                        "right-skewed tree"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "N1",
                        node(
                                -1,
                                node(-2),
                                node(
                                        -3,
                                        node(-4),
                                        null)),
                        List.of(
                                List.of(-2),
                                List.of(-1, -4),
                                List.of(-3)),
                        "negative values"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "D1",
                        node(
                                5,
                                node(
                                        5,
                                        node(5),
                                        node(5)),
                                node(
                                        5,
                                        null,
                                        node(5))),
                        List.of(
                                List.of(5),
                                List.of(5, 5),
                                List.of(5, 5, 5),
                                List.of(5),
                                List.of(5)),
                        "duplicate values"));

        /*
         * ============================================================
         * Larger Irregular Tree
         * ============================================================
         */

        Node largeTree =
                node(
                        10,
                        node(
                                20,
                                node(40),
                                node(
                                        50,
                                        node(80),
                                        null)),
                        node(
                                30,
                                node(
                                        60,
                                        null,
                                        node(90)),
                                node(70)));

        tests.add(
                new TestCase(
                        "L2",
                        largeTree,
                        List.of(
                                List.of(40),
                                List.of(20),
                                List.of(10, 50, 60),
                                List.of(30, 80, 90),
                                List.of(70)),
                        "larger irregular tree"));

        /*
         * ============================================================
         * Asymmetric Tree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "A1",
                        node(
                                1,
                                node(
                                        2,
                                        null,
                                        node(
                                                4,
                                                node(8),
                                                null)),
                                node(3)),
                        List.of(
                                List.of(2),
                                List.of(1, 4),
                                List.of(3, 8)),
                        "asymmetric tree"));

        return tests;
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(
            String[] args) {

        System.out.println(
                "############################################################");

        System.out.println(
                "############  VERTICAL TRAVERSAL BINARY TREE  ############");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Brute Force",
                                VerticalTraversalBinaryTree
                                        ::verticalTraversalBruteForce),

                        new MethodCase(
                                "DFS with Sorting",
                                VerticalTraversalBinaryTree
                                        ::verticalTraversalDFSSorting),

                        new MethodCase(
                                "BFS",
                                VerticalTraversalBinaryTree
                                        ::verticalTraversalBFS)
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

            runAlgorithmTests(
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
    }
}

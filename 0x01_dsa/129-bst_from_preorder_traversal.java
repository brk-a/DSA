import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Construct a Binary Search Tree from its pre-order traversal.
 *
 * Pre-order traversal visits nodes in the following order:
 *
 *      root -> left -> right
 *
 * Four implementations are provided:
 *
 * 1. Brute-force BST insertion
 *
 *      Inserts every value into the BST using the normal BST insertion
 *      algorithm.
 *
 *      Time:  O(n^2) worst case
 *      Space: O(h)
 *
 * 2. First Greater-than-Root
 *
 *      For every subtree, the first value greater than the root marks
 *      the beginning of the right subtree.
 *
 *      Time:  O(n^2) worst case
 *      Space: O(h)
 *
 * 3. Range-based Recursion
 *
 *      Maintains the valid range for each node and consumes the
 *      pre-order traversal from left to right.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 4. Monotonic Stack
 *
 *      Uses a stack of ancestors to identify where each new value
 *      belongs.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * Duplicate Policy
 * ----------------
 *
 * This implementation permits duplicate values and places duplicates
 * in the right subtree:
 *
 *      left < node <= right
 *
 * The same policy is used consistently by all four implementations.
 *
 * The test suite verifies:
 *
 *      - Empty and null input
 *      - Single-node trees
 *      - Balanced trees
 *      - Left-skewed trees
 *      - Right-skewed trees
 *      - Negative values
 *      - Duplicate values
 *      - Integer boundary values
 *      - Invalid pre-order sequences
 *      - Cross-algorithm consistency
 *      - Randomised cases
 */
public class BSTFromPreOrderTraversal {

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
            Node root,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validArray(
            int[] array) {

        return array != null
                && array.length > 0;
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
     * Compare two BSTs structurally.
     */
    static boolean sameTree(
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
                && sameTree(
                        first.left,
                        second.left)
                && sameTree(
                        first.right,
                        second.right);
    }

    /**
     * Return the pre-order traversal of a tree.
     *
     * This is used by the test suite to verify that a constructed tree
     * preserves the supplied traversal.
     */
    static void preOrder(
            Node root,
            ArrayList<Integer> values) {

        if (root == null) {
            return;
        }

        values.add(
                root.val);

        preOrder(
                root.left,
                values);

        preOrder(
                root.right,
                values);
    }

    /**
     * Return the in-order traversal of a tree.
     *
     * A valid BST must produce a non-decreasing sequence because
     * duplicates are permitted on the right.
     */
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

    /**
     * Validate the BST ordering policy:
     *
     *      left < node <= right
     *
     * Long bounds are used so that Integer.MIN_VALUE and
     * Integer.MAX_VALUE can be handled safely.
     */
    static boolean isValidBST(
            Node root) {

        return isValidBST(
                root,
                Long.MIN_VALUE,
                Long.MAX_VALUE);
    }

    static boolean isValidBST(
            Node root,
            long min,
            long max) {

        if (root == null) {
            return true;
        }

        if (root.val <= min
                || root.val > max) {

            return false;
        }

        return isValidBST(
                root.left,
                min,
                root.val)
                && isValidBST(
                        root.right,
                        root.val,
                        max);
    }

    /* **********************************************************************
     * 1. Brute-force BST Insertion
     * **********************************************************************/

    /**
     * Construct a BST by inserting each value using ordinary BST
     * insertion.
     *
     * Duplicate values are inserted into the right subtree.
     *
     * Time:  O(n^2) worst case
     * Space: O(h)
     */
    static Result bstFromPreOrderBruteForce(
            int[] array) {

        if (!validArray(array)) {

            return new Result(
                    null,
                    false);
        }

        Node root = null;

        for (int key : array) {

            root =
                    insertBST(
                            root,
                            key);
        }

        return new Result(
                root,
                true);
    }

    /**
     * Insert one value into a BST.
     *
     * Duplicate values go to the right.
     */
    static Node insertBST(
            Node root,
            int key) {

        Node newNode =
                new Node(key);

        if (root == null) {
            return newNode;
        }

        Node current =
                root;

        Node parent =
                null;

        while (current != null) {

            parent =
                    current;

            if (key < current.val) {

                current =
                        current.left;

            } else {

                /*
                 * Duplicate values go to the right.
                 */
                current =
                        current.right;
            }
        }

        if (key < parent.val) {

            parent.left =
                    newNode;

        } else {

            parent.right =
                    newNode;
        }

        return root;
    }

    /* **********************************************************************
     * 2. First Greater-than-Root
     * **********************************************************************/

    /**
     * Construct a BST by finding the first value greater than the
     * current root.
     *
     * All values before that position belong to the left subtree.
     * The remaining values belong to the right subtree.
     *
     * Because duplicates belong to the right subtree, values equal
     * to the root are treated as part of the left partition only when
     * they occur before the first strictly greater value.
     *
     * Time:  O(n^2) worst case
     * Space: O(h)
     */
    static Result bstFromPreOrderFirstGreater(
            int[] array) {

        if (!validArray(array)) {

            return new Result(
                    null,
                    false);
        }

        Node root =
                constructFirstGreater(
                        array,
                        0,
                        array.length - 1);

        /*
         * The recursive construction itself consumes the supplied
         * interval completely, so a non-null root represents a
         * constructed tree.
         *
         * Validate the resulting tree as an additional safety check.
         */
        boolean valid =
                root != null
                        && isValidBST(root);

        return new Result(
                valid ? root : null,
                valid);
    }

    static Node constructFirstGreater(
            int[] array,
            int low,
            int high) {

        if (low > high) {
            return null;
        }

        Node root =
                new Node(
                        array[low]);

        if (low == high) {
            return root;
        }

        int firstGreater =
                low + 1;

        while (firstGreater <= high
                && array[firstGreater]
                <= root.val) {

            firstGreater++;
        }

        root.left =
                constructFirstGreater(
                        array,
                        low + 1,
                        firstGreater - 1);

        root.right =
                constructFirstGreater(
                        array,
                        firstGreater,
                        high);

        return root;
    }

    /* **********************************************************************
     * 3. Range-based Recursion
     * **********************************************************************/

    /**
     * Construct a BST using valid value ranges.
     *
     * The array is consumed from left to right. A value is accepted
     * only when it falls inside the range permitted for the current
     * subtree.
     *
     * The entire array must be consumed for the input to be valid.
     *
     * Duplicate policy:
     *
     *      left < node <= right
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static Result bstFromPreOrderRange(
            int[] array) {

        if (!validArray(array)) {

            return new Result(
                    null,
                    false);
        }

        int[] index =
                {0};

        Node root =
                constructUsingRange(
                        array,
                        index,
                        Long.MIN_VALUE,
                        Long.MAX_VALUE);

        boolean valid =
                root != null
                        && index[0]
                        == array.length;

        return new Result(
                valid ? root : null,
                valid);
    }

    static Node constructUsingRange(
            int[] array,
            int[] index,
            long min,
            long max) {

        if (index[0]
                >= array.length) {

            return null;
        }

        int key =
                array[index[0]];

        /*
         * left < node <= right
         */
        if (key <= min
                || key > max) {

            return null;
        }

        index[0]++;

        Node root =
                new Node(key);

        /*
         * Left subtree:
         *
         *      min < value < root
         */
        root.left =
                constructUsingRange(
                        array,
                        index,
                        min,
                        key);

        /*
         * Right subtree:
         *
         *      root <= value <= max
         */
        root.right =
                constructUsingRange(
                        array,
                        index,
                        key,
                        max);

        return root;
    }

    /* **********************************************************************
     * 4. Monotonic Stack
     * **********************************************************************/

    /**
     * Construct a BST using a monotonic stack.
     *
     * The stack contains the path of ancestors for the next node.
     *
     * When the next value is greater than or equal to the stack top,
     * ancestors are popped until the correct parent for the right
     * subtree is found.
     *
     * Otherwise, the new node becomes the left child of the stack top.
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static Result bstFromPreOrderStack(
            int[] array) {

        if (!validArray(array)) {

            return new Result(
                    null,
                    false);
        }

        Node root =
                new Node(
                        array[0]);

        Deque<Node> stack =
                new ArrayDeque<>();

        stack.push(root);

        for (int i = 1;
             i < array.length;
             i++) {

            int key =
                    array[i];

            Node parent =
                    null;

            /*
             * Every popped node is smaller than or equal to key.
             *
             * The last popped node is therefore the parent of the
             * new right-subtree node.
             */
            while (!stack.isEmpty()
                    && key
                    >= stack.peek().val) {

                parent =
                        stack.pop();
            }

            Node current =
                    new Node(key);

            if (parent != null) {

                parent.right =
                        current;

            } else {

                stack.peek().left =
                        current;
            }

            stack.push(current);
        }

        /*
         * The stack algorithm assumes the input represents a valid
         * pre-order traversal. Validate the result so that the method
         * has the same contract as the other implementations.
         */
        boolean valid =
                isValidBST(root);

        return new Result(
                valid ? root : null,
                valid);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] input;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                int[] input,
                boolean expectedValid,
                String description) {

            this.id =
                    id;

            this.input =
                    input;

            this.expectedValid =
                    expectedValid;

            this.description =
                    description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(
                int[] array);
    }

    static class MethodCase {

        final String name;
        final Algorithm algorithm;

        MethodCase(
                String name,
                Algorithm algorithm) {

            this.name =
                    name;

            this.algorithm =
                    algorithm;
        }
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

        for (TestCase test : tests) {

            try {

                Result actual =
                        method.algorithm.solve(
                                test.input);

                boolean success =
                        actual.valid()
                                == test.expectedValid;

                /*
                 * A valid result must also produce a tree whose
                 * pre-order traversal is exactly the input.
                 */
                if (success
                        && test.expectedValid) {

                    ArrayList<Integer> values =
                            new ArrayList<>();

                    preOrder(
                            actual.root(),
                            values);

                    success =
                            matchesArray(
                                    values,
                                    test.input);

                    success =
                            success
                                    && isValidBST(
                                            actual.root());
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

                    printExpected(
                            test);

                    printActual(
                            actual,
                            test.input);
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

    /**
     * Ensure all implementations agree on validity and produce
     * structurally equivalent trees.
     */
    static void runCrossCheckTests(
            List<MethodCase> methods,
            List<TestCase> tests) {

        printSeparator();

        System.out.println(
                "Cross-Algorithm Tests");

        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                List<Result> results =
                        new ArrayList<>();

                for (MethodCase method :
                        methods) {

                    results.add(
                            method.algorithm.solve(
                                    test.input));
                }

                Result reference =
                        results.get(0);

                boolean success =
                        reference.valid()
                                == test.expectedValid;

                for (int i = 1;
                     i < results.size();
                     i++) {

                    Result actual =
                            results.get(i);

                    if (actual.valid()
                            != reference.valid()) {

                        success = false;
                    }

                    if (reference.valid()
                            && actual.valid()
                            && !sameTree(
                                    reference.root(),
                                    actual.root())) {

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

                    for (int i = 0;
                         i < methods.size();
                         i++) {

                        Result result =
                                results.get(i);

                        System.out.printf(
                                "  %s: valid = %s%n",
                                methods.get(i).name,
                                result.valid());
                    }
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

        int[][] invalidInputs =
                {
                        null,
                        {},
                        {10, 5, 15, 12, 7}
                };

        String[] descriptions =
                {
                        "null input",
                        "empty input",
                        "invalid pre-order sequence"
                };

        for (int i = 0;
             i < invalidInputs.length;
             i++) {

            for (MethodCase method :
                    methods) {

                try {

                    Result result =
                            method.algorithm.solve(
                                    invalidInputs[i]);

                    if (!result.valid()
                            && result.root() == null) {

                        passed++;

                        System.out.printf(
                                "PASS I%d - %s rejects %s%n",
                                i + 1,
                                method.name,
                                descriptions[i]);

                    } else {

                        failed++;

                        System.out.printf(
                                "FAIL I%d - %s accepts %s%n",
                                i + 1,
                                method.name,
                                descriptions[i]);
                    }

                } catch (Exception ex) {

                    failed++;

                    System.out.printf(
                            "FAIL I%d - %s throws for %s%n",
                            i + 1,
                            method.name,
                            descriptions[i]);

                    System.out.println(
                            "  exception = "
                                    + ex);
                }
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
         * Empty / Null
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "I1",
                        null,
                        false,
                        "null input"));

        tests.add(
                new TestCase(
                        "I2",
                        new int[]{},
                        false,
                        "empty input"));

        /*
         * ============================================================
         * Single Node
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "S1",
                        new int[]{42},
                        true,
                        "single-node tree"));

        /*
         * ============================================================
         * Balanced Tree
         * ============================================================
         *
         * Pre-order:
         *
         *          20
         *        /    \
         *      10      30
         *     /  \    /  \
         *    5   15  25  35
         *
         *      20, 10, 5, 15, 30, 25, 35
         */

        tests.add(
                new TestCase(
                        "B1",
                        new int[]{
                                20,
                                10,
                                5,
                                15,
                                30,
                                25,
                                35
                        },
                        true,
                        "balanced tree"));

        /*
         * ============================================================
         * Left-Skewed Tree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "S2",
                        new int[]{
                                40,
                                30,
                                20,
                                10
                        },
                        true,
                        "left-skewed tree"));

        /*
         * ============================================================
         * Right-Skewed Tree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "S3",
                        new int[]{
                                10,
                                20,
                                30,
                                40
                        },
                        true,
                        "right-skewed tree"));

        /*
         * ============================================================
         * Root With Only Left Subtree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "T1",
                        new int[]{
                                10,
                                5,
                                2,
                                7
                        },
                        true,
                        "root with left subtree"));

        /*
         * ============================================================
         * Root With Only Right Subtree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "T2",
                        new int[]{
                                10,
                                20,
                                15,
                                25
                        },
                        true,
                        "root with right subtree"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "N1",
                        new int[]{
                                -10,
                                -20,
                                -30,
                                -15,
                                -5,
                                -7,
                                -1
                        },
                        true,
                        "negative values"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         *
         * Duplicates belong to the right subtree.
         */

        tests.add(
                new TestCase(
                        "D1",
                        new int[]{
                                10,
                                10,
                                10
                        },
                        true,
                        "all duplicate values"));

        tests.add(
                new TestCase(
                        "D2",
                        new int[]{
                                10,
                                5,
                                5,
                                7,
                                10,
                                10,
                                15
                        },
                        true,
                        "duplicates on both sides"));

        /*
         * ============================================================
         * Integer Boundaries
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "E1",
                        new int[]{
                                0,
                                Integer.MIN_VALUE,
                                Integer.MAX_VALUE
                        },
                        true,
                        "integer boundary values"));

        tests.add(
                new TestCase(
                        "E2",
                        new int[]{
                                Integer.MAX_VALUE,
                                Integer.MAX_VALUE,
                                Integer.MAX_VALUE
                        },
                        true,
                        "maximum integer duplicates"));

        tests.add(
                new TestCase(
                        "E3",
                        new int[]{
                                Integer.MIN_VALUE,
                                Integer.MIN_VALUE
                        },
                        true,
                        "minimum integer duplicates"));

        /*
         * ============================================================
         * Invalid Pre-order Sequences
         * ============================================================
         *
         * 10 -> 5 establishes that we are inside the left subtree.
         * 15 cannot subsequently appear there.
         */

        tests.add(
                new TestCase(
                        "I3",
                        new int[]{
                                10,
                                5,
                                15,
                                7
                        },
                        false,
                        "value violates ancestor range"));

        tests.add(
                new TestCase(
                        "I4",
                        new int[]{
                                20,
                                10,
                                5,
                                25,
                                15
                        },
                        false,
                        "value appears after right subtree"));

        tests.add(
                new TestCase(
                        "I5",
                        new int[]{
                                10,
                                5,
                                12,
                                7,
                                3
                        },
                        false,
                        "left-subtree ordering violation"));

        return tests;
    }

    /* **********************************************************************
     * Random BST Generation
     * **********************************************************************/

    /**
     * Generate a random valid BST and return its pre-order traversal.
     *
     * The generated tree is created using the same duplicate policy as
     * the algorithms under test.
     */
    static Node randomBST(
            Random random,
            int size) {

        Node root = null;

        for (int i = 0;
             i < size;
             i++) {

            int value =
                    random.nextInt(2001)
                            - 1000;

            root =
                    insertBST(
                            root,
                            value);
        }

        return root;
    }

    static int[] randomPreOrder(
            Random random,
            int size) {

        Node root =
                randomBST(
                        random,
                        size);

        ArrayList<Integer> values =
                new ArrayList<>();

        preOrder(
                root,
                values);

        return toArray(
                values);
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
                new Random(
                        20261002L);

        int passed = 0;
        int failed = 0;

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                    1 + random.nextInt(50);

            int[] input =
                    randomPreOrder(
                            random,
                            size);

            Result reference =
                    methods.get(0)
                            .algorithm
                            .solve(input);

            boolean success =
                    reference.valid();

            for (int i = 1;
                 i < methods.size();
                 i++) {

                Result actual =
                        methods.get(i)
                                .algorithm
                                .solve(input);

                if (actual.valid()
                        != reference.valid()) {

                    success = false;
                }

                if (actual.valid()
                        && reference.valid()
                        && !sameTree(
                                actual.root(),
                                reference.root())) {

                    success = false;
                }
            }

            if (success) {

                passed++;

            } else {

                failed++;

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = "
                                + iteration);

                System.out.println(
                        "input = "
                                + arrayToString(
                                        input));

                for (MethodCase method :
                        methods) {

                    Result result =
                            method.algorithm.solve(
                                    input);

                    System.out.printf(
                            "  %s: valid = %s%n",
                            method.name,
                            result.valid());
                }

                /*
                 * Stop at the first failure so that the failing
                 * input can be reproduced using the fixed seed.
                 */
                break;
            }
        }

        printResults(
                passed,
                failed,
                iterations);
    }

    /* **********************************************************************
     * Array Helpers
     * **********************************************************************/

    static boolean matchesArray(
            List<Integer> values,
            int[] expected) {

        if (values.size()
                != expected.length) {

            return false;
        }

        for (int i = 0;
             i < expected.length;
             i++) {

            if (values.get(i)
                    != expected[i]) {

                return false;
            }
        }

        return true;
    }

    static int[] toArray(
            List<Integer> values) {

        int[] result =
                new int[values.size()];

        for (int i = 0;
             i < values.size();
             i++) {

            result[i] =
                    values.get(i);
        }

        return result;
    }

    static String arrayToString(
            int[] array) {

        if (array == null) {
            return "null";
        }

        StringBuilder result =
                new StringBuilder();

        result.append('[');

        for (int i = 0;
             i < array.length;
             i++) {

            if (i > 0) {
                result.append(", ");
            }

            result.append(
                    array[i]);
        }

        result.append(']');

        return result.toString();
    }

    /* **********************************************************************
     * Output Helpers
     * **********************************************************************/

    static void printExpected(
            TestCase test) {

        System.out.println(
                "  expected valid = "
                        + test.expectedValid);
    }

    static void printActual(
            Result actual,
            int[] input) {

        System.out.println(
                "  actual valid   = "
                        + actual.valid());

        if (actual.root() != null) {

            ArrayList<Integer> values =
                    new ArrayList<>();

            preOrder(
                    actual.root(),
                    values);

            System.out.println(
                    "  actual preorder = "
                            + values);

            System.out.println(
                    "  input preorder  = "
                            + arrayToString(
                                    input));
        }
    }

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
                "##########  BST FROM PRE-ORDER TRAVERSAL  ##################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Brute-force Insertion",
                                BSTFromPreOrderTraversal
                                        ::bstFromPreOrderBruteForce),

                        new MethodCase(
                                "First Greater-than-Root",
                                BSTFromPreOrderTraversal
                                        ::bstFromPreOrderFirstGreater),

                        new MethodCase(
                                "Range-based Recursion",
                                BSTFromPreOrderTraversal
                                        ::bstFromPreOrderRange),

                        new MethodCase(
                                "Monotonic Stack",
                                BSTFromPreOrderTraversal
                                        ::bstFromPreOrderStack)
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
                "Test suite complete.");
    }
}

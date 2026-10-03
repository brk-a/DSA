import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Find the kth largest element in a Binary Search Tree (BST).
 *
 * Two implementations are provided:
 *
 * 1. Recursive reverse-inorder traversal
 *      right -> root -> left
 *
 *      Time:  O(n) worst case
 *      Space: O(h)
 *
 * 2. Reverse Morris traversal
 *      right -> root -> left
 *
 *      Time:  O(n)
 *      Space: O(1) auxiliary space
 *
 * The input is assumed to be a valid BST using strict ordering:
 *
 *      all values in left subtree  < node.val
 *      all values in right subtree > node.val
 *
 * k is 1-based:
 *
 *      k = 1 -> largest
 *      k = 2 -> second largest
 *      ...
 *
 * Result.valid is false when:
 *
 *      - root is null
 *      - k <= 0
 *      - k is larger than the number of nodes
 *
 * Result.valid is true when the kth largest element exists.
 */
public class KthLargestElement {

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
     * Result
     * **********************************************************************/

    static record Result(
            int kthLargest,
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

    static boolean validInput(
            Node root,
            int k) {

        return root != null
                && k > 0;
    }

    /* **********************************************************************
     * 1. Recursive Reverse-Inorder Traversal
     * **********************************************************************/

    /**
     * Finds the kth largest element using reverse inorder:
     *
     *      right -> root -> left
     *
     * In a BST, this visits values in descending order.
     */
    static Result kthLargestElementRecursion(
            Node root,
            int k) {

        if (!validInput(root, k)) {
            return new Result(
                    0,
                    false);
        }

        int[] count = {0};
        int[] answer = {0};

        boolean found =
                kthLargestRecursion(
                        root,
                        count,
                        k,
                        answer);

        return new Result(
                answer[0],
                found);
    }

    static boolean kthLargestRecursion(
            Node root,
            int[] count,
            int k,
            int[] answer) {

        if (root == null) {
            return false;
        }

        /*
         * Visit larger values first.
         */
        if (kthLargestRecursion(
                root.right,
                count,
                k,
                answer)) {

            return true;
        }

        /*
         * Visit current node.
         */
        count[0]++;

        if (count[0] == k) {

            answer[0] = root.val;

            return true;
        }

        /*
         * Visit smaller values.
         */
        return kthLargestRecursion(
                root.left,
                count,
                k,
                answer);
    }

    /* **********************************************************************
     * 2. Reverse Morris Traversal
     * **********************************************************************/

    /**
     * Finds the kth largest element using reverse Morris traversal.
     *
     * Reverse inorder:
     *
     *      right -> root -> left
     *
     * Morris traversal temporarily creates links from the leftmost node
     * of a right subtree back to the current node.
     *
     * The traversal continues after finding the answer so that all
     * temporary links are restored before returning.
     */
    static Result kthLargestElementMorrisTraversal(
            Node root,
            int k) {

        if (!validInput(root, k)) {
            return new Result(
                    0,
                    false);
        }

        Node current = root;

        int count = 0;
        int answer = 0;

        boolean found = false;

        while (current != null) {

            /*
             * No right subtree.
             *
             * Current is the next node in reverse inorder.
             */
            if (current.right == null) {

                count++;

                if (count == k) {

                    answer = current.val;
                    found = true;
                }

                current = current.left;

                continue;
            }

            /*
             * Find the leftmost node in the right subtree.
             */
            Node successor =
                    current.right;

            while (successor.left != null
                    && successor.left != current) {

                successor = successor.left;
            }

            /*
             * First visit:
             *
             * Create a temporary thread back to current.
             */
            if (successor.left == null) {

                successor.left = current;

                current = current.right;

            } else {

                /*
                 * Second visit:
                 *
                 * Remove the temporary thread.
                 */
                successor.left = null;

                count++;

                if (count == k) {

                    answer = current.val;
                    found = true;
                }

                current = current.left;
            }
        }

        return new Result(
                answer,
                found);
    }

    /* **********************************************************************
     * Independent Reference Implementation
     * **********************************************************************/

    /**
     * Standard inorder traversal:
     *
     *      left -> root -> right
     *
     * For a BST this produces ascending values.
     */
    static void inorder(
            Node root,
            List<Integer> result) {

        if (root == null) {
            return;
        }

        inorder(
                root.left,
                result);

        result.add(root.val);

        inorder(
                root.right,
                result);
    }

    /**
     * Independent reference implementation used by the tests.
     */
    static Result expectedKthLargest(
            Node root,
            int k) {

        if (!validInput(root, k)) {
            return new Result(
                    0,
                    false);
        }

        List<Integer> values =
                new ArrayList<>();

        inorder(
                root,
                values);

        if (k > values.size()) {
            return new Result(
                    0,
                    false);
        }

        int index =
                values.size() - k;

        return new Result(
                values.get(index),
                true);
    }

    /* **********************************************************************
     * BST Construction
     * **********************************************************************/

    /**
     * Inserts a value into a strict BST.
     *
     * Duplicate values are rejected.
     */
    static Node insert(
            Node root,
            int value) {

        if (root == null) {
            return new Node(value);
        }

        if (value < root.val) {

            root.left =
                    insert(
                            root.left,
                            value);

        } else if (value > root.val) {

            root.right =
                    insert(
                            root.right,
                            value);

        } else {

            throw new IllegalArgumentException(
                    "Duplicate BST value: " + value);
        }

        return root;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int k;
        final Integer expected;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                Node root,
                int k,
                Integer expected,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.root = root;
            this.k = k;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(
                Node root,
                int k);
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

    static boolean resultMatches(
            Result actual,
            Integer expected,
            boolean expectedValid) {

        if (actual.valid() != expectedValid) {
            return false;
        }

        if (!expectedValid) {
            return true;
        }

        return actual.kthLargest() == expected;
    }

    static void printResult(
            Result result) {

        System.out.println(
                "  result = " + result);
    }

    /* **********************************************************************
     * Algorithm Tests
     * **********************************************************************/

    static void runAlgorithmTests(
            MethodCase method,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                method.name);

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Result actual =
                        method.algorithm.solve(
                                test.root,
                                test.k);

                if (resultMatches(
                        actual,
                        test.expected,
                        test.expectedValid)) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  k = " + test.k);

                    printResult(actual);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  k = " + test.k);

                    System.out.println(
                            "  expected = "
                                    + test.expected
                                    + ", valid = "
                                    + test.expectedValid);

                    printResult(actual);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = " + ex);
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
     * Morris Tree Integrity Tests
     * **********************************************************************/

    static void runMorrisIntegrityTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Morris Tree Integrity Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        Node root =
                node(
                        10,
                        node(
                                5,
                                node(2),
                                node(
                                        7,
                                        node(6),
                                        node(8))),
                        node(
                                15,
                                node(12),
                                node(
                                        20,
                                        node(18),
                                        node(25))));

        /*
         * Capture the tree before Morris traversal.
         */
        List<Integer> before =
                new ArrayList<>();

        inorder(
                root,
                before);

        /*
         * 4th largest:
         *
         * 25, 20, 18, 15, 12, 10, 8, 7, 6, 5, 2
         *
         * Answer = 15.
         */
        Result result =
                kthLargestElementMorrisTraversal(
                        root,
                        4);

        /*
         * Capture the tree after Morris traversal.
         */
        List<Integer> after =
                new ArrayList<>();

        inorder(
                root,
                after);

        boolean correctResult =
                result.valid()
                        && result.kthLargest() == 15;

        boolean treeUnchanged =
                before.equals(after);

        if (correctResult
                && treeUnchanged) {

            passed++;

            System.out.println(
                    "PASS M1 - Morris returns correct value "
                            + "and restores the tree");

        } else {

            failed++;

            System.out.println(
                    "FAIL M1 - Morris result/integrity");

            System.out.println(
                    "  result = " + result);

            System.out.println(
                    "  before = " + before);

            System.out.println(
                    "  after  = " + after);
        }

        /*
         * Run Morris repeatedly against the same tree.
         */
        boolean repeatedCallsCorrect =
                true;

        for (int k = 1;
             k <= before.size();
             k++) {

            Result actual =
                    kthLargestElementMorrisTraversal(
                            root,
                            k);

            Result expected =
                    expectedKthLargest(
                            root,
                            k);

            if (!actual.equals(expected)) {

                repeatedCallsCorrect = false;

                System.out.println(
                        "  mismatch at k = " + k);

                System.out.println(
                        "  expected = " + expected);

                System.out.println(
                        "  actual   = " + actual);

                break;
            }
        }

        List<Integer> afterRepeatedCalls =
                new ArrayList<>();

        inorder(
                root,
                afterRepeatedCalls);

        repeatedCallsCorrect &=
                before.equals(
                        afterRepeatedCalls);

        if (repeatedCallsCorrect) {

            passed++;

            System.out.println(
                    "PASS M2 - repeated Morris calls "
                            + "preserve the tree");

        } else {

            failed++;

            System.out.println(
                    "FAIL M2 - repeated Morris calls");

            System.out.println(
                    "  original = " + before);

            System.out.println(
                    "  final    = " + afterRepeatedCalls);
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                passed + failed);

        System.out.println();
    }

    /* **********************************************************************
     * Invalid Input Tests
     * **********************************************************************/

    static void runInvalidInputTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Invalid Input Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursive",
                                KthLargestElement
                                        ::kthLargestElementRecursion),

                        new MethodCase(
                                "Morris",
                                KthLargestElement
                                        ::kthLargestElementMorrisTraversal)
                );

        for (MethodCase method : methods) {

            /*
             * Null root.
             */
            Result nullRoot =
                    method.algorithm.solve(
                            null,
                            1);

            if (!nullRoot.valid()) {

                passed++;

                System.out.println(
                        "PASS I1 - "
                                + method.name
                                + " rejects null root");

            } else {

                failed++;

                System.out.println(
                        "FAIL I1 - "
                                + method.name
                                + " should reject null root");
            }

            /*
             * k = 0.
             */
            Node root =
                    node(
                            10,
                            node(5),
                            node(15));

            Result zeroK =
                    method.algorithm.solve(
                            root,
                            0);

            if (!zeroK.valid()) {

                passed++;

                System.out.println(
                        "PASS I2 - "
                                + method.name
                                + " rejects k = 0");

            } else {

                failed++;

                System.out.println(
                        "FAIL I2 - "
                                + method.name
                                + " should reject k = 0");
            }

            /*
             * Negative k.
             */
            Result negativeK =
                    method.algorithm.solve(
                            root,
                            -1);

            if (!negativeK.valid()) {

                passed++;

                System.out.println(
                        "PASS I3 - "
                                + method.name
                                + " rejects negative k");

            } else {

                failed++;

                System.out.println(
                        "FAIL I3 - "
                                + method.name
                                + " should reject negative k");
            }

            /*
             * k larger than the number of nodes.
             */
            Result tooLarge =
                    method.algorithm.solve(
                            root,
                            4);

            if (!tooLarge.valid()) {

                passed++;

                System.out.println(
                        "PASS I4 - "
                                + method.name
                                + " rejects k > node count");

            } else {

                failed++;

                System.out.println(
                        "FAIL I4 - "
                                + method.name
                                + " should reject k > node count");
            }
        }

        /*
         * -1 must be treated as a legitimate value.
         */
        Node minusOneTree =
                node(
                        -1,
                        node(-2),
                        node(0));

        for (MethodCase method : methods) {

            Result actual =
                    method.algorithm.solve(
                            minusOneTree,
                            2);

            if (actual.valid()
                    && actual.kthLargest() == -1) {

                passed++;

                System.out.println(
                        "PASS I5 - "
                                + method.name
                                + " accepts -1 as a legitimate value");

            } else {

                failed++;

                System.out.println(
                        "FAIL I5 - "
                                + method.name
                                + " incorrectly treats -1 as not found");

                printResult(actual);
            }
        }

        /*
         * Integer.MIN_VALUE and Integer.MAX_VALUE.
         */
        Node boundaryTree =
                node(
                        0,
                        node(Integer.MIN_VALUE),
                        node(Integer.MAX_VALUE));

        for (MethodCase method : methods) {

            Result largest =
                    method.algorithm.solve(
                            boundaryTree,
                            1);

            Result smallest =
                    method.algorithm.solve(
                            boundaryTree,
                            3);

            boolean correct =
                    largest.valid()
                            && largest.kthLargest()
                            == Integer.MAX_VALUE
                            && smallest.valid()
                            && smallest.kthLargest()
                            == Integer.MIN_VALUE;

            if (correct) {

                passed++;

                System.out.println(
                        "PASS I6 - "
                                + method.name
                                + " handles integer boundaries");

            } else {

                failed++;

                System.out.println(
                        "FAIL I6 - "
                                + method.name
                                + " integer boundary test");

                System.out.println(
                        "  largest = " + largest);

                System.out.println(
                        "  smallest = " + smallest);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                passed + failed);

        System.out.println();
    }

    /* **********************************************************************
     * Random BST Generation
     * **********************************************************************/

    static Node randomBst(
            Random rng,
            int nodeCount) {

        if (nodeCount <= 0) {
            return null;
        }

        Node root = null;

        /*
         * Generate unique values.
         */
        boolean[] used =
                new boolean[10000];

        int generated = 0;

        while (generated < nodeCount) {

            int value =
                    rng.nextInt(10000);

            if (used[value]) {
                continue;
            }

            used[value] = true;

            root =
                    insert(
                            root,
                            value);

            generated++;
        }

        return root;
    }

    /* **********************************************************************
     * Randomised Cross Checks
     * **********************************************************************/

    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260926L);

        int passed = 0;

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int nodeCount =
                    1 + rng.nextInt(50);

            Node root =
                    randomBst(
                            rng,
                            nodeCount);

            int k =
                    1 + rng.nextInt(nodeCount);

            /*
             * Independent reference result.
             */
            Result expected =
                    expectedKthLargest(
                            root,
                            k);

            /*
             * Recursive implementation.
             */
            Result recursive =
                    kthLargestElementRecursion(
                            root,
                            k);

            /*
             * Morris implementation.
             */
            Result morris =
                    kthLargestElementMorrisTraversal(
                            root,
                            k);

            boolean recursiveCorrect =
                    recursive.equals(expected);

            boolean morrisCorrect =
                    morris.equals(expected);

            if (!recursiveCorrect
                    || !morrisCorrect) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = "
                                + iteration);

                System.out.println(
                        "nodeCount = "
                                + nodeCount);

                System.out.println(
                        "k = "
                                + k);

                System.out.println(
                        "expected = "
                                + expected);

                System.out.println(
                        "recursive = "
                                + recursive);

                System.out.println(
                        "morris = "
                                + morris);

                return;
            }

            /*
             * Verify Morris did not permanently modify the tree.
             */
            List<Integer> before =
                    new ArrayList<>();

            inorder(
                    root,
                    before);

            kthLargestElementMorrisTraversal(
                    root,
                    k);

            List<Integer> after =
                    new ArrayList<>();

            inorder(
                    root,
                    after);

            if (!before.equals(after)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "Morris modified the tree");

                System.out.println(
                        "iteration = "
                                + iteration);

                System.out.println(
                        "before = "
                                + before);

                System.out.println(
                        "after = "
                                + after);

                return;
            }

            passed++;
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                passed);
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(
            String[] args) {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Basic Cases
         * ============================================================
         */

        Node single =
                node(10);

        tests.add(
                new TestCase(
                        "B1",
                        single,
                        1,
                        10,
                        true,
                        "single node"));

        Node basic =
                node(
                        10,
                        node(5),
                        node(15));

        tests.add(
                new TestCase(
                        "B2",
                        basic,
                        1,
                        15,
                        true,
                        "largest element"));

        tests.add(
                new TestCase(
                        "B3",
                        basic,
                        2,
                        10,
                        true,
                        "second largest element"));

        tests.add(
                new TestCase(
                        "B4",
                        basic,
                        3,
                        5,
                        true,
                        "smallest element"));

        /*
         * ============================================================
         * Complete BST
         * ============================================================
         */

        Node complete =
                node(
                        10,
                        node(
                                5,
                                node(2),
                                node(7)),
                        node(
                                15,
                                node(12),
                                node(20)));

        tests.add(
                new TestCase(
                        "C1",
                        complete,
                        1,
                        20,
                        true,
                        "largest in complete BST"));

        tests.add(
                new TestCase(
                        "C2",
                        complete,
                        2,
                        15,
                        true,
                        "second largest"));

        tests.add(
                new TestCase(
                        "C3",
                        complete,
                        3,
                        12,
                        true,
                        "third largest"));

        tests.add(
                new TestCase(
                        "C4",
                        complete,
                        4,
                        10,
                        true,
                        "root is fourth largest"));

        tests.add(
                new TestCase(
                        "C5",
                        complete,
                        7,
                        2,
                        true,
                        "smallest in complete BST"));

        /*
         * ============================================================
         * Right-Skewed BST
         * ============================================================
         */

        Node rightSkewed =
                node(
                        1,
                        null,
                        node(
                                2,
                                null,
                                node(
                                        3,
                                        null,
                                        node(
                                                4,
                                                null,
                                                node(5))))));

        tests.add(
                new TestCase(
                        "R1",
                        rightSkewed,
                        1,
                        5,
                        true,
                        "right-skewed largest"));

        tests.add(
                new TestCase(
                        "R2",
                        rightSkewed,
                        3,
                        3,
                        true,
                        "right-skewed middle"));

        tests.add(
                new TestCase(
                        "R3",
                        rightSkewed,
                        5,
                        1,
                        true,
                        "right-skewed smallest"));

        /*
         * ============================================================
         * Left-Skewed BST
         * ============================================================
         */

        Node leftSkewed =
                node(
                        5,
                        node(
                                4,
                                node(
                                        3,
                                        node(
                                                2,
                                                node(1),
                                                null),
                                        null),
                                null),
                        null);

        tests.add(
                new TestCase(
                        "L1",
                        leftSkewed,
                        1,
                        5,
                        true,
                        "left-skewed largest"));

        tests.add(
                new TestCase(
                        "L2",
                        leftSkewed,
                        3,
                        3,
                        true,
                        "left-skewed middle"));

        tests.add(
                new TestCase(
                        "L3",
                        leftSkewed,
                        5,
                        1,
                        true,
                        "left-skewed smallest"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        Node negativeTree =
                node(
                        -10,
                        node(
                                -20,
                                node(-30),
                                node(-15)),
                        node(
                                -5,
                                node(-7),
                                node(-1)));

        tests.add(
                new TestCase(
                        "N1",
                        negativeTree,
                        1,
                        -1,
                        true,
                        "negative values"));

        tests.add(
                new TestCase(
                        "N2",
                        negativeTree,
                        4,
                        -10,
                        true,
                        "negative root"));

        tests.add(
                new TestCase(
                        "N3",
                        negativeTree,
                        7,
                        -30,
                        true,
                        "negative minimum"));

        /*
         * ============================================================
         * Integer Boundary Values
         * ============================================================
         */

        Node boundaryTree =
                node(
                        0,
                        node(Integer.MIN_VALUE),
                        node(Integer.MAX_VALUE));

        tests.add(
                new TestCase(
                        "I1",
                        boundaryTree,
                        1,
                        Integer.MAX_VALUE,
                        true,
                        "Integer.MAX_VALUE"));

        tests.add(
                new TestCase(
                        "I2",
                        boundaryTree,
                        2,
                        0,
                        true,
                        "zero"));

        tests.add(
                new TestCase(
                        "I3",
                        boundaryTree,
                        3,
                        Integer.MIN_VALUE,
                        true,
                        "Integer.MIN_VALUE"));

        /*
         * ============================================================
         * -1 as a Legitimate Value
         * ============================================================
         */

        Node minusOneTree =
                node(
                        -1,
                        node(-2),
                        node(0));

        tests.add(
                new TestCase(
                        "S1",
                        minusOneTree,
                        2,
                        -1,
                        true,
                        "-1 is a legitimate kth-largest value"));

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "##############  KTH LARGEST ELEMENT IN BST  ###############");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Algorithm Implementations
         * ============================================================
         */

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursive Reverse-Inorder",
                                KthLargestElement
                                        ::kthLargestElementRecursion),

                        new MethodCase(
                                "Reverse Morris Traversal",
                                KthLargestElement
                                        ::kthLargestElementMorrisTraversal)
                );

        /*
         * ============================================================
         * Deterministic Tests
         * ============================================================
         */

        for (MethodCase method : methods) {

            runAlgorithmTests(
                    method,
                    tests);
        }

        /*
         * ============================================================
         * Invalid Input Tests
         * ============================================================
         */

        runInvalidInputTests();

        /*
         * ============================================================
         * Morris Integrity Tests
         * ============================================================
         */

        runMorrisIntegrityTests();

        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */

        runRandomisedTests(5000);

        System.out.println(
                "############################################################");

        System.out.println(
                "####################  TESTS COMPLETE  ######################");

        System.out.println(
                "############################################################");
    }
}

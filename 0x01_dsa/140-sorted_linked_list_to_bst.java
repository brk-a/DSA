import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Convert a sorted singly linked list into a height-balanced BST.
 *
 * The linked list must be sorted in ascending order.
 *
 * Two implementations are provided:
 *
 * 1. Recursion with an ArrayList
 *
 *      Copies the linked-list values into an ArrayList and recursively
 *      chooses the middle element as the root.
 *
 *      Time:  O(n)
 *      Space: O(n)
 *
 * 2. Direct in-order traversal
 *
 *      Counts the list nodes and constructs the BST while advancing a
 *      shared linked-list reference.
 *
 *      Time:  O(n)
 *      Space: O(log n) auxiliary recursion space
 *
 * The resulting tree is height-balanced, and its in-order traversal is
 * identical to the original linked list.
 */
public class LinkedListToBST {

    /* **********************************************************************
     * Binary Search Tree Node
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
     * Linked-List Node
     * **********************************************************************/

    static class LinkedListNode {

        int val;
        LinkedListNode next;

        LinkedListNode(int val) {
            this.val = val;
        }

        LinkedListNode(
                int val,
                LinkedListNode next) {

            this.val = val;
            this.next = next;
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
     * Helper Methods
     * **********************************************************************/

    static boolean validNode(
            Node node) {

        return node != null;
    }

    static boolean validLinkedListNode(
            LinkedListNode node) {

        return node != null;
    }

    static Node node(
            int val) {

        return new Node(val);
    }

    static Node node(
            int val,
            Node left,
            Node right) {

        return new Node(
                left,
                right,
                val);
    }

    static LinkedListNode linkedListNode(
            int val) {

        return new LinkedListNode(val);
    }

    static LinkedListNode linkedListNode(
            int val,
            LinkedListNode next) {

        return new LinkedListNode(
                val,
                next);
    }

    /* **********************************************************************
     * 1. ArrayList Recursion
     * **********************************************************************/

    /**
     * Convert a sorted linked list to a balanced BST by first copying all
     * values into an ArrayList.
     *
     * An empty list is considered invalid for this API.
     */
    static Result linkedListToBSTRecursionArray(
            LinkedListNode head) {

        if (!validLinkedListNode(head)) {
            return new Result(
                    null,
                    false);
        }

        ArrayList<Integer> array =
                new ArrayList<>();

        LinkedListNode current =
                head;

        while (current != null) {
            array.add(current.val);
            current = current.next;
        }

        Node root =
                buildTreeRecursionArray(
                        array,
                        0,
                        array.size() - 1);

        return new Result(
                root,
                validNode(root));
    }

    /**
     * Recursively build a balanced BST from the values in array[start..end].
     */
    static Node buildTreeRecursionArray(
            ArrayList<Integer> array,
            int start,
            int end) {

        if (start > end) {
            return null;
        }

        int middle =
                start + (end - start) / 2;

        Node left =
                buildTreeRecursionArray(
                        array,
                        start,
                        middle - 1);

        Node right =
                buildTreeRecursionArray(
                        array,
                        middle + 1,
                        end);

        return node(
                array.get(middle),
                left,
                right);
    }

    /* **********************************************************************
     * 2. Direct In-order Traversal
     * **********************************************************************/

    /**
     * Convert a sorted linked list directly into a balanced BST.
     *
     * The list is not copied into an array. Instead, the tree is built in
     * in-order sequence while the shared list reference advances.
     */
    static Result linkedListToBSTInOrderTraversal(
            LinkedListNode head) {

        if (!validLinkedListNode(head)) {
            return new Result(
                    null,
                    false);
        }

        int n =
                countNodes(head);

        LinkedListNode[] headRef =
                new LinkedListNode[] {
                        head
                };

        Node root =
                buildTreeInOrderTraversal(
                        headRef,
                        n);

        return new Result(
                root,
                validNode(root));
    }

    /**
     * Count the number of nodes in the linked list.
     */
    static int countNodes(
            LinkedListNode head) {

        int count = 0;

        LinkedListNode current =
                head;

        while (current != null) {
            count++;
            current = current.next;
        }

        return count;
    }

    /**
     * Build a balanced BST using in-order traversal.
     *
     * The recursive call constructs the left subtree first. The current
     * linked-list node then becomes the root. Finally, the right subtree
     * is constructed.
     */
    static Node buildTreeInOrderTraversal(
            LinkedListNode[] headRef,
            int n) {

        if (n <= 0) {
            return null;
        }

        int leftSize =
                n / 2;

        Node left =
                buildTreeInOrderTraversal(
                        headRef,
                        leftSize);

        if (headRef[0] == null) {
            return null;
        }

        Node root =
                node(headRef[0].val);

        root.left =
                left;

        headRef[0] =
                headRef[0].next;

        root.right =
                buildTreeInOrderTraversal(
                        headRef,
                        n - leftSize - 1);

        return root;
    }

    /* **********************************************************************
     * Tree Traversal and Validation
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

        values.add(root.val);

        inOrder(
                root.right,
                values);
    }

    static ArrayList<Integer> inOrderValues(
            Node root) {

        ArrayList<Integer> values =
                new ArrayList<>();

        inOrder(
                root,
                values);

        return values;
    }

    static ArrayList<Integer> linkedListValues(
            LinkedListNode head) {

        ArrayList<Integer> values =
                new ArrayList<>();

        LinkedListNode current =
                head;

        while (current != null) {
            values.add(current.val);
            current = current.next;
        }

        return values;
    }

    static int height(
            Node root) {

        if (root == null) {
            return 0;
        }

        return 1 + Math.max(
                height(root.left),
                height(root.right));
    }

    static boolean isHeightBalanced(
            Node root) {

        return balanceHeight(root) >= 0;
    }

    /**
     * Returns the height if balanced, or -1 if unbalanced.
     */
    static int balanceHeight(
            Node root) {

        if (root == null) {
            return 0;
        }

        int leftHeight =
                balanceHeight(root.left);

        if (leftHeight < 0) {
            return -1;
        }

        int rightHeight =
                balanceHeight(root.right);

        if (rightHeight < 0) {
            return -1;
        }

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }

        return 1 + Math.max(
                leftHeight,
                rightHeight);
    }

    static boolean isSorted(
            ArrayList<Integer> values) {

        for (int i = 1;
             i < values.size();
             i++) {

            if (values.get(i - 1)
                    > values.get(i)) {

                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * Test Infrastructure
     * **********************************************************************/

    @FunctionalInterface
    interface Algorithm {

        Result solve(
                LinkedListNode head);
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

    static class TestCase {

        final String id;
        final LinkedListNode head;
        final List<Integer> expected;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                LinkedListNode head,
                List<Integer> expected,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.head = head;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    static class TestOutcome {

        final boolean passed;
        final String message;

        TestOutcome(
                boolean passed,
                String message) {

            this.passed = passed;
            this.message = message;
        }
    }

    /* **********************************************************************
     * Test Runner
     * **********************************************************************/

    static TestOutcome evaluate(
            MethodCase method,
            TestCase test) {

        try {
            Result actual =
                    method.algorithm.solve(test.head);

            if (actual.valid()
                    != test.expectedValid) {

                return new TestOutcome(
                        false,
                        "validity mismatch: expected "
                                + test.expectedValid
                                + ", actual "
                                + actual.valid());
            }

            if (!test.expectedValid) {
                return new TestOutcome(
                        true,
                        "invalid input rejected");
            }

            if (!validNode(actual.root())) {
                return new TestOutcome(
                        false,
                        "valid input produced a null root");
            }

            ArrayList<Integer> actualValues =
                    inOrderValues(actual.root());

            if (!actualValues.equals(test.expected)) {
                return new TestOutcome(
                        false,
                        "in-order values: expected "
                                + test.expected
                                + ", actual "
                                + actualValues);
            }

            if (!isSorted(actualValues)) {
                return new TestOutcome(
                        false,
                        "tree in-order traversal is not sorted");
            }

            if (!isHeightBalanced(actual.root())) {
                return new TestOutcome(
                        false,
                        "resulting tree is not height-balanced");
            }

            return new TestOutcome(
                    true,
                    "in-order values = " + actualValues);

        } catch (Exception ex) {
            return new TestOutcome(
                    false,
                    "exception: " + ex);
        }
    }

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
            TestOutcome outcome =
                    evaluate(
                            method,
                            test);

            if (outcome.passed) {
                passed++;

                System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  " + outcome.message);

            } else {
                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  " + outcome.message);
            }
        }

        printResults(
                passed,
                failed,
                tests.size());
    }

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
                Result first =
                        methods.get(0)
                                .algorithm
                                .solve(test.head);

                ArrayList<Integer> firstValues =
                        inOrderValues(first.root());

                boolean success =
                        first.valid()
                                == test.expectedValid;

                for (int i = 1;
                     i < methods.size();
                     i++) {

                    Result actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(test.head);

                    ArrayList<Integer> actualValues =
                            inOrderValues(actual.root());

                    if (actual.valid()
                            != first.valid()
                            || !actualValues.equals(firstValues)) {

                        success = false;

                        System.out.printf(
                                "  %s differs: %s%n",
                                methods.get(i).name,
                                actualValues);
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

        printResults(
                passed,
                failed,
                tests.size());
    }

    static void runInvalidInputTests(
            List<MethodCase> methods) {

        printSeparator();

        System.out.println(
                "Invalid Input Tests");

        printSeparator();

        int passed = 0;
        int failed = 0;

        for (MethodCase method : methods) {
            Result result =
                    method.algorithm.solve(null);

            if (!result.valid()
                    && result.root() == null) {

                passed++;

                System.out.printf(
                        "PASS null input - %s%n",
                        method.name);

            } else {
                failed++;

                System.out.printf(
                        "FAIL null input - %s%n",
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

        LinkedListNode empty =
                null;

        tests.add(
                new TestCase(
                        "I1",
                        empty,
                        List.of(),
                        false,
                        "null linked list"));

        LinkedListNode one =
                linkedListNode(10);

        tests.add(
                new TestCase(
                        "B1",
                        one,
                        List.of(10),
                        true,
                        "single-node list"));

        LinkedListNode three =
                linkedListNode(
                        1,
                        linkedListNode(
                                2,
                                linkedListNode(3)));

        tests.add(
                new TestCase(
                        "B2",
                        three,
                        List.of(1, 2, 3),
                        true,
                        "three-node list"));

        LinkedListNode seven =
                linkedListNode(
                        1,
                        linkedListNode(
                                2,
                                linkedListNode(
                                        3,
                                        linkedListNode(
                                                4,
                                                linkedListNode(
                                                        5,
                                                        linkedListNode(
                                                                6,
                                                                linkedListNode(
                                                                        7)))))));

        tests.add(
                new TestCase(
                        "B3",
                        seven,
                        List.of(
                                1,
                                2,
                                3,
                                4,
                                5,
                                6,
                                7),
                        true,
                        "seven-node list"));

        LinkedListNode negative =
                linkedListNode(
                        -20,
                        linkedListNode(
                                -10,
                                linkedListNode(
                                        -5,
                                        linkedListNode(
                                                -1))));

        tests.add(
                new TestCase(
                        "B4",
                        negative,
                        List.of(
                                -20,
                                -10,
                                -5,
                                -1),
                        true,
                        "negative values"));

        LinkedListNode duplicates =
                linkedListNode(
                        2,
                        linkedListNode(
                                2,
                                linkedListNode(
                                        2,
                                        linkedListNode(3))));

        tests.add(
                new TestCase(
                        "B5",
                        duplicates,
                        List.of(
                                2,
                                2,
                                2,
                                3),
                        true,
                        "duplicate values"));

        LinkedListNode even =
                linkedListNode(
                        1,
                        linkedListNode(
                                2,
                                linkedListNode(
                                        3,
                                        linkedListNode(4))));

        tests.add(
                new TestCase(
                        "B6",
                        even,
                        List.of(
                                1,
                                2,
                                3,
                                4),
                        true,
                        "even number of nodes"));

        return tests;
    }

    /* **********************************************************************
     * Randomised Test Data
     * **********************************************************************/

    static LinkedListNode buildLinkedList(
            List<Integer> values) {

        LinkedListNode head =
                null;

        for (int i = values.size() - 1;
             i >= 0;
             i--) {

            head =
                    linkedListNode(
                            values.get(i),
                            head);
        }

        return head;
    }

    static void runRandomisedTests(
            List<MethodCase> methods,
            int iterations) {

        printSeparator();

        System.out.println(
                "Randomised Cross Checks");

        printSeparator();

        Random random =
                new Random(20261009L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                    1 + random.nextInt(100);

            ArrayList<Integer> values =
                    new ArrayList<>();

            int current =
                    random.nextInt(11) - 20;

            for (int i = 0;
                 i < size;
                 i++) {

                current +=
                        random.nextInt(4);

                values.add(current);
            }

            LinkedListNode head =
                    buildLinkedList(values);

            for (MethodCase method : methods) {
                Result result =
                        method.algorithm.solve(head);

                if (!result.valid()
                        || !inOrderValues(result.root())
                                .equals(values)
                        || !isHeightBalanced(result.root())) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = " + iteration);

                    System.out.println(
                            "algorithm = " + method.name);

                    System.out.println(
                            "expected = " + values);

                    System.out.println(
                            "actual = "
                                    + inOrderValues(result.root()));

                    System.out.println(
                            "valid = " + result.valid());

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
                "##############  LINKED LIST TO BST  ########################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursion with ArrayList",
                                LinkedListToBST
                                        ::linkedListToBSTRecursionArray),

                        new MethodCase(
                                "Direct In-order Traversal",
                                LinkedListToBST
                                        ::linkedListToBSTInOrderTraversal)
                );

        List<TestCase> tests =
                buildTests();

        for (MethodCase method : methods) {
            runTests(
                    method,
                    tests);
        }

        runCrossCheckTests(
                methods,
                tests);

        runInvalidInputTests(
                methods);

        runRandomisedTests(
                methods,
                5000);
    }
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class CloneLinkedList {

    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {
        int val;
        Node random;
        Node next;

        Node(int val) {
            this.val = val;
            this.random = null;
            this.next = null;
        }

        Node(int val, Node next) {
            this.val = val;
            this.random = null;
            this.next = next;
        }

        Node(
            int val,
            Node random,
            Node next
        ) {
            this.val = val;
            this.random = random;
            this.next = next;
        }
    }

    /* **********************************************************************
     * Result
     * **********************************************************************/

    static record Result(
        Node head,
        boolean valid
    ) {}

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validNode(Node node) {
        return node != null;
    }

    static Node node(int val) {
        return new Node(val);
    }

    static Node node(int val, Node next) {
        return new Node(val, next);
    }

    static Node node(
        int val,
        Node random,
        Node next
    ) {
        return new Node(val, random, next);
    }

    /**
     * Hashing solution.
     *
     * Pass 1: create every clone and map original -> clone.
     * Pass 2: connect clone.next and clone.random using the map.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result cloneLinkedListHashing(Node head) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        Map<Node, Node> originalToClone =
            new HashMap<>();

        Node current = head;

        while (current != null) {
            originalToClone.put(
                current,
                node(current.val)
            );

            current = current.next;
        }

        current = head;

        while (current != null) {
            Node clone =
                originalToClone.get(current);

            clone.next =
                current.next == null
                    ? null
                    : originalToClone.get(current.next);

            clone.random =
                current.random == null
                    ? null
                    : originalToClone.get(current.random);

            current = current.next;
        }

        return new Result(
            originalToClone.get(head),
            true
        );
    }

    /**
     * In-place interleaving solution.
     *
     * Pass 1: insert each clone immediately after its original.
     * Pass 2: connect clone random pointers.
     * Pass 3: restore the original list and extract the clone.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result cloneLinkedListInPlace(Node head) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        Node current = head;

        /*
         * A -> A' -> B -> B' -> C -> C'
         */
        while (current != null) {
            Node clone = node(current.val);

            clone.next = current.next;
            current.next = clone;

            current = clone.next;
        }

        current = head;

        while (current != null) {
            Node clone = current.next;

            clone.random =
                current.random == null
                    ? null
                    : current.random.next;

            current = clone.next;
        }

        Node originalHead = head;
        Node cloneHead = head.next;

        Node originalCurrent = originalHead;
        Node cloneCurrent = cloneHead;

        while (cloneCurrent.next != null) {
            originalCurrent.next =
                cloneCurrent.next;

            originalCurrent =
                originalCurrent.next;

            cloneCurrent.next =
                originalCurrent.next;

            cloneCurrent =
                cloneCurrent.next;
        }

        originalCurrent.next = null;
        cloneCurrent.next = null;

        return new Result(cloneHead, true);
    }

    /* **********************************************************************
     * List Helpers
     * **********************************************************************/

    static Node buildList(int... values) {
        Node head = null;
        Node tail = null;

        for (int value : values) {
            Node current = node(value);

            if (head == null) {
                head = current;
                tail = current;
            } else {
                tail.next = current;
                tail = current;
            }
        }

        return head;
    }

    static ArrayList<Node> toNodes(Node head) {
        ArrayList<Node> nodes =
            new ArrayList<>();

        Node current = head;

        while (current != null) {
            nodes.add(current);
            current = current.next;
        }

        return nodes;
    }

    static boolean hasCycle(Node head) {
        Node slow = head;
        Node fast = head;

        while (
            fast != null
            && fast.next != null
        ) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return true;
            }
        }

        return false;
    }

    static boolean isDeepClone(
        Node original,
        Node clone
    ) {
        if (
            original == null
            || clone == null
        ) {
            return original == clone;
        }

        ArrayList<Node> originalNodes =
            toNodes(original);

        ArrayList<Node> cloneNodes =
            toNodes(clone);

        if (
            originalNodes.size()
            != cloneNodes.size()
        ) {
            return false;
        }

        Map<Node, Node> expectedMapping =
            new HashMap<>();

        for (
            int i = 0;
            i < originalNodes.size();
            i++
        ) {
            Node originalNode =
                originalNodes.get(i);

            Node cloneNode =
                cloneNodes.get(i);

            if (originalNode == cloneNode) {
                return false;
            }

            if (
                originalNode.val
                != cloneNode.val
            ) {
                return false;
            }

            expectedMapping.put(
                originalNode,
                cloneNode
            );
        }

        for (
            int i = 0;
            i < originalNodes.size();
            i++
        ) {
            Node originalNode =
                originalNodes.get(i);

            Node cloneNode =
                cloneNodes.get(i);

            if (
                cloneNode.next
                != expectedMapping.get(
                    originalNode.next
                )
            ) {
                return false;
            }

            if (
                cloneNode.random
                != expectedMapping.get(
                    originalNode.random
                )
            ) {
                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final Node head;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            Node head,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.head = head;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(Node head);
    }

    static class MethodCase {
        final String name;
        final Algorithm algorithm;

        MethodCase(
            String name,
            Algorithm algorithm
        ) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static void runTests(
        MethodCase method,
        List<TestCase> tests
    ) {
        printSeparator();
        System.out.println(method.name);
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual =
                    method.algorithm.solve(
                        test.head
                    );

                boolean cloneCorrect =
                    isDeepClone(
                        test.head,
                        actual.head()
                    );

                if (
                    actual.valid()
                        == test.expectedValid
                    && cloneCorrect
                ) {
                    passed++;

                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );
                } else {
                    failed++;

                    System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  expected valid = "
                            + test.expectedValid
                    );

                    System.out.println(
                        "  actual valid   = "
                            + actual.valid()
                    );

                    System.out.println(
                        "  clone correct  = "
                            + cloneCorrect
                    );
                }
            } catch (Exception ex) {
                failed++;

                System.out.printf(
                    "FAIL %s (%s)%n",
                    test.id,
                    test.description
                );

                System.out.println(
                    "  exception = "
                        + ex
                );
            }
        }

        printResults(
            passed,
            failed,
            tests.size()
        );
    }

    static void runCrossCheckTests(
        List<MethodCase> methods,
        List<TestCase> tests
    ) {
        printSeparator();
        System.out.println("Cross-Algorithm Tests");
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            boolean success = true;

            for (MethodCase method : methods) {
                Result result =
                    method.algorithm.solve(
                        test.head
                    );

                boolean cloneCorrect =
                    isDeepClone(
                        test.head,
                        result.head()
                    );

                if (
                    result.valid()
                        != test.expectedValid
                    || !cloneCorrect
                ) {
                    success = false;
                }
            }

            if (success) {
                passed++;

                System.out.printf(
                    "PASS %s (%s)%n",
                    test.id,
                    test.description
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL %s (%s)%n",
                    test.id,
                    test.description
                );
            }
        }

        printResults(
            passed,
            failed,
            tests.size()
        );
    }

    static void runInvalidInputTests(
        List<MethodCase> methods
    ) {
        printSeparator();
        System.out.println("Invalid Input Tests");
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (MethodCase method : methods) {
            Result result =
                method.algorithm.solve(null);

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I1 - %s rejects null head%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I1 - %s accepts null head%n",
                    method.name
                );
            }
        }

        printResults(
            passed,
            failed,
            passed + failed
        );
    }

    /* **********************************************************************
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {
        List<TestCase> tests =
            new ArrayList<>();

        /*
         * List: A -> B -> C
         *
         * A.random = C
         * B.random = A
         * C.random = B
         */
        Node a = node(1);
        Node b = node(2);
        Node c = node(3);

        a.next = b;
        b.next = c;

        a.random = c;
        b.random = a;
        c.random = b;

        tests.add(
            new TestCase(
                "B1",
                a,
                true,
                "cross-list random pointers"
            )
        );

        /*
         * Single node pointing to itself.
         */
        Node self = node(10);
        self.random = self;

        tests.add(
            new TestCase(
                "S1",
                self,
                true,
                "single-node self-random pointer"
            )
        );

        /*
         * All random pointers are null.
         */
        Node plain = buildList(1, 2, 3);

        tests.add(
            new TestCase(
                "S2",
                plain,
                true,
                "all random pointers null"
            )
        );

        /*
         * Mixed ancestor, descendant, and self random pointers.
         */
        Node n1 = node(10);
        Node n2 = node(20);
        Node n3 = node(30);
        Node n4 = node(40);

        n1.next = n2;
        n2.next = n3;
        n3.next = n4;

        n1.random = n3;
        n2.random = n1;
        n3.random = n3;
        n4.random = n2;

        tests.add(
            new TestCase(
                "S3",
                n1,
                true,
                "mixed random-pointer structure"
            )
        );

        /*
         * Duplicate values must still clone by identity.
         */
        Node d1 = node(7);
        Node d2 = node(7);
        Node d3 = node(7);

        d1.next = d2;
        d2.next = d3;

        d1.random = d2;
        d2.random = d3;
        d3.random = d1;

        tests.add(
            new TestCase(
                "D1",
                d1,
                true,
                "duplicate values with identity pointers"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                null,
                false,
                "null list"
            )
        );

        return tests;
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
        List<MethodCase> methods,
        int iterations
    ) {
        printSeparator();
        System.out.println("Randomised Cross Checks");
        printSeparator();

        Random random =
            new Random(20261026L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int size =
                random.nextInt(50) + 1;

            Node[] nodes =
                new Node[size];

            for (int i = 0; i < size; i++) {
                nodes[i] =
                    node(
                        random.nextInt(100)
                    );

                if (i > 0) {
                    nodes[i - 1].next =
                        nodes[i];
                }
            }

            for (Node current : nodes) {
                if (random.nextBoolean()) {
                    current.random =
                        nodes[
                            random.nextInt(size)
                        ];
                }
            }

            Node head = nodes[0];

            for (MethodCase method : methods) {
                Result result =
                    method.algorithm.solve(head);

                boolean cloneCorrect =
                    isDeepClone(
                        head,
                        result.head()
                    );

                if (
                    !result.valid()
                    || !cloneCorrect
                ) {
                    System.out.println(
                        "Randomised test FAILED"
                    );

                    System.out.println(
                        "iteration = "
                            + iteration
                    );

                    System.out.println(
                        "algorithm = "
                            + method.name
                    );

                    System.out.println(
                        "size = "
                            + size
                    );

                    System.out.println(
                        "valid = "
                            + result.valid()
                    );

                    System.out.println(
                        "cloneCorrect = "
                            + cloneCorrect
                    );

                    return;
                }
            }
        }

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
    }

    /* **********************************************************************
     * Output Helpers
     * **********************************************************************/

    static void printSeparator() {
        System.out.println(
            "============================================================"
        );
    }

    static void printResults(
        int passed,
        int failed,
        int total
    ) {
        System.out.println();

        System.out.printf(
            "Results: %d passed, %d failed, %d total%n",
            passed,
            failed,
            total
        );

        System.out.println();
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {
        System.out.println(
            "############################################################"
        );

        System.out.println(
            "################## CLONE LINKED LIST #######################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "Hashing",
                    CloneLinkedList
                        ::cloneLinkedListHashing
                ),
                new MethodCase(
                    "In-Place Interleaving",
                    CloneLinkedList
                        ::cloneLinkedListInPlace
                )
            );

        List<TestCase> tests =
            buildTests();

        for (MethodCase method : methods) {
            runTests(method, tests);
        }

        runCrossCheckTests(methods, tests);
        runInvalidInputTests(methods);
        runRandomisedTests(methods, 5000);
    }
}

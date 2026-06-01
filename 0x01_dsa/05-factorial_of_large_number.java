#!/usr/bin/env jshell

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class FactorialOfALargeNumber {

    // CONSTANTS
    private static final int ARRAY_SIZE = 500;

    // Test case record
    static class TestCase {
        final String id;
        final int num;
        final String expected;

        TestCase(String id, int num, String expected) {
            this.id = id;
            this.num = num;
            this.expected = expected;
        }
    }

    /**
     * Array-based approach: O(n × d) time where d = number of digits
     * Store digits in an array and multiply manually
     */
    static int[] factorialOfALargeNumberLongInt(int num) {
        if (num == 0 || num == 1) {
            return new int[]{1};
        }

        int[] result = new int[ARRAY_SIZE];
        result[0] = 1;
        int resultSize = 1;

        for (int i = 2; i <= num; i++) {
            resultSize = multiplyLongInt(i, result, resultSize);
        }

        return Arrays.copyOf(result, resultSize);
    }

    /**
     * BigInteger approach: O(n × d) time
     * Use Java's built-in BigInteger for arbitrary precision
     */
    static BigInteger factorialOfALargeNumberBigInt(int num) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= num; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }

        return result;
    }

    /**
     * Linked list approach: O(n × d) time
     * Store digits in a linked list (one digit per node)
     */
    static Node factorialOfALargeNumberLinkedList(int num) {
        if (num == 0 || num == 1) {
            return new Node(1);
        }

        // Start with 1
        Node head = new Node(1);
        
        // Multiply by each number from 2 to num
        for (int i = 2; i <= num; i++) {
            head = multiplyLinkedList(head, i);
        }

        return head;
    }

    /**
     * Multiply a linked list number by x
     */
    static Node multiplyLinkedList(Node head, int x) {
        Node current = head;
        int carry = 0;

        // Multiply each digit by x
        while (current != null) {
            int prod = current.val * x + carry;
            current.val = prod % 10;
            carry = prod / 10;
            current = current.next;
        }

        // Add remaining carry as new nodes
        while (carry != 0) {
            Node newNode = new Node(carry % 10);
            // Find the last node
            Node last = head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = newNode;
            carry /= 10;
        }

        return head;
    }

    /**
     * Multiply array-based number by x and return new size
     */
    static int multiplyLongInt(int x, int[] result, int resultSize) {
        int carry = 0;
        for (int i = 0; i < resultSize; i++) {
            int prod = result[i] * x + carry;
            result[i] = prod % 10;
            carry = prod / 10;
        }

        while (carry != 0) {
            result[resultSize] = carry % 10;
            carry /= 10;
            resultSize++;
        }

        return resultSize;
    }

    /**
     * Convert array-based result to string (digits stored in reverse order)
     */
    static String arrayResultToString(int[] result) {
        StringBuilder sb = new StringBuilder();
        for (int i = result.length - 1; i >= 0; i--) {
            sb.append(result[i]);
        }
        return sb.toString();
    }

    /**
     * Convert linked list result to string (digits stored in reverse order)
     */
    static String linkedListToString(Node head) {
        if (head == null) return "";
        
        // Build list of digits
        List<Integer> digits = new ArrayList<>();
        Node current = head;
        while (current != null) {
            digits.add(current.val);
            current = current.next;
        }
        
        // Reverse to get correct order
        StringBuilder sb = new StringBuilder();
        for (int i = digits.size() - 1; i >= 0; i--) {
            sb.append(digits.get(i));
        }
        return sb.toString();
    }

    /**
     * Runs a named approach against all test cases
     */
    static void runTests(String name, java.util.function.Function<Integer, String> func,
                         List<TestCase> tests) {
        System.out.println("========================= method: " + name + " =========================");
        for (TestCase test : tests) {
            String got = func.apply(test.num);
            boolean passed = got.equals(test.expected);
            String gotDisplay = got.length() > 50 ? got.substring(0, 50) + "..." : got;
            String expectedDisplay = test.expected.length() > 50 ? test.expected.substring(0, 50) + "..." : test.expected;
            System.out.printf(
                "Test %s: num=%d, got=%s, expected=%s, passed=%b%n",
                test.id,
                test.num,
                gotDisplay,
                expectedDisplay,
                passed
            );
        }
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // Test 1: Small factorial
        tests.add(new TestCase("1", 5, "120"));

        // Test 2: Factorial of 0
        tests.add(new TestCase("2", 0, "1"));

        // Test 3: Factorial of 1
        tests.add(new TestCase("3", 1, "1"));

        // Test 4: Factorial of 10
        tests.add(new TestCase("4", 10, "3628800"));

        // Test 5: Factorial of 20
        tests.add(new TestCase("5", 20, "2432902008176640000"));

        // Test 6: Factorial of 50 (large number)
        tests.add(new TestCase(
            "6",
            50,
            "30414093201713378043612608166064768844377641568960512000000000000"
        ));

        System.out.println("========================= Factorial of a Large Number Problem =========================");

        // Test BigInteger approach (most reliable)
        runTests(
            "BigInteger",
            FactorialOfALargeNumber::factorialOfALargeNumberBigInt,
            tests
        );

        // Test array-based approach
        runTests(
            "array-based (long int)",
            (num) -> {
                if (num == 0 || num == 1) {
                    return "1";
                }
                int[] result = factorialOfALargeNumberLongInt(num);
                return arrayResultToString(result);
            },
            tests
        );

        // Test linked list approach
        runTests(
            "linked list",
            (num) -> {
                if (num == 0 || num == 1) {
                    return "1";
                }
                Node result = factorialOfALargeNumberLinkedList(num);
                return linkedListToString(result);
            },
            tests
        );
    }
}

/**
 * Node class for linked list representation
 */
class Node {
    public int val;
    public Node next;

    public Node(int val) {
        this.val = val;
        this.next = null;
    }
}
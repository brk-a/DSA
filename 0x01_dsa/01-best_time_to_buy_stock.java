// #!/usr/bin/env jshell

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class BestTimeToBuyStock {

    // Test case record
    static class TestCase {
        final String id;
        final int[] prices;
        final int expected;

        TestCase(String id, int[] prices, int expected) {
            this.id = id;
            this.prices = prices.clone();
            this.expected = expected;
        }
    }

    /**
     * Brute force approach: O(n²) time, O(1) space
     * Check every pair of buy/sell days
     */
    static int bestTimeToBuyStockBruteForce(int[] prices) {
        int n = prices.length;
        int result = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                result = Math.max(result, prices[j] - prices[i]);
            }
        }

        return result;
    }

    /**
     * One-pass optimised approach: O(n) time, O(1) space
     * Track minimum price so far and max profit at each step
     */
    static int bestTimeToBuyStockOnePass(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }

        int n = prices.length;
        int result = 0;
        int minSoFar = prices[0];

        for (int i = 0; i < n; i++) {
            minSoFar = Math.min(minSoFar, prices[i]);
            result = Math.max(result, prices[i] - minSoFar);
        }

        return result;
    }

    /**
     * Runs a named approach against all test cases
     */
    static void runTests(String name, java.util.function.Function<int[], Integer> func,
                         List<TestCase> tests) {
        System.out.println("========================= method: " + name + " =========================");
        for (TestCase test : tests) {
            int got = func.apply(test.prices);
            boolean passed = got == test.expected;
            System.out.printf(
                "Test %s: prices=%s, got=%d, expected=%d, passed=%b%n",
                test.id,
                Arrays.toString(test.prices),
                got,
                test.expected,
                passed
            );
        }
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // Test 1: Classic case with profit
        tests.add(new TestCase(
            "1",
            new int[]{7, 1, 5, 3, 6, 4},
            5  // buy at 1, sell at 6
        ));

        // Test 2: No profit possible (prices always decline)
        tests.add(new TestCase(
            "2",
            new int[]{7, 6, 4, 3, 1},
            0  // no transaction
        ));

        // Test 3: Single element
        tests.add(new TestCase(
            "3",
            new int[]{5},
            0  // can't sell
        ));

        // Test 4: Two elements with profit
        tests.add(new TestCase(
            "4",
            new int[]{1, 2},
            1  // buy at 1, sell at 2
        ));

        // Test 5: Two elements with loss
        tests.add(new TestCase(
            "5",
            new int[]{2, 1},
            0  // no transaction
        ));

        // Test 6: Multiple opportunities, pick best
        tests.add(new TestCase(
            "6",
            new int[]{3, 3, 5, 0, 0, 3, 1, 4},
            4  // buy at 0, sell at 4
        ));

        // Test 7: Empty array
        tests.add(new TestCase(
            "7",
            new int[]{},
            0
        ));

        System.out.println("========================= Best Time to Buy and Sell Stock Problem =========================");

        runTests(
            "brute force",
            BestTimeToBuyStock::bestTimeToBuyStockBruteForce,
            tests
        );

        runTests(
            "one pass",
            BestTimeToBuyStock::bestTimeToBuyStockOnePass,
            tests
        );
    }
}

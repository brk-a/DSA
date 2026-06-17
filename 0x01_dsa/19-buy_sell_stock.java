import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

public class BuySellStockMultiple {

    static class TestCase {

        final String id;
        final int[] prices;
        final int expected;
        final String description;

        TestCase(
            String id,
            int[] prices,
            int expected,
            String description
        ) {
            this.id = id;
            this.prices = prices;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Approach 1
     *
     * Recursive brute force.
     *
     * Time: Exponential
     * Space: O(n)
     */
    static int maxProfitBruteForce(int[] prices) {

        if (prices == null || prices.length < 2) {
            return 0;
        }

        return maxProfitBruteForceHelper(
            prices,
            0,
            prices.length - 1
        );
    }

    static int maxProfitBruteForceHelper(
        int[] prices,
        int start,
        int end
    ) {

        int best = 0;

        for (int buy = start; buy <= end; buy++) {

            for (
                int sell = buy + 1;
                sell <= end;
                sell++
            ) {

                if (prices[sell] > prices[buy]) {

                    int profit =
                        prices[sell] - prices[buy]
                        + maxProfitBruteForceHelper(
                            prices,
                            start,
                            buy - 1
                        )
                        + maxProfitBruteForceHelper(
                            prices,
                            sell + 1,
                            end
                        );

                    best = Math.max(best, profit);
                }
            }
        }

        return best;
    }

    /**
     * Approach 2
     *
     * Local minima / maxima.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static int maxProfitLocalMinMax(
        int[] prices
    ) {

        if (prices == null || prices.length < 2) {
            return 0;
        }

        int profit = 0;
        int i = 0;

        while (i < prices.length - 1) {

            while (
                i < prices.length - 1
                && prices[i] >= prices[i + 1]
            ) {
                i++;
            }

            int buy = prices[i];

            while (
                i < prices.length - 1
                && prices[i] <= prices[i + 1]
            ) {
                i++;
            }

            int sell = prices[i];

            profit += sell - buy;
        }

        return profit;
    }

    /**
     * Approach 3 (Optimal)
     *
     * Sum every positive increase.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static int maxProfitAccumulating(
        int[] prices
    ) {

        if (prices == null || prices.length < 2) {
            return 0;
        }

        int profit = 0;

        for (int i = 1; i < prices.length; i++) {

            if (prices[i] > prices[i - 1]) {
                profit += prices[i] - prices[i - 1];
            }
        }

        return profit;
    }

    static void runTests(
        String name,
        ToIntFunction<int[]> method,
        List<TestCase> tests
    ) {

        System.out.println(
            "===================================================="
        );
        System.out.println(name);
        System.out.println(
            "===================================================="
        );

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            int actual =
                method.applyAsInt(test.prices);

            boolean success =
                actual == test.expected;

            if (success) {

                passed++;

                System.out.printf(
                    "✓ %s (%s)%n",
                    test.id,
                    test.description
                );

            } else {

                failed++;

                System.out.printf(
                    "✗ %s (%s)%n",
                    test.id,
                    test.description
                );

                System.out.printf(
                    "  input    = %s%n",
                    Arrays.toString(test.prices)
                );

                System.out.printf(
                    "  expected = %d%n",
                    test.expected
                );

                System.out.printf(
                    "  actual   = %d%n",
                    actual
                );
            }
        }

        System.out.printf(
            "%nResults: %d passed, %d failed, %d total%n%n",
            passed,
            failed,
            tests.size()
        );
    }

    public static void main(String[] args) {

        List<TestCase> tests =
            new ArrayList<>();

        // =================================================
        // Standard Examples
        // =================================================

        tests.add(new TestCase(
            "P1",
            new int[]{100, 180, 260, 310, 40, 535, 695},
            865,
            "Classic example"
        ));

        tests.add(new TestCase(
            "P2",
            new int[]{7, 1, 5, 3, 6, 4},
            7,
            "Two profitable trades"
        ));

        tests.add(new TestCase(
            "P3",
            new int[]{1, 2, 3, 4, 5},
            4,
            "Always increasing"
        ));

        tests.add(new TestCase(
            "P4",
            new int[]{7, 6, 4, 3, 1},
            0,
            "Always decreasing"
        ));

        // =================================================
        // Edge Cases
        // =================================================

        tests.add(new TestCase(
            "E1",
            new int[]{},
            0,
            "Empty array"
        ));

        tests.add(new TestCase(
            "E2",
            null,
            0,
            "Null input"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{5},
            0,
            "Single element"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{5, 10},
            5,
            "Single transaction"
        ));

        tests.add(new TestCase(
            "E5",
            new int[]{10, 5},
            0,
            "No profit"
        ));

        // =================================================
        // Duplicates
        // =================================================

        tests.add(new TestCase(
            "D1",
            new int[]{5, 5, 5, 5},
            0,
            "All duplicates"
        ));

        tests.add(new TestCase(
            "D2",
            new int[]{1, 2, 2, 3},
            2,
            "Duplicates while rising"
        ));

        // =================================================
        // Negatives
        // =================================================

        tests.add(new TestCase(
            "N1",
            new int[]{-3, -1, -5, 2},
            9,
            "Negative values"
        ));

        // =================================================
        // Zeroes
        // =================================================

        tests.add(new TestCase(
            "Z1",
            new int[]{0, 0, 0},
            0,
            "All zeroes"
        ));

        tests.add(new TestCase(
            "Z2",
            new int[]{0, 5, 0, 5},
            10,
            "Multiple gains from zero"
        ));

        // =================================================
        // Integer Limits
        // =================================================

        tests.add(new TestCase(
            "L1",
            new int[]{
                Integer.MIN_VALUE,
                Integer.MAX_VALUE
            },
            -1, // overflow expected with int arithmetic
            "Integer overflow demonstration"
        ));

        System.out.println(
            "########################################################"
        );
        System.out.println(
            "######## BUY & SELL STOCK (MULTIPLE TRADES) ###########"
        );
        System.out.println(
            "########################################################"
        );
        System.out.println();

        runTests(
            "Brute Force",
            BuySellStockMultiple::maxProfitBruteForce,
            tests
        );

        runTests(
            "Local Minima / Maxima",
            BuySellStockMultiple::maxProfitLocalMinMax,
            tests
        );

        runTests(
            "Accumulating Positive Differences",
            BuySellStockMultiple::maxProfitAccumulating,
            tests
        );
    }
}
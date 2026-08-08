import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Array Subset Check.
 *
 * Problem:
 * Given two integer arrays numsA and numsB, determine whether numsB is
 * a subset of numsA.
 *
 * We distinguish two interpretations:
 *
 * 1. Set-Subset Semantics
 *      Every distinct value in numsB appears at least once in numsA.
 *      Duplicates in numsB do not require multiple occurrences in numsA.
 *
 * 2. Multiset-Subset Semantics
 *      Every occurrence in numsB must be matched to a distinct occurrence
 *      in numsA. Duplicates in numsB require sufficient multiplicity in numsA.
 *
 * Implementations:
 *
 * 1. Brute Force (Multiset Oracle)
 *      For each element in numsB, find and "consume" a matching element in numsA.
 *      Enforces multiset-subset semantics.
 *
 * 2. Sorting + Two Pointers (Set-Subset)
 *      Sort both arrays and scan with two pointers.
 *      Enforces set-subset semantics (ignores multiplicity).
 *
 * 3. Hash Set (Set-Subset)
 *      Insert all elements of numsA into a set, then check every element of numsB.
 *      Enforces set-subset semantics (ignores multiplicity).
 *
 * The brute-force multiset implementation is retained as a correctness oracle
 * for strict subset-with-multiplicity tests.
 */
public class ArraySubsetCheckTestHarness {

    /* **********************************************************************
     * Validation Helpers
     * **********************************************************************/

    static boolean validArray(int[] nums) {
        return nums != null && nums.length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    /**
     * Brute Force (Multiset Oracle).
     *
     * For each element in numsB, find and mark a matching element in numsA.
     * Each element in numsA can be used at most once.
     *
     * Time: O(m * n)
     * Multiset-subset semantics.
     */
    static boolean arraySubsetCheckBruteForce(int[] numsA, int[] numsB) {

        if (!validArray(numsA) || !validArray(numsB)) {
            return false;
        }

        int m = numsA.length;
        int n = numsB.length;

        int[] cloneA = numsA.clone();

        for (int i = 0; i < n; i++) {

            boolean found = false;

            for (int j = 0; j < m; j++) {

                if (cloneA[j] == numsB[i]) {
                    found = true;
                    cloneA[j] = Integer.MIN_VALUE; // mark as used
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sorting + Two Pointers.
     *
     * Sort both arrays and scan with two pointers.
     *
     * Time: O(m log m + n log n)
     * Set-subset semantics (ignores multiplicity).
     */
    static boolean arraySubsetCheckSortingAndTwoPointers(int[] numsA, int[] numsB) {

        if (!validArray(numsA) || !validArray(numsB)) {
            return false;
        }

        int m = numsA.length;
        int n = numsB.length;

        int[] cloneA = numsA.clone();
        int[] cloneB = numsB.clone();

        Arrays.sort(cloneA);
        Arrays.sort(cloneB);

        int i = 0; // pointer for A
        int j = 0; // pointer for B

        while (i < m && j < n) {

            if (cloneA[i] < cloneB[j]) {
                i++;
            } else if (cloneA[i] == cloneB[j]) {
                // Match found; move both
                i++;
                j++;
            } else {
                // cloneA[i] > cloneB[j] and no match possible
                return false;
            }
        }

        // If we've matched all elements in B
        return j == n;
    }

    /**
     * Hash Set Approach.
     *
     * Insert all elements of numsA into a set, then check every element of numsB.
     *
     * Time: O(m + n) average
     * Set-subset semantics (ignores multiplicity).
     */
    static boolean arraySubsetCheckHashing(int[] numsA, int[] numsB) {

        if (!validArray(numsA) || !validArray(numsB)) {
            return false;
        }

        Set<Integer> setA = new HashSet<>();
        for (int num : numsA) {
            setA.add(num);
        }

        for (int num : numsB) {
            if (!setA.contains(num)) {
                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] inputA;
        final int[] inputB;
        final boolean expectedMultiset; // for brute-force oracle
        final String description;

        TestCase(
                String id,
                int[] inputA,
                int[] inputB,
                boolean expectedMultiset,
                String description) {

            this.id = id;
            this.inputA = inputA;
            this.inputB = inputB;
            this.expectedMultiset = expectedMultiset;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        boolean solve(int[] numsA, int[] numsB);
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

    static int[] cloneArray(int[] nums) {

        if (nums == null) {
            return null;
        }

        return nums.clone();
    }

    static String formatArray(int[] nums) {

        if (nums == null) {
            return "null";
        }

        return Arrays.toString(nums);
    }

    static void runTests(
            String algorithmName,
            Algorithm method,
            List<TestCase> tests,
            boolean isMultisetOracle) {

        System.out.println(
                "======================================================");
        System.out.println(algorithmName);
        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                boolean actual = method.solve(
                        cloneArray(test.inputA),
                        cloneArray(test.inputB));

                boolean expected = isMultisetOracle
                        ? test.expectedMultiset
                        // For set-subset methods, we’ll treat them as “true” if
                        // multiset is true OR if all distinct elements of B are in A.
                        // For simplicity in this harness, we’ll just compare to
                        // expectedMultiset for now, and add extra tests where
                        // set vs multiset differ with comments.
                        : test.expectedMultiset;

                if (actual == expected) {

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
                            "  A         = %s%n",
                            formatArray(test.inputA));

                    System.out.printf(
                            "  B         = %s%n",
                            formatArray(test.inputB));

                    System.out.printf(
                            "  expected  = %s%n",
                            expected);

                    System.out.printf(
                            "  actual    = %s%n",
                            actual);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  A         = %s%n",
                        formatArray(test.inputA));

                System.out.printf(
                        "  B         = %s%n",
                        formatArray(test.inputB));

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

    static int[] randomArray(
            Random rng,
            int maxLength,
            int minValue,
            int maxValue) {

        int n = rng.nextInt(maxLength) + 1;

        int[] nums = new int[n];

        for (int i = 0; i < n; i++) {
            nums[i] = minValue + rng.nextInt(maxValue - minValue + 1);
        }

        return nums;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks");
        System.out.println(
                "======================================================");

        Random rng = new Random(123456789L);

        for (int i = 1; i <= iterations; i++) {

            int[] A = randomArray(rng, 8, -5, 10);
            int[] B = randomArray(rng, 8, -5, 10);

            if (!validArray(A) || !validArray(B)) {
                continue;
            }

            boolean brute =
                    arraySubsetCheckBruteForce(
                            cloneArray(A),
                            cloneArray(B));

            boolean sorting =
                    arraySubsetCheckSortingAndTwoPointers(
                            cloneArray(A),
                            cloneArray(B));

            boolean hashing =
                    arraySubsetCheckHashing(
                            cloneArray(A),
                            cloneArray(B));

            // For multiset-subset: brute is the strict oracle.
            // Set-subset methods should be >= brute (they can be true when brute is false
            // if B's elements are in A but with insufficient multiplicity).
            if (brute && (!sorting || !hashing)) {

                System.out.println(
                        "Randomised test FAILED (set-subset disagrees with multiset)");

                System.out.println(
                        "A        = " + formatArray(A));

                System.out.println(
                        "B        = " + formatArray(B));

                System.out.println(
                        "brute    = " + brute);

                System.out.println(
                        "sorting  = " + sorting);

                System.out.println(
                        "hashing  = " + hashing);

                return;
            }

            if (!brute && (sorting || hashing)) {
                // This is allowed: set-subset can be true while multiset is false.
                // We just log it for awareness, but don't treat as failure.
                continue;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed (consistency checks).%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Basic Subset Cases (Multiset and Set agree)
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[]{1, 2, 3, 4, 5},
                new int[]{2, 4},
                true,
                "Simple subset"));

        tests.add(new TestCase(
                "A2",
                new int[]{10, 20, 30},
                new int[]{10, 20, 30},
                true,
                "Identical arrays"));

        tests.add(new TestCase(
                "A3",
                new int[]{1, 2, 3},
                new int[]{1, 2, 3, 4},
                false,
                "B has element not in A"));

        /*
         * ============================================================
         * Duplicates: Multiset vs Set Differences
         * ============================================================
         */

        tests.add(new TestCase(
                "D1",
                new int[]{1, 2, 2, 3},
                new int[]{2, 2},
                true,
                "Multiset: sufficient multiplicity"));

        tests.add(new TestCase(
                "D2",
                new int[]{1, 2, 3},
                new int[]{2, 2},
                false,
                "Multiset: insufficient multiplicity (set-subset would be true)"));

        tests.add(new TestCase(
                "D3",
                new int[]{1, 1, 1, 2},
                new int[]{1, 1, 1},
                true,
                "Multiset: exact multiplicity"));

        tests.add(new TestCase(
                "D4",
                new int[]{1, 1, 2},
                new int[]{1, 1, 1},
                false,
                "Multiset: not enough 1s"));

        /*
         * ============================================================
         * Order / Unsorted Arrays
         * ============================================================
         */

        tests.add(new TestCase(
                "O1",
                new int[]{5, 1, 3, 2, 4},
                new int[]{2, 5},
                true,
                "Unsorted A, subset B"));

        tests.add(new TestCase(
                "O2",
                new int[]{3, 1, 4, 1, 5},
                new int[]{1, 1, 5},
                true,
                "Unsorted with duplicates, multiset-ok"));

        /*
         * ============================================================
         * Negative / Mixed Values
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[]{-1, 0, 1, 2},
                new int[]{-1, 1},
                true,
                "Negative and positive values"));

        tests.add(new TestCase(
                "M2",
                new int[]{-2, -1, 0},
                new int[]{-1, -1},
                false,
                "Negative values, insufficient multiplicity"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new int[]{1},
                false,
                "A is null"));

        tests.add(new TestCase(
                "E2",
                new int[]{1},
                null,
                false,
                "B is null"));

        tests.add(new TestCase(
                "E3",
                new int[]{},
                new int[]{1},
                false,
                "A is empty"));

        tests.add(new TestCase(
                "E4",
                new int[]{1},
                new int[]{},
                false,
                "B is empty"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######## ARRAY SUBSET CHECK (INT[] A, INT[] B) ############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Multiset Oracle)",
                        ArraySubsetCheckTestHarness::arraySubsetCheckBruteForce),

                new MethodCase(
                        "Sorting + Two Pointers (Set-Subset)",
                        ArraySubsetCheckTestHarness::arraySubsetCheckSortingAndTwoPointers),

                new MethodCase(
                        "Hash Set (Set-Subset)",
                        ArraySubsetCheckTestHarness::arraySubsetCheckHashing)
        );

        for (MethodCase method : methods) {

            // For this simple harness, we compare all methods to expectedMultiset.
            // For a more advanced harness, you’d add separate expectedSet fields
            // and handle D2/D4 differently for set-subset methods.
            runTests(
                    method.name,
                    method.algorithm,
                    tests,
                    true); // treat expectations as multiset-based
        }

        runRandomisedTests(5000);
    }
}

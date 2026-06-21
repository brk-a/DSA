import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MissingAndRepeatingInArray {

    record Result(
        int repeating,
        int missing
    ) {}

    static class TestCase {

        final String id;
        final int[] input;
        final Result expected;
        final String description;

        TestCase(
            String id,
            int[] input,
            Result expected,
            String description
        ) {
            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Frequency array.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result visitedArray(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return new Result(
                -1,
                -1
            );
        }

        int n = nums.length;

        int[] freq =
            new int[n + 1];

        for (int num : nums) {

            if (
                num < 1 ||
                num > n
            ) {
                throw new IllegalArgumentException(
                    "Values must be in range [1,n]"
                );
            }

            freq[num]++;
        }

        int repeating = -1;
        int missing = -1;

        for (
            int i = 1;
            i <= n;
            i++
        ) {

            if (
                freq[i] == 0
            ) {
                missing = i;
            } else if (
                freq[i] == 2
            ) {
                repeating = i;
            }
        }

        return new Result(
            repeating,
            missing
        );
    }

    /**
     * Array marking.
     *
     * Time: O(n)
     * Space: O(1)
     * (excluding cloned copy)
     */
    static Result arrayMarking(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return new Result(
                -1,
                -1
            );
        }

        int n = nums.length;

        int[] arr =
            nums.clone();

        int repeating = -1;

        for (
            int i = 0;
            i < n;
            i++
        ) {

            int value =
                Math.abs(arr[i]);

            if (
                value < 1 ||
                value > n
            ) {
                throw new IllegalArgumentException(
                    "Values must be in range [1,n]"
                );
            }

            int index =
                value - 1;

            if (
                arr[index] < 0
            ) {
                repeating = value;
            } else {
                arr[index] =
                    -arr[index];
            }
        }

        int missing = -1;

        for (
            int i = 0;
            i < n;
            i++
        ) {

            if (
                arr[i] > 0
            ) {
                missing =
                    i + 1;
                break;
            }
        }

        return new Result(
            repeating,
            missing
        );
    }

    /**
     * Mathematical equations.
     *
     * Time: O(n)
     * Space: O(1)
     *
     * Uses:
     * M - R
     * M² - R²
     *
     * Uses long to avoid overflow.
     */
    static Result mathsEquations(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return new Result(
                -1,
                -1
            );
        }

        long n =
            nums.length;

        long expectedSum =
            n * (n + 1) / 2;

        long expectedSquares =
            n * (n + 1)
            * (2 * n + 1)
            / 6;

        long actualSum = 0;
        long actualSquares = 0;

        for (int num : nums) {

            actualSum += num;

            actualSquares +=
                (long) num * num;
        }

        long diff =
            expectedSum -
            actualSum;

        long squareDiff =
            expectedSquares -
            actualSquares;

        long sumMR =
            squareDiff / diff;

        long missing =
            (diff + sumMR) / 2;

        long repeating =
            missing - diff;

        return new Result(
            (int) repeating,
            (int) missing
        );
    }

    /**
     * XOR partitioning.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result xorMethod(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return new Result(
                -1,
                -1
            );
        }

        int n =
            nums.length;

        int xor = 0;

        for (int num : nums) {
            xor ^= num;
        }

        for (
            int i = 1;
            i <= n;
            i++
        ) {
            xor ^= i;
        }

        int rightMostSetBit =
            xor & -xor;

        int x = 0;
        int y = 0;

        for (int num : nums) {

            if (
                (num & rightMostSetBit)
                    != 0
            ) {
                x ^= num;
            } else {
                y ^= num;
            }
        }

        for (
            int i = 1;
            i <= n;
            i++
        ) {

            if (
                (i & rightMostSetBit)
                    != 0
            ) {
                x ^= i;
            } else {
                y ^= i;
            }
        }

        int countX = 0;

        for (int num : nums) {

            if (num == x) {
                countX++;
            }
        }

        if (countX == 2) {

            return new Result(
                x,
                y
            );
        }

        return new Result(
            y,
            x
        );
    }

    static boolean matchesExpected(
        Result actual,
        Result expected
    ) {

        return
            actual.repeating ==
            expected.repeating
            &&
            actual.missing ==
            expected.missing;
    }

    static void runTests(
        String algorithm,
        Function<int[], Result> method,
        List<TestCase> tests
    ) {

        System.out.println(
            "=================================================="
        );

        System.out.println(
            algorithm
        );

        System.out.println(
            "=================================================="
        );

        int passed = 0;
        int failed = 0;

        for (
            TestCase test
                : tests
        ) {

            try {

                int[] inputCopy =
                    test.input == null
                        ? null
                        : test.input.clone();

                Result actual =
                    method.apply(
                        inputCopy
                    );

                boolean success =
                    matchesExpected(
                        actual,
                        test.expected
                    );

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
                        test.input == null
                            ? "null"
                            : Arrays.toString(
                                test.input
                            )
                    );

                    System.out.printf(
                        "  expected = %s%n",
                        test.expected
                    );

                    System.out.printf(
                        "  actual   = %s%n",
                        actual
                    );
                }

            } catch (
                Exception ex
            ) {

                failed++;

                System.out.printf(
                    "✗ %s (%s)%n",
                    test.id,
                    test.description
                );

                System.out.printf(
                    "  exception = %s%n",
                    ex.getMessage()
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

    public static void main(
        String[] args
    ) {

        List<TestCase> tests =
            new ArrayList<>();

        /*
         * Standard examples
         */

        tests.add(
            new TestCase(
                "S1",
                new int[]{
                    4,3,6,2,1,1
                },
                new Result(
                    1,
                    5
                ),
                "Classic example"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{
                    1,2,2,4
                },
                new Result(
                    2,
                    3
                ),
                "LeetCode/GFG example"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{
                    3,1,3
                },
                new Result(
                    3,
                    2
                ),
                "Missing in middle"
            )
        );

        /*
         * Edge positions
         */

        tests.add(
            new TestCase(
                "E1",
                new int[]{
                    1,1
                },
                new Result(
                    1,
                    2
                ),
                "Missing last"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                new int[]{
                    2,2
                },
                new Result(
                    2,
                    1
                ),
                "Missing first"
            )
        );

        tests.add(
            new TestCase(
                "E3",
                new int[]{
                    2,3,4,5,5
                },
                new Result(
                    5,
                    1
                ),
                "Missing first larger array"
            )
        );

        tests.add(
            new TestCase(
                "E4",
                new int[]{
                    1,1,3,4,5
                },
                new Result(
                    1,
                    2
                ),
                "Missing near beginning"
            )
        );

        tests.add(
            new TestCase(
                "E5",
                new int[]{
                    1,2,3,4,4
                },
                new Result(
                    4,
                    5
                ),
                "Missing last value"
            )
        );

        /*
         * Small arrays
         */

        tests.add(
            new TestCase(
                "SM1",
                new int[]{
                    2,1,2
                },
                new Result(
                    2,
                    3
                ),
                "Length three"
            )
        );

        tests.add(
            new TestCase(
                "SM2",
                new int[]{
                    1,3,3
                },
                new Result(
                    3,
                    2
                ),
                "Length three variant"
            )
        );

        /*
         * Larger examples
         */

        tests.add(
            new TestCase(
                "L1",
                new int[]{
                    7,3,4,5,5,6,2
                },
                new Result(
                    5,
                    1
                ),
                "Missing first"
            )
        );

        tests.add(
            new TestCase(
                "L2",
                new int[]{
                    1,2,3,4,6,6,7,8
                },
                new Result(
                    6,
                    5
                ),
                "Missing middle"
            )
        );

        tests.add(
            new TestCase(
                "L3",
                new int[]{
                    9,1,2,3,4,5,6,7,9
                },
                new Result(
                    9,
                    8
                ),
                "Missing near end"
            )
        );

        /*
         * Empty / null
         */

        tests.add(
            new TestCase(
                "N1",
                null,
                new Result(
                    -1,
                    -1
                ),
                "Null input"
            )
        );

        tests.add(
            new TestCase(
                "N2",
                new int[]{},
                new Result(
                    -1,
                    -1
                ),
                "Empty array"
            )
        );

        System.out.println(
            "############################################################"
        );

        System.out.println(
            "########### MISSING AND REPEATING IN ARRAY ###########"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        runTests(
            "Visited Array O(n) Space",
            MissingAndRepeatingInArray::
                visitedArray,
            tests
        );

        runTests(
            "Array Marking O(1) Space",
            MissingAndRepeatingInArray::
                arrayMarking,
            tests
        );

        runTests(
            "Math Equations O(1) Space",
            MissingAndRepeatingInArray::
                mathsEquations,
            tests
        );

        runTests(
            "XOR Partitioning O(1) Space",
            MissingAndRepeatingInArray::
                xorMethod,
            tests
        );
    }
}

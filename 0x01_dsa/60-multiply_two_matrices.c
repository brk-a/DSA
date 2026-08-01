#include <stdio.h>
#include <stdlib.h>
#include <time.h>

/*
 * ============================================================
 * Matrix Multiplication Core
 * ============================================================
 *
 * Multiplies two matrices:
 *  - matrix1 is r1 x c1
 *  - matrix2 is r2 x c2
 * Requires: c1 == r2
 *
 * Returns:
 *  - Pointer to a newly allocated result matrix (r1 x c2)
 *  - NULL if dimensions are incompatible or allocation fails
 */

int **multiplyTwoMatrices(
        int **matrix1,
        int r1,
        int c1,
        int **matrix2,
        int r2,
        int c2) {

    if (matrix1 == NULL || matrix2 == NULL) {
        return NULL;
    }

    if (c1 != r2) {
        /* Invalid dimensions */
        fprintf(stderr, "Invalid input: c1 (%d) != r2 (%d)\n", c1, r2);
        return NULL;
    }

    /* Allocate result matrix r1 x c2 */
    int **result = malloc(r1 * sizeof(int *));
    if (result == NULL) {
        fprintf(stderr, "Allocation failed for result rows\n");
        return NULL;
    }

    for (int i = 0; i < r1; i++) {
        result[i] = malloc(c2 * sizeof(int));
        if (result[i] == NULL) {
            fprintf(stderr, "Allocation failed for result row %d\n", i);
            /* Clean up already-allocated rows */
            for (int k = 0; k < i; k++) {
                free(result[k]);
            }
            free(result);
            return NULL;
        }
    }

    /* Standard triple loop multiplication */
    for (int i = 0; i < r1; i++) {
        for (int j = 0; j < c2; j++) {

            int sum = 0;

            for (int k = 0; k < c1; k++) {
                sum += matrix1[i][k] * matrix2[k][j];
            }

            result[i][j] = sum;
        }
    }

    return result;
}

/*
 * ============================================================
 * Utilities
 * ============================================================
 */

int **createMatrixFromFlat(const int *flat, int rows, int cols) {

    int **matrix = malloc(rows * sizeof(int *));
    if (matrix == NULL) {
        return NULL;
    }

    for (int i = 0; i < rows; i++) {

        matrix[i] = malloc(cols * sizeof(int));
        if (matrix[i] == NULL) {
            for (int k = 0; k < i; k++) {
                free(matrix[k]);
            }
            free(matrix);
            return NULL;
        }

        for (int j = 0; j < cols; j++) {
            matrix[i][j] = flat[i * cols + j];
        }
    }

    return matrix;
}

void freeMatrix(int **matrix, int rows) {

    if (matrix == NULL) {
        return;
    }

    for (int i = 0; i < rows; i++) {
        free(matrix[i]);
    }

    free(matrix);
}

void printMatrix(int **matrix, int rows, int cols) {

    if (matrix == NULL) {
        printf("NULL matrix\n");
        return;
    }

    for (int i = 0; i < rows; i++) {

        for (int j = 0; j < cols; j++) {
            printf("%d\t", matrix[i][j]);
        }

        printf("\n");
    }
}

int matricesEqual(int **a, int **b, int rows, int cols) {

    if (a == b) {
        return 1;
    }

    if (a == NULL || b == NULL) {
        return 0;
    }

    for (int i = 0; i < rows; i++) {

        for (int j = 0; j < cols; j++) {

            if (a[i][j] != b[i][j]) {
                return 0;
            }
        }
    }

    return 1;
}

int **randomMatrix(int rows, int cols, int minValue, int maxValue) {

    int **matrix = malloc(rows * sizeof(int *));
    if (matrix == NULL) {
        return NULL;
    }

    for (int i = 0; i < rows; i++) {

        matrix[i] = malloc(cols * sizeof(int));
        if (matrix[i] == NULL) {
            for (int k = 0; k < i; k++) {
                free(matrix[k]);
            }
            free(matrix);
            return NULL;
        }

        for (int j = 0; j < cols; j++) {

            int range = maxValue - minValue + 1;
            matrix[i][j] = minValue + rand() % range;
        }
    }

    return matrix;
}

/*
 * ============================================================
 * Test Harness
 * ============================================================
 */

typedef struct {
    const char *id;
    int r1;
    int c1;
    int r2;
    int c2;
    int *flat1;
    int *flat2;
    int **expected;
    const char *description;
} TestCase;

void runTests(const char *label, TestCase *tests, int count) {

    printf("======================================================\n");
    printf("%s\n", label);
    printf("======================================================\n");

    int passed = 0;
    int failed = 0;

    for (int t = 0; t < count; t++) {

        TestCase *test = &tests[t];

        int **m1 = NULL;
        int **m2 = NULL;
        int **actual = NULL;

        /* Prepare inputs */
        if (test->flat1 != NULL && test->flat2 != NULL) {
            m1 = createMatrixFromFlat(test->flat1, test->r1, test->c1);
            m2 = createMatrixFromFlat(test->flat2, test->r2, test->c2);
        }

        actual = multiplyTwoMatrices(m1, test->r1, test->c1, m2, test->r2, test->c2);

        int expectNull = (test->expected == NULL);

        int ok = 0;

        if (expectNull) {
            ok = (actual == NULL);
        } else {
            ok = matricesEqual(actual, test->expected, test->r1, test->c2);
        }

        if (ok) {

            passed++;
            printf("✓ %s (%s)\n", test->id, test->description);

        } else {

            failed++;
            printf("✗ %s (%s)\n", test->id, test->description);

            printf("  r1,c1    = %d,%d\n", test->r1, test->c1);
            printf("  r2,c2    = %d,%d\n", test->r2, test->c2);

            if (m1 != NULL) {
                printf("  matrix1:\n");
                printMatrix(m1, test->r1, test->c1);
            } else {
                printf("  matrix1: NULL\n");
            }

            if (m2 != NULL) {
                printf("  matrix2:\n");
                printMatrix(m2, test->r2, test->c2);
            } else {
                printf("  matrix2: NULL\n");
            }

            printf("  expected:\n");
            if (test->expected != NULL) {
                printMatrix(test->expected, test->r1, test->c2);
            } else {
                printf("NULL\n");
            }

            printf("  actual:\n");
            printMatrix(actual, test->r1, test->c2);
        }

        freeMatrix(m1, test->r1);
        freeMatrix(m2, test->r2);
        freeMatrix(actual, test->r1);
    }

    printf("\n");
    printf("Results: %d passed, %d failed, %d total\n", passed, failed, count);
    printf("\n");
}

/*
 * ============================================================
 * Randomised Cross Checks
 * ============================================================
 *
 * We’ll use the same multiplyTwoMatrices as both “oracle” and
 * implementation, but the randomised tests help detect crashes,
 * dimension mismatches, and allocation issues.
 */

void runRandomisedTests(int iterations) {

    printf("======================================================\n");
    printf("Randomised Cross Checks\n");
    printf("======================================================\n");

    for (int i = 1; i <= iterations; i++) {

        /* Choose random compatible dimensions */
        int r1 = 1 + rand() % 5;
        int c1 = 1 + rand() % 5;
        int r2 = c1;               /* ensure c1 == r2 */
        int c2 = 1 + rand() % 5;

        int **m1 = randomMatrix(r1, c1, -5, 5);
        int **m2 = randomMatrix(r2, c2, -5, 5);

        if (m1 == NULL || m2 == NULL) {
            printf("Allocation failed in randomised test %d\n", i);
            freeMatrix(m1, r1);
            freeMatrix(m2, r2);
            return;
        }

        int **result = multiplyTwoMatrices(m1, r1, c1, m2, r2, c2);

        if (result == NULL) {
            printf("Randomised test FAILED (NULL result)\n");
            printf("r1,c1 = %d,%d\n", r1, c1);
            printf("r2,c2 = %d,%d\n", r2, c2);
            printf("matrix1:\n");
            printMatrix(m1, r1, c1);
            printf("matrix2:\n");
            printMatrix(m2, r2, c2);
            freeMatrix(m1, r1);
            freeMatrix(m2, r2);
            return;
        }

        /* Basic sanity: we expect r1 x c2 output */
        /* (if multiplyTwoMatrices ever misallocates, we’d likely crash) */

        freeMatrix(m1, r1);
        freeMatrix(m2, r2);
        freeMatrix(result, r1);
    }

    printf("All %d randomised tests passed.\n\n", iterations);
}

/*
 * ============================================================
 * Main: Build deterministic tests and run everything
 * ============================================================
 */

int main(void) {

    srand((unsigned int)time(NULL));

    /* Precompute expected results for deterministic tests */

    /*
     * S1: 2x2 × 2x2
     *
     * [1 2]   [5 6]
     * [3 4] * [7 8]
     */

    int r1_S1 = 2, c1_S1 = 2, r2_S1 = 2, c2_S1 = 2;

    int flat1_S1[] = {
            1, 2,
            3, 4
    };

    int flat2_S1[] = {
            5, 6,
            7, 8
    };

    int flatExpected_S1[] = {
            1 * 5 + 2 * 7, 1 * 6 + 2 * 8,
            3 * 5 + 4 * 7, 3 * 6 + 4 * 8
    };

    int **expected_S1 = createMatrixFromFlat(flatExpected_S1, r1_S1, c2_S1);

    /*
     * S2: 2x3 × 3x2
     */

    int r1_S2 = 2, c1_S2 = 3, r2_S2 = 3, c2_S2 = 2;

    int flat1_S2[] = {
            1, 2, 3,
            4, 5, 6
    };

    int flat2_S2[] = {
            7,  8,
            9, 10,
            11, 12
    };

    int flatExpected_S2[] = {
            1 * 7 + 2 * 9 + 3 * 11, 1 * 8 + 2 * 10 + 3 * 12,
            4 * 7 + 5 * 9 + 6 * 11, 4 * 8 + 5 * 10 + 6 * 12
    };

    int **expected_S2 = createMatrixFromFlat(flatExpected_S2, r1_S2, c2_S2);

    /*
     * I1: Identity 2x2 × arbitrary 2x2
     */

    int r1_I1 = 2, c1_I1 = 2, r2_I1 = 2, c2_I1 = 2;

    int flat1_I1[] = {
            1, 0,
            0, 1
    };

    int flat2_I1[] = {
            5, 6,
            7, 8
    };

    int flatExpected_I1[] = {
            5, 6,
            7, 8
    };

    int **expected_I1 = createMatrixFromFlat(flatExpected_I1, r1_I1, c2_I1);

    /*
     * Z1: Zero matrix 2x2 × arbitrary 2x2
     */

    int r1_Z1 = 2, c1_Z1 = 2, r2_Z1 = 2, c2_Z1 = 2;

    int flat1_Z1[] = {
            0, 0,
            0, 0
    };

    int flat2_Z1[] = {
            5, 6,
            7, 8
    };

    int flatExpected_Z1[] = {
            0, 0,
            0, 0
    };

    int **expected_Z1 = createMatrixFromFlat(flatExpected_Z1, r1_Z1, c2_Z1);

    /*
     * N1: Negative values
     */

    int r1_N1 = 2, c1_N1 = 2, r2_N1 = 2, c2_N1 = 2;

    int flat1_N1[] = {
            -1, 2,
            3, -4
    };

    int flat2_N1[] = {
            5, -6,
            -7, 8
    };

    int flatExpected_N1[] = {
            -1 * 5 + 2 * -7, -1 * -6 + 2 * 8,
            3 * 5 + -4 * -7, 3 * -6 + -4 * 8
    };

    int **expected_N1 = createMatrixFromFlat(flatExpected_N1, r1_N1, c2_N1);

    /*
     * E1/E2/E3/E4/E5: Edge cases
     */

    /* E1: NULL matrix1 */
    TestCase tests[] = {

            {
                    "S1",
                    r1_S1, c1_S1,
                    r2_S1, c2_S1,
                    flat1_S1, flat2_S1,
                    expected_S1,
                    "2x2 × 2x2 example"
            },

            {
                    "S2",
                    r1_S2, c1_S2,
                    r2_S2, c2_S2,
                    flat1_S2, flat2_S2,
                    expected_S2,
                    "2x3 × 3x2 example"
            },

            {
                    "I1",
                    r1_I1, c1_I1,
                    r2_I1, c2_I1,
                    flat1_I1, flat2_I1,
                    expected_I1,
                    "Identity × arbitrary"
            },

            {
                    "Z1",
                    r1_Z1, c1_Z1,
                    r2_Z1, c2_Z1,
                    flat1_Z1, flat2_Z1,
                    expected_Z1,
                    "Zero × arbitrary"
            },

            {
                    "N1",
                    r1_N1, c1_N1,
                    r2_N1, c2_N1,
                    flat1_N1, flat2_N1,
                    expected_N1,
                    "Negative values"
            },

            /* E1: Null matrix1 – we simulate by passing NULL flat arrays */
            {
                    "E1",
                    2, 2,
                    2, 2,
                    NULL, flat2_S1,
                    NULL,
                    "Null matrix1"
            },

            /* E2: Null matrix2 */
            {
                    "E2",
                    2, 2,
                    2, 2,
                    flat1_S1, NULL,
                    NULL,
                    "Null matrix2"
            },

            /* E3: Dimension mismatch: 1x2 × 1x2 */
            {
                    "E3",
                    1, 2,
                    1, 2,
                    flat1_S1, flat2_S1,
                    NULL,
                    "Dimension mismatch: c1 != r2"
            }
    };

    int testCount = (int)(sizeof(tests) / sizeof(tests[0]));

    printf("############################################################\n");
    printf("######## MULTIPLY TWO MATRICES (C TEST HARNESS) ###########\n");
    printf("############################################################\n\n");

    runTests("Deterministic Tests", tests, testCount);

    runRandomisedTests(2000);

    /* Free expected matrices */
    freeMatrix(expected_S1, r1_S1);
    freeMatrix(expected_S2, r1_S2);
    freeMatrix(expected_I1, r1_I1);
    freeMatrix(expected_Z1, r1_Z1);
    freeMatrix(expected_N1, r1_N1);

    return 0;
}

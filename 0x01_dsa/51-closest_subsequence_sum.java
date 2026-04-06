import java.util.*;

public class MinAbsDifference {
    public static void generate_sums(List<Integer> nums, int start, int end, List<Integer> sums) {
        int n = end - start;
        for (int i = 0; i < (1 << n); ++i) {
            int sum = 0;
            for (int j = 0; j < n; ++j) {
                if ((i & (1 << j)) != 0) {
                    sum += nums.get(start + j);
                }
            }
            sums.add(sum);
        }
    }

    public static int min_abs_difference(List<Integer> nums, int goal) {
        int n = nums.size();
        List<Integer> left_sums = new ArrayList<>();
        List<Integer> right_sums = new ArrayList<>();

        generate_sums(nums, 0, n / 2, left_sums);
        generate_sums(nums, n / 2, n, right_sums);

        Collections.sort(right_sums);

        int min_diff = Math.abs(goal);

        for (int left_sum : left_sums) {
            int remaining = goal - left_sum;
            int pos = Collections.binarySearch(right_sums, remaining);

            if (pos >= 0) {
                return 0;
            } else {
                pos = -pos - 1;
            }

            if (pos < right_sums.size()) {
                min_diff = Math.min(min_diff, Math.abs(remaining - right_sums.get(pos)));
            }

            if (pos > 0) {
                min_diff = Math.min(min_diff, Math.abs(remaining - right_sums.get(pos - 1)));
            }
        }

        return min_diff;
    }

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(5, -7, 3, 5);
        int target = 6;
        System.out.println(min_abs_difference(nums, target));
    }
    
}
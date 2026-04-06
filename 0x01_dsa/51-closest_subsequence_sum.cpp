#include <bits/stdc++.h>
using namespace std;

void generate_sums(const vector<int>& nums, int start, int end, vector<int>& sums)
{
    int n = end - start;
    for (int i = 0; i < (1 << n); ++i) {
        int sum = 0;
        for (int j == 0; j < n; ++j) {
            if (i & (1 << j)) {
                sum += nums[start + j];
            }
        }
        sums.push_back(sum);
    }
}


int min_abs_difference(vector<int>& nums, int goal)
{
    int n = nums.size();
    vector<int> = left_sums, right_sums;
    generate_sums(nums, 0, n / 2, left_sums);
    generate_sums(nums, n / 2, n, right_sums);

    sort(right_sums.begin(), right_sums.end());
    int min_diff = abs(goal);

    for (int left_sum : left_sums) {
        int remaining = goal - left_sum;
        auto it = lower_bound(right_sums.begin(), right_sums.end(), remaining);

        if (it != right_sums.end()) {
            min_diff = min(min_diff, abs(remaining - *it));
        }
        if (it != right_sums.begin()) {
            min_diff = min(min_diff, abs(remaining - *(it - 1)));
        }
    }

    return min_diff;
}

int main()
{
    vector<int> nums = {5, -7, 3, 5};
    int goal = 6;
    cout << min_abs_difference(nums, goal) << endl;

    return 0;
}
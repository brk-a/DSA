#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

struct TestCase {
    int id;
    vector<int> nums;        // Use value, not reference
    vector<int> expected;
};

vector<int>& nextPermutation(vector<int>& nums) {
    next_permutation(nums.begin(), nums.end());
    return nums;  // Return modified input
}

int main() {
    vector<TestCase> tests = {
        {1, {2, 4, 1, 7, 5, 0}, {2, 4, 5, 0, 1, 7}},
        {2, {1, 2, 3, 5, 4}, {1, 2, 4, 3, 5}},
        {3, {1, 2, 3, 4, 5}, {1, 2, 3, 5, 4}},
        {4, {1, 2, 3, 6, 5, 4}, {1, 2, 4, 3, 5, 6}},
    };

    cout << "=================== Next Permutation Problem ===================" << endl;
    
    for (const auto& test : tests) {
        vector<int> input = test.nums;  // Copy for testing
        vector<int>& result = nextPermutation(input);
        
        bool passed = (result == test.expected);
        
        cout << "Test " << test.id << ": ";
        cout << "nums=[";
        for (size_t i = 0; i < input.size(); ++i) {
            cout << input[i];
            if (i < input.size() - 1) cout << ", ";
        }
        cout << "] -> got=[";
        for (size_t i = 0; i < input.size(); ++i) {
            cout << input[i];
            if (i < input.size() - 1) cout << ", ";
        }
        cout << "], expected=[";
        for (size_t i = 0; i < test.expected.size(); ++i) {
            cout << test.expected[i];
            if (i < test.expected.size() - 1) cout << ", ";
        }
        cout << "] - " << (passed ? "PASSED" : "FAILED") << endl;
    }

    return 0;
}

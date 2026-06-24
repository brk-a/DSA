#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// Function to find the next permutation
void nextPermutation(vector<int>& nums) {

    // Rearranges elements into the next lexicographical order
    // If already last permutation, rearranges to the smallest
    next_permutation(nums.begin(), nums.end());
}

int main() {

    vector<int> nums = {2, 4, 1, 7, 5, 0};

    nextPermutation(nums);

    for (int i = 0; i < nums.size(); i++) {
        cout << nums[i] << " ";
    }

    return 0;
}
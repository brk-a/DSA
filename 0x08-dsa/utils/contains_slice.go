package utils

func ContainsSlice(slices [][]int, target []int) bool {
	for _, s := range slices {
		if equal(s, target) {
			return true
		}
	}

	return false
}

func equal(a, b []int) bool {
	if len(a) != len(b) {
		return false
	}

	for i, v := range a {
		if v != b[i] {
			return false
		}
	}

	return true
}

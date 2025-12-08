package utils

func AllUnique(s string, start, end int) bool {
	all_unique := true

	mp := make(map[byte]bool)
	for i:=start; i<=end; i++ {
		if _, ok := mp[s[i]]; ok {
			all_unique = false
			return  all_unique
		}
		mp[s[i]] = true
	}

	return all_unique
}
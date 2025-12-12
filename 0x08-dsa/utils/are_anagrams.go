package utils

func AreAnagrams(s1, s2 string) bool {
    if len(s1) != len(s2) {
        return false
    }
    sorted1 := SortString(s1)
    sorted2 := SortString(s2)
    return sorted1 == sorted2
}
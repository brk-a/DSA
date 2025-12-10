package utils

func ContainsAll(s_map, t_map map[byte]int) bool {
	contains_all := true

	for k, v := range t_map {
		if s_map[k] < v{
			contains_all = false
			return contains_all
		}
	}

	return  contains_all
}
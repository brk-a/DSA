package utils

type NodeHeap []*ListNode

func (nh NodeHeap) Len() int {
	return len(nh)
}

func (nh NodeHeap) Less(i, j int) bool {
	return nh[i].Val < nh[j].Val
}

func (nh NodeHeap) Swap(i, j int) {
	nh[i], nh[j] = nh[j], nh[i]
}

func (nh *NodeHeap) Push(x interface{}) {
	*nh = append(*nh, x.(*ListNode))
}

func (nh *NodeHeap) Pop() interface{} {
	old := *nh
	l := len(old)
	x := old[l-1]
	*nh = old[0:l-1]

	return x
}
public class BinarysearchTree <T extends Comparable<T>>{
    //#nodes
    private int nodeCount = 0;

    //track root node
    private root = null;

    //internal node w. node refs and node data
    private class Node{
        T data;
        Node left, right;
        public Node(Node left, Node right, T elem){
            this.data = elem;
            this.left = left;
            this.right = right;
        }
    }

    //is bst empty?
    public boolean isEmpty(){
        return size() == 0;
    }

    //#nodes in tree
    public int size(){
        return nodeCount;
    }

    //add element to bst
    public boolean add(T elem){
        if(contains(elem)){
            return false;
        } else {
            root = add(root, elem);
            nodeCount++;
            return true;
        }
    }

    //private method to add value to bst recursively
    private Node add(Node node, T elem){
        if(node=null){
            node = new Node(null, null, elem);
        } else {
            if(elem.compareTo(node.data)<0){
                node.left = add(node.left, elem);
            } else {
                node.right = add(node.right, elem);
            }
        }

        return node;
    }

    //remove a node from bst
    public boolean remove(Node node, T elem){
        if(contains(elem)){
            root = remove(root, elem);
            nodeCount--;
            return true;
        }
        return false;
    }

    private Node remove(Node node, T elem){
        if(node==null) return null;

        int cmp = elem.compareTo(node.data);

        if(cmp<0){//dig into left sub-tree
            node.left = remove(node.left, elem);
        } else if(cmp>0) {//dig into right sub-tree
            node.right = remove(node.right, elem);
        } else {
            if(node.left==null){//right  sub-tree or no sub-tree at all
                Node rightChild = node.right;

                node.data = null;
                node = null;

                return rightChild;
            } else if(node.right==null){//left  sub-tree or no sub-tree at all
                Node leftChild = node.left;

                node.data = null;
                node = null;

                return leftChild;
            } else {//has both left and right sub-tree
                Node tmp = digLeft(node.right);
                node.data = tmp.data;
                node.right = remove(nod.right, tmp.data);

                // //find largest node in left sub-tree
                // Node tmp = digRight(node.left);
                // node.data = tmp.data;
                // node.right = remove(nod.left, tmp.data);
            }
        }
        return node;
    }

    //helper method to find leftmost node
    private Node digLeft(Node node){
        Node cur = node;
        while(cur.left!=null)
            cur = cur.left;
        return cur;
    }

    //helper method to find rightmost node
    private Node digRight(Node node){
        Node cur = node;
        while(cur.right!=null)
            cur = cur.left;
        return cur;
    }

    //does element exist in tree?
    public boolean contains(T elem){
        return contains(root, elem);
    }

    private boolean contains(Node node, T elem){
        if(node==null) return false;

        int cmp = elem.compareTo(node.data);

        if(cmp<0) return contains(node.left, elem);
        else if(cmp>0) return contains(node.right, elem);
        else return true;
    }

    //what is the height of the bst?
    public int height(){
        return height(root);
    }

    private int height(Node node){
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    //an iterator for a given TreeTraversalOrder
    public java.util.Iterator <T> traverse(TreeTraversalOrder order){
        switch(order){
            case PRE_ORDER: return preOrderTraversal();
            case IN_ORDER: return inOrderTraversal();
            case POST_ORDER: return postOrderTraversal();
            case LEVEL_ORDER: return levelOrderTraversal();
            default: return null;
        }
    }

    //iterator to traverse pre-order
    private java.util.Iterator<T> preOrderTraversal() {

    final int expectedNodeCount = nodeCount;
    final java.util.Stack<Node> stack = new java.util.Stack<>();
    stack.push(root);

    return new java.util.Iterator<T>() {
      @Override
      public boolean hasNext() {
        if (expectedNodeCount != nodeCount) throw new java.util.ConcurrentModificationException();
        return root != null && !stack.isEmpty();
      }

      @Override
      public T next() {
        if (expectedNodeCount != nodeCount) throw new java.util.ConcurrentModificationException();
        Node node = stack.pop();
        if (node.right != null) stack.push(node.right);
        if (node.left != null) stack.push(node.left);
        return node.data;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

    //iterator to traverse in-order
    private java.util.Iterator <T> inOrderTraversal(){
        final int expectedNodeCount = nodeCount;
    final java.util.Stack<Node> stack = new java.util.Stack<>();
    stack.push(root);

    return new java.util.Iterator<T>() {
      Node trav = root;

      @Override
      public boolean hasNext() {
        if (expectedNodeCount != nodeCount) throw new java.util.ConcurrentModificationException();
        return root != null && !stack.isEmpty();
      }

      @Override
      public T next() {

        if (expectedNodeCount != nodeCount) throw new java.util.ConcurrentModificationException();

        // Dig left
        while (trav != null && trav.left != null) {
          stack.push(trav.left);
          trav = trav.left;
        }

        Node node = stack.pop();

        // Try moving down right once
        if (node.right != null) {
          stack.push(node.right);
          trav = node.right;
        }

        return node.data;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
    }

    //iterator to traverse post-order
    private java.util.Iterator <T> postOrderTraversal(){
        final int expectedNodeCount = nodeCount;
    final java.util.Stack<Node> stack1 = new java.util.Stack<>();
    final java.util.Stack<Node> stack2 = new java.util.Stack<>();
    stack1.push(root);
    while (!stack1.isEmpty()) {
      Node node = stack1.pop();
      if (node != null) {
        stack2.push(node);
        if (node.left != null) stack1.push(node.left);
        if (node.right != null) stack1.push(node.right);
      }
    }
    return new java.util.Iterator<T>() {
      @Override
      public boolean hasNext() {
        if (expectedNodeCount != nodeCount) throw new java.util.ConcurrentModificationException();
        return root != null && !stack2.isEmpty();
      }

      @Override
      public T next() {
        if (expectedNodeCount != nodeCount) throw new java.util.ConcurrentModificationException();
        return stack2.pop().data;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
    }

    //iterator to traverse pre-order
    private java.util.Iterator <T> levelOrderTraversal(){
        final int expectedNodeCount = nodeCount;
    final java.util.Queue<Node> queue = new java.util.LinkedList<>();
    queue.offer(root);

    return new java.util.Iterator<T>() {
      @Override
      public boolean hasNext() {
        if (expectedNodeCount != nodeCount) throw new java.util.ConcurrentModificationException();
        return root != null && !queue.isEmpty();
      }

      @Override
      public T next() {
        if (expectedNodeCount != nodeCount) throw new java.util.ConcurrentModificationException();
        Node node = queue.poll();
        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
        return node.data;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
    }

}
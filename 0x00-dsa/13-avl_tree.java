public class AVLTreeRecursive <T extends Comparable<T>> implements Iterable <T>{
    class Node implements TreePrinter.PrintableNode{}

    int bf; //balance factor
    T value; //value/data contained in node
    int height; //height of node in tree
    Node left, right;

    public Node(T value){
        this.value = value;
    }

    @Override
    public Node getLeft(){
        return left;
    }
    @Override
    public Node getRight(){
        return right;
    }
    @Override
    public Node getText(){
        return String.valueOf(value);
    }

    //root of AVL tree
    private Node root;

    //track #nodes in tree
    private int nodeCount = 0;

    //height of a rooted tree (tree w. a single node  has a height = zero)
    public int height(){
        if(root==null) return 0;
        return root.height;
    }
    //#nodes in a tree
    public int size(){
        return nodeCount;
    }
    //is tree empty?
    public boolean isEmpty(){
        return size() == 0;
    }
    //visual rep of tree on console
    public void display(){
        TreePrinter.print(root);
    }
    //does value exist in tree
    public boolean contains(root, value){
        return contains(root, value);
    }
    private boolean contains(Node node, T value){
        if(node==null) return false;

        //compare current val to val in node
        int cmp = value.compareTo(node.value);
        //dig into left subtree
        if(cmp<0) return contains(node.left, value);
        //dig into right subtree
        if(cmp>0) return contains(node.right, value);

        //found value in tree
        return true;
    }

    //insert/add value into AVT tree; value must not be null
    public boolean insert(T value){
        if(value==null) return false;
        if(!contains(root, value)){
            root = insert(root, value);
            nodeCount++;
            return true;
        }
        return false;
    }
    private Node insert(Node node, T value){
        //base case
        if(node==null) return new Node(value);

        //compare current value to value  in node
        int cmp = value.compareTo(node.value);

        //insert node in left tree
        if(cmp<0){
            node.left = insert(node.left, value);
        } else {
            //insert node in right
            node.right = insert(node.right, value);
        }

        //update balance factor & height vals
        update(node);

        //re-balance tree
        return balance(tree)
    }

    //update a node's  height and balance factor
    private void update(Node node){
        int leftNodeHeight = (node.left==null) ? -1 : node.left.height;
        int rightNodeHeight = (node.right==null) ? -1 : node.right.height;

        //update the node's height
        node.height = 1 + Math.max(leftNodeHeight, rightNodeHeight);
    }

    //re-balance a node if bf is +2 or -2
    private Node balance(Node node){
        //left-heavy subtree
        if(node.bf==-2){
            //left-left case
            if(node.left.bf<=0){
                return leftLeftCase(node);
            } else {//left-right case
                return leftRightCase(node);
            }
           //right-heavy subtree 
        } else if(node.bf==+2){
            //right-right case
            if(node.right.bf>=0){
                return rightRightCase(node);
            } else {//right-left case
                return rightLeftCase(node);
            }
        }

        //node has a bf of zero or ±1; this is alright
        return node;
    }

    private Node leftLeftCase(Node node){
        return rightRotation(node);
    }
    private Node leftRightCase(Node node){
        node.left = leftRotation(node.left);
        return leftLeftCase(node);
    }
    private rightRightCase(Node node){
        return leftRotation(node);
    }
    private rightLeftCase(Node node){
        node.right = rightRotation(node.right);
        return rightRightCase(node);
    }
    private Node leftRotation(Node node){
        Node newParent = node.right;
        node.right = newParent.left;
        newParent.left = node;
        update(node);
        update(newParent);
        return newParent;
    }
    private rightRotation(Node node){
        Node newParent = node.left;
        node.left =  newParent.right;
        newParent.right = node;
        update(node);
        update(newParent);
        return newParent;
    }

    //remove a value from the bin tree
    public boolean remove(T elem){
        if(elem==null) return false;
        if(contains(root, elem)){
            root = remove(root, elem);
            nodeCount--;
            return true;
        }
        return false;
    }
    //remove value from AVL tree
    private Node remove(Node node, T elem){
        if(node==null) return null;
        
        int cmp = elem.compareTo(node.value);

        //dig into left st
        if(cmp<0){
            node.left = remove(node.left.elem);
        } else if(cmp>0){//dig into right st
            node.right = remove(node.right.elem);
        } else {
            //only a right st or no subtree
            if(node.left==null){
                return node.right;
            } else if(node.right==null){//only a left st or no subtree
                return node.right
            } else {
                if(node.left.height>node.right.height){}
            }
        }

    }
}
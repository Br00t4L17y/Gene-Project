import java.util.List;
import java.util.ArrayList;

public class BTree {
    int t; // minimum degree 
    BTreeNode root;
    ArrayList<BTreeNode> nodes;

	public BTree(int degree){
		t = degree;
		//nodes = new ArrayList<BTreeNode>();
		root = new BTreeNode(true, t);
	}
	
    private void split(BTreeNode parent, int i) {
		// create new right node and get reference to the node to be split 
		BTreeNode rightNode = new BTreeNode(true, t);
		BTreeNode leftNode = parent.children.get(i); 
		rightNode.leaf = leftNode.leaf; 

		// Move elements into the right node 
		for (int j = 0; j < t - 1; j++) {
			rightNode.values.add(j, leftNode.values.remove(j+t)); 
		}
		// Move children from the left node to the right node if necessary 
		if (!leftNode.leaf) {
			for (int j = 0; j < t; j++) {
				rightNode.children.add(j, leftNode.children.remove(j+t - 1)); 
			}
		}

		// Set new right node to be a child 
		parent.children.add(i+1, rightNode);

		// Pull new element up from the left node 
		parent.values.add(i, leftNode.values.remove(t - 1)); 
	} 
    
    public void add(TreeObject element) {
    	// if BTree is empty
    	if(root.values.size() == 0) {
    		root = new BTreeNode(true, t);
    		root.values.add(0, element);
    	}
    	
    	// BTree is not empty
    	else {
    		// if root is full --> need to create new node and
        	if(root.isFull()) {
        		BTreeNode s = new BTreeNode(false, t); // make a new node
        		s.children.add(0, root); 
        		split(s, 0); //Should this be 0?
        		insertNonFull(s, element); 	
        		root = s;
        	}
        	// if root is not full
        	else {
        		insertNonFull(root, element);
        	}
    	}
    	
    	
    }
    
    /**
     * Inserting helper method, although this method does the real job of locating the place to insert and inserting. 
     * @param node
     * @param element
     */
    private void insertNonFull(BTreeNode node, TreeObject element) {
    	int i = node.values.size() - 1; // the last key in values
    	
    	// Determine correct index to insert at 
    	while(i >= 0 && element.compareTo(node.values.get(i)) < 0) {
			i--;
		}
    	i++; 
    	
    	// if node is a leaf then we can insert 
    	if(node.leaf) {
    		node.values.add(i, element);
    	}
    	
    	// if node is not a leaf then we cannot insert and we need to determine the correct child to descend the tree
    	else {    		
    		// if child is full
    		if(node.children.get(i).isFull()) { 
    			split(node, i); // split in the book uses index i
    			if(element.compareTo(node.values.get(i)) > 0)
    				i++;
    		}
    		
    	insertNonFull(node.children.get(i), element);
    	
    	}
    	
    }
    


	public class BTreeNode {
		int minKeys; // represents the number of key elements sorted in a node
		int maxKeys;
		boolean leaf;
		ArrayList<BTreeNode> children;
		ArrayList<TreeObject> values; 
		
		public BTreeNode() {
			minKeys = (BTree.this.t - 1);
			maxKeys = ((2 * BTree.this.t) - 1);
			values = new ArrayList<TreeObject>(2*BTree.this.t - 1);
			children = new ArrayList<BTreeNode>(2*BTree.this.t);
			leaf = true;
		}

		public BTreeNode(boolean isLeaf, int t){
			leaf = isLeaf;
			minKeys = (t - 1);
			maxKeys = ((2 * t) - 1);
			// need to also declare lists here? Should we use list, arrayList, or arrays??
			values = new ArrayList<TreeObject>(2*t-1);
			children = new ArrayList<BTreeNode>(2*t);
		}
		
		public boolean isFull() {
			return values.size() == 2*t-1;
		}
	}

}

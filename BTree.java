import java.util.List;
import java.util.ArrayList;

public class BTree {
    int t; // minimum degree 
    BTreeNode root;
    ArrayList<BTreeNode> nodes;

	public BTree(int degree){
		t = degree;
		nodes = new ArrayList<BTreeNode>();
		root = new BTreeNode(true, t);
	}

    // NOTE: I'm assuming that values (from BTreeNode) is an arrayList
    // Also I'm interacting directly with the values arrayLists, which eventually we may want to 
    // want to wrap in methods within the BTreeNode object instead 
    /*private void split(BTreeNode node) {

        // Take all values to the right of the middle and add them to a new node 
        BTreeNode newRightNode = new BTreeNode(); // TODO: add necessary constructor parameters
        for (int i = t+1; i < (2 * t); i++) { 
            newRightNode.values.add(node.values.remove[i]); 
        }

        // Remove the key that will be moved up 
        TreeObject middleObject = Object.values.remove(t); 
        
        // Check if parent exits 
        if (node.parent != null) {
            int index = 0; 
            // Find correct spot for the object in the parent node and add it there 
            while (index < node.parent.values.size()) {
                if (node.parent.values[index].compareTo(middleObject) == 1 || index == node.parent.values.size()) {  
                    node.parent.values.add(index, middleObject); 
                    node.parent.children.add(index+1, newRightNode); 
                    newRightNode.parent = node.parent; 
                    break; 
                } 
                index++; 
            }
        } else {
            // Create a new parent node and add the object 
            BTreeNode newParent = new BTreeNode(); // TODO: add necessary constructor parameters 
            newParent.values.add(middleObject); 

            // Add the split children to the new parent node 
            newParent.children.add(node); 
            newParent.children.add(newRightNode);
            
            // Set parent pointers
            node.parent = newParent; 
            newRightNode.parent = newParent; 
        }
	}*/
	
    private void split(BTreeNode parent, int i) {
		// create new right node and get reference to the node to be split 
		BTreeNode rightNode = new BTreeNode(true, t);
		BTreeNode leftNode = parent.children.get(i); 
		rightNode.leaf = leftNode.leaf; 

		// Move elements into the right node 
		for (int j = 0; j < t - 1; j++) {
			rightNode.values.set(j, leftNode.values.remove(j+t)); 
		}
		// Move children from the left node to the right node if necessary 
		if (!leftNode.leaf) {
			for (int j = 0; j < t; j++) {
				rightNode.children.set(j, leftNode.children.remove((j-1)+t)); 
			}
		}

		// Shift parent node's children to make space for new child 
		for (int j = parent.children.size(); j > i; j--) {
			parent.children.set(j+1, parent.children.get(j)); 
		}
		// Set new right node to be a child 
		parent.children.set(i+1, rightNode);

		// Shift parent values to make space for new element 
		for (int j = parent.values.size() - 1; j >= i; j--) {
			parent.values.set(j+1, parent.values.get(j)); 
		} 
		// Pull new element up from the left node 
		parent.values.set(i, leftNode.values.remove(t - 1)); 
	} 
    
    public void add(TreeObject element) {
    	// if BTree is empty
    	if(nodes.size() == 0) {
    		root = new BTreeNode(true, t);
    		root.values.add(0, element);
    	}
    	
    	// BTree is not empty
    	else {
    		// if root is full --> need to create new node and
        	if(root.values.size() == 2*t-1) {
        		BTreeNode s = new BTreeNode(false, t); // make a new node
        		s.children.set(0, root); 
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
    	int i = node.values.size();
    	// if node is a leaf then we can insert //TODO: need to create a compare to or figure out another way to compare
    	if(node.leaf) {
    		while(i <= 0 && element.compareTo(node.values.get(i)) < 0) {
        		node.values.set(i+1, node.values.get(i)); // shifts over elements
        		i--;
        	}
    		node.values.set(i+1, element);
    	}
    	
    	// if node is not a leaf then we cannot insert and we need to determine the correct child to descend the tree
    	else {
    		while(i >= 1 && element.compareTo(node.values.get(i)) < 0) {
    			i--;
    		}
    		i = i+1;
    		
    		// if child is full
    		if(node.children.get(i).values.size() == 2*t-1) { 
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
	}

}
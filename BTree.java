public class BTree {
    int t; // minimum degree 

    // NOTE: I'm assuming that keys (from BTreeNode) is an arrayList
    // Also I'm interacting directly with the keys arrayLists, which eventually we may want to 
    // want to wrap in methods within the BTreeNode object instead 
    private void split(BTreeNode node) {

        // Take all keys to the right of the middle and add them to a new node 
        BTreeNode newRightNode = new BTreeNode(); // TODO: add necessary constructor parameters
        for (int i = t+1; i < (2 * t); i++) { 
            newRightNode.keys.add(node.keys.remove[i]); 
        }

        // Remove the key that will be moved up 
        TreeObject middleObject = Object.keys.remove(t); 
        
        // Check if parent exits 
        if (node.parent != null) {
            int index = 0; 
            // Find correct spot for the object in the parent node and add it there 
            while (index < node.parent.keys.size()) {
                if (node.parent.keys[index] > middleObject || index == node.parent.keys.size()) { // TODO: will we be able to compare the keys like this? 
                    node.parent.keys.add(index, middleObject); 
                    node.parent.children.add(index+1, newRightNode); 
                    newRightNode.parent = node.parent; 
                    break; 
                } 
                index++; 
            }
        } else {
            // Create a new parent node and add the object 
            BTreeNode newParent = new BTreeNode(); // TODO: add necessary constructor parameters 
            newParent.keys.add(middleObject); 

            // Add the split children to the new parent node 
            newParent.children.add(node); 
            newParent.children.add(newRightNode);
            
            // Set parent pointers
            node.parent = newParent; 
            newRightNode.parent = newParent; 
        }
    }
}
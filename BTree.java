public class BTree {
	List<BTreeNode> nodes;
}

public class BTreeNode {
	int numberOfKeys;
	boolean leaf;
	List<TreeObject> values;
	List<BTreeNode> children;


}
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.Serializable;

public class BTree implements Serializable {
	private static final long serialVersionUID = -7504766932017737111L;

    int t; // minimum degree 
    BTreeNode root;
    ArrayList<BTreeNode> nodes;
    int nodeSize = 4096;   // hardcoded this in; if we have time we may want to calculate the exact value to save space
    int nextPosition; 
    
    String bFile;

	public BTree(int degree, String gbkFile, int seqLength) throws IOException, ClassNotFoundException {
		t = degree;
		bFile = gbkFile.substring(1) + ".btree.data." + seqLength + "." + t; 
		//nodeSize = (256 * t) - 88; 
		root = new BTreeNode(true, t);
		
		/*String[] letters = {"C", "A", "G"}; 
		for (int i = 0; i < 3; i++) {
			TreeObject to = new TreeObject(letters[i]); 
			add(to); 
		}
		
		
		
		
		diskWrite(0, root); 
		

		System.out.println("Root Node: " + diskRead(0).values.get(0).toString());*/
		
		 try {
			 RandomAccessFile bTreeFile = new RandomAccessFile(bFile, "rw"); 
			 
             RandomAccessFile out = new RandomAccessFile("metadata.bin", "rw");
             FileChannel fout = out.getChannel();
             fout.truncate(0); // truncate the file


     		int rootPosition = 0; 
     		int numChildren = 0; 
             
             ByteBuffer buff = ByteBuffer.allocateDirect(32 * 3);
             
             buff.putInt(rootPosition);
             buff.putInt(t); 
             buff.putInt(numChildren); 
             
             if (!buff.hasRemaining()) {
                 buff.flip();
                 fout.write(buff);
                 buff.clear();
             }
             
             if (buff.position() > 0) {
                 buff.flip();
                 fout.write(buff);
                 buff.clear();
             }	
             
             fout.close();

		 } catch (IOException e) {
             System.err.println(e);
             System.exit(1);

		 }
		
		
	}
	
	private void diskWrite(int pos, BTreeNode node) {
		try {
			RandomAccessFile out = new RandomAccessFile(bFile, "rw"); 
			 
            FileChannel fout = out.getChannel();
            fout.truncate(0); // truncate the file
            
            ByteBuffer buff = ByteBuffer.allocateDirect(nodeSize);
            System.out.println("Node size: " + nodeSize);
            byte[] serializedNode = toStream(node); 

            System.out.println("The size of the object is: " + serializedNode.length);
            byte[] fullSerializedNode = new byte[nodeSize]; 
            for (int i = 0; i < serializedNode.length; i++) {
            	fullSerializedNode[i] = serializedNode[i]; 
            }
            
            buff.put(fullSerializedNode, pos, nodeSize); 
           
            if (!buff.hasRemaining()) {
                buff.flip();
                fout.write(buff);
                buff.clear();
            }
            
            if (buff.position() > 0) {
                buff.flip();
                fout.write(buff);
                buff.clear();
            }	
            
            fout.close();

		 } catch (IOException e) {
            System.err.println(e);
            System.exit(1);

		 }
	}
	
	private BTreeNode diskRead(int pos) throws IOException, ClassNotFoundException {

		ByteBuffer buffer = ByteBuffer.allocateDirect(nodeSize);

        FileChannel dataFile = new RandomAccessFile(bFile,"rw").getChannel();

        dataFile.position(pos);
        buffer.clear();
        dataFile.read(buffer);
        buffer.flip();
        byte[] data = new byte[nodeSize]; 
        buffer.get(data);
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (BTreeNode)is.readObject();
        
	}
	
	public static byte[] toStream(BTreeNode node) {
	    byte[] stream = null;
	    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream oos = new ObjectOutputStream(baos);) {
	        oos.writeObject(node);
	        stream = baos.toByteArray();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return stream;
	}
	
    private void split(BTreeNode parent, int i) {
		// create new right node and get reference to the node to be split 
		BTreeNode rightNode = new BTreeNode(true, t);
		BTreeNode leftNode = parent.children.get(i); 
		rightNode.leaf = leftNode.leaf; 
		
		while(leftNode.values.size() > t) {
			rightNode.values.add(0,leftNode.values.remove(leftNode.values.size()-1));
		}
		// Move children from the left node to the right node if necessary 
		if (!leftNode.leaf) {
			
			while(leftNode.children.size() > t) {
				rightNode.children.add(0,leftNode.children.remove(leftNode.children.size()-1));
			}
		}

		// Set new right node to be a child 
		parent.children.add(i+1, rightNode);

		// Pull new element up from the left node 
		parent.values.add(i, leftNode.values.remove(t - 1)); 
	} 
    
    public void add(TreeObject element) throws IOException {
    	
    	// check if the element is already in the tree Where shuold I do this?
    	
    	// if BTree is empty
    	if(root.values.size() == 0) {
    		root = new BTreeNode(true, t);
			root.values.add(0, element);
			element.incFrequency(); 
    	}
    	
    	// BTree is not empty
    	else {
    		
    		// if root is full --> need to create new node and
    		if(root.isFull()) {
        		BTreeNode s = new BTreeNode(false, t); // make a new node
        		s.children.add(0, root); 
        		split(s, 0); //Splits the child
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
     * @throws IOException 
     */
    private void insertNonFull(BTreeNode node, TreeObject element) throws IOException {
    	int i = node.values.size() - 1; // the last key in values
    	
    	if(i >= 0 && element.compareTo(node.values.get(i)) == 0) {
    		node.values.get(i).incFrequency();
    		return;
    	}
    	
    	// Determine correct index to insert at 
    	while(i >= 0 && element.compareTo(node.values.get(i)) < 0) {
			i--;
			
			if(i >= 0 && node.values.get(i).compareTo(element) == 0) {
				node.values.get(i).incFrequency();
				return;
			}
		}
    	
    	if(i==0 && element.compareTo(node.values.get(i)) == 0) {
    		node.values.get(i).incFrequency();
			return;
    	}
    	
    	i++;
    	
    	
    	// if node is a leaf then we can insert 
    	if(node.isLeaf()) {
			node.values.add(i, element);
			element.incFrequency();
			//writeBTreeObject(node);
    	}
    	
    	// if node is not a leaf then we cannot insert and we need to determine the correct child to descend the tree
    	if(!node.isLeaf()) {   		
    		// if child is full
    		if(node.children.get(i).isFull()) { 
				if (node.children.get(i).values.get(t-1).compareTo(element) == 0) {
					node.children.get(i).values.get(t-1).incFrequency();
					return; 
				}
    			split(node, i); // split in the book uses index i
    			if(element.compareTo(node.values.get(i)) > 0)
    				i++;
    		}
    		
    	insertNonFull(node.children.get(i), element);
    	
    	}
    }
    
    public TreeObject search(BTreeNode node, long key) {
    	int i = 0;
    	
    	while(i < node.values.size() && key > node.values.get(i).getKey()) {
    		i++;
    	}
    	
    	if(i < node.values.size() && key == node.values.get(i).getKey()) {
    		
		}
		return null;
    }
    

	public void printToFile() {
		try{
			Path current = Paths.get("");
			String filePath = current.toAbsolutePath().toString();
			
			
			filePath += "/dump";
			
						
			File output = new File(filePath);
			
			if(output.exists()) {
				PrintWriter writer = new PrintWriter(filePath);
				writer.close();
			}else {
				output.createNewFile();
			}
			
			FileWriter fileWriter = new FileWriter(output);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			
			/* Put the logic for the inorder traveral here */

			printWriter.println(toString());	
			// for(int i = 0; i < tableSize; i++) {
			// 	if(table[i] != null) {
			// 		printWriter.println("table[" + i + "]: " + table[i].toString());
			// 	}
			// }
			printWriter.close();
			fileWriter.close();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}

	public String toString(){
		return toString(this.root);
	}

	// inorder 
	public String toString(BTreeNode curr){
		if(curr == null) return "";
			String result = "";
			int x;
			for (x = 0; x < curr.values.size(); x++) 
			{
				if (!curr.isLeaf()){
					result += toString(curr.children.get(x)) + curr.values.get(x).toString()+ "\n"; 
				}else{
					result += curr.values.get(x).toString()+ "\n";
				} 
			}
			if (!curr.isLeaf()){
				result += toString(curr.children.get(x));
			} 
			return result;
	}
	
	// levelorder 
/*	public String toString(BTreeNode curr){
		if(curr == null) return "";
			String result = "";
			int x;
			for (x = 0; x < curr.values.size(); x++) 
			{
				result += curr.values.get(x) + "\n"; 
				
			}
			if (!curr.isLeaf() || curr.children.size() == 0) {

				if (curr.children.size() != 0 && curr.children.size() < 2 * t) {
					for (int i = curr.children.size(); i < 2 * t; i++) {
						BTreeNode newNode = new BTreeNode(true, t); 
						if (!curr.children.get(0).isLeaf()) {
							newNode.setLeaf(false);
						}
						
						
						curr.children.add(newNode);
					}
				}
				if (curr.children.size() == 0) {
					result += "BLANK\n";
				}
				for (x = 0; x < curr.children.size(); x++) 
				{
					result += toString(curr.children.get(x)); 
					
				}
			}
			
			return result;
	}*/
    

	public class BTreeNode implements Serializable {
		private static final long serialVersionUID = -7504766932017737110L; 
		
		boolean leaf;
		ArrayList<TreeObject> values;
		ArrayList<Integer> offsetOfChildren;
		int offset;
		 
		
		public BTreeNode() {
			values = new ArrayList<TreeObject>(2*BTree.this.t - 1);
			offsetOfChildren = new ArrayList<Integer>(2*BTree.this.t);
			leaf = false;
		}

		public BTreeNode(boolean isLeaf, int t){
			
			leaf = isLeaf;
			values = new ArrayList<TreeObject>(2*t-1);
			offsetOfChildren = new ArrayList<Integer>(2*t);
		}
		
		public boolean isFull() {
			return values.size() == 2*t-1;
		}
		
		public boolean isLeaf(){
			return this.leaf;
		}
		
		public void setLeaf(boolean b) {
			this.leaf = b;
		}
	}

}
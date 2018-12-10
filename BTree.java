import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class BTree implements Serializable{
	int t; // minimum degree 
    BTreeNode root;
    ArrayList<BTreeNode> nodes;
    int nodeSize;   //Calculated based off t
    int nextPosition; 
    String bFile;
    static RandomAccessFile metaData;
    static RandomAccessFile bTreeOut;
    

	public BTree(int degree, String gbkFile, int seqLength) throws IOException, ClassNotFoundException {
		t = degree;
		nodeSize = 10000;
		nextPosition = 0;
		bFile = gbkFile.substring(1) + ".btree.data." + seqLength + "." + t; 
		metaData = new RandomAccessFile("metadata.bin", "rw");
		bTreeOut = new RandomAccessFile(bFile, "rw");
		root = new BTreeNode(true, t, nextPosition);
		nextPosition += nodeSize;

		diskWrite(0, root); 
		
		
		// Should probably close these files VVV
		// filling metaData with metaData
		 try {			 
             FileChannel fout = metaData.getChannel();
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

		 } catch (IOException e) {
             System.err.println(e);
             System.exit(1);

		 }
		
		
	}
	
	private void diskWrite(int pos, BTreeNode node) {

		byte[] b = serialize(node); 
		
		try {
		bTreeOut.seek(pos);
		bTreeOut.write(b);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private BTreeNode diskRead(int pos) {

		byte[] b = new byte[nodeSize];
		BTreeNode result = null;
		
		try {
			bTreeOut.seek(pos);
			bTreeOut.read(b);
			result = deserialize(b);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
		
		
		return result;
		
	}
	
	private byte[] serialize(BTreeNode node) {
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
	
	private BTreeNode deserialize(byte[] b) throws ClassNotFoundException, IOException {
		
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		ObjectInputStream is = new ObjectInputStream(in);
		
		return (BTreeNode)is.readObject();
	}

	
	private void split(BTreeNode parent, int i) {
		// create new right node and get reference to the node to be split 
		BTreeNode rightNode = new BTreeNode(true, t, nextPosition);
		nextPosition += nodeSize;

		BTreeNode leftNode = diskRead(parent.offsetOfChildren.get(i)); 
		rightNode.leaf = leftNode.leaf; 
		
		while(leftNode.values.size() > t) {
			rightNode.values.add(0,leftNode.values.remove(leftNode.values.size()-1));
		}
		// Move children from the left node to the right node if necessary 
		if (!leftNode.leaf) {
			
			while(leftNode.offsetOfChildren.size() > t) {
				rightNode.offsetOfChildren.add(0,leftNode.offsetOfChildren.remove(leftNode.offsetOfChildren.size()-1));
			}
		}

		// Set parents offsetOfChildren at i+1 to the position of the rightNode  
		parent.offsetOfChildren.add(i+1, rightNode.getOffset());

		// Pull new element up from the left node 
		parent.values.add(i, leftNode.values.remove(t - 1));
		
		//Writing all updated nodes to disk at their proper positions
		diskWrite(leftNode.getOffset(), leftNode);
		diskWrite(parent.getOffset(), parent);
		diskWrite(rightNode.getOffset(), rightNode);
		
		
	} 
    
	public void add(TreeObject element) throws IOException {    	
		// if BTree is empty
		if(root.values.size() == 0) {
			root.values.add(0, element);
			element.incFrequency(); 
			diskWrite(root.getOffset(), root);
		}
		
		// BTree is not empty
		else {
			
			// if root is full --> need to create new node and
			if(root.isFull()) {
					BTreeNode newRoot = new BTreeNode(false, t, nextPosition); // make a new node
					nextPosition += nodeSize;
					newRoot.offsetOfChildren.add(0, root.getOffset()); 
					split(newRoot, 0); //Splits the child
					insertNonFull(newRoot, element); 	
					root = newRoot;
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
			diskWrite(node.getOffset(),node);
			return;
		}
		
		// Determine correct index to insert at 
		while(i >= 0 && element.compareTo(node.values.get(i)) < 0) {
			i--;
			
			if(i >= 0 && node.values.get(i).compareTo(element) == 0) {
				node.values.get(i).incFrequency();
				diskWrite(node.getOffset(),node);
				return;
			}
		}
		
		if(i==0 && element.compareTo(node.values.get(i)) == 0) {
			node.values.get(i).incFrequency();
			diskWrite(node.getOffset(),node);
			return;
		}
		
		i++;
		
		
		// if node is a leaf then we can insert 
		if(node.isLeaf()) {
			node.values.add(i, element);
			element.incFrequency();
			// replace node in binary file with updated node
			diskWrite(node.getOffset(), node);
		}
		
		// if node is not a leaf then we cannot insert and we need to determine the correct child to descend the tree
		else {   		
			// if child is full
			BTreeNode childNode = diskRead(node.offsetOfChildren.get(i));
			if(childNode.isFull()) { 
				if (childNode.values.get(t-1).compareTo(element) == 0) {
					childNode.values.get(t-1).incFrequency();
					diskWrite(childNode.getOffset(), childNode);
					return; 
				}
				split(node, i); // split in the book uses index i
				if(element.compareTo(node.values.get(i)) > 0)
					i++;
			}
			
			insertNonFull(diskRead(node.offsetOfChildren.get(i)), element);		
		}
	}
	
  
	/**
	 * Call when finished writing to BTree
	 * Writes metaData to metaData files and closes both RandomAccessFiles
	 */
	public void writeMetaData(){
		try {			 
			FileChannel fout = metaData.getChannel();
			fout.truncate(0); // truncate the file 
			
			ByteBuffer buff = ByteBuffer.allocateDirect(32 * 3);
			
			buff.putInt(root.getOffset());
			buff.putInt(t); 
			
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
			bTreeOut.close();
			metaData.close();

		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);

		}

	}
    
	public void printToFile() {
		try {
			Path current = Paths.get("");
			String filePath = current.toAbsolutePath().toString();
			
			
			filePath += "/dump";
			
						
			File output = new File(filePath);
			
			if(output.exists()) {
				PrintWriter writer = new PrintWriter(filePath);
				writer.close();
			} 
			else {
				output.createNewFile();
			}
			
			FileWriter fileWriter = new FileWriter(output);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			
			printWriter.println(toString());  //toString does inorder traversal	
			
			printWriter.close();
			fileWriter.close();
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
	}

	public String toString(){
		return toString(this.root);
	}

	// inorder 
	private String toString(BTreeNode curr){
		if(curr == null) return "";
			String result = "";
			int x;
			BTreeNode currChild = null;
			for (x = 0; x < curr.values.size(); x++) 
			{	
				if (!curr.isLeaf()){
					currChild = diskRead(curr.offsetOfChildren.get(x));
					result += toString(currChild) + curr.values.get(x).toString()+ "\n"; 
				}else{
					result += curr.values.get(x).toString()+ "\n";
				} 
			}
			if (!curr.isLeaf()){
				result += toString(diskRead(curr.offsetOfChildren.get(x)));		
			} 
			return result;
	}
	
}	
class BTreeNode implements Serializable { 
	private static final long serialVersionUID = 894345046102526781L;
	boolean leaf;
	ArrayList<TreeObject> values;
	ArrayList<Integer> offsetOfChildren;
	int offset;
	int t;

	public BTreeNode(boolean isLeaf, int t, int offset) {
		this.offset = offset;
		this.t = t;
		//nextPosition += 10000;
		leaf = isLeaf;
		values = new ArrayList<TreeObject>(2*t-1);
		offsetOfChildren = new ArrayList<Integer>(2*t);
	}
	
	public boolean isFull() {
		return values.size() == 2*this.t-1;
	}

	public void setOffset(int offset){
		this.offset = offset;
	}

	public int getOffset(){
		return offset;
	}				
	
	public boolean isLeaf(){
		return this.leaf;
	}
	
	public void setLeaf(boolean b) {
		this.leaf = b;
	}
}

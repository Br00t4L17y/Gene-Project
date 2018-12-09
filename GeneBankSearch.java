import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


import java.util.List;
import java.util.ArrayList;
import java.lang.Enum;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class GeneBankSearch {
	public static void main(String args[]) {
		System.out.print("Search program started.");

		SearchArguments arguments = AssignArguments(args);

		List<String> queryLookups = BuildListFromFile(arguments.queryFileName);

		try {
			int rootPosition = GetTreeMetadata();

			for (int i = 0; i < queryLookups.size(); i++) {
				String sequence = queryLookups.get(i);
				//System.out.println(queryLookups.get(i));
				TreeObject treeObject = new TreeObject(sequence);
				TreeObject result = Search(rootPosition, arguments.btreeFileName, treeObject);
				
				if(result == null) {
					System.out.println(queryLookups.get(i) + ": 0" );
				}
				else {
					System.out.println(result.toString());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}


	private static int GetTreeMetadata() throws FileNotFoundException, IOException {
		int position;
		
		Path relativePath = Paths.get("");
		String filePath = relativePath.toAbsolutePath().toString() + "/metadata.bin";
		RandomAccessFile file = new RandomAccessFile("metadata.bin", "r");

		file.seek(0);
		position = file.readInt();
		file.close();

		return position;
	}	


// Search is incomplete but this is the start of the books sudo-code
// Should be able to finish, use read when it reads in the book use diskRead(node.getOffset()) to retrieve
// the node you need to find. May want to change parameters to deal with TreeObjects?? or Sequences?
private static TreeObject Search(int rootPosition, String btreeFileName, TreeObject treeObject) throws FileNotFoundException {
	// System.out.println(btreeFileName);
	// System.out.println();
	RandomAccessFile accessFile = new RandomAccessFile(btreeFileName, "r");
	BTreeNode node = diskRead(rootPosition, accessFile);
	Long key = treeObject.getKey();

	return Search(node, key, accessFile);
}

private static TreeObject Search(BTreeNode node, Long key, RandomAccessFile accessFile) {
	int i = 0;
	
	while(i < node.values.size() && key > node.values.get(i).getKey()) {
		i++;
	}

	if(i < node.values.size() && key == node.values.get(i).getKey()) {
		return node.values.get(i);
	}
	else if(node.isLeaf()) {
		return null;
	}
	else {
		BTreeNode n = diskRead(node.offsetOfChildren.get(i), accessFile);
		return Search(n, key, accessFile);
	}
}

private static BTreeNode diskRead(int position, RandomAccessFile file) {
	byte[] b = new byte[10000];
	BTreeNode result = null;
	
	try {
		file.seek(position);
		file.read(b);
		result = deserialize(b);
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	catch(ClassNotFoundException cnfe){
		cnfe.printStackTrace();
	}
	
	return result;	
}

private static BTreeNode deserialize(byte[] b) throws ClassNotFoundException, IOException {
		
	ByteArrayInputStream in = new ByteArrayInputStream(b);
	ObjectInputStream is = new ObjectInputStream(in);
	
	return (BTreeNode)is.readObject();
}

private static SearchArguments AssignArguments(String[] args) {
		SearchArguments retVal = new SearchArguments();
		
		try {
			if (args.length < 3 || (Integer.parseInt(args[0]) == 1 && args.length < 4))	{
				throw new IllegalArgumentException();
			}
			retVal.cache = Integer.parseInt(args[0]) == 1;
			retVal.btreeFileName = args[1];
			retVal.queryFileName = args[2];

			if (retVal.cache)	{
				retVal.cachSize = Integer.parseInt(args[3]);
			}
			else {
				if (args.length > 3) {
					retVal.debug = Integer.parseInt(args[3]) == 1;
				}
			}

			if (args.length > 4) {
				retVal.debug = Integer.parseInt(args[4]) == 0;
			}
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return retVal;
	}

	private static List<String> BuildListFromFile(String fileName) {
		Path relativePath = Paths.get("");
		String filePath = relativePath.toAbsolutePath().toString() + fileName;

		List<String> sequenceList = new ArrayList<String>();
		try {
			Scanner reader = new Scanner(new FileReader(filePath));
			
			while(reader.hasNextLine()) {
				String line = reader.nextLine();
				sequenceList.add(line);
			}

			reader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		
		return sequenceList;
	}
}
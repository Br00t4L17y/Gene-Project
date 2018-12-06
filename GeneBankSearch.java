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

		List<String> querySequences = BuildStringFromFile(arguments.queryFileName);
		
		for (int i = 0; i < querySequences.size(); i++) {
			String sequence = querySequences.get(i);
			System.out.println(querySequences.get(i));
			// TreeObject treeObject = new TreeObject(sequence);
			// TreeObject result = GeneBankCreateBTree.search(arguments.btreeFileName, treeObject);
			
			// if(result == null){
			// 	System.out.println(querySequences.get(i) + ": 0" );
			// }else{
			// 	System.out.println(result.toString());
			// }
			

		}
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

	private static List<String> BuildStringFromFile(String fileName) {

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
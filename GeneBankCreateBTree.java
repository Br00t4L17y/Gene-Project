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

public class GeneBankCreateBTree {

	public static void main(String args[]) throws IOException, ClassNotFoundException {
		
		CreateArguments arguments = AssignArguments(args);
		List<String> nucleotideSequences = BuildStringFromFile(arguments.fileName);

		//String binaryFileName = arguments.fileName + ".btree." + arguments.sequence + "." + arguments.degree; 
		//ObjectOutput out = new ObjectOutputStream(new FileOutputStream("test.ser"));

		BTree tree = new BTree(arguments.degree, arguments.fileName, arguments.sequence);
	
		
		for (int i = 0; i < nucleotideSequences.size(); i++) {
			String sequence = nucleotideSequences.get(i);
			int seqLength = arguments.sequence;

			if (sequence.length() >= seqLength) {
				int iterations = sequence.length() - seqLength + 1;
				for (int j = 0; j < iterations; j++) {
					System.out.println(sequence.substring(j, seqLength + j));
					TreeObject treeObject = new TreeObject(sequence.substring(j, seqLength + j));
					tree.add(treeObject);
				}
			}
		};

		if (arguments.debug) {
			tree.printToFile();
		} 

	}

	private static CreateArguments AssignArguments(String[] args) {
		CreateArguments retVal = new CreateArguments();
		
		try {
			if (args.length < 4 || (Integer.parseInt(args[0]) == 1 && args.length < 5))	{
				throw new IllegalArgumentException();
			}
			retVal.cache = Integer.parseInt(args[0]) == 1;
			retVal.degree = Integer.parseInt(args[1]);
			retVal.fileName = args[2];
			retVal.sequence = Integer.parseInt(args[3]);
			if (retVal.cache)	{
				retVal.cachSize = Integer.parseInt(args[4]);
			}
			else {
				if (args.length > 4) {
					retVal.debug = Integer.parseInt(args[4]) == 1;
				}
			}
			if (args.length > 5) {
				retVal.debug = Integer.parseInt(args[5]) == 1;
			}
		


		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return retVal;
	}

	private static List<String> BuildStringFromFile(String fileName) {
		String headTag = "ORIGIN";
		String tailTag = "//";
		boolean found = false;

		Path relativePath = Paths.get("");
		String filePath = relativePath.toAbsolutePath().toString() + fileName;
		String plainTextFile = ConvertFileToText(filePath);

		StringBuilder sequenceBuilder = new StringBuilder();

		try {
			Scanner reader = new Scanner(new FileReader(plainTextFile));
			while(reader.hasNextLine()) {
				String line = reader.nextLine();
				if (!found) {
					if (line.contains(headTag)) {
						found = true;
					}
				}
				else if (line.contains(tailTag)) {
					continue;
				}
				else {
					line = line.replaceAll(" ", "");
					line = line.replaceAll("\\d", "");
					sequenceBuilder.append(line.toUpperCase());
				}
			}

			reader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		File file = new File(plainTextFile);
		file.delete();

		
		List<String> sequenceList = new ArrayList<String>();
		int lastIndex = 0;

		for (int i = 0; i < sequenceBuilder.length(); i++) {
			String character = sequenceBuilder.substring(i, i + 1);
			try {
				Nucleotide.valueOf(character);
			} 
			catch (IllegalArgumentException e) {
				if (lastIndex != i) {
					sequenceList.add(sequenceBuilder.substring(lastIndex, i));				
				}
				lastIndex = i + 1;
			}			
		}
		sequenceList.add(sequenceBuilder.substring(lastIndex, sequenceBuilder.length()));



		return sequenceList;
	}

	private static String ConvertFileToText(String filePath) {
		String outputFileName = filePath + ".txt";
		try (BufferedReader br = new BufferedReader(new FileReader(filePath));
						PrintWriter pw = new PrintWriter(new FileWriter(outputFileName))) {
				int count = 1;
				String line;
				while ((line = br.readLine()) != null) {
						pw.printf("%03d %s%n", count, line);
						count++;
				}
		} catch (Exception e) {
				e.printStackTrace();
		}

		return outputFileName;
	}
	
}

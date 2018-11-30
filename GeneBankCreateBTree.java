import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.lang.Enum;

public class GeneBankCreateBTree {

	enum Nucleotide {
		A,
		C,
		G,
		T;
	}

	public static void main(String args[]) {
		BTree tree = new BTree(Integer.parseInt(args[1]));

		List<String> nucleotideSequences = BuildStringFromFile(args[2]);
		
		for (int i = 0; i < nucleotideSequences.size(); i++) {
			String sequence = nucleotideSequences.get(i);
			int seqLength = Integer.parseInt(args[3]);

			if (sequence.length() >= seqLength) {
				int iterations = sequence.length() - seqLength + 1;
				for (int j = 0; j < iterations; j++) {
					System.out.println(sequence.substring(j, seqLength + j));
					TreeObject treeObject = new TreeObject(sequence.substring(j, seqLength + j));
					tree.add(treeObject);
				}
			}
		};

		if (args.length > 5 && Integer.parseInt(args[5]) == 1){
			tree.printToFile();
		}

	}

	private static List<String> BuildStringFromFile(String fileName) {
		String headTag = "ORIGIN";
		String tailTag = "//";
		boolean found = false;

		Path relativePath = Paths.get("");
		String filePath = relativePath.toAbsolutePath().toString() + fileName;
		StringBuilder sequenceBuilder = new StringBuilder();

		try {
			Scanner reader = new Scanner(new FileReader(filePath));
			while(reader.hasNextLine()) {
				String line = reader.nextLine();
				if (!found) {
					if (line.equalsIgnoreCase(headTag)) {
						found = true;
					}
				}
				else if (line.equalsIgnoreCase(tailTag)) {
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
}

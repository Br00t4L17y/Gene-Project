import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class GeneBankCreateBTree {
	public static void main(String args[]) {
		BTree tree = new BTree(args[1]);

		String nucleotideSequence = BuildStringFromFile(args[2]);



		
		// for (int i = 0; i < builder.length(); i++) {
			
		// };

		System.out.println(nucleotideSequence);
	}

	private static String BuildStringFromFile(String fileName) {
		String headTag = "ORIGIN";
		String tailTag = "//";
		boolean found = false;

		Path relativePath = Paths.get("");
		String filePath = relativePath.toAbsolutePath().toString() + fileName;
		StringBuilder builder = new StringBuilder();

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
					builder.append(line);
				}
			}

			reader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		return builder.toString();
	}
}

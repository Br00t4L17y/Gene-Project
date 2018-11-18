import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class GeneBankCreateBTree {
	public static void main(String args[]) {

		String headTag = "ORIGIN";
		String tailTag = "//";
		boolean found = false;

		Path relativePath = Paths.get("");
		String filePath = relativePath.toAbsolutePath().toString() + args[0];
		try {
			StringBuilder builder = new StringBuilder();
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

			System.out.println(builder);
			reader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}
}

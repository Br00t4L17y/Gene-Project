import java.awt.List;

public class GeneBankSearch {
	public static void main(String args[]) {
		System.out.print("Search program started.");

		SearchArguments arguments = AssignArguments(args);

		List<String> querySequences = BuildStringFromFile(arguments.queryFileName);
		
		for (int i = 0; i < querySequences.size(); i++) {
			String sequence = querySequences.get(i);

			TreeObject treeObject = new TreeObject(sequence);
			TreeObject result = GeneBankCreateBTree.search(arguments.btreeFileName, treeObject);
			
			if(result == null){
				System.out.println(querySequences.get(i) + ": 0" );
			}else{
				System.out.println(result.toString());
			}
			

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
		String plainTextFile = ConvertFileToText(filePath);

		StringBuilder sequenceBuilder = new StringBuilder();

		try {
			Scanner reader = new Scanner(new FileReader(plainTextFile));
			List<String> sequenceList = new ArrayList<String>();
			while(reader.hasNextLine()) {
				String line = reader.nextLine();
				sequenceList.add(line);
			}

			reader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		File file = new File(plainTextFile);
		file.delete();
		
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
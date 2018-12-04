public class GeneBankSearch {
	public static void main(String args[]) {
		System.out.print("Search program started.");

		SearchArguments arguments = AssignArguments(args);

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
}
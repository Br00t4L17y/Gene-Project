****************
* Project 4/Bioinformatics
* CS321-003
* 12/07/2018
* AJ Trantham, Brent Metcalf, Olivia Thomas, Tyler Mathern
**************** 

OVERVIEW:

This program implements a searchable BTree using input files of known DNA sequences.
	

INCLUDED FILES:

 * Nucleotide.java - Enum file. Used to store the values for the four types of letters that represent nucleotides in a DNA sequence.
 * SearchArguments.java - Helper class file. Stores the arguments that are passed into GeneBankSearch.java
 * CreateArguments.java - Helper class file. Stores the arguments that are passed into GeneBankCreateBTree.java
 * GeneBankCreateBTree.java - Driver class file. Creates a BTree with given degree and sequence length specified by user input arguments
 * GeneBankSearch.java - Driver class file. //TODO: Add what it does.
 * BTree.java - Abstract data type. Hold all of the functionality for creating and searching the BTree. Also includes inner BTreeNode class.
 * TreeObject.java - Abstract data type. Takes in a sequence and converts it to a long binary value used as the key value.
 * README - this file


COMPILING AND RUNNING:

 *********
 * Create BTree
 *********
 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac GeneBankCreateBTree.java

 Run the compiled class file with the command:
 $ java GeneBankCreateBTree <0/1(no/with cache)> <degree> <gbkFile> <sequenceLength> [<cacheSize>] [<debugLevel>]

 where:
  * 0/1(no/with cache): //TODO: Description
	* degree: Specifies the degree of the BTree
	* gbkFile: Specifies the DNA file that will be parsed to create the TreeObjects
	* sequenceLength: Determines the length of each DNA sequence (must be between 1 and 31)
	* cacheSize: optional argument. //TODO: Description
	* debugLevel: optional argument. If 1, sequence and frequency are printed in a dump file

 Console output will give the results after the program finishes.

 *********
 * Search
 *********
 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac GeneBankSearch.java

 Run the compiled class file with the command:
 $ java GeneBankSearch <0/1(no/with cache)> <btreeFile> <queryFile> [<cacheSize>] [<debugLevel>]

 where:
  * 0/1(no/with cache): //TODO: Description
	* btreeFile: //TODO: Description
	* queryFile: //TODO: Description
	* cacheSize: optional argument. //TODO: Description
	* debugLevel: optional argument. //TODO: Description

 Console output will give the results after the program finishes.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 The most important concept of this program, is that it implements a hash table structure. This structure ensures that no duplicate items are inserted into the table,
 and quickly finds an open spot if the hashed index is already in use.

 This program inserts into an array based on a load factor. The hash table insert works as follows:
	1. A hash function is run on the first input value, which returns a valid table index.
	2. It tries to do an insert at that index.	
	3. It checks if the array has a value at that index, inserting the value if it is empty.
	4. If there is a value at that array index, it checks to see if the potential value is the same as the existing value.
	5. If the values are the same, it does not insert the potential value.
	6. If the values are not the same, a secondary hash function is run on the hashcode to get a new index value to insert at.


TESTING:

 //TODO: Testing Methods/Test Cases

 //TODO: Areas of Improvement

 //TODO: Known Bugs


DISCUSSION:
 
 //TODO: Process

 //TODO: Breakthroughs/Eurika Moments

 //TODO: How you felt about the project in whole

 
EXTRA CREDIT:
 * Implementing a cache (20 points)



 

 //TODO: Add details about implementing the cache and what it does 

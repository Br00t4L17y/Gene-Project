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
 * GeneBankSearch.java - Driver class file. Reads in a BTree binary file as well as a query file. Uses the BTree file to search for the sequences in the query file and displays the sequence and frequency to console.
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
	* btreeFile: This is a binary BTree file created by GeneBankCreateBTree
	* queryFile: Contains sequences to search for in the BTree
	* cacheSize: optional argument. Specifies the size of the cache if one is being used
	* debugLevel: optional argument. Displays output to console if set to 0 or not specified

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
 
 Our process proved to be very effective. We got everyone setup with GitHub so that we could make a project repository to all work on. A couple of us had never used Git before so there was a little quick learning that needed to be done so that we could all be on the same page. After getting Git all setup we sat down together to plan out the sections of the project and their priority. We originally decided on a 3-phase, 3-week process. Week 1 was going to be dedicated to everything we needed to read in the GeneBank file and create the BTree. Week 2 was going to be for Writing everything that had to do with Searching the BTree. Week 3 was then set to be debugging and working out any final issues. We split the tasks for phase 1 up amongst the 4 of us and then went to Thanksgiving break. Our next meeting was the final Sunday of Thanksgiving break. We got together to discuss what we had done. Debugging was mandatory as not everything was working together the right way. We worked through those issues and realized that we forgot to implement the debug option and writing to a binary file. We spent week 2 fixing issues adding and splitting, adding argument handling, implementing debug level, and trying the figure out how to writie the BTree to a binary file. The binary file created the most issues for us in this project. We spent a long time trying to figure out the concept and the implementation of it.  

 //TODO: Breakthroughs/Eurika Moments

 The concepts that we have learned throughout the course of this project have been really great to learn. I think

 
EXTRA CREDIT:
 * Implementing a cache (20 points)



 

 //TODO: Add details about implementing the cache and what it does 

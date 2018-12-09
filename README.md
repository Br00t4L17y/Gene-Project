****************
* Project 4/Bioinformatics
* CS321-003
* 12/09/2018
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
  * 0/1(no/with cache): This is where we could implement a cache. We did not.
	* degree: Specifies the degree of the BTree
	* gbkFile: Specifies the DNA file that will be parsed to create the TreeObjects
	* sequenceLength: Determines the length of each DNA sequence (must be between 1 and 31)
	* cacheSize: optional argument. Determines the size of the cache if one is being used
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
  * 0/1(no/with cache): This is where we could implement a cache. We did not.
	* btreeFile: This is a binary BTree file created by GeneBankCreateBTree
	* queryFile: Contains sequences to search for in the BTree
	* cacheSize: optional argument. Specifies the size of the cache if one is being used
	* debugLevel: optional argument. Displays output to console if set to 0 or not specified

 Console output will give the results after the program finishes.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 For this project, we need to write to and read from a binary file. This file is 
 responsible for holding our BTree structure as it is too big to be stored in 
 memory. 

  ***LAYOUT OF B-TREE FILE***
  When we write to the BTree file, we are serializing each BTreeNode and then 
  writing that array of bytes to the file. Similarly, when we read from the file,
  we deserialize the data so that we can get the BTreeNode back. Bassed off the 
  way we have structured it, The nodes do no move in the file. Instead, the 
  nodes just get updated when information changes. We also have a metadata file 
  that holds offset of the root and some other important information about the 
  tree.

  Other important concepts include the BTree structure along with its important 
  methods such as insert and search. We also made many other methods that helped 
  us accomplish insert and search. These included methods to read and write from 
  disk, serialize and deserialize our BTreeNode objects, and other methods to 
  store and retrieve variables.


TESTING:

 When we first started, testing for each of consisted of making a main method 
 for what we were working on so that we could test the functionality of our 
 sections. Once we brought everything together for the first time, we were able 
 to tweak and debug righ in the project. When it came to the issue of writing to 
 and reading from disk, we had to create a simple case outside the project in 
 order to test and debug the process. We did it this way because it was hard to 
 keep track of what everything was doing inside the depths of the project. Once 
 we got everything to work correctly on our smaller example, we then implemented 
 it into the overall project. This method of testing worked well because we only 
 had to look at code that was relevant to the issue we were trying to solve.

 I think that we could come up with a more space efficient calculation to find
 the size of each node. If we were able to write an equation that calculates
 the exact size of each serialized node and allow it to grow if it needs to be
 updated then we would not have any wasted bytes in our disk file.

 Known bugs:
	- We have to reserve a larger amount of diskspace for each node then we would like to


DISCUSSION:
 
 Our process proved to be very effective. We got everyone setup with GitHub so
 that we could make a project repository to all work on. A couple of us had 
 never used Git before so there was a little quick learning that needed to be
 done so that we could all be on the same page. After getting Git all setup we 
 sat down together to plan out the sections of the project and their priority.   
 We originally decided on a 3-phase, 3-week process. Week 1 was going to be    
 dedicated to everything we needed to read in the GeneBank file and create the 
 BTree. Week 2 was going to be for Writing everything that had to do with 
 Searching the BTree. Week 3 was then set to be debugging and working out any   
 final issues. We split the tasks for phase 1 up amongst the 4 of us and then    
 went to Thanksgiving break. Our next meeting was the final Sunday of    
 Thanksgiving break. We got together to discuss what we had done. Debugging    
 was mandatory as not everything was working together the right way. We worked   
 through those issues and realized that we forgot to implement the debug   
 option and writing to a binary file. We spent week 2 fixing issues adding and   
 splitting, adding argument handling, implementing debug level, and trying the   
 figure out how to writie the BTree to a binary file. The binary file created   
 the most issues for us in this project. We spent a long time trying to figure   
 out the concept and the implementation of it. Once we figured this out, we   
 were able to go ahead and  write the search method that retrieves the   
 BTreeNode objects from the binary file and reports its sequence and frequency   
 to the console.

 There were definately a few breakthrough moments for us. The biggest 
 breakthrough was understanding why we needed to write to a binary file and then 
 read the information back in. We spent a long time trying to figure out why we 
 needed to do this. We also experienced a breakthrough moment when we learned 
 how RandomAccessFile and serialization work. This was a huge help for us 
 because we were then able to implement that process for writing and reading 
 from the binary file.

 The concepts that we have learned throughout the course of this project have 
 been really great to learn. I think it would have been nice to spend a little 
 time talking about how to read and write to files along with why it is 
 necessary to do so. I think that spending some time with this concept in class 
 would make it easier to work through for this project.

 
EXTRA CREDIT:
 * Implementing a cache (20 points)

No cache

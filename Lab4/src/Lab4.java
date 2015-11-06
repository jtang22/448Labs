import java.io.*;
import java.util.*;

public class Lab4 {

	public int seqSearch(File fafstaFile, File queryFile, char reverseFlag) {
			
		SuffixTree tree = new SuffixTree();
		// Scanner fileScanner = new Scanner(System.in);
		int matches = 0, reverseMatches = 0;
		ArrayList<Integer> indexLocations = new ArrayList<Integer>();
		ArrayList<Integer> reverseIndexLocations = new ArrayList<Integer>();

		// System.out.println("Enter FAFSTA file: ");
		// File fafstaFile = new File(fileScanner.next());
		
		// System.out.println("Enter query file: "); 
		// File queryFile = new File(fileScanner.next());
		
		// System.out.println("Do you want to reverse compliment: (y/n)");
		// char reverseFlag = fileScanner.next().charAt(0);
		try {
			// Initialize input scanners
			Scanner fafstaScanner = new Scanner(fafstaFile);
			Scanner queryScanner = new Scanner(queryFile);
			
			// Initialize output writers
			String outputFile = fafstaFile.getName().replace (".txt", ("Matches.txt"));
            Writer outputWriter = new FileWriter(outputFile);
            BufferedWriter outputBufferWriter = new BufferedWriter(outputWriter);
			
			// Skips the first line
			fafstaScanner.nextLine();
			
			StringBuilder dnaSequence = new StringBuilder();
			while (fafstaScanner.hasNextLine()) {
				dnaSequence.append(fafstaScanner.nextLine());
			}
			
			// Creates the initial suffix tree
			tree.createTree(dnaSequence.toString());
			
			// Goes through the query file
			String currentQuery = "";
			String lastComment = "";
			while (queryScanner.hasNextLine()) {
				// currentQuery is the query sequence and is not a comment
				currentQuery = queryScanner.nextLine();
				matches = 0;
				reverseMatches = 0;
				indexLocations.clear();
				reverseIndexLocations.clear();

                System.out.println("String: " + currentQuery);
                
				// Line is a comment
				if (currentQuery.charAt(0) == '>') {
					lastComment = currentQuery;
					outputBufferWriter.write(lastComment);
					outputBufferWriter.newLine();
				}
				// Line is a query sequence
				else {
					ArrayList<String> possibleStrings = substituteCharacters(currentQuery);
					if (possibleStrings != null) {
						// Go through each possible sequence and see if it exists
						//System.out.println(possibleStrings.size());
						for (int i = 0; i < possibleStrings.size(); i++) {
							// Check to see if query is in dnaseq
							if (tree.findSuffix(possibleStrings.get(i)) != -1) {
								matches += tree.findSuffix(possibleStrings.get(i));
								
								// Print index of the match
								indexLocations.addAll(tree.getLocations());
							}

							if(reverseFlag == 'y') {
								String reverseComp = reverseComplement(possibleStrings.get(i));
								if (tree.findSuffix(reverseComp) != -1) {
									reverseMatches += tree.findSuffix(reverseComp);
								
									// Print index of reverse match 
									reverseIndexLocations.addAll(tree.getLocations());
								}
							}

						}
                        
						outputBufferWriter.write("Matches : " + matches + "\n");
						for (int j = 0; j < indexLocations.size(); j++) {
							outputBufferWriter.write("" + ((indexLocations.get(j) + 1) -  (currentQuery.length() - 1)) + "\n");
						}

						if(reverseFlag == 'y') {
							outputBufferWriter.write("Reverse Matches : " + reverseMatches + "   REVERSE \n");
							for (int j = 0; j < reverseIndexLocations.size(); j++) {
								outputBufferWriter.write("" + ((reverseIndexLocations.get(j) + 1) - (currentQuery.length() - 1))+ "\n");
							}
						}
						outputBufferWriter.newLine();
					}
				}
			}
			outputBufferWriter.flush();
			fafstaScanner.close();
			queryScanner.close();
			outputBufferWriter.close();
			outputWriter.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("File(s) not found.");
			return 1;

		}
		catch (IOException e) {
			System.out.println("Error occurred, cannot write to file.");
			return 1;

		}
		return 0;
	}
	
	/*
	 * Helper method for reverse complementing a query sequence string
	 */
	private static String reverseComplement(String strand) {
		StringBuilder revComp = new StringBuilder();
		char[] dna = strand.toCharArray();
		for (int i = 0; i < strand.length(); i++) {
			
			if (dna[i] == 'A')
				revComp.append("T");
			else if (dna[i] == 'T')
				revComp.append("A");
			else if (dna[i] == 'C')
				revComp.append("G");
			else
				revComp.append("C");
		}
		
		return revComp.reverse().toString(); 
	}
	
	/* Looks to see if special characters occur only twice.
	 * Returns an arraylist of up to two integers representing the indexes at which they were found.
	 * Returns null if there's more than 2 indexes. 
	 */
	private static ArrayList<Integer> validateSuffix(String suffix) {
		int count = 0;
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		String symbolString = "WSMKRY";
		for(int ndx = 0; ndx < suffix.length(); ndx++) {
			
			if(symbolString.contains(suffix.charAt(ndx) + "")) {
				indexes.add(ndx);
				count++;
			}
			if(count > 2) {
				return null;
			}
		}
		
		// If there were no special characters at all
		if (count == 0) {
			return null;
		}
		return indexes;
	}
	
	/* Takes the special character(s) and replaces them.
	 * Returns an arraylist of all the new possible strings.
	 * Returns null if invalid suffix characters. 
	 */
	public static ArrayList<String> substituteCharacters(String suffix) {
		ArrayList<Integer> indexes = validateSuffix(suffix);
		ArrayList<String> possibleStrings = new ArrayList<String>();
		if (indexes != null && indexes.size() != 0) {
			for (int i = 0; i < indexes.size(); i++) {
				char specialChar = suffix.charAt(indexes.get(i));
				// There's a second special character
				if (i == 1) {
					char[] string1 = possibleStrings.get(0).toCharArray();
					char[] string2 = possibleStrings.get(1).toCharArray();
					if (specialChar == 'W') {
						
						string1[indexes.get(i)] = 'A';
						possibleStrings.add(new String(string1));
						string1[indexes.get(i)] = 'T';
						possibleStrings.add(new String(string1));
						
						string2[indexes.get(i)] = 'A';
						possibleStrings.add(new String(string2));
						string2[indexes.get(i)] = 'T';
						possibleStrings.add(new String(string2));
						
					} 
					else if (specialChar == 'S') {
						
						string1[indexes.get(i)] = 'C';
						possibleStrings.add(new String(string1));
						string1[indexes.get(i)] = 'G';
						possibleStrings.add(new String(string1));
						
						string2[indexes.get(i)] = 'C';
						possibleStrings.add(new String(string2));
						string2[indexes.get(i)] = 'G';
						possibleStrings.add(new String(string2));
						
					} 
					else if (specialChar == 'M') {
						
						string1[indexes.get(i)] = 'A';
						possibleStrings.add(new String(string1));
						string1[indexes.get(i)] = 'C';
						possibleStrings.add(new String(string1));
						
						string2[indexes.get(i)] = 'A';
						possibleStrings.add(new String(string2));
						string2[indexes.get(i)] = 'C';
						possibleStrings.add(new String(string2));
						
					} 
					else if (specialChar == 'K') {
						
						string1[indexes.get(i)] = 'G';
						possibleStrings.add(new String(string1));
						string1[indexes.get(i)] = 'T';
						possibleStrings.add(new String(string1));
						
						string2[indexes.get(i)] = 'G';
						possibleStrings.add(new String(string2));
						string2[indexes.get(i)] = 'T';
						possibleStrings.add(new String(string2));
						
					}
					else if (specialChar == 'R') {
						
						string1[indexes.get(i)] = 'A';
						possibleStrings.add(new String(string1));
						string1[indexes.get(i)] = 'G';
						possibleStrings.add(new String(string1));
						
						string2[indexes.get(i)] = 'A';
						possibleStrings.add(new String(string2));
						string2[indexes.get(i)] = 'G';
						possibleStrings.add(new String(string2));
							
					}
					else if (specialChar == 'Y') {
						string1[indexes.get(i)] = 'C';
						possibleStrings.add(new String(string1));
						string1[indexes.get(i)] = 'T';
						possibleStrings.add(new String(string1));
						
						string2[indexes.get(i)] = 'C';
						possibleStrings.add(new String(string2));
						string2[indexes.get(i)] = 'T';
						possibleStrings.add(new String(string2));
					}
					
					// Removes the previous two unmodified strings 
					possibleStrings.remove(0);
					possibleStrings.remove(0);
				}
				// Got through the first pass, replacing one special character and coming out with two strings
				else {
					if (specialChar == 'W') {
						char[] firstString = suffix.toCharArray();
						firstString[indexes.get(i)] = 'A';
						possibleStrings.add(new String(firstString));
						firstString[indexes.get(i)] = 'T';
						possibleStrings.add(new String(firstString));
					} 
					else if (specialChar == 'S') {
						char[] firstString = suffix.toCharArray();
						firstString[indexes.get(i)] = 'C';
						possibleStrings.add(new String(firstString));
						firstString[indexes.get(i)] = 'G';
						possibleStrings.add(new String(firstString));
					}
					else if (specialChar == 'M') {
						char[] firstString = suffix.toCharArray();
						firstString[indexes.get(i)] = 'A';
						possibleStrings.add(new String(firstString));
						firstString[indexes.get(i)] = 'C';
						possibleStrings.add(new String(firstString));
					}
					else if (specialChar == 'K') {
						char[] firstString = suffix.toCharArray();
						firstString[indexes.get(i)] = 'G';
						possibleStrings.add(new String(firstString));
						firstString[indexes.get(i)] = 'T';
						possibleStrings.add(new String(firstString));
					}
					else if (specialChar == 'R') {
						char[] firstString = suffix.toCharArray();
						firstString[indexes.get(i)] = 'A';
						possibleStrings.add(new String(firstString));
						firstString[indexes.get(i)] = 'G';
						possibleStrings.add(new String(firstString));
					}
					else if (specialChar == 'Y') {
						char[] firstString = suffix.toCharArray();
						firstString[indexes.get(i)] = 'C';
						possibleStrings.add(new String(firstString));
						firstString[indexes.get(i)] = 'T';
						possibleStrings.add(new String(firstString));
					}
				}
			}
		}
		else {
			// Invalid amount of special characters, return original query
			possibleStrings.add(suffix);
		}
		return possibleStrings;
	}
}

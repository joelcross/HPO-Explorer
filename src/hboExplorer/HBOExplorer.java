package hboExplorer;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

/*
 * This class reads and parses the data from the ontology’s text file.
 * Then, it reads a set of queries from a text file and prints the query results
 * to a different text file. It also prints the the info of all terms belonging 
 * to the longest path to a different text file.
 */
public class HBOExplorer {
	// initialize "terms" array- contains "id" and "is_a" data for each term
	private static Term[] terms = new Term[13941];
	//initialize "termData" array- contains all info for each term
	private static String[] termData = new String[13941];
	
	
	// this method reads the HPO.txt file and parses the data
	private static void readData() throws IOException {
		// initialize new file and specify the file path
        File file = new File("src/input/HPO.txt");
        
        try {
        	// initialize new buffered reader to read the data file
        	BufferedReader br = new BufferedReader(new FileReader(file));
        	
        	// read lines until there are none left in the file
            String line;
            String next;
            int count = 0;
            while ((line = br.readLine()) != null) {
            	// go through each term
            	// create new arraylist which will contain all data for one term
            	ArrayList<String> list = new ArrayList();
            	// find each term in the data
            	if (line.contentEquals("[Term]")) {
            		// while there are still more lines
            		while ((next = br.readLine()) != null ) {
                		list.add(next); // add the data on the next line to the list
                		if (next.contentEquals("")) // if next line is empty
                			break; // we are done reading data for that term
                	}
            		// add new term object to term array, using the data collected for that term
                	terms[count] = new Term(list);
                	// put the term's data together in one string in an organized format
                	String data = "";
                	for (int x=0; x<list.size(); x++) {
                		data = data + list.get(x) + "\n";
                		termData[count] = data; // add all data for that term to the termData array
                	}
                	count++; // increase count so the next term can be added to the array
            	}
            }            
            br.close(); // close buffered reader
        }
        catch (IOException e) {
        	System.out.println("File I/O error!");
        }
    }
	
	
	// this method reads the queries text file and writes the matching term path info to a results file
	private static void manageQueries() throws IOException {
		// initialize new files
		File queries = new File("src/input/queries.txt");
		File results = new File("src/results.txt");
		
		try {
			// create new buffered reader to read queries file
			BufferedReader br = new BufferedReader(new FileReader(queries));
			// create new printwriter to write to results file
			PrintWriter pw = new PrintWriter(results);
			
			// read queries line by line
			String line;
			while ((line = br.readLine()) != null) {
				// split query into tokens
				String[] tokens = line.split(":");
				// make new query object
				Query query = new Query(tokens[2], terms);
				// write data to file:
				pw.write("[query_answer]\n");
				// for each term in the path, go through each term in the terms array
				for (int x=0; x<query.getPath().size(); x++) {
					// print "[Term]" before the data
					pw.write("[Term]\n");
					for (int i=0; i<terms.length; i++) {
						// if the query id matches an id found in the array
						if(query.getPath().get(x).contentEquals(terms[i].getId())) {
							// write the data for that term to the results text file
							pw.write(termData[i]);
							break; // loop through each query
						}
					}
				}
			}
			pw.close(); // close print writer
			br.close(); // close buffered reader
		}
		catch(IOException e) {
			System.out.println("File I/O error!");
		}
	}
	
	
	// this method finds and writes the info of all terms belonging to the longest path to the maxpath.txt file
	private static void findLongestPath() throws IOException {
		// initialize new maxpath file
		File maxPath = new File("src/maxpath.txt");
		
		try {
			// create new print writer to write to maxPath file
			PrintWriter pw = new PrintWriter(maxPath);
			// pathLengths array will contain the length of the path from each term to the root
			int[] pathLengths = new int[13941];
			
			// go through each term
			for (int x=0; x<13941; x++) {
				// if term is obsolete, don't try to trace the path
				if (terms[x].getIs_obsolete() == 1) {
					continue;
				}
				// otherwise, perform a "query" on every term in the list
				String id = terms[x].getId();
				Query query = new Query(id, terms);
				// find the length of each term's path and add it to the pathLengths array
				pathLengths[x] = query.getPathSize();
			}
			
			// find the length and index of the term with the longest path
			int longestPathLength = 0;
			int longestPathIndex = 0;
			// loop through the pathLengths array
			for (int x=0; x<pathLengths.length; x++) {
			    if (pathLengths[x] > longestPathLength) {
			    	longestPathLength = pathLengths[x]; // get the length of the longest path
			    	longestPathIndex = x; // get the index of the term with the longest path
			    }
			}
			
			// get info for term with longest path
			String id = terms[longestPathIndex].getId();
			// perform "query" on term with the longest path in order to trace the path back to the root
			Query query2 = new Query(id, terms);
			
			// for each term belonging to the longest path:
			pw.write("[max_path=" + (longestPathLength-1) + "]\n");
			for (int x=0; x<query2.getPathSize(); x++) {
				// print "[Term]" before the data
				pw.write("[Term]\n");
				String ids = query2.getPath().get(x);
				for (int i=0; i<terms.length; i++) { // for each term in the path, print the term data
					// compare the id's from each of the terms in the longest path to 
					// every other id in order to get their index
					if (ids.contentEquals(terms[i].getId())) {
						pw.write(termData[i]); // write the data of each term in the longest path to the maxpath file
					}
				}
			}
			pw.close(); // close printwriter
		}
		catch(IOException e) {
			System.out.println("File I/O error!");
		}
	}

	
	/* This main method runs above methods to read and parse the data, read the queries
	 * and write the resulting info to a text file, and find/write the info regarding
	 * the longest path to a different text file
	 */
	public static void main(String[] args) {
		// read and parse the data
		try {
			readData();
		}
		catch (IOException e) {
        	System.out.println("File I/O error!");
        }
		
		//read the queries and write the resulting info to a text file
		try {
			manageQueries();
		}
		catch (IOException e) {
        	System.out.println("File I/O error!");
        }
		
		// find and write the info regarding the longest path to a text file
		try {
			findLongestPath();
		}
		catch (IOException e) {
        	System.out.println("File I/O error!");
        }
	}
}

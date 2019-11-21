package hboExplorer;
import java.util.ArrayList;

/*
 * This class contains a constructor for query objects and return methods
 * for the path and path size of the query.
 */
public class Query {
	// this arraylist will contain the id of each term in the path from the query to the root
	private ArrayList<String> path = new ArrayList();
	
	// Query object constructor
	public Query(String id, Term[] termsArray) {
		// run until we reach the root
		while (!(id.contentEquals("0000001"))) {
			// go through each term in the array, try to find a match for the query id
			for (int i=0; i<termsArray.length; i++) {
				String node = termsArray[i].getId();
				String parent = termsArray[i].getIs_A();
				// if the query id matches any id in the array of terms
				if (id.contentEquals(node)) {
					// add that id to the path
					path.add(id);
					// the parent now becomes the new id
					id=parent;
					break; // loops until we get to the root
				}
			}
		}
		// add the root to the end of the path
		path.add("0000001");
	}
	
	
	// returns the size of the query path
	public int getPathSize(){
		return path.size();
	}
	
	
	// returns the arraylist path of the query
	public ArrayList<String> getPath(){
		return path;
	}
}
package hboExplorer;
import java.util.ArrayList;

/*
 * This class contains a constructor for term objects and return methods for
 * a term's "id", "is_a", and "is_obsolete" values.
 */
public class Term {
	private String id; // the id of the term being queried
	private String is_a; // the "is_a" id of the term being queried
	private int isObsolete = 0; // the 0 specifies that the term is not (yet) obsolete
	
	
	// Term object constructor
	 public Term(ArrayList<String> list) {
		 // go through each line of data for the term
		 for (int x=0; x<list.size(); x++) {
			 // split line into tokens (where ":" is the delimiter)
			 String[] tokens = list.get(x).split(":");
			 
			 // if the line starts with "id"
			 if (tokens[0].contentEquals("id")) {
				 // assign the id value to the term's "id" variable
				 this.id = tokens[2];
			 }
			 
			 // if the line starts with "is_a"
			 if (tokens[0].contentEquals("is_a")) {
				 // split the third token of the line into more tokens to 
				 // isolate the "is_a" id value from the irrelevant text after it
				 String[] moreTokens = tokens[2].split(" ");
				// assign the "is_a" id value to the term's "is_a" variable
				 this.is_a = moreTokens[0];
				 break; // reads only the first "is_a" statement
			 }
			 
			// if the line starts with "is_obsolete"
			 if (tokens[0].contentEquals("is_obsolete")) {
				 // the 1 specifies that the term is obsolete, so we want to skip it
				 this.isObsolete = 1;
			 }
		 }
	 }
	 
	 
	 // returns the term's "id" value
	 public String getId() {
		 return this.id;
	 }
	 
	 
	 // returns the term's "is_a" id value
	 public String getIs_A() {
		 return this.is_a;
	 }
	 
	 
	 // returns a 0 or 1 depending on whether or not the term is obsolete
	 public int getIs_obsolete() {
		 // if a 1 is returned, then the term is obsolete
		 // if a 0 is returned, then the term is not obsolete
		 return this.isObsolete;
	 }
}
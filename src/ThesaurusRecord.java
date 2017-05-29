
/////////////////////////////////////////////////////////////////////////////
// Semester: CS367 Spring 2017
// PROJECT: Program 2
// FILE: FileIterator.java, FileLine.java, FileLinePriorityQueue.java, 
//       MinPriorityQueueADT.java, PriorityQueueEmptyException.java,
//       PriorityQueueFullException.java, Record.java, Reducer.java,
//		 ThesaurusRecord.java, WeatherRecord.java
//
// TEAM: Team 57a import teamName
// Authors: Matthew Schmude, Xuezhan Yan
// Author1: Matthew Schmude, schmude@wisc.edu, 9074395576, Lec 002
// Author2: Xuezhan Yan, xyan56@wisc.edu, 9074973794, Lec 002
//
// ---------------- OTHER ASSISTANCE CREDITS
// Persons: Identify persons by name, relationship to you, and email.
// Describe in detail the the ideas and help they provided.
//
// Online sources: avoid web searches to solve your problems, but if you do
// search, be sure to include Web URLs and description of
// of any information you find.
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The ThesaurusRecord class is the child class of Record to be used when merging thesaurus data.
 */

public class ThesaurusRecord extends Record {
	private String word; // store word
	// store all synonym in the specific word
	private ArrayList<String> recordList = new ArrayList<String>();

	/**
	 * Constructs a new ThesaurusRecord by passing the parameter to the parent constructor and then
	 * calling the clear method()
	 */
	public ThesaurusRecord(int numFiles) {
		super(numFiles);
		clear();
	}

	/**
	 * This Comparator should simply behave like the default (lexicographic) compareTo() method for
	 * Strings, applied to the portions of the FileLines' Strings up to the ":" The getComparator()
	 * method of the ThesaurusRecord class will simply return an instance of this class.
	 */
	private class ThesaurusLineComparator implements Comparator<FileLine> {
		public int compare(FileLine l1, FileLine l2) {
			String[] L1 = l1.getString().split(":");
			String[] L2 = l2.getString().split(":");
			return L1[0].compareTo(L2[0]); // only need to compare word part
		}

		public boolean equals(Object o) {
			return this.equals(o);
		}
	}

	/**
	 * This method should simply create and return a new instance of the ThesaurusLineComparator
	 * class.
	 */
	public Comparator<FileLine> getComparator() {
		return new ThesaurusLineComparator();
	}

	/**
	 * This method should (1) set the word to null and (2) empty the list of synonyms.
	 */
	public void clear() {
		word = null; // set the word to null
		// empty the list of synonyms
		while (!recordList.isEmpty())
			recordList.remove(0);
	}

	/**
	 * This method should parse the list of synonyms contained in the given FileLine and insert any
	 * which are not already found in this ThesaurusRecord's list of synonyms.
	 */
	public void join(FileLine w) {
		String[] W = w.getString().split(":");
		word = W[0]; // update word, but it will always contain the specific "key"
		// add every synonyms to the list
		String[] synonym = W[1].split(",");
		for (int i = 0; i < synonym.length; i++)
			recordList.add(synonym[i]);
	}

	/**
	 * See the assignment description and example runs for the exact output format.
	 */
	public String toString() {
		if (word != null) {
			removeDuplicate(recordList);
			String output = word + ":" + recordList.get(0);
			// Arrays.sort(recordList); // cannot use Arrays.sort() to sort arraylist
			Collections.sort(recordList); // sort list alphabetically
			for (int i = 1; i < recordList.size(); i++)
				output += "," + recordList.get(i);
			return output + "\n";
		} else
			return "";
	}

	/**
	 * private helper method to remove any duplicate synonym from the list
	 * 
	 * @param al
	 *            the list that need to remove duplicate synonym
	 */
	private void removeDuplicate(ArrayList al) {
		for (int i = 0; i < al.size(); i++) {
			for (int j = i + 1; j < al.size(); j++) {
				if (al.get(i).equals(al.get(j))) {
					al.remove(j);
					j--;
				}
			}
		}
	}
}

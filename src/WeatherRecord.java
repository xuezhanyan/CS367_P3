
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
import java.util.Comparator;

/**
 * The WeatherRecord class is the child class of Record to be used when merging weather data.
 * Station and Date store the station and date associated with each weather reading that this object
 * stores. l stores the weather readings, in the same order as the files from which they came are
 * indexed.
 */
public class WeatherRecord extends Record {
	private String s; // store station
	private String d; // store date
	private String[] recordList = new String[super.getNumFiles()]; // store data point

	/**
	 * Constructs a new WeatherRecord by passing the parameter to the parent constructor and then
	 * calling the clear method()
	 */
	public WeatherRecord(int numFiles) {
		super(numFiles);
		clear();
	}

	/**
	 * This comparator should first compare the stations associated with the given FileLines. If
	 * they are the same, then the dates should be compared.
	 */
	private class WeatherLineComparator implements Comparator<FileLine> {
		public int compare(FileLine l1, FileLine l2) {
			String[] L1 = l1.getString().split(",");
			String[] L2 = l2.getString().split(",");
			if (L1[0].compareTo(L2[0]) == 0)
				return L1[1].compareTo(L2[1]);
			else
				return L1[0].compareTo(L2[0]);
		}

		public boolean equals(Object o) {
			return this.equals(o);
		}
	}

	/**
	 * This method should simply create and return a new instance of the WeatherLineComparator
	 * class.
	 */
	public Comparator<FileLine> getComparator() {
		return new WeatherLineComparator();
	}

	/**
	 * This method should fill each entry in the data structure containing the readings with
	 * Double.MIN_VALUE
	 */
	public void clear() {
		s = null;
		d = null;
		// clear the list
		for (int i = 0; i < recordList.length; i++)
			recordList[i] = null;
	}

	/**
	 * This method should parse the String associated with the given FileLine to get the station,
	 * date, and reading contained therein. Then, in the data structure holding each reading, the
	 * entry with index equal to the parameter FileLine's index should be set to the value of the
	 * reading. Also, so that this method will handle merging when this WeatherRecord is empty, the
	 * station and date associated with this WeatherRecord should be set to the station and date
	 * values which were similarly parsed.
	 */
	public void join(FileLine li) {
		String[] Li = li.getString().split(",");
		s = Li[0]; // update station
		d = Li[1]; // update date
		recordList[li.getFileIterator().getIndex()] = Li[2]; // update data point list
	}

	/**
	 * See the assignment description and example runs for the exact output format.
	 */
	public String toString() {
		// this if handle the special case that the first time whether the key exist in record
		if (s != null && d != null) {
			String output = s + "," + d;
			for (int i = 0; i < recordList.length; i++) {
				if (recordList[i] == null)
					output += ",-";
				else
					output += "," + Double.parseDouble(recordList[i]);
			}
			return output + "\n";
		} else
			return "";
	}
}

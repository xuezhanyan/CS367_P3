
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

import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * Reducer solves the following problem: given a set of sorted input files (each containing the same
 * type of data), merge them into one sorted file.
 *
 */
public class Reducer {
	// list of files for stocking the PQ
	private List<FileIterator> fileList;
	private String type, dirName, outFile;

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: java Reducer <weather|thesaurus> <dir_name> <output_file>");
			System.exit(1);
		}

		String type = args[0];
		String dirName = args[1];
		String outFile = args[2];

		Reducer r = new Reducer(type, dirName, outFile);
		r.run();

	}

	/**
	 * Constructs a new instance of Reducer with the given type (a string indicating which type of
	 * data is being merged), the directory which contains the files to be merged, and the name of
	 * the output file.
	 */
	public Reducer(String type, String dirName, String outFile) {
		this.type = type;
		this.dirName = dirName;
		this.outFile = outFile;
	}

	/**
	 * Carries out the file merging algorithm described in the assignment description.
	 */
	public void run() {
		File dir = new File(dirName);
		File[] files = dir.listFiles();
		Arrays.sort(files);

		Record r = null;

		// list of files for stocking the PQ
		fileList = new ArrayList<FileIterator>();

		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile() && f.getName().endsWith(".txt")) {
				fileList.add(new FileIterator(f.getAbsolutePath(), i));
			}
		}

		switch (type) {
			case "weather":
				r = new WeatherRecord(fileList.size());
				break;
			case "thesaurus":
				r = new ThesaurusRecord(fileList.size());
				break;
			default:
				System.out.println("Invalid type of data! " + type);
				System.exit(1);
		}

		try {
			// SHOULD NOT ADD ".txt" BC outFile HAS ALREADY CONTAINS ".txt"
			File file = new File(outFile + ".txt");
			PrintWriter pw = new PrintWriter(file);
			// Take one entry from each input file and insert them into the queue.
			Comparator<FileLine> cmp = r.getComparator();
			FileLinePriorityQueue heap = new FileLinePriorityQueue(fileList.size(), cmp);
			for (int i = 0; i < fileList.size(); i++) {
				heap.insert(fileList.get(i).next());
			}
			// While the queue is nonempty: remove the minimum entry e from the queue
			while (!heap.isEmpty()) {
				// compare its key to the key associated with r
				FileLine thisFileLine = heap.removeMin();
				if (cmp.compare(new FileLine(r.toString(), null), thisFileLine) == 0) {
					// If they are the same (or r is empty), merge e with r.
					r.join(thisFileLine);
				} else {
					// Otherwise, write r to the output file, clear r, and then merge e with r.
					pw.print(r.toString());
					r.clear();
					r.join(thisFileLine); // pass this new key entry to r
				}
				// Either way, take the next entry e' from the same file from which e came and
				// insert it
				// into the queue
				if (fileList.get(thisFileLine.getFileIterator().getIndex()).hasNext()) {
					heap.insert(fileList.get(thisFileLine.getFileIterator().getIndex()).next());
				} else {
					// if this file has been exhausted, do nothing
				}
			}
			// Write r to the output file (this step is necessary because otherwise the last record
			// would never get written).
			pw.print(r.toString());
			pw.close();
		} catch (PriorityQueueFullException e) {
			System.out.println("PriorityQueueFullException");
			return;
		} catch (PriorityQueueEmptyException e) {
			System.out.println("PriorityQueueEmptyException");
			return;
		} catch (Exception e) {
			System.out.println("otherException");
			return;
		}
	}
}

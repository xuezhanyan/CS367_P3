
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

import java.util.Comparator;

/**
 * An implementation of the MinPriorityQueueADT interface. This implementation stores FileLine
 * objects. See MinPriorityQueueADT.java for a description of each method.
 *
 */
public class FileLinePriorityQueue implements MinPriorityQueueADT<FileLine> {
	private int numItems; // num item in the list
	private FileLine[] heap; // priority queue
	private Comparator<FileLine> cmp; // Comparator
	private int maxSize; // max size of the heap

	/**
	 * Constructor that initializing all fields and create the heap that can store initialSize items
	 * 
	 * @param initialSize
	 *            num of item need to be in the heap
	 * @param cmp
	 *            comparator used to compare priority
	 */
	public FileLinePriorityQueue(int initialSize, Comparator<FileLine> cmp) {
		this.cmp = cmp;
		maxSize = initialSize;
		numItems = 0;
		heap = new FileLine[initialSize + 1]; // make sure initialSize items in heap
	}

	/**
	 * Removes the minimum element from the Priority Queue, and returns it.
	 *
	 * @return the minimum element in the queue, according to the compare() method of the Comparator
	 *         passed in the constructor.
	 * @throws PriorityQueueEmptyException
	 *             if the priority queue has no elements in it
	 */
	public FileLine removeMin() throws PriorityQueueEmptyException {
		if (isEmpty())
			throw new PriorityQueueEmptyException();
		int position = 1;
		// remove from root (save for return)
		FileLine item = heap[1];
		// replace root with last item (keep shape)
		heap[1] = heap[numItems];
		heap[numItems] = null; // may not need this step
		numItems--;
		// reheapify: swap down from root
		while (2 * position <= numItems) {
			// if this is larger than left
			if (cmp.compare(heap[position], heap[position * 2]) > 0) {
				// if this have no right child, swap with left
				if ((2 * position + 1) > numItems) {
					swap(position, 2 * position);
					position = 2 * position;
				} else {
					// if this larger than right
					if (cmp.compare(heap[position], heap[position * 2 + 1]) > 0) {
						// if left is larger than right, swap with right
						if (cmp.compare(heap[position * 2], heap[position * 2 + 1]) > 0) {
							swap(position, 2 * position + 1);
							position = 2 * position + 1;
						} else {
							// if left is smaller than right, swap with left
							swap(position, 2 * position);
							position = 2 * position;
						}
					} else {
						// if this no larger than right (only larger than left), swap left
						swap(position, 2 * position);
						position = 2 * position;
					}
				}
			} else {
				// if this have no right child
				if ((2 * position + 1) > numItems)
					break;
				else {
					// if this is larger than right
					if (cmp.compare(heap[position], heap[position * 2 + 1]) > 0) {
						swap(position, 2 * position + 1);
						position = 2 * position + 1;
					} else
						// if this is not larger than both left and right
						break;
				}
			}
		}
		return item;
	}

	/**
	 * Inserts a FileLine into the queue, making sure to keep the shape and order properties intact.
	 *
	 * @param fl
	 *            the FileLine to insert
	 * @throws PriorityQueueFullException
	 *             if the priority queue is full.
	 */
	public void insert(FileLine fl) throws PriorityQueueFullException {
		if (fl == null)
			throw new IllegalArgumentException();
		if (numItems == heap.length - 1)
			throw new PriorityQueueFullException();
		// place new item in the last pos (keep shape)
		heap[numItems + 1] = fl;
		numItems++;
		// reheapify: fix order with parent until done (at root or parent >= child)
		int position = numItems;
		while (position > 1) {
			if (cmp.compare(heap[position], heap[position / 2]) < 0) {
				// if parent < child, swap, and then prepare to compare pare and pare's pare
				swap(position, position / 2);
				position = position / 2;
			} else
				break;
		}
	}

	/**
	 * Checks if the queue is empty. e.g.
	 * 
	 * <pre>
	 * {@code
	 * m = new MinPriorityQueue(); 
	 * // m.isEmpty(): true
	 * m.insert(FileLine fl);
	 * // m.isEmpty(): false
	 * m.remove();
	 * // m.isEmpty(): true
	 * }
	 * </pre>
	 *
	 * @return true, if it is empty; false otherwise
	 */
	public boolean isEmpty() {
		return numItems == 0;
	}

	/**
	 * Private helper method to swap contents in the position of index1 and index2
	 * 
	 * @param index1
	 *            index of first element
	 * @param index2
	 *            index of second element
	 */
	private void swap(int index1, int index2) {
		FileLine temp = heap[index1];
		heap[index1] = heap[index2];
		heap[index2] = temp;
	}
}

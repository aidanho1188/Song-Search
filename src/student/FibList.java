package student;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.jdi.Value;


/*
 * Fibonacci List
 * 
 * Aidan Ho and Billy Susanto
 * 
 * Initial starting code by Prof. Boothe 2020
 *
 * To an external user a Fibonacci List appears as a single sorted list ordered
 * by the comparator. Duplicates are allowed. New items with duplicate keys
 * are added after any matching items.
 * 
 * Internally a Fibonacci List is a series of sorted arrays (called rows)
 * whose sizes grow like a Fibonacci series with each row's size equal to the
 * sum of its two predecessors. The first row holds 4 items, and thus the row
 * sizes will be: 4 4 8 12 20 32 52 84 136 220 356 576 932 ...
 * 
 * Searching starts in the largest row and only proceeds into the smaller
 * rows if a match has not yet been found. Searching is fast because it can
 * use binary search on the sorted arrays.
 * 
 * Adding starts in the smallest row but occasionally triggers merges with
 * larger rows. Adding is fast because all adds are insertions into the
 * smallest array, and large merges are infrequent.
 *   
 * The Fibonacci List is stored as an array of arrays. The main array
 * contains references to the row arrays that contain the data.
 * 
 * NOTE: the fields and nested class, that would normally be declared private
 * were declared as public so that the testing code can access them.
 */
@SuppressWarnings("unchecked")  // added to suppress type casting warnings
public class FibList<E> implements Iterable<E> {
    public static final int SmallestArraySize = 4;
    public Object[] mainArray; // really is an array of rows,
                             // but Java can't create an array of generic type 
    public int size;          // total number of elements stored
    public Comparator<E> comp;

    /*
     * nested class for rows
     * (DONE)
     */   
    public class Row {
        public int numUsed;
        public E[] items;

        /*
         * create a Row object with the specified capacity
         * (DONE)
         */
        public Row(int capacity) {
            // the items array is really an array of generic type E elements,
            // but Java can't create an array of a generic type, so it is
            // created as an array of Objects and then cast to an array of E
            numUsed = 0;
            items = (E[])new Object[capacity];
        }
    }

    /**
     * create an empty list
     * (DONE)
     */
    public FibList(Comparator<E> c) {
        comp = c;                               // save the comparator
        clear();         // creates the initial empty data structure
    }

    /**
     * clear out all data values and set back to the initial state
     * also used to create the initial empty data structure
     * (DONE)
     */
    public void clear() {
        mainArray = new Object[1];                    // 1 row to start
        mainArray[0] = new Row(SmallestArraySize);    // first row
        size = 0;       						      // no items yet 
    }

    /**
     * return the size (number of entries) in the entire data structure
     * (DONE)
     */
    public int size() {
        return size;
    }

    /**
     * check if list contains a match
     * use the findFirstInArray helper method
     */
    public boolean contains(E item) {
        // TO DO
    	// Initialize the highest index for the for loop
    	int mainArrLenght = mainArray.length - 1;
    	// For loop for checking to see if an item is within
  	    // the data structure, starting from the largest array
    	for (int i = mainArrLenght; i >= 0; i--) {
            // If the array contains the element we’re looking for
        	// it returns TRUE
			if(findFirstInRow(i, item) >= 0) {
				return true;
			}
		}
        return false; 
    }

    /*
     * Find the index of the 1st matching entry in the specified row.
     * If the item is not found, it returns a negative value
     * (-insertion point -1). 
     * The insertion point is the point at which the key would be inserted into
     * the array. This location might be after the last item in the the array.
     */
    private int findFirstInRow(int rowNum, E item) {
        // TO DO
        // Castings
        Row row = (Row) mainArray[rowNum];
        E[] items = (E[]) row.items;
        int low = 0;
        int high = row.numUsed;
        int insertPoint;
        // return if an array contains nothing
        if (items[low] == null) {
            return -1;
        }
        insertPoint = Arrays.binarySearch(items, low, high, item, comp);
        // find the first element
        int first= Math.abs(insertPoint);
        if(insertPoint <= 0) {
        	//if there element not found return insertionPoint
        	if(item.toString().length() > 1) {
        		return -insertPoint-1;
        	}
        		return insertPoint;
		} else {
			// else check for the first find in row
			while (true) {
				if (comp.compare(items[first - 1], item) == 0) {
					first = first - 1;
				} else {
					return first;
				}
			}

		}
    }
	
    /** 
     * Add this new item after any other matching items.
     * 
     * Always adds to the first row. 
     * 
     * If the first row is full, then the rows are merged up to
     * make room by clearing out the first row. Most of the time this
     * will be fast and involves 1 or just a few levels.
     *       
     * Remember to increment "size" for the whole data structure.
     * 
     * Always returns true because duplicates are allowed
     */
    public boolean add(E item) {
        // TO DO
        Row firstRow = (Row) mainArray[0];
        E[] firstRowItems = (E[]) firstRow.items;
        /*
            * If mainArray[0]'s row has space, then add. 
            * Else mergeUp, then add. 
            * Update size and numUsed after adding a new item. 
            * Sort firstRowItems
            */
        int tempSize = firstRow.numUsed;
        if (firstRow.numUsed != firstRowItems.length) {
            firstRowItems[tempSize] = item;
            size++;
            firstRow.numUsed++;
        } else { 										 // mergeUp
            mergeUp(); 	
            // then continue to add
            tempSize = 0;
            firstRowItems[0] = item;
            size++;
            firstRow.numUsed++;
        }
        Arrays.sort(firstRowItems, 0, firstRow.numUsed,comp); // sorting firstRowItems (a, fromIndex, toIndex,comp)
        return true;
    }

    /*
     * called when the first row is full
     * merge rows up just enough to empty out the first row
     * grows the main array if everything is full
     */
    private void mergeUp() {
        // TO DO
        Row firstRow = (Row) mainArray[0];
        E[] firstRowItems = (E[]) firstRow.items;
        
        // while numUsed of first row is full
        while (firstRow.numUsed == firstRowItems.length) {
            Object[] tempArray = new Object[mainArray.length];
            boolean isEmpty = false;                                        
            // Copy mainArray to tempArray
            for (int i = 0; i < mainArray.length; i++) {
                tempArray[i] = mainArray[i];
            }
            // Check if there is an empty row
            for (int i = 0; i < tempArray.length; i++) {
                Row tempRow = (Row) tempArray[i];
                if (tempRow.numUsed == 0) { 						
                    isEmpty = true;            					
                    break;
                } else {                    						
                    isEmpty = false;            						
                }
            }
        	int totalNumUsed = 0;
        	int n = 1;
        	// calculate total numUsed
        	for (int i = 0; i < mainArray.length; i++) {
        		Row row = (Row) mainArray[i];
    			totalNumUsed += row.numUsed;
    		}    
        	
            // if there is no empty row available, add new row.
            // else merge row
			if (!isEmpty) {
				if (totalNumUsed < 8) {
					mainArray = new Object[n + 1];
					mainArray[0] = new Row(SmallestArraySize);
					mainArray[1] = new Row(SmallestArraySize);
					for (int i = 0; i < tempArray.length; i++) {
						mainArray[i] = tempArray[i];
					}
				} else {
					int a = 0, n1 = 4, n2 = 4;
					int length = n1 + n2;
					while (a < totalNumUsed && length <= totalNumUsed) {
						a = n1 + n2;
						n1 = n2;
						n2 = a;
						length += a;
						n++;
					}
					// increase size of mainArray
					mainArray = new Object[n + 1];
					// a = n1 + n2
					mainArray[0] = new Row(SmallestArraySize);
					mainArray[1] = new Row(SmallestArraySize);
					for (int i = 0; i < n - 1; i++) {
						Row currentRow = (Row) mainArray[i];
						Row nextRow = (Row) mainArray[i + 1];
						mainArray[i + 2] = new Row(currentRow.items.length + nextRow.items.length);
					}
					for (int i = 0; i < tempArray.length; i++) {
						mainArray[i] = tempArray[i];
					}
				}
            } else {
            	// merge up
            	int isDone = 1;
            	for (int i = mainArray.length-1; i > 0; i-=2) {
            		if(isDone == 0) {
            			break;
            		}
            		Row insertRow = (Row) mainArray[i];
            		if (i==1) {
            			mergeRows(0, 1, 1);
            		} else if(insertRow.numUsed==insertRow.items.length) {
            			i++;
            		}
            		else {
            			Row r1 = (Row) mainArray[i-1];
            			Row r2 = (Row) mainArray[i-2];
            			if(r1.numUsed+r2.numUsed == insertRow.items.length) {
            				mergeRows(i-2, i-1, i);
            			} else {
            				i++;
            			}
            			
            		}
            		if(firstRow.numUsed==0) {
        				isDone = 0;
        			}
            		
				}
            }
        }
    }
    /*
     * merge 2 rows up into third
     * r1: [....]  source - matching values go later
     * r2: [........]    source - matching values go earlier
     * r3: [............]    destination
     * this should start from the top end so it can be used for mergeAll where:
     * Note: sometimes r3 might be the same array as r1 or r2 
     * Note: sometimes the arrays will not be full
     */
    private void mergeRows(int r1, int r2, int r3) {
        // TO DO
        Row row1 = (Row) mainArray[r1];
        Row row2 = (Row) mainArray[r2];
        Row row3 = (Row) mainArray[r3];
        Row tempRow = new Row(row1.numUsed + row2.numUsed);

        E[] items1 = (E[]) row1.items;       // r1
        E[] items2 = (E[]) row2.items;       // r2
        E[] items3 = (E[]) row3.items;       // r3
        E[] tempItems = (E[]) tempRow.items; // tempRow items

        int i = 0, j = 0, k = 0;

        // compare and add smallest elements first
        while (i < row1.numUsed && j < row2.numUsed) {
            if(comp.compare(items1[i],items2[j]) < 0) {
                tempItems[k++] = items1[i++];
            } else {
                tempItems[k++] = items2[j++];  
            }
        }
        // fill in the rest
        while(i < row1.numUsed) {
            tempItems[k++] = items1[i++];
        }

        while(j < row2.numUsed) {
            tempItems[k++] = items2[j++];
        }
        // set new row numUsed
        row3.numUsed = row1.numUsed + row2.numUsed;
        
        // reset row1 and row2
		if (r1 == r3) {
			row2.numUsed = 0;
			Arrays.fill(items2, null);
		} else if (r2 == r3) {
			row1.numUsed = 0;
			Arrays.fill(items1, null);
		} else {
			row1.numUsed = 0;
			row2.numUsed = 0;
			Arrays.fill(items1, null);
			Arrays.fill(items2, null);
		}
		// fill items3 with tempItems
        for (int u = 0; u < tempItems.length; u++) {
        	items3[u] = tempItems[u]; 
        }
    }

    /*
     * Merge all rows into one single row. This will be the largest row
     * in the FibList (and probably will not be full.)
     * 
     * mergeAll() is used: by iterator(), toArray() and subList(),
     * and makes them much easier to implement. The time taken for the full
     * merge would likely be required for these operations anyway.
     *
     * Invariants:
     * 1. The only rows that can be partially full are row 0 where items are
     *    inserted, and the largest row if created by mergeAll. 
     *    (During mergeAll, middle rows can also be partially full.)
     * 2. A partially full largest row will contain more items than the
     *    capacity of its preceding row (otherwise it never would have been
     *    created).
     *
     * Scenarios to make sure this works for:
     * 0: There are no items.
     * 1: There is only 1 row with items.
     * 2: Everything will not fit into the largest row.
     *    A new larger row will be needed. All rows are merged from smallest to
     *    largest into that new row.
     *    example: 3 4 _ 12 20           (these are the number in each row)
     *    goes to: _ _ _ __ __ __ 39/52  (largest row uses 39 of capacity 52)
     * 3: There are only 2 rows with items, and the smaller row will fit into
     *    the partially full largest row. 
     *    Note: this can happen only with the smallest row,
     *    example: 2 _ _ 9/12
     *    goes to: _ _ _ 11/12
     * 4: There are 3 or more rows with items, and they all will fit into the
     *    largest row.
     *    First all rows except the largest are merged into the empty row
     *    before the largest (similar to scenario 2). The invariants guarantee
     *    that row will be empty.    
     *    Then these two rows are merged (similar to scenario 3).
     *    example: 1 4 _ __ 20 __ _____ 55/84
     *    goes to: _ _ _ __ __ __ 25/52 55/84
     *    goes to: _ _ _ __ __ __ _____ 80/84 
     */
    private void mergeAll() {
        // TO DO
    	
    	/* calculate the numUsed for ALL ROWS	DONE
    	 * check to see if there's enough room in the last row to fit all the numUsed	DONE
    	 * if there is enough room to fit all, then merge everything into that row
    	 * if there is NOT enough room
    	 * then create a new row to fit all the numUsed
    	 * sort the row
    	 */
    	
    	int totalNumUsed = 0;
    	int n = 1;
    	// calculate total numUsed
    	for (int i = 0; i < mainArray.length; i++) {
    		Row row = (Row) mainArray[i];
			totalNumUsed += row.numUsed;
		}    
    	
        Object[] tempArray = new Object[mainArray.length];
        // Copy mainArray to tempArray
        for (int i = 0; i < mainArray.length; i++) {
            tempArray[i] = mainArray[i];
        }
        
    	Row largestRow = (Row) mainArray[mainArray.length-1];
    	if(largestRow.items.length < totalNumUsed) {
		// find how many rows needed to create
			int a = 0, n1= 4, n2 = 4;
			while (a < totalNumUsed) {
				a = n1+n2;
	    		n1 = n2;
	    		n2 = a;
	    		n++;
			}
			// increase size of mainArray
			mainArray = new Object[n+1];
			// a = n1 + n2
			mainArray[0] = new Row(SmallestArraySize);
			mainArray[1] = new Row(SmallestArraySize); 
			for (int i = 0; i < n-1; i++) {
		    	Row currentRow = (Row) mainArray[i];
		    	Row nextRow = (Row) mainArray[i+1];
		    	mainArray[i+2] = new Row(currentRow.items.length + nextRow.items.length);
			}
			for (int i = 0; i < tempArray.length; i++) {
	                mainArray[i] = tempArray[i]; 
	        }
    	}
		// merge using mergeRows(r1, r2, r3);
		for (int i = 0; i < mainArray.length-1; i++) {
			mergeRows(mainArray.length-1, i, mainArray.length-1);
		}
    }
    
    // Testing code
//    public void countASong() {
//    	int count = 0;
//    	for (int i = 0; i < mainArray.length; i++) {
//			Row row = (Row) mainArray[i];
//			for (int j = 0; j < row.numUsed; j++) {
//				Song s = (Song) row.items[j];
//				if(s.getTitle().toLowerCase().startsWith("a")) {
//					count++;
//					System.out.println(j + " "+ s.getTitle());
//				}
//			}
//		}
//    	System.out.println("Number of A: "+ count);
//    }
//    
//    public void verifyAllRowSorted(String r) {
//    	System.out.println(r);
//    	int count = 0;
//    	for (int i = 0; i < mainArray.length; i++) {
//			Row row = (Row) mainArray[i];
//			if(row.numUsed==0) {
//				continue;
//			}
//			Song s = (Song) row.items[0];
//			String previousTitle = s.getTitle().toLowerCase();
//			for (int j = 0; j < row.numUsed; j++) {
//				Song s2 = (Song) row.items[j];
//				String title = s2.getTitle().toLowerCase();
//				if(previousTitle.compareTo(title)>0) {
//					System.out.println(j + " Song out of order: "+ s2.getTitle());
//					System.exit(1);
//				}
//				previousTitle = title;
//			}
//		}
//    	System.out.println(r);
//    }
    /**
     * copy the contents of the FibList into the specified array
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {
        // merge all into the largest row
        mergeAll();
        // the number of elements is a.lenght
        Row row = (Row) mainArray[mainArray.length - 1];
        int correctSize = row.numUsed;
        if (row.numUsed > a.length) {
            // not enough space
            correctSize = a.length;
        } else if (row.numUsed <= a.length) {
            // enough space then set size
            correctSize = row.numUsed;
        }
        System.arraycopy(row.items, 0, a, 0, correctSize);
        return a;

    }
     
    /**
     * Returns a new independent FibList whose elements range 
     * from fromElement(inclusive) to toElement(exclusive).
     * The original list is unaffected.
     * findFirstInRow() will be useful.
     * @param fromElement
     * @param toElement
     * @return the sublist
     */
    public FibList<E> subList(E fromElement, E toElement){
        // TO DO
        // merge all into largest row
        mergeAll();
        // find largest row
        int largest = mainArray.length - 1;
        Row largestRow = (Row) mainArray[largest];
        E[] items = (E[]) largestRow.items;
        // create new empty list
        FibList<E> subList = new FibList<E>(comp);
        // find starting index (inclusive)
        int start = Math.abs(findFirstInRow(largest, fromElement));

        // find ending index (exclusive)
        int end = Math.abs(findFirstInRow(largest, toElement));
        // copy element from starting index to ending index 
        // from our largestRow to subList
        for (int i = start; i < end; i++) {
            subList.add(items[i]);
        }
        // return sub list
        return subList;
    }
     
    /**
     * Returns an iterator for this list.
     * This method just merges the items into a single array and creates an
     * instance of the inner Itr() class
     * (DONE)   
     */
    public Iterator<E> iterator() {
        mergeAll();
        return new Itr();
    }

    /**
     * Iterator 
     */
    private class Itr implements Iterator<E> {
        int index;    // the index of the next item to return
        
        /*
            * create iterator at start of list
            */
        Itr(){
            // To DO
            index = 0;
        }

        /**
            * check if more items
            */
        public boolean hasNext() {
            // TO DO
            Row row = (Row)mainArray[mainArray.length-1];
            return index != row.numUsed;
        }

        /**
            * return item and move to next
            * throws NoSuchElementException if off end of list
            */
        public E next() throws NoSuchElementException{
            // TO DO
            Row row = (Row)mainArray[mainArray.length-1];
            return row.items[index++];
        }

        /**
            * Remove is not implemented. Just use this code.
            * (DONE)
            */
        public void remove() {
            throw new UnsupportedOperationException();	
        }
    }
}

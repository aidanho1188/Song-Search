package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

import com.sun.jdi.Value;

// timer for the FibList
// adding a bunch or random integers
public class P5timer {
    public static void main(String[] args) {
        final int N = 100000;
        Integer[] keys = new Integer[N];
        // initialize & create random Integers
        Random rand = new Random(1L); // set seed so same sequence every time
        for (int i = 0; i < N; i++) {
            int value = rand.nextInt();
            keys[i] = value; // auto boxed
        }
        // make a copy and sort it for later verification 
        Integer[] sorted = Arrays.copyOf(keys, keys.length);
        Arrays.sort(sorted);

        // start timing
        // best of 10 trials because of Jit, doesn't significantly improve after 2nd trial  
        long startTime, finishTime, elapsedTime=0, bestTime=999999999999L;
        boolean allGood = true;

        for (int trial = 1; trial <= 10; trial++) {
            startTime = System.nanoTime();
            Comparator<Integer> comp = new IntegerComparator();
            FibList<Integer> fibL = new FibList<Integer>(comp);
            // add all the keys
            for (int i = 0; i < N; i++)
                fibL.add(keys[i]);
            	

            // System.out.println("Verifying");
            // verify by comparing the sorted order to the iterator sequence
            Iterator<Integer> itr = fibL.iterator();
            for (int i = 0; i < N; i++){
                if (!itr.hasNext()) {
                    System.out.println("Error: hasNext() ended after " + i + " of " + N);
                    allGood = false;
                    break;
                }
                Integer val = itr.next();
                if (sorted[i] != val) {  // is comparing object references which detects if duplicates are swapped
                    System.out.printf("Error: wrong order item[%d]=%d but should be %d\n", i, val, sorted[i]);
                    allGood = false;
                    break;
                }
            }         

            // Calculate the elapsed time:
            finishTime = System.nanoTime();
            elapsedTime = finishTime - startTime;
            if (bestTime > elapsedTime)
                bestTime = elapsedTime;
            System.out.printf("Trial %d %.2fms\n", trial, (elapsedTime / 1000000.0) );
        } 
        System.out.printf("Best %.2fms\n",(bestTime / 1000000.0) );

        File f = new File("timeinfofile");
        PrintStream outputFile = null;
        try {
            outputFile = new PrintStream(f);
        } catch (FileNotFoundException e) {
            System.err.println("Could not create timeinfofile");
            System.exit(1);
        }
        if (!allGood)
            outputFile.print(" Errors ");
        outputFile.printf(" %.2fms\n",(bestTime / 1000000.0) );
    }  

    public static class IntegerComparator implements Comparator<Integer> {
        public int compare(Integer i1, Integer i2) {
            return  i1.compareTo(i2);
        }
    }
}

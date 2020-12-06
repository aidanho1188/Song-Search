package student;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

/* testing code for finished FibList
 * There is a default test case, but
 * you can also specify an argument on the command line that will be
 * processed as a sequence of characters to insert into the list.
 * 
 * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
 */
public class FibListTesterProg5 {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("testing routine for FibList");
        String order = "qwertyuiopasdfghjklzxcvbnmaeiou";  // default test
        if (args.length > 0)
            order = args[0];   // use user specified test case instead

        // insert them character by character into the list
        System.out.println("insertion order: "+order);
        Comparator<String> comp = new StringCmp();
        ((CmpCnt)comp).resetCmpCnt();            // reset the counter inside the comparator
        FibList<String> fibList = new FibList<String>(comp);
        for (int i = 0; i < order.length(); i++){
            String s = order.substring(i, i+1);
            fibList.add(s);
        }
        System.out.println("The number of comparison to build the FibList = "+
                ((CmpCnt)comp).getCmpCnt());

        System.out.println("TEST: after adds - data structure dump");
        dump(fibList);

        stats(fibList, comp);     

        System.out.println("TEST: contains(\"c\") -> " + fibList.contains("c"));
        System.out.println("TEST: contains(\"7\") -> " + fibList.contains("7"));

        // test toArray (calls mergeAll)
        ((CmpCnt)comp).resetCmpCnt();     
        String[] a = new String[fibList.size()];
        fibList.toArray(a);
        System.out.println("TEST: toArray (mergeAll() took "+((CmpCnt)comp).getCmpCnt()+
                           " comparisons)");
        for (int i=0; i<a.length; i++)
            System.out.print("["+a[i]+"]");
        System.out.println();

        System.out.println("TEST: iterator");
        Iterator<String> itr = fibList.iterator();
        while (itr.hasNext())
            System.out.print("["+itr.next()+"]");
        System.out.println();

        System.out.println("TEST: sublist(i,o)");
        FibList<String> sublist = fibList.subList("i", "o");
        dump(sublist); 

        System.out.println("TEST: sublist(in,out)");
        FibList<String> sublist2 = fibList.subList("in", "out");
        dump(sublist2); 
    }


    /**
     * string comparator with cmpCnt for testing
     */
    public static class StringCmp extends CmpCnt implements Comparator<String> {
        public int compare(String s1, String s2) {
            cmpCnt++;
            return s1.compareTo(s2);
        }
    }

    /**
     * print out an organized display of the list
     * intended for testing purposes on small examples
     * it looks nice for the test case where the objects are characters
     *
     * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
     */
    public static void dump(FibList<String> fibList){
        if (fibList == null) {
            System.out.println("Error null pointer");
            System.exit(1); 
        }
        System.out.println("size= " + fibList.size());
        System.out.println("    used len");
        int levels = fibList.mainArray.length;
        for (int rowNum = 0; rowNum < levels; rowNum++) {
            FibList<String>.Row row = (FibList<String>.Row)fibList.mainArray[rowNum];
            Object[] a = row.items;
            int len = a.length;
            System.out.printf("[%d]%4d / %-4d ", rowNum, row.numUsed, len);

            for (int i = 0; i < len; i++) {
                String item = (String)a[i];
                if (item == null)
                    System.out.print("[ ]");
                else
                    System.out.print("["+item+"]");
            }
            System.out.println();
        }
    }

    /**
     * calculate and display statistics
     * 
     * It use a comparator that implements the given CmpCnt interface.
     * It then runs through the list searching for every item and calculating
     * search statistics.
     * 
     * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
     */
    public static void stats(FibList<String> fibList, Comparator<String> comp) {
        System.out.println("STATS:");
        int size = fibList.size();

        // search stats, search for every item
        int totalCmps = 0, minCmps = Integer.MAX_VALUE, maxCmps = 0;

        int levels = fibList.mainArray.length;
        for (int rowNum = 0; rowNum < levels; rowNum++) {
            FibList<String>.Row row = (FibList<String>.Row)fibList.mainArray[rowNum];
      
            for (int i = 0; i < row.numUsed; i++) {
                Object[] itemsArray = row.items;
                String item = (String)(itemsArray[i]);
                ((CmpCnt)comp).resetCmpCnt();
                if (!fibList.contains(item))
                    System.err.println("Did not expect an unsuccesful search in stats");
                int cnt = ((CmpCnt)comp).getCmpCnt();
                totalCmps += cnt;
                if (cnt > maxCmps)
                    maxCmps = cnt;
                if (cnt < minCmps)
                    minCmps = cnt;
            }
        }

        System.out.printf("Successful search: min cmps = %d, avg cmps = %.1f, max cmps %d\n",
                minCmps, (double)totalCmps/size, maxCmps);
    }
}

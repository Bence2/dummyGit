package dummy;

import edu.princeton.cs.algs4.StdOut;

public class RecursiveCombinations {

    // print all subsets of the characters in s
    public static void comb1(String s) { comb1("", s); }

    // print all subsets of the remaining elements, with given prefix 
    private static void comb1(String prefix, String s) {
        if (s.length() > 0) {
            StdOut.println(prefix + s.charAt(0));
            comb1(prefix + s.charAt(0), s.substring(1));
            comb1(prefix,               s.substring(1));
        }
    }  

    // alternate implementation
    public static void comb2(String s) { comb2("", s); }
    private static void comb2(String prefix, String s) {
        StdOut.println(prefix);
        for (int i = 0; i < s.length(); i++)
            comb2(prefix + s.charAt(i), s.substring(i + 1));
    }  


    // read in N from command line, and print all subsets among N elements
    public static void main(String[] args) {
//        int n = Integer.parseInt(args[0]);
    	int n = 4;
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String elements = alphabet.substring(0, n);

        // using first implementation
        comb1(elements);
        StdOut.println();

        // using second implementation
        comb2(elements);
        StdOut.println();
    }

}
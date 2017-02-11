package dummy;

import edu.princeton.cs.algs4.StdOut;

public class Comb2 {

    private static void showCombination(int[] s) {
        for (int i = 0; i < s.length; i++)
            StdOut.print(s[i] + " ");
        StdOut.println();
    }

    public static void generate(int[] s, int position, int nextInt, int k, int n) {
        if (position == k) {
            showCombination(s);
            return;
        }
        for (int i = nextInt; i < n; i++) {
            s[position] = i;
            generate(s, position + 1, i + 1, k, n);
        }
    }  

    public static void main(String[] args) {
    	int k = 3;
    	int n = 4;
        int[] s = new int[k];
        generate(s, 0, 0, k, n);
    }
}



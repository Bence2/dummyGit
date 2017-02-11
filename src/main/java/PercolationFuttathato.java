import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationFuttathato {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 4;
		Percolation percolation = new Percolation(n);
		Boolean isOpen = percolation.isOpen(1, 1);
		System.out.println(isOpen);
		percolation.open(4, 1);
		percolation.open(1, 1);
		percolation.open(1, 3);
		percolation.open(2, 3);
		percolation.open(3, 3);
		percolation.open(1, 2);
		percolation.open(4, 3);
//		percolation.open(1, 2);
//		percolation.open(2, 1);
//		percolation.open(3, 1);
//		percolation.open(2, 3);
//		percolation.open(3, 3);
//		percolation.open(2, 2);
//		percolation.open(4, 3);
//		percolation.toString();
//		percolation.checkRootHelper();
		System.out.println(percolation.percolates());
		
		System.out.println(StdRandom.uniform(6));
		System.out.println(StdRandom.uniform(6));
		System.out.println(StdRandom.uniform(6));
		System.out.println(StdRandom.uniform(6));
		System.out.println(StdRandom.uniform(6));
		System.out.println(StdRandom.uniform(6));
		System.out.println(StdRandom.uniform(6));
		
	}

}

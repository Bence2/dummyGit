

public interface PercolationInterface {
	
		/**
		 * when i = 1 and j=1 means the top left corner of the matrix,
		 * but we use matrix indexed from 0. this means that with whatever number this function is going to get called
		 * we will subtract 1.
		 * @param i
		 * @param j
		 */
	   public void open(int i, int j);         // open site (row i, column j) if it is not open already
	   public boolean isOpen(int i, int j); // is site (row i, column j) open?
	   public boolean isFull(int i, int j);   // is site (row i, column j) full?
	   public boolean percolates(); // does the system percolate?
}

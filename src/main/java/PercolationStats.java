
import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private int matrixSize;
	private int numberOfTrials;
	private Percolation percolation;
	private double mean;
	private List<PercolationResult> percolationResults;
	private double stddev;
	
	public static void main(String[] args) {
		int cmdLnN = Integer.parseInt(args[0]);
		int cmdLnT = Integer.parseInt(args[1]);
		PercolationStats ps = new PercolationStats(cmdLnN, cmdLnT);
	}
	
	/**
	 * 
	 * @param n - size of the grid
	 * @param trials - number of trials until percolation (all the trials are executed until the grid percolates)
	 */
	public PercolationStats(int n, int trials) {
		if(n <= 0 || trials <= 0) {
			throw new IllegalArgumentException();
		}
		percolationResults = new ArrayList<>();
		this.matrixSize = n;
		this.numberOfTrials = trials;
		for (int i = 0; i < trials; i++) {
			PercolationResult result = makeTrial();
			percolationResults.add(result);
		}
		mean = computeMean();
		stddev = computeStddev();
        // System.out.println(mean());
        // System.out.println(stddev());
	}

	
	private PercolationResult makeTrial() {
		List<ComplexIndex> ciList = new ArrayList<>();
		PercolationResult result = new PercolationResult();
		percolation = new Percolation(matrixSize);
		double numberOfExecutedOpening = 0;
		while (!percolation.percolates()) {
			int randomI = StdRandom.uniform(matrixSize) + 1;
			int randomJ = StdRandom.uniform(matrixSize) + 1;
			ComplexIndex ci = new ComplexIndex(randomI, randomJ);
			if (!ciList.contains(ci)) {
				percolation.open(randomI, randomJ);
				numberOfExecutedOpening++;
				ciList.add(ci);
			}
		}
		result.setPortionOfOpenedSites( numberOfExecutedOpening / (matrixSize * matrixSize));
		return result;
	}
	
	private double computeMean() {
		double percolationResultSum = 0;
		for (PercolationResult percolationResult : percolationResults) {
			percolationResultSum = percolationResultSum + percolationResult.getPortionOfOpenedSites();
		}
		mean = percolationResultSum / numberOfTrials;
		return mean;
	}
	
	private double computeStddev() {
		stddev = 0;
		double szamlalo = 0;
		for (PercolationResult percolationResult : percolationResults) {
			szamlalo += (percolationResult.getPortionOfOpenedSites() - mean) * (percolationResult.getPortionOfOpenedSites() - mean);
		}
		stddev = szamlalo / (numberOfTrials - 1);
		return stddev;
		
	}
	
	public double mean() {
		return mean;
	}
    public double confidenceHi() {
    	return ((mean + 1.96 *  Math.sqrt(stddev))/Math.sqrt(numberOfTrials));
    };
  
    public double confidenceLo() {
    	return ((mean - 1.96 *  Math.sqrt(stddev))/Math.sqrt(numberOfTrials));
    };
	  
    public double stddev(){
	    return stddev;
    };
	
	private class PercolationResult {
		private double portionOfOpenedSites;

		public double getPortionOfOpenedSites() {
			return portionOfOpenedSites;
		}

		public void setPortionOfOpenedSites(double d) {
			this.portionOfOpenedSites = d;
		}
	}
	private class ComplexIndex{
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + i;
			result = prime * result + j;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ComplexIndex other = (ComplexIndex) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (i != other.i)
				return false;
			if (j != other.j)
				return false;
			return true;
		}

		int i;
		int j;
		
		public ComplexIndex(int i, int j){
			this.i = i;
			this.j = j;
		}

		private PercolationStats getOuterType() {
			return PercolationStats.this;
		}
	}
	
	
}

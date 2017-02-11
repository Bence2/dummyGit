

import java.util.ArrayList;
import java.util.List;

public class Percolation {

	private Site[][] siteMatrix;
	private int numberOfAllSites;
	private List<ComplexIndex> openedIndices = new ArrayList<>();
	private Boolean doesMatrixPercolate = false;
	private Boolean isMatrixFull = false;
	private int matrixSize;
	private List<Site> openedUpperSites = new ArrayList<>();
	
	// [static variables, instance variables, constructors, methods]
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException(); 
		}
		this.matrixSize = n;
		numberOfAllSites = n*n;
		siteMatrix = new Site[n][n];
		for (int i = 0; i < siteMatrix.length; i++) {
			for (int j = 0; j < siteMatrix.length; j++) {
				ComplexIndex ci = new ComplexIndex(i, j);
				Site site = new Site(ci);
				site.setParent(site);
				site.isSiteOpen = false;
				site.isFull = false;
				siteMatrix[i][j] = site;
			}			
		}
	}
	
	private void checkRootHelper()
	{
		for (int i = 0; i < siteMatrix.length; i++) {
			for (int j = 0; j < siteMatrix.length; j++) {
				Site site = siteMatrix[i][j];
				if (site.isSiteOpen) {
					Site rootSite = getRootParent(site);
					System.out.println(rootSite.getComplexIndex().i);
					System.out.println(rootSite.getComplexIndex().j);
				}
			}
		}
	}
	
	/**
	 * full site is an open site that can be connected to an open site in the top row via
	 * a chain of neighboring (left, right, up, down) open sites.
	 * ez kurvara nem ezt csinalja, mert elkurtam
	 * @param percolatingTreeRootIndex
	 */
	private void updateIsFull(ComplexIndex percolatingTreeRootIndex) {
		for (ComplexIndex openedIndex : openedIndices) {
			Site openedSite = siteMatrix[openedIndex.i][openedIndex.j];
			Site openedSiteRootParent = getRootParent(openedSite);
			if (percolatingTreeRootIndex.equals(openedSiteRootParent.getComplexIndex())) {
				openedSite.isFull = true;
			}
		}
	}
	
	
	private void printIsFull()
	{
		StringBuffer sbPrint = new StringBuffer();
		for (int i = 0; i < siteMatrix.length; i++) {
			for (int j = 0; j < siteMatrix.length; j++) {
				sbPrint.append(isFull(i+1, j+1));
			}
			System.out.println(sbPrint);
			sbPrint = new StringBuffer();
		}
	}
	
	public void open(int i, int j) {
		int matrixIndexI = i -1; 
		int matrixIndexJ = j -1;
		Site siteToBeOpened = siteMatrix[matrixIndexI][matrixIndexJ];
		if (matrixIndexI == 0) {
			openedUpperSites.add(siteToBeOpened);
			siteToBeOpened.isFull = true;
		}
		ComplexIndex newComplexIndex = new ComplexIndex(matrixIndexI, matrixIndexJ);
		if (!openedIndices.contains(newComplexIndex)) {
			openedIndices.add(newComplexIndex);
			List<ComplexIndex> openNeighboringIndices = getOpenNeighboringSites(matrixIndexI, matrixIndexJ);
			siteToBeOpened.setIfRootSiteThenValidTreeSize(1);
			
			for (ComplexIndex complexIndex : openNeighboringIndices)
			{
				Site neighborSite = siteMatrix[complexIndex.i][complexIndex.j];
				if (neighborSite.isSiteOpen) {
					makeConnection(neighborSite, siteToBeOpened);
				}
			}
			siteToBeOpened.isSiteOpen = true;
			bruteForceDoesPercolate();
		}
	}
	
	private List<ComplexIndex> getOpenNeighboringSites(int i, int j) {
		List<ComplexIndex> cIndices = new ArrayList<>();
		ComplexIndex ci;
		// upper neighbor exists
		if ((i - 1) >= 0) {
			ci = new ComplexIndex(i-1, j);
			if (siteMatrix[i-1][j].isSiteOpen) {
				cIndices.add(ci);
			}
		}
		// left neighbor exists
		if ((j-1) >= 0) {
			ci = new ComplexIndex(i, j-1);
			if (siteMatrix[i][j-1].isSiteOpen) {
				cIndices.add(ci);
			}
		}
		
		if ((i + 1) < matrixSize) {
			ci = new ComplexIndex(i+1, j);
			if (siteMatrix[i+1][j].isSiteOpen) {
				cIndices.add(ci);
			}
		}
		if ((j + 1) < matrixSize) {
			ci = new ComplexIndex(i, j+1);
			if (siteMatrix[i][j+1].isSiteOpen) {
				cIndices.add(ci);
			}
		}
		
		return cIndices;
	}
	
	
	/**
	 * check all the border sites to see which one is open and what tree they belong to.
	 * @return
	 */
	private void bruteForceDoesPercolate()
	{
		List<Site> openDownSiteRoots = new ArrayList<>();
		for (Site siteDown : siteMatrix[matrixSize-1]) {
			if (siteDown.isSiteOpen) {
				Site downRootParent = getRootParent(siteDown);
				openDownSiteRoots.add(downRootParent);
			}
		}
		outerloop:
		for (Site siteUp : siteMatrix[0]) {
			if (siteUp.isSiteOpen) {
				Site siteUpRoot = getRootParent(siteUp);
				for (Site siteDownRoot : openDownSiteRoots) {
					if (siteUpRoot.getComplexIndex().equals(siteDownRoot.getComplexIndex())) {
						doesMatrixPercolate = true;
						// updateIsFull(siteUpRoot.getComplexIndex());
						// printIsFull();
						// checkRootHelper();
						break outerloop;
					}
				}
			}
		}
	}
	
	
	/**
	 * checks whether two sites are in the same tree
	 * @param siteI
	 * @param siteJ
	 * @return
	 */
	private boolean areTheyInTheSameTree(Site siteI, Site siteJ)
	{
		Site siteIRoot = getRootParent(siteI);
		Site siteJRoot = getRootParent(siteJ);
		return siteIRoot.getComplexIndex().equals(siteJRoot.getComplexIndex());
	}
	
	
	/**
	 * connecting two trees by point at the nodes that are in each tree
	 * @param siteI - neighboring site
	 * @param siteJ - the site to be opened
	 */
	private void makeConnection(Site siteI, Site siteJ)
	{
		//i neighbor site, j current site
		Site siteIRoot = getRootParent(siteI);
		Site siteJRoot = getRootParent(siteJ);
		if (siteJRoot.isFull || siteIRoot.isFull) {
			siteJRoot.isFull = true;
			siteIRoot.isFull = true;
		}
		if (siteIRoot.getIfRootSiteThenValidTreeSize() >= siteJRoot.getIfRootSiteThenValidTreeSize()) {
			siteIRoot.setIfRootSiteThenValidTreeSize(siteIRoot.getIfRootSiteThenValidTreeSize() 
					+ siteJRoot.getIfRootSiteThenValidTreeSize());
			siteJRoot.setParent(siteIRoot);
		}
		else {
			siteJRoot.setIfRootSiteThenValidTreeSize(siteJRoot.getIfRootSiteThenValidTreeSize() 
					+ siteIRoot.getIfRootSiteThenValidTreeSize());
			siteIRoot.setParent(siteJRoot);
		}
	}
	
	/**
	 * if the site is a lone node, then its parent is itself
	 * otherwise the parent is the root of the node that the site belongs to
	 * tmp: make it avaiblae
	 * @param site
	 * @return
	 */
	private Site getRootParent(Site site) {
		Site immediateParentSite = site.getParent();
		if (immediateParentSite.getComplexIndex().equals(site.getComplexIndex())) {
			return site;
		}
		else {
			while (!immediateParentSite.getComplexIndex().equals(site.getComplexIndex())) {
				site = immediateParentSite;
				immediateParentSite = site.getParent();
			}
			return site;
		}
	}

	public boolean isOpen(int i, int j) {
		int matrixIndexI = i -1; 
		int matrixIndexJ = j -1;

		return siteMatrix[matrixIndexI][matrixIndexJ].isSiteOpen;
	}

	/**
	 * tells when a system percolates, is the given site involved in the percolation process
	 * does sthg goes through it
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isFull(int i, int j) {
		return getRootParent(siteMatrix[i-1][j-1]).isFull;
	}

	public boolean percolates() {
		return doesMatrixPercolate;
	}

	private class Site
	{
		private Site parent;
		private Boolean isSiteOpen;
		private ComplexIndex complexIndex;
		private int ifRootSiteThenValidTreeSize;
		private Boolean isFull;

		public int getIfRootSiteThenValidTreeSize() {
			return ifRootSiteThenValidTreeSize;
		}

		public void setIfRootSiteThenValidTreeSize(int ifRootSiteThenValidTreeSize) {
			this.ifRootSiteThenValidTreeSize = ifRootSiteThenValidTreeSize;
		}

		public Site(ComplexIndex complexIndex)
		{
			this.complexIndex = complexIndex;
		}
		
		Site getParent() {
			return parent;
		}

		void setParent(Site parent) {
			this.parent = parent;
		}

		public ComplexIndex getComplexIndex() {
			return complexIndex;
		}

		private void setComplexIndex(ComplexIndex complexIndex) {
			this.complexIndex = complexIndex;
		}
	}
	private class ComplexIndex{
		private int i;
		private int j;
		
		public ComplexIndex(int i, int j){
			this.i = i;
			this.j = j;
		}
		
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

		private Percolation getOuterType() {
			return Percolation.this;
		}
	}
	
	private String toStringLofasz() {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbPrint = new StringBuffer();
		for (int i = 0; i < siteMatrix.length; i++) {
			for (int j = 0; j < siteMatrix.length; j++) {
				sb.append(siteMatrix[i][j].isSiteOpen);
				sbPrint.append(siteMatrix[i][j].isSiteOpen);
			}
			System.out.println(sbPrint);
			sbPrint = new StringBuffer();
		}
		return sb.toString();
	}
}


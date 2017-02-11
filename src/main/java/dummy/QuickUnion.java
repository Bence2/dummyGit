package dummy;

public class QuickUnion {
	
	private static int[] initialArray;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		                   //{0,1,2,3,4,5,6,7}
		int[] exampleArray = {1,1,1,2,2,0,7,7};
		
		//initialArray = DummyMain.initArray(8);
		initialArray = exampleArray;
		DummyMain.printArray(initialArray);
		System.out.println(getRoot(3));
		boolean wut = isConnected(5, 6);
		System.out.println(wut);
		
	}
	/**
	 * connect p TO q
	 * @param p
	 * @param q
	 */
	private static void makeConnection(int p, int q)
	{
		int rootOfp = getRoot(p);
		int rootOfq = getRoot(q);
		initialArray[rootOfp] = rootOfq;
	}
	
	/**
	 * check if p and q have the same root
	 * @return
	 */
	private static boolean isConnected(int p, int q)
	{
		int rootOfp = getRoot(p);
		int rootOfq = getRoot(q);
		return rootOfp == rootOfq;
		
	}
	
	/**
	 * returns the root of the given node
	 * eg.: if 2 is a leaf and has parent 4 which has parent 6, then
	 * for int p = 2 it will return 6
	 * in this case p is an index of the array
	 * if the given node is a single node with no parent node, then
	 * the index (node) will equal to the array[index]
	 * @param p
	 * @return
	 */
	private static int getRoot(int p)
	{
		int parent = getImmediateParent(p);
		if (parent == p){
			return p;
		} 
		else {
			while (true)
			{
				int newParent = getImmediateParent(parent);
				if (parent == newParent) 
				{
					return parent;
				}
				else
				{
					parent = newParent;
				}
			}
		}
	}
	
	private static int getRoot2(int p)
	{
		while(p != initialArray[p])
		{
			p =initialArray[p]; 
		}
		return p;
	}
	
	/**
	 * returns the immediate parent of the given node
	 * @param p
	 * @return
	 */
	private static int getImmediateParent(int p)
	{
		return initialArray[p];
	}

}

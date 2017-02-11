package dummy;

import java.lang.reflect.Array;

public class DummyMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("printel");
		//list of id-s [0,1,2,3,4,5,6,7,8] initially
		//the indices are the objects
		//the values indicate the connection between the objects
		//p and q
		// to merge components containing p and q, change all entries whose id equals id[p] to id[q]
		int[] intArray = initArray(8);
		printArray(intArray);
		makeConnection(intArray, 0,1);
		printArray(intArray);
		makeConnection(intArray, 1,7);
		printArray(intArray);
		

	}
	public static void printArray(int[] array){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
		}
		System.out.println(sb);
	}
	
	/**
	 * creates an array with the specified size and initializes it like this:
	 * [0,1,2,3] in case intTombHossz is 4
	 * @param intTombHossz
	 * @return
	 */
	public static int[] initArray(int intTombHossz) {
		int[] intArray = new int[intTombHossz];
		for (int i = 0; i < intArray.length; i++) {
			intArray[i] = i;
		}
		return intArray;
	}
	
	private static void makeConnection(int[] connectionArray, int indexConnectTo, int indexConnectFrom)
	{
		//connectionArray[indexConnectTo] = indexConnectFrom;
		for (int i = 0; i < connectionArray.length; i++) {
			if(connectionArray[i] == indexConnectTo){
				connectionArray[i] = indexConnectFrom;
			}
		}
	}
	private static boolean isConnected(int[] connectionArray, int index0, int index1)
	{
		if (connectionArray[index0] == connectionArray[index1]){
			return true;
		}
		else {
			return false;
		}
	}

}

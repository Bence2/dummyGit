package dummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.Stack;

public class DummyCombinatorics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> elements = new ArrayList<>();
		elements.add("A");
		elements.add("B");
		elements.add("C");
		elements.add("D");
		// makeNSizeCombinations(3, elements);
		BruteCollinearPoints

	}
	
	
	private static List<String[]> makeNSizeCombinations(int N, List<String> elements) {
		List<String[]> allCombinations = new ArrayList<>();
		boolean loopCondition = true;
		Stack<String[]> activeElementStack = new Stack<>();
		Stack<String[]> newElementStack = new Stack<>();
		for (int i = elements.size()-1; i >= 0 ; i--) {
			/*
			 * how many elements remained before insertion and how many place has to be filled
			 */
			if ((elements.size() - (i))>= N) {
				String[] combination = new String[N];
				combination[0] = elements.get(i);
				activeElementStack.push(combination);
			}
		}
		int nthelementStackDumpPeriodCounter = 1;
		while (nthelementStackDumpPeriodCounter <= N ) {
			String[] combination = activeElementStack.pop();
			for (int i = nthelementStackDumpPeriodCounter; i < elements.size(); i++) {
				if ((elements.size() - (i))>= N - nthelementStackDumpPeriodCounter) {
					String[] newCombination = Arrays.copyOf(combination, combination.length);
					newCombination[nthelementStackDumpPeriodCounter] = elements.get(i);
					newElementStack.push(newCombination);
				}
			}
			if (activeElementStack.isEmpty()) {
				nthelementStackDumpPeriodCounter++;
				activeElementStack = newElementStack;
				newElementStack = new Stack<>();
			}
		}
		return null;
	}
}

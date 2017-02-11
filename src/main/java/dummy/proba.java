package dummy;

import edu.princeton.cs.algs4.Stack;

public class proba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer v = 2;
		Integer w = 1;
		System.out.println(v.compareTo(w));
		String sampleString = "(1 + (2*3))";
		Stack<Integer> eredmeny = parseArithmeticString(sampleString);
		System.out.println(eredmeny);
	

	}
	
	private static Stack<Integer> parseArithmeticString(String string) {
		String[] allowedOperations = {"+", "-", "*", "/"};
//		String[] numbers = {0,1,2,3,4,5,6,7,8,9};
		Stack<Integer> valueStack = new Stack<>();
		Stack<String> operatorStack = new Stack<>();
		for (int i = 0; i < string.length(); i++) {
			String ch  = Character.toString((string.charAt(i)));
			if (Character.isDigit(string.charAt(i))) {
				valueStack.push(new Integer(string.charAt(i)));
			}
			else if (isOperation(allowedOperations, string.charAt(i))) {
				operatorStack.push(Character.toString(string.charAt(i)));
			}
			else if (string.charAt(i) == ')') {
				String operation = operatorStack.pop();
				Integer firstOperand = valueStack.pop();
				Integer secondOperand = valueStack.pop();
				if (operation == "+") {
					valueStack.push(firstOperand+secondOperand);
				}
				if (operation == "*") {
					valueStack.push(firstOperand*secondOperand);
				}
				if (valueStack.size() == 1) {
					return valueStack;
				}
			}
		}
		return valueStack;
	}
	
	private static boolean isOperation(String[] allowedOperations, char ch) {
		String chString = Character.toString(ch);
		for (String c : allowedOperations) {
			if (chString == c) {
				return true;
			}
		}
		return false;
	}

}

import java.util.*;

public class SuffixTree {

	private Node root; 
	private ArrayList<Integer> lastSuffixLocations;
	
	//A - 0 T - 1 G - 2 C - 3
	private class Node {
		private Node[] array;
		private ArrayList<Integer> locationNdxes;
		private int count;
		
		public Node() {
			this.array = new Node[4];
			this.count = 0;
			this.locationNdxes = new ArrayList<Integer>();
		}
		
		public Node getIndex(int index) {
			return this.array[index];
		}
		
		public void setIndex(int index) {
			this.array[index] = new Node();
		}
		
		public void setLocation(int location) {
			this.locationNdxes.add(location);
		}
		
		public ArrayList<Integer> getLocations() {
			return this.locationNdxes;
		}
		
		public int getCount() {
			return this.count;
		}
		
		public void incrementCount() {
			this.count++;
		}
	}
	
	public SuffixTree() {
		this.root = new Node();

	}
	
	/* 
	 * Creates our suffix tree through recursion.
	 */
	public void createTree(String entireSequence, int length) {
		for (int i = 0; i < entireSequence.length(); i++) {

			if(length + i >= entireSequence.length()) {
				insertSuffix(entireSequence.substring(i, entireSequence.length()), this.root, i);
			}
			else {
				insertSuffix(entireSequence.substring(i, i + length), this.root, i);
			}
		}
	}
	
	private void insertSuffix(String suffix, Node node, int index) {
		Node next;
		
		if (suffix.length() == 0) {
			return;
		} 

		//System.out.println("char: " + suffix.charAt(0) + " index: " + index);
		switch (suffix.charAt(0)) {
		case 'A':
			if ((next = node.getIndex(0)) != null) {
				next.setLocation(index);
				next.incrementCount();
				insertSuffix(suffix.substring(1, suffix.length()), next, index + 1);
			}
			else {
				node.setIndex(0);
				next = node.getIndex(0);
				next.setLocation(index);
				next.incrementCount();
				insertSuffix(suffix.substring(1, suffix.length()), node.getIndex(0), index + 1);
				
			}
			break;
		case 'T':
			if ((next = node.getIndex(1)) != null) {
				next.setLocation(index);
				next.incrementCount();
				insertSuffix(suffix.substring(1, suffix.length()), next, index + 1);
			}
			else {
				node.setIndex(1);
				next = node.getIndex(1);
				next.setLocation(index);
				next.incrementCount();
				insertSuffix(suffix.substring(1, suffix.length()), node.getIndex(1), index + 1);
				
			}
			break;
		case 'G':
			if ((next = node.getIndex(2)) != null) {
				next.setLocation(index);
				next.incrementCount();
				insertSuffix(suffix.substring(1, suffix.length()), next, index + 1);
			}
			else {
				node.setIndex(2);
				next = node.getIndex(2);
				next.setLocation(index);
				next.incrementCount();
				insertSuffix(suffix.substring(1, suffix.length()), node.getIndex(2), index + 1);
				
			}
			break;
		case 'C':
			if ((next = node.getIndex(3)) != null) {
				next.setLocation(index);
				next.incrementCount();
				insertSuffix(suffix.substring(1, suffix.length()), next, index + 1);
			}
			else {
				node.setIndex(3);
				next = node.getIndex(3);
				next.setLocation(index);
				next.incrementCount();
				insertSuffix(suffix.substring(1, suffix.length()), node.getIndex(3), index + 1);
				
			}
			break;
		default:
			break;
		}
	}

	public void printTree() {
		inOrder(root);
	}
	
	private void inOrder(Node node) {
		if (node == null) {
			return;
		}
		inOrder(node.getIndex(0));
		if (node.getIndex(0) != null) {
			System.out.print("A");
		}
		inOrder(node.getIndex(1));
		if (node.getIndex(1) != null) {
			System.out.print("T");
		}
		inOrder(node.getIndex(2));
		if (node.getIndex(2) != null) {
			System.out.print("G");
		}	
		inOrder(node.getIndex(3));
		if (node.getIndex(3) != null) {
			System.out.print("C");
		}
	}
	
	/* Searches the tree for the provided suffix. 
	 * If not found, return -1. Otherwise, return the number of times suffix was found.
	 */
	public int findSuffix(String suffix) {
		char currentChar;
		Node currentNode = root;
		
		for (int i = 0; i < suffix.length(); i++) {
			
			currentChar = suffix.charAt(i);
			switch (currentChar) {
				case 'A': 
					currentNode = currentNode.getIndex(0);
					break;
				case 'T': 
					currentNode = currentNode.getIndex(1);
					break;
				case 'G':
					currentNode = currentNode.getIndex(2);
					break;
				case 'C':
					currentNode = currentNode.getIndex(3);
					break;
				default:
					break;
			}

			// Couldn't find the next character in the tree, will not exist
			if (currentNode == null) {
				this.lastSuffixLocations = null;
				return -1;
			}
		}
		for (int i = 0; i < currentNode.getLocations().size(); i++) {
			System.out.println(currentNode.getLocations().get(i));
		}
		this.lastSuffixLocations = currentNode.getLocations();
		return currentNode.getCount();
	}
	
	public ArrayList<Integer> getLocations() {
		return this.lastSuffixLocations;
	}
	
}

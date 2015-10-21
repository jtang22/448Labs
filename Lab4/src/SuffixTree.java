public class SuffixTree {

	private Node root; 
	
	//A - 0 T - 1 G - 2 C - 3
	private class Node {
		private Node[] array;
		
		public Node() {
			this.array = new Node[4];
		}
		
		public Node getIndex(int index) {
			return this.array[index];
		}
	}
	
	public SuffixTree() {
		this.root = new Node();
	}
	
	public void createTree(String suffix) {
		for (int i = 0; i < suffix.length(); i++) {
			insertSuffix(suffix.substring(i, suffix.length()), this.root);
		}
	}
	
	public void insertSuffix(String suffix, Node node) {
		Node next;
		if (suffix.length() == 0) {
			return;
		} 

		switch (suffix.charAt(0)) {
		case 'A':
			if ((next = node.getIndex(0)) != null) {
				insertSuffix(suffix.substring(1, suffix.length()), next);
			}
			break;
		case 'T':
			if ((next = node.getIndex(1)) != null) {
				insertSuffix(suffix.substring(1, suffix.length()), next);
			}
			break;
		case 'G':
			if ((next = node.getIndex(2)) != null) {
				insertSuffix(suffix.substring(1, suffix.length()), next);
			}
			break;
		case 'C':
			if ((next = node.getIndex(3)) != null) {
				insertSuffix(suffix.substring(1, suffix.length()), next);
			}
			break;
		default:
			break;
		}
	}

	public void printTree() {
		inOrder(root);
	}
	public void inOrder(Node node) {
		if(node == null) {
			return;
		}
		for(int i = 0; i < 4; i++) {
			inOrder(node.getIndex(i));
			switch(i) {
			case 0:
				System.out.println("A");
				break;
			case 1:
				System.out.println("T");
				break;
			case 2:
				System.out.println("G");
				break;
			case 3:
				System.out.println("C");
				break;
			}
		}
	}
	
//	public Node getRoot() {
//		return this.root;
//	}
	
}

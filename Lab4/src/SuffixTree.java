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
		
		public void setIndex(int index) {
			this.array[index] = new Node();
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
			else {
				node.setIndex(0);
				insertSuffix(suffix.substring(1, suffix.length()), node.getIndex(0));
				
			}
			break;
		case 'T':
			if ((next = node.getIndex(1)) != null) {
				insertSuffix(suffix.substring(1, suffix.length()), next);
			}
			else {
				node.setIndex(1);
				insertSuffix(suffix.substring(1, suffix.length()), node.getIndex(1));
				
			}
			break;
		case 'G':
			if ((next = node.getIndex(2)) != null) {
				insertSuffix(suffix.substring(1, suffix.length()), next);
			}
			else {
				node.setIndex(2);
				insertSuffix(suffix.substring(1, suffix.length()), node.getIndex(2));
				
			}
			break;
		case 'C':
			if ((next = node.getIndex(3)) != null) {
				insertSuffix(suffix.substring(1, suffix.length()), next);
			}
			else {
				node.setIndex(3);
				insertSuffix(suffix.substring(1, suffix.length()), node.getIndex(3));
				
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
		inOrder(node.getIndex(0));
		if(node.getIndex(0) != null) {
			System.out.print("A");
		}
		inOrder(node.getIndex(1));
		if(node.getIndex(1) != null) {
			System.out.print("T");
		}
		inOrder(node.getIndex(2));
		if(node.getIndex(2) != null) {
			System.out.print("G");
		}	
		inOrder(node.getIndex(3));
		if(node.getIndex(3) != null) {
			System.out.print("C");
		}
	}
	
//	public Node getRoot() {
//		return this.root;
//	}
	
}

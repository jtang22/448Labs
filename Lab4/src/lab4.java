import java.io.File;
import java.lang.*;
import java.util.Scanner;

public class lab4 {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter gene seq: ");
		String gene = scanner.next();
		
		SuffixTree tree = new SuffixTree();
		tree.createTree(gene);
		//tree.printTree();
		
		System.out.println("Enter suffix to look for: ");
		String suffix = scanner.next();
		
		
		
        System.out.println("Found suffix: " + tree.findSuffix(suffix));
//		File geneFile = new File(scanner.next());
//		try {
//			
//			SuffixTree tree = new SuffixTree();
//			
//			
//		} catch (FileNotFoundException e) {
//			System.out.println("File not found! ");
//		}
	}
	
	public boolean validateSuffix(String suffix) {
		int count = 0;
		String symbolString = "WSMKRY";
		for(int ndx = 0; ndx < suffix.length(); ndx++) {
			if(symbolString.contains(suffix.charAt(ndx) + "")) {
				count++;	
			}
		}
		if(count > 2) {
			return false;
		}
		return true;
	}
	
	public int suffixLookup(String suffix) {
		
	}
}

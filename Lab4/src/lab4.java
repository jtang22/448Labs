import java.io.File;
import java.util.Scanner;

public class lab4 {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter gene seq: ");
		String gene = scanner.next();
		
		SuffixTree tree = new SuffixTree();
		tree.createTree(gene);
		tree.printTree();
		
		
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
	
}

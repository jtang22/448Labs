import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class CpGFinder {
	//Scan in file for reading
	private int window = 200;
	private int stepSize = 1;
	private int cpgCount;
	private int cCount;
	private int gCount;
	private Writer fileWriterCSV;
	private BufferedWriter bufferedWriterCSV;
	private Writer fileWriterTXT;
	private BufferedWriter bufferedWriterTXT;
	private String dnaSeq;
	private int nCounter;
	private int remainderLen;
	private String filename;
	private double gcContent;
	private double cpgRatio;
	private int island;

	public static void main(String[] args) {
		readFile(new File(args[0]);
		bufferedWriter = null;
		fileWriter = null;
		try {
			String outputFile = filename.replace(".txt", ("_CpG.csv"));
			fileWriterCSV = new FileWriter(outputFile);
			bufferedWriterCSV = new BufferedWriter(fileWriter);

			outputFile = filename.replace(".txt", ("_CpG.txt"));
			fileWriterTXT = new FileWriter(outputFile);
			bufferedWriterTXT = new BufferedWriter(fileWriter);			
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	private int readFile(File file) {
		try { 
			Scanner scan = new Scanner(file);
			dnaSeq = "";
			filename = file.getName();
			while(scan.hasNextLine()) {
				String currLine = scan.nextLine();
				if(currLine.charAt(0) != '>') {
					dnaSeq += currLine;
				}
			}
			dnaSeq = dnaSeq.toUpperCase();
		} catch (FileNotFoundException e) {
			return 0;
		}
		return 1;
	}

	private void findCpGIsland() {
		int startNdx = 0;
		int gcContentLen = 0;
		remainderLen = 0;
		cpgCount = 0;
		cCount = 0;
		gCount = 0;
		countN();
		while(startNdx < dnaSeq.length()) {
			//remainder window
			island = 0;
			if((startNdx + windowSize) > dnaSeq.length()) {
				windowSize = dnaSeq.length() - startNdx;
			}
			for(int ndx = 0; ndx < windowSize; ndx++) {
				if (dnaSeq.charAt(startNdx + ndx) == 'G') {
					gCount++;
				}
				else if (dnaSeq.charAt(startNdx + ndx) == 'C') {
					cCount++;
					if((ndx + 1) < windowSize && dnaSeq.charAt(startNdx + ndx + 1) == 'G') {
						cpgCount++;
					}
				}
				if(dnaSeq.charAt(startNdx + ndx) != 'N') {
					gcContentLen++;
				}
			}
			//calculates gcContent
			gcContent = ((double)(gCount + cCount) / gcContentLen);
			cpgRatio = observedCpG() / expectedCpG();
			if(gcContent > .5 && cpgRatio > .6) {
				island = 1;
			}
			writeFile(startNdx);
			if(windowSize == 200) {
				cpgCount = 0;
				gCount = 0;
				cCount = 0;
				startNdx += stepSize;
			} else {
				remainderLen = windowSize;
				break;
			}
		}
	}

	private double expectedCpG() {
		return (cCount / windowSize) * (gCount / windowSize);
	}

	private double observedCpG() {
		return (cpgCount * windowSize) / (cCount * gCount);
	}

	private void countN() {
		nCounter = 0;
		for(int ndx = 0; ndx < dnaSeq.length(); ndx++) {
			if(dnaSeq.charAt(ndx) == 'N') {
				nCounter++;
			}
		}
	}

	private void writeFile(int startNdx) {
		DecimalFormat formatter = new DecimalFormat("0.00000");
		bufferedWriterCSV.write(startNdx + ", " + formatter.format(gcContent) + "%, " + formatter.format(cpgRatio) + ", 0.6, " + island + ", 0.5");
		bufferedWriterCSV.newLine();
		if(island == 1) {
			bufferedWriterTXT.write("CpG island in region " + starNdx + " to " + (startNdx + windowSize) + " (Obs/Exp = " + formatter.format(cpgRatio) + ", %GC = " + formatter.format(gcContent) + ")");
		}	
	}
}
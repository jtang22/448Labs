import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class CpGFinder {
	//Scan in file for reading
	private int windowSize = 200;
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
	
	public CpGFinder() {
		this.bufferedWriterCSV = null;
		this.fileWriterCSV = null;
		this.bufferedWriterTXT = null;
		this.fileWriterTXT = null;
	}

	public void setupWriters(String filename) {
		try {
			String outputFile = filename.replace(".txt", ("_CpG.csv"));
			this.fileWriterCSV = new FileWriter(outputFile);
			this.bufferedWriterCSV = new BufferedWriter(fileWriterCSV);

			outputFile = filename.replace(".txt", ("_CpG.txt"));
			this.fileWriterTXT = new FileWriter(outputFile);
			this.bufferedWriterTXT = new BufferedWriter(fileWriterTXT);
		} catch (IOException e) {
			System.out.println("File not found");
		}
	}
	
	public static void main(String[] args) {
		CpGFinder finder = new CpGFinder();
		finder.readFile(new File(args[0]));
		finder.setupWriters(args[0]);
		finder.findCpGIsland();	
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
			scan.close();
		} catch (FileNotFoundException e) {
			return 0;
		}
		return 1;
	}

	public void findCpGIsland() {
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
			gcContent = ((double)(gCount + cCount) / windowSize);
			//System.out.println("cCount: " + cCount + " gCount: " + gCount  + " windowSize: " + windowSize);
			//System.out.println("gccontent: " + gcContent + "observed: " + observedCpG() + "expected: " + expectedCpG() + "\n");
			cpgRatio = (observedCpG() / expectedCpG());
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
		return ((double)cCount / windowSize) * ((double)gCount / windowSize);
	}

	private double observedCpG() {
		return (double)(cpgCount * windowSize) / (cCount * gCount);
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
		try {
			bufferedWriterCSV.write(startNdx + ", " + formatter.format(gcContent) + ", " + formatter.format(cpgRatio) + ", 0.6, " + island + ", 0.5");
			bufferedWriterCSV.newLine();
			bufferedWriterCSV.flush();
			if(island == 1) {
				bufferedWriterTXT.write("CpG island in region " + startNdx + " to " + (startNdx + windowSize) + " (Obs/Exp = " + formatter.format(cpgRatio) + ", %GC = " + formatter.format(gcContent) + ")\n");
				bufferedWriterTXT.flush();
			}	
		}
		catch (IOException e) {
			System.out.println("Could not write to file");
		}
	}
}
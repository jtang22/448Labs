import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class GCContentWindow {

	private Writer fileWriter;
	private BufferedWriter bufferedWriter;
	private String dnaSeq;
	private int nCounter;
	private int remainderLen;
	private String filename;

	public GCContentWindow() {
		reset();
	}

	public int readFile(File file) {
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

	public void calcGCContent(int initWindowSize, int stepSize) {
		int gcCounter = 0;
		int startNdx = 0;
		int gcContentLen = 0;
		int windowSize = initWindowSize;
		nCounter = 0;
		remainderLen = 0;
		bufferedWriter = null;
		fileWriter = null;

		try {

			String outputFile = filename.replace(".txt", ("_w" + windowSize + "s" + stepSize + ".csv"));
			fileWriter = new FileWriter(outputFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			countN();
			while(startNdx < dnaSeq.length()) {

				//remainder window
				if((startNdx + windowSize) > dnaSeq.length()) {
					windowSize = dnaSeq.length() - startNdx;
				}

				for(int ndx = 0; ndx < windowSize; ndx++) {
					if (dnaSeq.charAt(startNdx + ndx) == 'G' || dnaSeq.charAt(startNdx + ndx) == 'C') {
						gcCounter++;
					}
					if(dnaSeq.charAt(startNdx + ndx) != 'N') {
						gcContentLen++;
					}
				}

				//calculates gcContent
				double gcContent = ((double)gcCounter / gcContentLen);
				DecimalFormat formatter = new DecimalFormat("0.00000");

				//prints to file
				bufferedWriter.write(startNdx + ", " + formatter.format(gcContent));

				if(initWindowSize == windowSize) {
					bufferedWriter.newLine();
					gcCounter = 0;
					gcContentLen = 0;
					startNdx += stepSize;
				} else {
					remainderLen = windowSize;
					break;
				}
			}
			bufferedWriter.close();
		} catch(IOException e) {
			System.out.println("Error writing to file.");
		}	
	}

	public void countN() {
		nCounter = 0;
		for(int ndx = 0; ndx < dnaSeq.length(); ndx++) {
			if(dnaSeq.charAt(ndx) == 'N') {
				nCounter++;
			}
		}
	}
	public void reset() {
		fileWriter = null;
		bufferedWriter = null;
		dnaSeq = "";
		nCounter = 0;
		remainderLen = 0;
		filename = "";
	}

	public String getRemainder() {
		return "" + remainderLen;
	}

	public String getNCount() {
		return "" + nCounter;
	}
}
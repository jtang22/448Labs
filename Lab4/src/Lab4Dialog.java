
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

public class Lab4Dialog extends JPanel implements ActionListener{

    Lab4 model;
    JButton openSeqButton, openFASTAButton, runButton, clearButton; 
    JFileChooser seqFC, fastaFC;
    JTextField seqFileTextField, fastaFileTextField;
    File seqFile, fastaFile;
    JRadioButton reverseComp, noneComp;
    char reverseFlag;
    public Lab4Dialog() {
        super(new BorderLayout());
        model = new Lab4();
        createGUI();
        reverseFlag = 'n';
    }

    public static void main(String[] args) {
        runGUI();
    }

    private static void runGUI() {
        JFrame frame = new JFrame("Gene Match Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Lab4Dialog());

        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        //Handle open button action.
        if (e.getSource() == openSeqButton) {
            int returnVal = seqFC.showOpenDialog(Lab4Dialog.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                seqFile = seqFC.getSelectedFile();
                seqFileTextField.setText(seqFile.getName());
            } 
        } else if (e.getSource() == openFASTAButton) {
            int returnVal = fastaFC.showOpenDialog(Lab4Dialog.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fastaFile = fastaFC.getSelectedFile();
                fastaFileTextField.setText(fastaFile.getName());
            } 
        } else if (e.getSource() == runButton) {
            String[] fastaName, seqName;

            if(seqFile == null || fastaFile == null) {
                JOptionPane.showMessageDialog(null, "Please insert file(s).");
                return;
            }


            fastaName = fastaFile.getName().split("\\.");
            seqName = seqFile.getName().split("\\.");
            if ( (fastaName[1].equals("txt") || fastaName[1].equals("fasta")) && seqName[1].equals("txt") ) {
                int check = model.seqSearch(fastaFile, seqFile, reverseFlag);
                if(check == 1) {
                    JOptionPane.showMessageDialog(null, "Unable to open files.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "File is not .txt or .fasta");
            }

        } else if ( e.getSource() == clearButton) {
            seqFileTextField.setText("");
            fastaFileTextField.setText("");
            seqFile = null;
            fastaFile = null;
            noneComp.setSelected(true);
        } else if(e.getSource() == reverseComp) {
            reverseFlag = 'y';
        }
        else if(e.getSource() == noneComp) {
            reverseFlag = 'n';
        }
    }

    private void createGUI() {
        //file chooser for seq File
        JPanel seqPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel seqLabel = new JLabel();
        seqLabel.setText("Insert Sequence File: ");
        seqPanel.add(seqLabel);

        seqFC = new JFileChooser();
        seqFC.setCurrentDirectory(new File("."));
        openSeqButton = new JButton("Open Sequence File");
        openSeqButton.addActionListener(this);

        seqFileTextField = new JTextField();
        seqFileTextField.setEditable(false);
        seqFileTextField.setPreferredSize(new Dimension(300, 24));

        JPanel seqFilePanel = new JPanel();
        seqFilePanel.add(seqFileTextField);
        seqFilePanel.add(openSeqButton);

        //file chooser for fasta File
        JPanel fastaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fastaLabel = new JLabel();
        fastaLabel.setText("Insert FASTA File: ");
        fastaPanel.add(fastaLabel);

        fastaFC = new JFileChooser();
        fastaFC.setCurrentDirectory(new File("."));
        openFASTAButton = new JButton("Open FASTA File");
        openFASTAButton.addActionListener(this);

        fastaFileTextField = new JTextField();
        fastaFileTextField.setEditable(false);
        fastaFileTextField.setPreferredSize(new Dimension(300, 24));

        JPanel fastaFilePanel = new JPanel();
        fastaFilePanel.add(fastaFileTextField);
        fastaFilePanel.add(openFASTAButton);

        //action buttons
        JPanel actionButtons = new JPanel();
        runButton = new JButton("Run");
        runButton.addActionListener(this);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        actionButtons.add(runButton);
        actionButtons.add(clearButton);

        JPanel radioButtons = new JPanel();
        reverseComp = new JRadioButton("Reverse Complement");
        noneComp = new JRadioButton("None");
        ButtonGroup buttons = new ButtonGroup();
        buttons.add(reverseComp);
        buttons.add(noneComp);
        reverseComp.addActionListener(this);
        noneComp.addActionListener(this);
        noneComp.setSelected(true);
        radioButtons.add(reverseComp);
        radioButtons.add(noneComp);

        setLayout(new GridLayout(6, 1));
        add(seqPanel);
        add(seqFilePanel);
        add(fastaPanel);
        add(fastaFilePanel);
        add(radioButtons);
        add(actionButtons);
    }
 }
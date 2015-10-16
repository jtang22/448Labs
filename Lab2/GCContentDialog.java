
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

public class GCContentDialog extends JPanel implements ActionListener{

    GCContentWindow model;
    JButton openButton;
    JButton runButton;
    JButton clearButton; 
    JFileChooser fc;
    JTextField fileTextField;
    JTextField windowTextField;
    JTextField stepTextField;
    JLabel nSolution;
    JLabel remainderSolution;
    File file;

    public static void main(String[] args) {
        runGUI();
    
    }

    public GCContentDialog() {
        super(new BorderLayout());

        model = new GCContentWindow();

        //file chooser
        fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));
        openButton = new JButton("Open File");
        openButton.addActionListener(this);

        fileTextField = new JTextField();
        fileTextField.setEditable(false);
        fileTextField.setPreferredSize(new Dimension(300, 24));

        JPanel filePanel = new JPanel();
        filePanel.add(fileTextField);
        filePanel.add(openButton);

        //window size
        JPanel windowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel windowLabel = new JLabel();
        windowLabel.setText("Window Size: ");
        windowTextField = new JTextField();
        windowTextField.setPreferredSize(new Dimension(100, 24));
        windowTextField.addActionListener(this);
        windowPanel.add(windowLabel);
        windowPanel.add(windowTextField);

        //step size
        JPanel stepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel stepLabel = new JLabel();
        stepLabel.setText("Step Size: ");
        stepTextField = new JTextField();
        stepTextField.setPreferredSize(new Dimension(100, 24));
        stepTextField.addActionListener(this);
        stepPanel.add(stepLabel);
        stepPanel.add(stepTextField);

        //number of 'N' panel
        JPanel nPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nLabel = new JLabel();
        nLabel.setText("Number of 'N' in sequence: ");
        nSolution = new JLabel();
        nPanel.add(nLabel);
        nPanel.add(nSolution);
        //remainder panel
        JPanel remainderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel remainderLabel = new JLabel();
        remainderLabel.setText("Number of Remainder: ");
        remainderSolution = new JLabel();
        remainderPanel.add(remainderLabel);
        remainderPanel.add(remainderSolution);

        //action buttons
        JPanel actionButtons = new JPanel();
        runButton = new JButton("Run");
        runButton.addActionListener(this);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        actionButtons.add(runButton);
        actionButtons.add(clearButton);

        setLayout(new GridLayout(6, 1));
        add(filePanel);
        add(windowPanel);
        add(stepPanel);
        add(nPanel);
        add(remainderPanel);
        add(actionButtons);
    }

    private static void runGUI() {
        JFrame frame = new JFrame("GC Content Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GCContentDialog());

        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(GCContentDialog.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                fileTextField.setText(file.getName());

                if(model.readFile(file) != 1) {
                    System.out.println("File was not found.");
                }
            } 
        } else if (e.getSource() == runButton) {
            String[2] name = file.getName().split("\\.");
            if(name[1].equals("txt") || name[1].equals("fasfa")) {
                try {
                    model.calcGCContent(Integer.parseInt(windowTextField.getText()), Integer.parseInt(stepTextField.getText()));
                    nSolution.setText(model.getNCount());
                    remainderSolution.setText(model.getRemainder());
                }
                catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Enter number for step or window size");
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "File is not .txt or .fasfa");
            }

        } else if ( e.getSource() == clearButton) {
            model.reset();
            fileTextField.setText("");
            nSolution.setText("");
            remainderSolution.setText("");
            windowTextField.setText("");
            stepTextField.setText("");
        }
    }
 }
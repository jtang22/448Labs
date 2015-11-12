
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

public class CpGFinderDialog extends JPanel implements ActionListener{

    CpGFinder model;
    JButton openButton;
    JButton runButton;
    JButton clearButton; 
    JFileChooser fc;
    JTextField fileTextField;
    File file;

    public static void main(String[] args) {
        runGUI();
    
    }

    public CpGFinderDialog() {
        super(new BorderLayout());

        model = new CpGFinder();

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


        //action buttons
        JPanel actionButtons = new JPanel();
        runButton = new JButton("Run");
        runButton.addActionListener(this);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        actionButtons.add(runButton);
        actionButtons.add(clearButton);

        setLayout(new GridLayout(2, 1));
        add(filePanel);
        add(actionButtons);
    }

    private static void runGUI() {
        JFrame frame = new JFrame("CpG Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new CpGFinderDialog());

        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(CpGFinderDialog.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                fileTextField.setText(file.getName());
            } 
        } else if (e.getSource() == runButton) {
            String[] name;
            name = file.getName().split("\\.");
            if(name[1].equals("txt")) {
                model.run(file);
            }
            else {
                JOptionPane.showMessageDialog(null, "File is not .txt");
            }

        } else if ( e.getSource() == clearButton) {
            model.reset();
            fileTextField.setText("");
        }
    }
 }
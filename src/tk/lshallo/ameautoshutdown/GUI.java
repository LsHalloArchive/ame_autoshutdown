package tk.lshallo.ameautoshutdown;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class GUI extends JFrame {
	private static final long serialVersionUID = 20170420l;
	JPanel upperPanel;
	JPanel fileNamePanel;
	JPanel checkBoxPanel;

	JTextField filePath;
	JTextField fileName;
	
	JButton findFilePath;
	
	JCheckBox enabled;
	JCheckBox deleteFile;
	
	File selectedFile;

	JLabel fPath;
	JLabel fName;

	public GUI(String title) {
		super(title);
		setLayout(new BorderLayout());

		upperPanel = new JPanel();
		fileNamePanel = new JPanel();
		checkBoxPanel = new JPanel();

		filePath = new JTextField();
		findFilePath = new JButton("Search");
		enabled = new JCheckBox("Enabled");
		fileName = new JTextField("finished.mp4");
		deleteFile = new JCheckBox("Delete finished.mp4 afterwards.");

		fPath = new JLabel("Path: ");
		fName = new JLabel("Filename: ");

		findFilePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					selectedFile = new File(chooser.getSelectedFile().getAbsolutePath());
					filePath.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		
		fileName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				deleteFile.setText("Delete " + fileName.getText() + " afterwards.");		
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				deleteFile.setText("Delete " + fileName.getText() + " afterwards.");
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				deleteFile.setText("Delete " + fileName.getText() + " afterwards.");
			}
		});
		
		deleteFile.setSelected(true);

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(fPath, BorderLayout.WEST);
		upperPanel.add(filePath, BorderLayout.CENTER);
		upperPanel.add(findFilePath, BorderLayout.EAST);
		upperPanel.add(Box.createRigidArea(new Dimension(300, 6)), BorderLayout.SOUTH);

		fileNamePanel.setLayout(new BorderLayout());
		fileNamePanel.add(fName, BorderLayout.WEST);
		fileNamePanel.add(fileName, BorderLayout.CENTER);
		fileNamePanel.add(Box.createRigidArea(new Dimension(300, 6)), BorderLayout.SOUTH);
		
		checkBoxPanel.setLayout(new BorderLayout());
		checkBoxPanel.add(enabled, BorderLayout.NORTH);
		checkBoxPanel.add(deleteFile, BorderLayout.SOUTH);

		add(upperPanel, BorderLayout.NORTH);
		add(fileNamePanel, BorderLayout.CENTER);
		add(checkBoxPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}
	
	boolean checkForFile() {
		if(enabled.isSelected()) {
			File toCheck = new File(selectedFile, fileName.getText());
			System.out.println("Checking for file: " + toCheck.getAbsolutePath());
			if(toCheck.exists()) {
				if(deleteFile.isSelected()) {
					toCheck.delete();
				}
				return true;
			}
		}
		return false;
	}
}

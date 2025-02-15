package es.florida.add_ae01;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldSearch, textFieldReplace;
	private JButton btnOpen, btnSearch, btnReplace;
	private JLabel lblDir;
	private JTextArea textAreaContents;
	private JCheckBox chckbxIgnoreCase, chckbxIgnoreAccents;
	private String workdir;
	private ArrayList<String> tree = new ArrayList<String>();

	/**
	 * Main: launches the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor: creates the frame.
	 */
	public App() {
		setTitle("AE01 - ADD - 24/25");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 593);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tree.clear();
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File("."));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File dir = fc.getSelectedFile();
					workdir = dir.getAbsolutePath();
					lblDir.setText(workdir);
					tree.add(dir.getName());
					getContents(new File(workdir));
					showTree();
				}
			}
		});
		btnOpen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnOpen.setBounds(10, 10, 114, 21);
		contentPane.add(btnOpen);

		lblDir = new JLabel("Open directory...");
		lblDir.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDir.setBounds(134, 15, 848, 13);
		contentPane.add(lblDir);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 51, 990, 376);
		contentPane.add(scrollPane);

		textAreaContents = new JTextArea();
		textAreaContents.setFont(new Font("Consolas", Font.PLAIN, 10));
		scrollPane.setViewportView(textAreaContents);

		JLabel lblNewLabel = new JLabel("Search string:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 449, 85, 13);
		contentPane.add(lblNewLabel);

		textFieldSearch = new JTextField();
		textFieldSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFieldSearch.setBounds(92, 447, 172, 19);
		contentPane.add(textFieldSearch);
		textFieldSearch.setColumns(10);

		chckbxIgnoreCase = new JCheckBox("Ignore case");
		chckbxIgnoreCase.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chckbxIgnoreCase.setBounds(52, 480, 105, 21);
		contentPane.add(chckbxIgnoreCase);

		chckbxIgnoreAccents = new JCheckBox("Ignore accents");
		chckbxIgnoreAccents.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chckbxIgnoreAccents.setBounds(159, 480, 105, 21);
		contentPane.add(chckbxIgnoreAccents);

		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tree.clear();
				File dir = new File(workdir);
				tree.add(dir.getName());
				searchContents(new File(workdir), textFieldSearch.getText());
				showTree();
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSearch.setBounds(10, 519, 254, 21);
		contentPane.add(btnSearch);

		JLabel lblReplaceString = new JLabel("Replace string:");
		lblReplaceString.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblReplaceString.setBounds(312, 451, 85, 13);
		contentPane.add(lblReplaceString);

		textFieldReplace = new JTextField();
		textFieldReplace.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFieldReplace.setColumns(10);
		textFieldReplace.setBounds(394, 449, 172, 19);
		contentPane.add(textFieldReplace);

		btnReplace = new JButton("Replace");
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tree.clear();
				File dir = new File(workdir);
				tree.add(dir.getName());
				replaceContents(new File(workdir), textFieldSearch.getText(), textFieldReplace.getText());
				showTree();
			}
		});
		btnReplace.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnReplace.setBounds(312, 480, 254, 21);
		contentPane.add(btnReplace);
	}

	/**
	 * This method fills a global ArrayList of strings with the contents of a given
	 * directory.
	 * 
	 * @param dir Directory to get the contents of
	 */
	private void getContents(File dir) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				int indent = calculateIndent(files[i]);
				String indentStr = "";
				for (int j = 0; j < indent; j++) {
					indentStr += "|   ";
				}
				tree.add(indentStr + "|-- \\" + files[i].getName());
				getContents(new File(files[i].getAbsolutePath()));
			} else {
				String fileSize = String.format("%.1f", (float) (files[i].length()) / 1024); // KB
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYY HH:mm:ss");
				String fileModified = sdf.format(new Date(files[i].lastModified()));
				int indent = calculateIndent(files[i]);
				String indentStr = "";
				for (int j = 0; j < indent; j++) {
					indentStr += "|   ";
				}
				tree.add(indentStr + "|-- " + files[i].getName() + " (" + fileSize + " KB - " + fileModified + ")");
			}
		}
	}

	/**
	 * This method searches the contents of a text file given a search string
	 * considering case and accent select options.
	 * 
	 * @param dir          Work directory
	 * @param searchString String to search
	 */
	private void searchContents(File dir, String searchString) {
		try {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					int indent = calculateIndent(files[i]);
					String indentStr = "";
					for (int j = 0; j < indent; j++) {
						indentStr += "|   ";
					}
					tree.add(indentStr + "|-- \\" + files[i].getName());
					searchContents(new File(files[i].getAbsolutePath()), searchString);
				} else {
					String text = "";
					if (files[i].getName().endsWith(".pdf")) {
						PDDocument document = PDDocument.load(files[i]);
						PDFTextStripper stripper = new PDFTextStripper();
						text = stripper.getText(document);
						document.close();
					} else {
						FileReader fr = new FileReader(files[i]);
						BufferedReader br = new BufferedReader(fr);
						String linea = "";
						while ((linea = br.readLine()) != null) {
							text += linea;
						}
						br.close();
					}
					searchString = reFormat(searchString);
					text = reFormat(text);
					int occurrences = 0;
					int index = text.indexOf(searchString);
					while (index > -1) {
						occurrences++;
						index = text.indexOf(searchString, index + 1);
					}
					int indent = calculateIndent(files[i]);
					String indentStr = "";
					for (int j = 0; j < indent; j++) {
						indentStr += "|   ";
					}
					tree.add(indentStr + "|-- " + files[i].getName() + " (" + occurrences + " occurrences)");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method replaces the contents of a text file changing a search string for
	 * a replace string (find and replace) considering case and accent select
	 * options.
	 * 
	 * @param dir           Work directory
	 * @param searchString  String to search
	 * @param replaceString New string
	 */
	private void replaceContents(File dir, String searchString, String replaceString) {
		try {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					int indent = calculateIndent(files[i]);
					String indentStr = "";
					for (int j = 0; j < indent; j++) {
						indentStr += "|   ";
					}
					tree.add(indentStr + "|-- \\" + files[i].getName());
					replaceContents(new File(files[i].getAbsolutePath()), searchString, replaceString);
				} else {
					String textOriginal = "";
					FileReader fr = new FileReader(files[i]);
					BufferedReader br = new BufferedReader(fr);
					String linea = "";
					while ((linea = br.readLine()) != null) {
						textOriginal += linea + "\n";
					}
					br.close();
					searchString = reFormat(searchString);
					String textReformat = reFormat(textOriginal);
					String textMod = ""; 
					int occurrences = 0;
					int index = textReformat.indexOf(searchString);
					while (index > -1) {
						occurrences++;
						textMod += textOriginal.substring(0, index) + replaceString;
						textReformat = textReformat.substring(index + searchString.length());
						textOriginal = textOriginal.substring(index + searchString.length());
						index = textReformat.indexOf(searchString);
					}
					textMod += textOriginal;
					if (occurrences > 0) {
						String newName = files[i].getParentFile().getAbsolutePath()
								+ System.getProperty("file.separator") + "MOD_" + files[i].getName();
						File newFile = new File(newName);
						FileWriter fw = new FileWriter(newFile);
						fw.write(textMod);
						fw.close();
					}
					int indent = calculateIndent(files[i]);
					String indentStr = "";
					for (int j = 0; j < indent; j++) {
						indentStr += "|   ";
					}
					tree.add(indentStr + "|-- " + files[i].getName() + " (" + occurrences + " replacements)");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method calculates the amount of indent of a given file or directory.
	 * 
	 * @param f File or directory
	 * @return integer with the amount of indent
	 */
	private int calculateIndent(File f) {
		String currentDir = f.getAbsolutePath();
		int slashesWorkdir = 0;
		int index = workdir.indexOf("\\");
		while (index > -1) {
			index = workdir.indexOf("\\", index + 1);
			slashesWorkdir++;
		}
		int slashesCurrentDir = 0;
		index = currentDir.indexOf("\\");
		while (index > -1) {
			index = currentDir.indexOf("\\", index + 1);
			slashesCurrentDir++;
		}
		int indent = slashesCurrentDir - slashesWorkdir - 1;
		return indent;
	}

	/**
	 * This method prepares and sets the output to be placed in the JTextArea
	 * component.
	 */
	private void showTree() {
		String textAreaTree = "";
		for (String s : tree) {
			textAreaTree += s + "\n";
		}
		textAreaContents.setText(textAreaTree);
	}
	
	
	/**
	 * This method reformats a string to ignore case and accents if checkboxes are selected
	 * @param strOriginal
	 * @return reformatted string
	 */
	private String reFormat(String strOriginal) {
		String strModified = strOriginal;
		if (chckbxIgnoreAccents.isSelected()) {
			strModified = strModified.replaceAll("[áàäâã]", "a").replaceAll("[éèëê]", "e")
					.replaceAll("[íìïî]", "i").replaceAll("[óòöôõ]", "o").replaceAll("[úùüû]", "u")
					.replaceAll("[ÁÀÄÂÃ]", "A").replaceAll("[ÉÈËÊ]", "E").replaceAll("[ÍÌÏÎ]", "I")
					.replaceAll("[ÓÒÖÔÕ]", "O").replaceAll("[ÚÙÜÛ]", "U");
		}
		if (chckbxIgnoreCase.isSelected()) {
			strModified = strModified.toUpperCase();
		}
		return strModified;
	}

}

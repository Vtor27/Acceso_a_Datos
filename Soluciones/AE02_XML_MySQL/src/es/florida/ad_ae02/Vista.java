package es.florida.ad_ae02;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JEditorPane;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldSQL;
	private JButton btnRegister, btnLogin, btnImportData, btnSql, btnExportToCsv, btnLogout;
	JTextArea txtrXmlData;
	JEditorPane txtrQuery;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Vista frame = new Vista();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public Vista() {
		setTitle("Population Data Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 812, 625);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnRegister = new JButton("Register new user (only admin)");
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRegister.setBounds(243, 22, 219, 21);
		contentPane.add(btnRegister);
		
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogin.setBounds(25, 22, 208, 21);
		contentPane.add(btnLogin);
		
		btnImportData = new JButton("Import data (only admin)");
		btnImportData.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnImportData.setBounds(472, 22, 213, 21);
		contentPane.add(btnImportData);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 59, 745, 206);
		contentPane.add(scrollPane);
		
		txtrXmlData = new JTextArea();
		txtrXmlData.setText("XML data...");
		scrollPane.setViewportView(txtrXmlData);
		
		textFieldSQL = new JTextField();
		textFieldSQL.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFieldSQL.setBounds(25, 290, 654, 19);
		contentPane.add(textFieldSQL);
		textFieldSQL.setColumns(10);
		
		btnSql = new JButton("SQL");
		btnSql.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSql.setBounds(698, 289, 70, 21);
		contentPane.add(btnSql);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(25, 329, 743, 204);
		contentPane.add(scrollPane_1);
		
		txtrQuery = new JEditorPane();
		txtrQuery.setFont(new Font("Monospaced", Font.PLAIN, 13));
		txtrQuery.setContentType("text/html");
		scrollPane_1.setViewportView(txtrQuery);
		txtrQuery.setText("Query results...");
		
		btnExportToCsv = new JButton("Export to CSV");
		btnExportToCsv.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExportToCsv.setBounds(536, 557, 232, 21);
		contentPane.add(btnExportToCsv);
		
		btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogout.setBounds(698, 22, 70, 21);
		contentPane.add(btnLogout);
		this.setVisible(true);
	}

	public JTextField getTextFieldSQL() {
		return textFieldSQL;
	}

	public JButton getBtnRegister() {
		return btnRegister;
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}
	
	public JButton getBtnLogout() {
		return btnLogout;
	}

	public JButton getBtnImportData() {
		return btnImportData;
	}

	public JButton getBtnSql() {
		return btnSql;
	}

	public JButton getBtnExportToCsv() {
		return btnExportToCsv;
	}

	public JTextArea getTxtrXmlData() {
		return txtrXmlData;
	}

	public JEditorPane getTxtrQuery() {
		return txtrQuery;
	}
	
	
}

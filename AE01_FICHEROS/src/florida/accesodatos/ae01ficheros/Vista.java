package florida.accesodatos.ae01ficheros;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class Vista {

	private JFrame frame;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JTextField textFieldBuscar;
	private JTextField textFieldReemplazar;
	private JTextPane textPane_RutaDirectorio;
	private JButton btnExaminar;
	private JButton btnBuscar;
	private JButton btnReemplazar;
	private JRadioButton rdbtnIgnoraAcentos;
	private JRadioButton rdbtnIgnoraMayusculas;

	/**
	 * Create the application.
	 */
	public Vista() {
		initialize();
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 845, 535);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textPane_RutaDirectorio = new JTextPane();
		textPane_RutaDirectorio.setBackground(new Color(192, 192, 192));
		textPane_RutaDirectorio.setBounds(46, 355, 628, 33);
		frame.getContentPane().add(textPane_RutaDirectorio);
		
		btnExaminar = new JButton("Explorar");
		btnExaminar.setFont(new Font("Arial", Font.PLAIN, 13));
		btnExaminar.setBounds(707, 355, 112, 33);
		frame.getContentPane().add(btnExaminar);
		
		JLabel lblNewLabel = new JLabel("Explorador de Archivos");
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 25));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(220, 11, 359, 33);
		frame.getContentPane().add(lblNewLabel);
		
		textFieldBuscar = new JTextField();
		textFieldBuscar.setBounds(46, 409, 465, 27);
		frame.getContentPane().add(textFieldBuscar);
		textFieldBuscar.setColumns(10);
		
		textFieldReemplazar = new JTextField();
		textFieldReemplazar.setColumns(10);
		textFieldReemplazar.setBounds(46, 447, 465, 27);
		frame.getContentPane().add(textFieldReemplazar);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(521, 409, 112, 27);
		frame.getContentPane().add(btnBuscar);
		
		btnReemplazar = new JButton("Reemplazar");
		btnReemplazar.setBounds(521, 447, 112, 27);
		frame.getContentPane().add(btnReemplazar);
		
		rdbtnIgnoraAcentos = new JRadioButton("Ignorar Acentos");
		rdbtnIgnoraAcentos.setBounds(679, 402, 144, 40);
		frame.getContentPane().add(rdbtnIgnoraAcentos);
		
		rdbtnIgnoraMayusculas = new JRadioButton("Ignorar Mayúsculas");
		rdbtnIgnoraMayusculas.setBounds(679, 449, 144, 40);
		frame.getContentPane().add(rdbtnIgnoraMayusculas);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Arial", Font.PLAIN, 17));
		textArea.setBounds(10, 42, 809, 302);
		frame.getContentPane().add(textArea);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 42, 809, 302);
		frame.getContentPane().add(scrollPane);
		
		this.frame.setVisible(true);
	}
	
	
	/**
	 *Métodos que permitiran interactuar con la interfaz.
	 *
	 * @return	Devuelven los elementos a los que se refieren.
	 */
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public JRadioButton getRdbtnIgnoraMayusculas() {
		return rdbtnIgnoraMayusculas;
	}
	
	public JRadioButton getRdbtnIgnoraAcentos() {
		return rdbtnIgnoraAcentos;
	}

	public JTextField getTextFieldReemplazar() {
		return textFieldReemplazar;
	}
	
	public JTextField getTextFieldBuscar() {
		return textFieldBuscar;
	}
	
	public JTextPane getTextPane_RutaDirectorio() {
		return textPane_RutaDirectorio;
	}
	public JButton getExaminar() {
		return btnExaminar;
	}
	public JButton getBuscar() {
		return btnBuscar;
	}
	public JButton getReemplazar() {
		return btnReemplazar;
	}
}

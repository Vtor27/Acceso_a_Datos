package es.florida.AE02_ADD_XML_MySQL;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa la interfaz gráfica principal de la aplicación.
 * Contiene todos los elementos visuales para interactuar con la base de datos, la creación y visualización de los archivos(csy y xml).
 */
public class Vista {
	
	private JFrame frame;
	private JTextField textField_ConsultaSQL;
	private JButton btnLogin;
	private JButton btnEjecutarConsulta;
	private JButton btnImportarCSV;
	private JButton btnCargarXML;
	private JButton btnExportarResult;
	private JTextArea textAreaConsultaSQL;
	private JTextArea textAreaArchivos;
	private JLabel lblEstadoConexion;
	private JLabel lblTipo_User;
	private JButton btn_NuevoUser;
	private JLabel lblUser_Actual;
	private JButton btnInsertar_ArchivosXML;

	/**
	 * Constructor de la clase Vista.
	 * Inicializa la ventana principal y sus componentes.
	 */
	public Vista() {
		initialite();
	}
	
	/**
	 * Inicializa los componentes de la interfaz gráfica.
	 * Configura la ventana principal, los botones, etiquetas y áreas de texto.
	 */
	private void initialite() {
		frame = new JFrame();
        frame.setSize(946, 657);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
		
        frame.getContentPane().setFont(new Font("Calibri", Font.PLAIN, 17));
        frame.getContentPane().setLayout(null);
		
		btnLogin = new JButton("LOGIN");
		btnLogin.setFont(new Font("Calibri", Font.PLAIN, 20));
		btnLogin.setBounds(763, 11, 156, 57);
		frame.getContentPane().add(btnLogin);
		
		lblTipo_User = new JLabel("Tipo de Usuario: Sin usuario");
		lblTipo_User.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblTipo_User.setBounds(555, 11, 198, 25);
		frame.getContentPane().add(lblTipo_User);
		
		JSeparator separatorTop = new JSeparator();
		separatorTop.setBounds(10, 79, 909, 2);
		frame.getContentPane().add(separatorTop);
		
		JSeparator separatorLeft = new JSeparator();
		separatorLeft.setOrientation(SwingConstants.VERTICAL);
		separatorLeft.setBounds(10, 79, 1, 104);
		frame.getContentPane().add(separatorLeft);
		
		JSeparator separatorRight = new JSeparator();
		separatorRight.setOrientation(SwingConstants.VERTICAL);
		separatorRight.setBounds(918, 79, 1, 104);
		frame.getContentPane().add(separatorRight);
		
		JSeparator separatorBottom = new JSeparator();
		separatorBottom.setBounds(10, 181, 909, 2);
		frame.getContentPane().add(separatorBottom);
		
		textField_ConsultaSQL = new JTextField();
		textField_ConsultaSQL.setFont(new Font("Calibri", Font.PLAIN, 15));
		textField_ConsultaSQL.setBounds(20, 97, 418, 31);
		frame.getContentPane().add(textField_ConsultaSQL);
		textField_ConsultaSQL.setColumns(10);
		
		JLabel lblTituloAcciones = new JLabel("Acciones ");
		lblTituloAcciones.setFont(new Font("Calibri", Font.PLAIN, 13));
		lblTituloAcciones.setBounds(28, 67, 95, 19);
		frame.getContentPane().add(lblTituloAcciones);
		
		JLabel lblLabelConsulta = new JLabel("Consulta SQL");
		lblLabelConsulta.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblLabelConsulta.setBounds(20, 86, 136, 14);
		frame.getContentPane().add(lblLabelConsulta);
		
		btnEjecutarConsulta = new JButton("Ejecutar Consulta");
		btnEjecutarConsulta.setFont(new Font("Calibri", Font.BOLD, 13));
		btnEjecutarConsulta.setBounds(448, 97, 136, 31);
		frame.getContentPane().add(btnEjecutarConsulta);
		
		btnImportarCSV = new JButton("Importar CSV");
		btnImportarCSV.setFont(new Font("Calibri", Font.BOLD, 13));
		btnImportarCSV.setBounds(772, 92, 136, 31);
		frame.getContentPane().add(btnImportarCSV);
		
		btnCargarXML = new JButton("Cargar XML");
		btnCargarXML.setFont(new Font("Calibri", Font.BOLD, 13));
		btnCargarXML.setBounds(772, 139, 136, 31);
		frame.getContentPane().add(btnCargarXML);
		
		JLabel lblLabelVisualCosulta = new JLabel("Visualización de la Consulta SQL");
		lblLabelVisualCosulta.setFont(new Font("Calibri", Font.PLAIN, 16));
		lblLabelVisualCosulta.setBounds(135, 186, 232, 31);
		frame.getContentPane().add(lblLabelVisualCosulta);
		
		JLabel lblLabelVisualCsvXml = new JLabel("Visualización de archivos XML/CSV");
		lblLabelVisualCsvXml.setFont(new Font("Calibri", Font.PLAIN, 16));
		lblLabelVisualCsvXml.setBounds(594, 186, 232, 31);
		frame.getContentPane().add(lblLabelVisualCsvXml);
		
		btnExportarResult = new JButton("Exportar Resultado");
		btnExportarResult.setFont(new Font("Calibri", Font.BOLD, 13));
		btnExportarResult.setBounds(158, 139, 145, 31);
		frame.getContentPane().add(btnExportarResult);
		
		JScrollPane scrollPaneConsultaSQL = new JScrollPane();
		scrollPaneConsultaSQL.setBounds(10, 211, 439, 377);
		frame.getContentPane().add(scrollPaneConsultaSQL);
		
		textAreaConsultaSQL = new JTextArea();
		textAreaConsultaSQL.setFont(new Font("Calibri", Font.PLAIN, 15));
		scrollPaneConsultaSQL.setViewportView(textAreaConsultaSQL);
		
		JScrollPane scrollPaneArchivos = new JScrollPane();
		scrollPaneArchivos.setBounds(480, 211, 439, 377);
		frame.getContentPane().add(scrollPaneArchivos);
		
		textAreaArchivos = new JTextArea();
		textAreaArchivos.setFont(new Font("Calibri", Font.PLAIN, 15));
		scrollPaneArchivos.setViewportView(textAreaArchivos);
		
		JLabel lblLabelConexion = new JLabel("Estado de la conexión: ");
		lblLabelConexion.setBounds(695, 599, 131, 14);
		frame.getContentPane().add(lblLabelConexion);
		
		lblEstadoConexion = new JLabel("Desconectado");
		lblEstadoConexion.setForeground(Color.RED);
		lblEstadoConexion.setBounds(832, 599, 87, 14);
		frame.getContentPane().add(lblEstadoConexion);
		
		JLabel lblTitulo = new JLabel("Consultor SQL y creador de Archivos");
		lblTitulo.setFont(new Font("Calibri", Font.BOLD, 26));
		lblTitulo.setBounds(76, 15, 456, 46);
		frame.getContentPane().add(lblTitulo);
		
		btn_NuevoUser = new JButton("Nuevo User");
		btn_NuevoUser.setFont(new Font("Calibri", Font.PLAIN, 15));
		btn_NuevoUser.setBounds(623, 39, 110, 29);
		frame.getContentPane().add(btn_NuevoUser);
		
		lblUser_Actual = new JLabel("Usuario: Ninguno");
		lblUser_Actual.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblUser_Actual.setBounds(20, 599, 257, 14);
		frame.getContentPane().add(lblUser_Actual);
		
		btnInsertar_ArchivosXML = new JButton("Insertar XML");
		btnInsertar_ArchivosXML.setFont(new Font("Calibri", Font.BOLD, 13));
		btnInsertar_ArchivosXML.setBounds(623, 139, 136, 31);
		frame.getContentPane().add(btnInsertar_ArchivosXML);

		this.frame.setVisible(true);
	}
	public JTextField getTextField_ConsultaSQL() {
		return textField_ConsultaSQL;
	}
	
	public JTextArea getTextAreaConsultaSQL() {
		return textAreaConsultaSQL;
	}
	
	public JTextArea getTextAreaArchivos() {
		return textAreaArchivos;
	}
	
	public JButton getBtnInsertar_ArchivosXML() {
		return btnInsertar_ArchivosXML;
	}
	
	public JButton getBtnLogin() {
		return btnLogin;
	}
	
	public JButton getBtnEjecutarConsulta() {
		return btnEjecutarConsulta;
	}
	
	public JButton getBtnImportarCSV() {
		return btnImportarCSV;
	}
	
	public JButton getBtnCargarXML() {
		return btnCargarXML;
	}
	
	public JButton getBtnExportarResult() {
		return btnExportarResult;
	}
	
	public JLabel getLblEstadoConexion() {
		return lblEstadoConexion;
	}
	
	public JLabel getLblTipo_User() {
		return lblTipo_User;
	}
	
	public JButton getBtn_NuevoUser() {
		return btn_NuevoUser;
	}
	
	public JLabel getLblUser_Actual() {
		return lblUser_Actual;
	}
}
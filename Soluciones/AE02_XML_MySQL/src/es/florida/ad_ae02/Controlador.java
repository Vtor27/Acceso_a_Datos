package es.florida.ad_ae02;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Controlador {
	private Modelo modelo;
	private Vista vista;
	private ActionListener actionListener_login, actionListener_register, actionListener_logout, actionListener_import, actionListener_sql, actionListener_exportar;

	/**
	 * Método constructor de la clase Controlador, recibe instancias de las clases
	 * Modelo y Vista
	 * 
	 * @param modelo
	 * @param vista
	 */
	Controlador(Modelo modelo, Vista vista) {
		this.modelo = modelo;
		this.vista = vista;
		control();
	}

	private void control() {
		
		actionListener_login = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String[] dataLogin = ventanaLogin();
				boolean respuesta = modelo.login(dataLogin[0], dataLogin[1]);
				if (respuesta) {
					vista.getBtnLogin().setBackground(Color.GREEN);
				}
			}
		};
		vista.getBtnLogin().addActionListener(actionListener_login);
		
		actionListener_logout = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				modelo.logout();
				vista.getBtnLogin().setBackground(null);
			}
		};
		vista.getBtnLogout().addActionListener(actionListener_logout);
		
		actionListener_register = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String[] dataRegister = ventanaRegister();
				if (!dataRegister[0].equals("") ) {
					modelo.register(dataRegister[0], dataRegister[1]);
				}
			}
		};
		vista.getBtnRegister().addActionListener(actionListener_register);
		
		actionListener_import = new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				String textXML = modelo.importarDatos();
				vista.getTxtrXmlData().setText(textXML);
			}
		};
		vista.getBtnImportData().addActionListener(actionListener_import);
		
		actionListener_sql = new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				String resultado = modelo.consultaSQL(vista.getTextFieldSQL().getText());
				vista.getTxtrQuery().setText(resultado);
			}
		};
		vista.getBtnSql().addActionListener(actionListener_sql);
		
		actionListener_exportar = new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				modelo.exportarConsulta(vista.getTextFieldSQL().getText());
			}
		};
		vista.getBtnExportToCsv().addActionListener(actionListener_exportar);
		
	}

	private String[] ventanaRegister() {
		String[] respuesta = { "", "" };
		JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
		JTextField campoUsuario = new JTextField();
		JPasswordField campoContrasenya = new JPasswordField();
		JPasswordField campoConfirmarContrasenya = new JPasswordField();
		panel.add(new JLabel("User:"));
		panel.add(campoUsuario);
		panel.add(new JLabel("Password:"));
		panel.add(campoContrasenya);
		panel.add(new JLabel("Rewrite password:"));
		panel.add(campoConfirmarContrasenya);
		String[] opciones = { "Accept", "Cancel" };
		int opcion = JOptionPane.showOptionDialog(null, panel, "New user...", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
		if (opcion == JOptionPane.OK_OPTION) {
			String usuario = campoUsuario.getText();
			String contrasenya = new String(campoContrasenya.getPassword());
			String confirmarContrasenya = new String(campoConfirmarContrasenya.getPassword());
			if (contrasenya.equals(confirmarContrasenya)) {
				respuesta[0] = usuario;
				respuesta[1] = contrasenya;
			} else {
				JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return respuesta;
	}

	private String[] ventanaLogin() {
		String[] respuesta = { "", "" };
		JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
		JTextField campoUsuario = new JTextField();
		JPasswordField campoContrasenya = new JPasswordField();
		panel.add(new JLabel("User:"));
		panel.add(campoUsuario);
		panel.add(new JLabel("Password:"));
		panel.add(campoContrasenya);
		String[] opciones = { "Accept", "Cancel" };
		int opcion = JOptionPane.showOptionDialog(null, panel, "Login...", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
		if (opcion == JOptionPane.OK_OPTION) {
			String usuario = campoUsuario.getText();
			String contrasenya = new String(campoContrasenya.getPassword());
			respuesta[0] = usuario;
			respuesta[1] = contrasenya;
		}
		return respuesta;
	}

}

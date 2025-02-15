package es.florida.acceso_a_datos;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

/**
 * Clase que representa la vista de login para la aplicación. Permite al usuario
 * iniciar sesión o registrarse en el sistema.
 */
public class VistaLogin {

	private final JPanel contentPanel = new JPanel();

	private JDialog dialog;
	private JLabel lblTitulo;
	private JButton btnLogin_Login;
	private JButton btnRegistrar_Login;
	private JTextField textField_User;
	private JPasswordField passwordField_Password;
	private JPasswordField passwordField_RepitePasword;
	private JLabel lblRepiteContraseña;

	/**
	 * Constructor de la clase 'VistaLogin'. Configura los componentes gráficos de
	 * la ventana de login.
	 */
	public VistaLogin() {
		dialog = new JDialog();
		dialog.setBounds(100, 100, 331, 343);
		dialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lblTitulo = new JLabel("LOGIN");
			lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitulo.setFont(new Font("Calibri", Font.BOLD, 40));
			lblTitulo.setBounds(10, 11, 295, 55);
			contentPanel.add(lblTitulo);
		}
		{
			JLabel lblUser = new JLabel("User:");
			lblUser.setFont(new Font("Calibri", Font.PLAIN, 15));
			lblUser.setBounds(45, 73, 46, 14);
			contentPanel.add(lblUser);
		}
		{
			textField_User = new JTextField();
			textField_User.setFont(new Font("Calibri", Font.PLAIN, 15));
			textField_User.setBounds(47, 86, 226, 20);
			contentPanel.add(textField_User);
			textField_User.setColumns(10);
		}
		{
			JLabel lblContraseña = new JLabel("Contraseña:");
			lblContraseña.setFont(new Font("Calibri", Font.PLAIN, 15));
			lblContraseña.setBounds(45, 127, 86, 14);
			contentPanel.add(lblContraseña);
		}
		{
			lblRepiteContraseña = new JLabel("Respite la Contraseña:");
			lblRepiteContraseña.setFont(new Font("Calibri", Font.PLAIN, 15));
			lblRepiteContraseña.setBounds(45, 183, 152, 14);
			contentPanel.add(lblRepiteContraseña);
			lblRepiteContraseña.setVisible(false);
		}
		{
			btnLogin_Login = new JButton("Login");
			btnLogin_Login.setFont(new Font("Calibri", Font.PLAIN, 15));
			btnLogin_Login.setBounds(94, 225, 110, 29);
			contentPanel.add(btnLogin_Login);
		}
		{
			btnRegistrar_Login = new JButton("Registrar");
			btnRegistrar_Login.setFont(new Font("Calibri", Font.PLAIN, 15));
			btnRegistrar_Login.setBounds(94, 265, 110, 29);
			contentPanel.add(btnRegistrar_Login);
			btnRegistrar_Login.setVisible(false);
		}

		passwordField_Password = new JPasswordField();
		passwordField_Password.setFont(new Font("Calibri", Font.PLAIN, 15));
		passwordField_Password.setBounds(45, 139, 228, 20);
		contentPanel.add(passwordField_Password);

		passwordField_RepitePasword = new JPasswordField();
		passwordField_RepitePasword.setFont(new Font("Calibri", Font.PLAIN, 15));
		passwordField_RepitePasword.setBounds(45, 194, 228, 20);
		contentPanel.add(passwordField_RepitePasword);
		passwordField_RepitePasword.setVisible(false);

		this.dialog.setVisible(false);
	}

	/**
	 * Restablece la vista de login a su estado inicial. Limpia los campos de
	 * entrada y oculta elementos que no vaya a utilizar.
	 */
	public void resetearVistaLogin() {
		dialog.setVisible(false);
		textField_User.setText("");
		passwordField_Password.setText("");
		passwordField_RepitePasword.setText("");

		btnRegistrar_Login.setVisible(false);
		passwordField_RepitePasword.setVisible(false);
		lblRepiteContraseña.setVisible(false);

		lblTitulo.setText("LOGIN");
		btnLogin_Login.setVisible(true);
	}

	public void mostrarVistaLogin() {
		dialog.setVisible(true);
	}

	public void cerrarVentanaLogin() {
		dialog.dispose();
	}

	public JLabel getLblTitulo() {
		return lblTitulo;
	}

	public JButton getBtnLogin_Login() {
		return btnLogin_Login;
	}

	public JButton getBtnRegistrar_Login() {
		return btnRegistrar_Login;
	}

	public JTextField getTextField_User() {
		return textField_User;
	}

	public JPasswordField getPasswordField_Password() {
		return passwordField_Password;
	}

	public JPasswordField getPasswordField_RepitePasword() {
		return passwordField_RepitePasword;
	}

	public JLabel getLblRepiteContraseña() {
		return lblRepiteContraseña;
	}
}
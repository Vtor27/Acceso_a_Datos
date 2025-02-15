package es.florida.AE02_ADD_XML_MySQL;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * Clase controlador que gestiona la interacción entre el modelo, las vistas y
 * la lógica de la aplicación.
 */
public class Controlador {

	private Modelo modelo;
	private Vista vista;
	private VistaLogin vistaLogin;
	private String[] cabeceraActual;
	private List<String[]> datosActuales;

	/**
	 * Constructor de la clase Controlador. Inicializa los componentes principales
	 * del controlador. También desactiva los botones al inicio y define los
	 * controladores para las acciones de los botones.
	 *
	 * @param modelo     Instancia del modelo que gestiona la lógica de negocio y la
	 *                   interacción con la base de datos.
	 * @param vista      Instancia de la vista principal de la aplicación.
	 * @param vistaLogin Instancia de la vista para iniciar de sesión.
	 */
	public Controlador(Modelo modelo, Vista vista, VistaLogin vistaLogin) {
		this.modelo = modelo;
		this.vista = vista;
		this.vistaLogin = vistaLogin;

		controlActEstadoConexion(false);
		vista.getBtn_NuevoUser().setEnabled(false);
		vista.getBtnImportarCSV().setEnabled(false);
		vista.getBtnCargarXML().setEnabled(false);
		vista.getBtnInsertar_ArchivosXML().setEnabled(false);
		vista.getBtnEjecutarConsulta().setEnabled(false);
		vista.getBtnExportarResult().setEnabled(false);
		vista.getTextField_ConsultaSQL().setEditable(false);

		controlPressBtnLogin();
		controlPressBtnNuevoUser();
		controlPressBtnLogin_Login();
		controlBtnRegistrarUser();
		controlBtnImportarCSV();
		controlBtnCargarXML();
		controlInsertarArchivosXML();
		controlBtnEjecutarConsulta();
		controlBtnExportarResultado();
	}

	/**
	 * Controlador del botón "Login". Muestra la ventana de inicio de sesión.
	 */
	public void controlPressBtnLogin() {
		vista.getBtnLogin().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				vistaLogin.mostrarVistaLogin();
			}
		});
	}

	/**
	 * Controlador del botón "Insertar desde Archivos XML". Inserta los datos desde
	 * los archivos XML en la base de datos.
	 */
	public void controlInsertarArchivosXML() {
		vista.getBtnInsertar_ArchivosXML().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modelo.insertarDesdeArchivosXML();

			}
		});
	}

	/**
	 * Controlador del botón "Nuevo Usuario". Configura la vista para registrar un
	 * nuevo usuario.
	 */
	public void controlPressBtnNuevoUser() {
		vista.getBtn_NuevoUser().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				vistaLogin.resetearVistaLogin();
				vistaLogin.mostrarVistaLogin();
				vistaLogin.getBtnRegistrar_Login().setVisible(true);
				vistaLogin.getPasswordField_RepitePasword().setVisible(true);
				vistaLogin.getLblRepiteContraseña().setVisible(true);

				vistaLogin.getBtnLogin_Login().setVisible(false);
				vistaLogin.getLblTitulo().setText("Registrar User");
			}
		});
	}

	/**
	 * Controlador del botón de inicio de sesión en la ventana de login. Valida las
	 * credenciales introducidas por el usuario y configura la interfaz según el
	 * tipo de usuario.
	 */
	public void controlPressBtnLogin_Login() {
		vistaLogin.getBtnLogin_Login().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String user = vistaLogin.getTextField_User().getText();
				String password = new String(vistaLogin.getPasswordField_Password().getPassword());

				if (user.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Para poder loguearse el Usuario y la Contraseña no pueden estar vacios.");
					return;
				}

				if (modelo.validarUsuario(user, password)) {
					String tipoUser = modelo.obtenerTipoUsuario(user);

					vista.getLblUser_Actual().setText("Usuario: " + user);
					JOptionPane.showMessageDialog(null, "Bienvenido " + user);
					vista.getBtnEjecutarConsulta().setEnabled(true);
					vista.getBtnExportarResult().setEnabled(true);
					vista.getTextField_ConsultaSQL().setEditable(true);

					if (tipoUser.equals("admin")) {
						vista.getBtn_NuevoUser().setEnabled(true);
						vista.getBtnImportarCSV().setEnabled(true);
						vista.getBtnCargarXML().setEnabled(true);
						vista.getBtnInsertar_ArchivosXML().setEnabled(true);
						vista.getBtnEjecutarConsulta().setEnabled(true);
						vista.getBtnExportarResult().setEnabled(true);
						vista.getTextField_ConsultaSQL().setEditable(true);
					} else {
						vista.getBtn_NuevoUser().setEnabled(false);
						vista.getBtnImportarCSV().setEnabled(false);
						vista.getBtnCargarXML().setEnabled(false);
						vista.getBtnInsertar_ArchivosXML().setEnabled(false);

					}

					if (modelo.iniciarConexion() != null) {
						controlActEstadoConexion(true);
					} else {
						controlActEstadoConexion(false);
					}

					vistaLogin.getTextField_User().setText("");
					vistaLogin.getPasswordField_Password().setText("");

					vista.getLblTipo_User().setText("Tipo de Usuario: " + tipoUser);

					vistaLogin.cerrarVentanaLogin();

					vista.getBtnLogin().setText("LOGOUT");
					for (ActionListener al : vista.getBtnLogin().getActionListeners()) {
						vista.getBtnLogin().removeActionListener(al);
					}
					vista.getBtnLogin().addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							controlCerrarSesion();
						}
					});
				} else {
					JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Maneja el cierre de sesión del usuario actual. - Muestra un mensaje de
	 * notificación indicando que la sesión ha sido cerrada. - Resetea la vista de
	 * inicio de sesión. - Cierra la conexión con la base de datos y actualiza el
	 * estado de conexión a "Desconectado". - Desactiva todos los botones de
	 * funcionalidades para evitar pulsar los botones si nhaberte logueado. -
	 * Restablece las etiquetas de usuario y tipo de usuario. - Cambia el texto del
	 * botón "Logout" a "Login" y reasigna su funcionalidad para mostrar la vista de
	 * inicio de sesión.
	 */
	public void controlCerrarSesion() {
		JOptionPane.showMessageDialog(null, "Sesión cerrada.");

		vistaLogin.resetearVistaLogin();

		Modelo.cerrarConexionDB();
		controlActEstadoConexion(false);
		vista.getBtn_NuevoUser().setEnabled(false);
		vista.getBtnImportarCSV().setEnabled(false);
		vista.getBtnCargarXML().setEnabled(false);
		vista.getBtnInsertar_ArchivosXML().setEnabled(false);
		vista.getBtnEjecutarConsulta().setEnabled(false);
		vista.getBtnExportarResult().setEnabled(false);
		vista.getTextField_ConsultaSQL().setEditable(false);
		vista.getTextField_ConsultaSQL().setText("");

		vista.getLblTipo_User().setText("Tipo de Usuario: Sin usuario");
		vista.getLblUser_Actual().setText("Usuario: Ninguno");

		vista.getBtnLogin().setText("LOGIN");

		for (ActionListener al : vista.getBtnLogin().getActionListeners()) {
			vista.getBtnLogin().removeActionListener(al);
		}
		vista.getBtnLogin().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vistaLogin.mostrarVistaLogin();
			}
		});
	}

	/**
	 * Actualiza el indicador visual del estado de conexión en la interfaz gráfica.
	 * Cambia el texto y el color del indicador según el estado de conexión
	 * proporcionado.
	 *
	 * @param conectado Indica si la conexión está activa (true) o no (false).
	 */
	public void controlActEstadoConexion(boolean conectado) {
		if (conectado == true) {
			vista.getLblEstadoConexion().setText("Conectado");
			vista.getLblEstadoConexion().setForeground(Color.GREEN);
		} else {
			vista.getLblEstadoConexion().setText("Desconectado");
			vista.getLblEstadoConexion().setForeground(Color.RED);
		}
	}

	/**
	 * Controlador del botón de registrar usuario, maneja la acción de registrar un
	 * nuevo usuario desde la interfaz de registro. Este método valida los datos
	 * ingresados, verifica si el usuario ya existe y, en caso de ser válido,
	 * registra al nuevo usuario en la base de datos.
	 */
	public void controlBtnRegistrarUser() {

		vistaLogin.getBtnRegistrar_Login().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String user = vistaLogin.getTextField_User().getText();
				String password = new String(vistaLogin.getPasswordField_Password().getPassword());
				String confirmPassword = new String(vistaLogin.getPasswordField_RepitePasword().getPassword());

				if (user.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Todos los campos són obligatorios.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!password.equals(confirmPassword)) {
					JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (modelo.comprobarUsuario(user)) {
					JOptionPane.showMessageDialog(null, "El usuario ya existe.", "ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean userInsertado = modelo.registrarUser(user, password);
				if (userInsertado == true) {
					JOptionPane.showMessageDialog(null,
							"Usuario " + user + " registrado en la base de datos con exito.");
					vistaLogin.resetearVistaLogin();
				} else {
					JOptionPane.showMessageDialog(null, "ERROR al insertar el nuevo usuario.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Controlador del boton importar csv, maneja la acción de importar archivos
	 * csv. Este método verifica la existencia de un archivo CSV, lee su contenido,
	 * lo muestra en la vista y crea una tabla en la base de datos con las columnas
	 * correspondientes a la cabecera del archivo.
	 */
	public void controlBtnImportarCSV() {
		vista.getBtnImportarCSV().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					File archivoCSV = new File("AE02_population.csv");
					if (!archivoCSV.exists()) {
						JOptionPane.showMessageDialog(null, "El archivo CSV no existe.", "ERROR",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					List<String[]> populationCSV = modelo.leerCSV(archivoCSV.getAbsolutePath());
					String[] cabecera = modelo.obtenereCabeceraCSV(archivoCSV);

					formatearResultado(cabecera, populationCSV, vista.getTextAreaArchivos());

					modelo.crearTablaPopulation(cabecera);

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Error al leer el archivo CSV.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Error al interactuar con la base de datos.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * Controlador del boton cargar xml, manejar la acción de cargar en la vista
	 * todos los archivos xml.. Este método lee un archivo CSV existente, genera
	 * archivos XML a partir de sus datos y los muestra en la interfaz.
	 */
	public void controlBtnCargarXML() {
		vista.getBtnCargarXML().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<String[]> populationCSV = modelo.leerCSV("AE02_population.csv");
					String[] cabecera = modelo.obtenereCabeceraCSV(new File("AE02_population.csv"));

					modelo.generarArchivosXML(populationCSV, cabecera);

					JOptionPane.showMessageDialog(null, "Archivos XML creados con exito.");

					mostrarArchivosXML(populationCSV, cabecera);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error al generar los archivos XML: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}

			}
		});
	}

	/**
	 * Controla el botón para ejecutar consultas SQL personalizadas. Dependiendo del
	 * tipo de usuario, verifica y ejecuta la consulta, luego muestra los resultados
	 * en la vista.
	 */
	public void controlBtnEjecutarConsulta() {
		vista.getBtnEjecutarConsulta().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String consulta = vista.getTextField_ConsultaSQL().getText();

				String tipoUser = modelo.obtenerTipoUsuario(vista.getLblUser_Actual().getText().split(": ")[1]);

				try {
					List<String[]> query = modelo.ejecutaConsultaPersonalizada(consulta, tipoUser);

					if (query != null && !query.isEmpty()) {
						cabeceraActual = query.get(0);
						datosActuales = query.subList(1, query.size());

						formatearResultado(cabeceraActual, datosActuales, vista.getTextAreaConsultaSQL());
					} else {
						JOptionPane.showMessageDialog(null, "La consulta no devolvió resultados.", "INFO",
								JOptionPane.INFORMATION_MESSAGE);
					}

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta: " + ex.getMessage(), "ERROR",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * Formatea los resultados de una consulta y los muestra en un área de texto.
	 * 
	 * @param cabecera Array que contiene los nombres de las columnas, es la
	 *                 cabecera de la tabla.
	 * @param datos    Lista de arrays de strings, donde cada array representa una
	 *                 fila de la tabla.
	 * @param textArea Área de texto en la que se mostrarán los resultados
	 *                 formateados.
	 */
	public void formatearResultado(String[] cabecera, List<String[]> datos, JTextArea textArea) {
		try {
			String resultado = "";
			String separador = "|";
			datos.remove(0);

			for (String columna : cabecera) {
				resultado += String.format("%-25s" + separador, columna);
			}
			resultado += "\n";

			for (int i = 0; i < cabecera.length; i++) {
				resultado += "=========================" + separador;
			}
			resultado += "\n";

			for (String[] fila : datos) {
				for (String valor : fila) {
					resultado += String.format("%-25s" + separador, valor != null ? valor : "NULL");
				}
				resultado += "\n";
			}

			textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
			textArea.setText(resultado.toString());

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al mostrar los datos.", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Muestra el contenido de los datos en formato XML en el área de texto.
	 *
	 * @param populationCSV Lista de arrays de strings, donde cada array representa
	 *                      una fila de datos. La primera fila corresponde a la
	 *                      cabecera.
	 * @param cabecera      Array de strings que contiene los nombres de las
	 *                      columnas.
	 */
	public void mostrarArchivosXML(List<String[]> populationCSV, String[] cabecera) {
		vista.getTextAreaArchivos().setText("");

		String contenido = "";
		populationCSV.remove(0);

		for (String[] fila : populationCSV) {
			contenido += "<country>\n";
			for (int i = 1; i < cabecera.length; i++) {
				contenido += "     <" + cabecera[i] + ">" + fila[i] + "</" + cabecera[i] + ">\n";
			}
			contenido += "</country>\n\n";
		}
		vista.getTextAreaArchivos().setText(contenido);
	}

	/**
	 * Controla el botón para exportar los resultados de una consulta a un archivo
	 * CSV. Verifica que haya datos para exportar y crea un archivo CSV en la
	 * carpeta "CSV_Exportados", si esta no existe la creará. El archivo incluye la
	 * fecha y hora actual en el nombre para identificarlo.
	 */
	public void controlBtnExportarResultado() {
		vista.getBtnExportarResult().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (cabeceraActual == null || cabeceraActual == null || cabeceraActual.length == 0) {
						JOptionPane.showMessageDialog(null, "No hay datos para exportar.", "INFO",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					File carpetaExportacion = new File("CSV_Exportados");
					if (!carpetaExportacion.exists()) {
						carpetaExportacion.mkdirs();
					}
					DateTimeFormatter dateFormatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
					String fechaHoraActual = LocalDateTime.now().format(dateFormatterDate);

					File archivoCSV = new File(carpetaExportacion, "consulta_" + fechaHoraActual + ".csv");
					System.out.println("Datos actuales: " + datosActuales.size());
					boolean exportacion = modelo.exportarArchivoCSV(cabeceraActual, datosActuales, archivoCSV);

					if (exportacion) {
						JOptionPane.showMessageDialog(null, "Archivo exportado correctamente.", "Éxito",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Error al exportar el archivo.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error al procesar la exportación.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

	}

}

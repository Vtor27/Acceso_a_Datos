package es.florida.AE02_ADD_XML_MySQL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.security.MessageDigest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * La clase Modelo gestiona la lógica principal de interacción con la base de
 * datos.
 * 
 * Esta clase incluye métodos para establecer conexiones con la base de datos,
 * ejecutar consultas, insertar y leer datos, manejar usuarios, y exportar
 * resultados a diferentes formatos como XML o CSV.
 */
public class Modelo {

	private static Connection conexion;

	private static final String URL = "jdbc:mysql://localhost:3306/population"; 		
	private static String userSQLCon = "admin";
	private static String passwordSQLCon = "21232f297a57a5a743894a0e4a801fc3";

	/**
	 * Establece la conexión inicial con la base de datos. Metodo hecho para
	 * inicializar o reutilizar la conexión existente con la base de datos.
	 * 
	 * @return true si la conexión se establece correctamente.
	 */
	public static boolean conectarConDB() {
		try {
			if (conexion == null || conexion.isClosed()) {
				conexion = DriverManager.getConnection(URL, userSQLCon, passwordSQLCon);
				System.out.println("Conexión realizada con exito.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Establece la conexión con la base de datos. Si la conexión no está
	 * inicializada o está cerrada, llama al método `conectarConDB`.
	 * 
	 * @return Connection que se reutiliza en los diferentes métodos que se conectan
	 *         a la base de datos..
	 */
	public Connection iniciarConexion() {
		try {
			if (conexion == null || conexion.isClosed()) {
				conectarConDB();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conexion;
	}

	/**
	 * Cierra la conexión con la base de datos. Este método se asegura de cerrar la
	 * conexión si esta está activa.
	 */
	public static void cerrarConexionDB() {
		try {
			if (conexion != null) {
				conexion.close();
				System.out.println("Conexión cerrada.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Valida si un usuario y contraseña existen en la base de datos.
	 *
	 * @param user     Nombre del usuario.
	 * @param password Contraseña del usuario sin estar encriptada.
	 * @return true si las credenciales coinciden con las de algún usuario devuelve
	 *         'true'.
	 */
	public boolean validarUsuario(String user, String password) {
		try {
			Connection conexion = iniciarConexion();
			String hashedPasword = hashMd5Pasword(password);

			PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM users WHERE login = ? AND password = ?");
			pstmt.setString(1, user);
			pstmt.setString(2, hashedPasword);

			ResultSet rs = pstmt.executeQuery();
			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Obtiene el tipo de usuario desde la base de datos basado en el nombre de
	 * usuario proporcionado.
	 *
	 * @param user Nombre del usuario.
	 * @return Tipo de usuario (por ejemplo, "admin" o "client") si existe, o
	 *         "Unknown" en caso de error o si el usuario no se encuentra.
	 */
	public String obtenerTipoUsuario(String user) {
		try {
			Connection conexion = iniciarConexion();
			PreparedStatement pstmt = conexion.prepareStatement("SELECT type FROM users WHERE login = ?");
			pstmt.setString(1, user);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("type");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Unknown";
	}

	/**
	 * Comprueba si un usuario existe en la base de datos para poder crear un nuevo
	 * usuario.
	 *
	 * @param user Nombre del usuario a verificar.
	 * @return true Si el usuario existe, false en caso contrario o si ocurre un
	 *         error.
	 */
	public boolean comprobarUsuario(String user) {
		try {
			Connection conexion = iniciarConexion();
			PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM users WHERE login = ?");

			pstmt.setString(1, user);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Hashea una contraseña utilizando el algoritmo MD5.
	 *
	 * @param password La contraseña en texto plano que se va a hashear.
	 * @return sb.toString() La contraseña hasheada en formato hexadecimal.
	 * @throws RuntimeException Si ocurre algún error al intentar generar el hash.
	 */
	public static String hashMd5Pasword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hashedBytes = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error al hashear la contraseña", e);
		}
	}

	/**
	 * Registra un nuevo usuario en la base de datos y en el sistema MySQL con unos
	 * permisos específicos.
	 *
	 * @param user     El nombre de usuario que se va a registrar.
	 * @param password La contraseña del usuario en texto plano, que hasheará.
	 * @return true Si el usuario fue registrado correctamente, false en caso
	 *         contrario.
	 */
	public boolean registrarUser(String user, String password) {
		try {
			Connection conexion = iniciarConexion();
			String hashedPassword = hashMd5Pasword(password);

			user = user.replaceAll("[^a-zA-Z0-9_]", "");

			PreparedStatement pstmtInsert = conexion
					.prepareStatement("INSERT INTO users (login, password, type) VALUES (?, ?, 'client')");

			pstmtInsert.setString(1, user);
			pstmtInsert.setString(2, hashedPassword);
			int resultadoInsert = pstmtInsert.executeUpdate();

			if (resultadoInsert > 0) {
				String pstmtCrear = "CREATE USER `" + user + "`@'localhost' IDENTIFIED BY '" + password + "'";
				Statement stmtCreate = conexion.createStatement();
				stmtCreate.executeUpdate(pstmtCrear);

				String pstmtGrant = "GRANT SELECT ON population.population TO `" + user + "`@'localhost'";
				Statement stmtGrant = conexion.createStatement();
				stmtGrant.executeUpdate(pstmtGrant);
				
				return true;
			}
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Lee todo el contenido de un archivo CSV y lo almacena en una lista arrays.
	 *
	 * @param file La ruta del archivo CSV a leer.
	 * @return populationCSV Una lista de arrays, donde cada array representa una
	 *         fila del archivo CSV.
	 */
	public List<String[]> leerCSV(String file) {
		List<String[]> populationCSV = new ArrayList<String[]>();

		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String linea = br.readLine();
			if (linea == null) {
				JOptionPane.showMessageDialog(null, "El archivo CSV está vacío.", "ERROR", JOptionPane.ERROR_MESSAGE);
			}

			while (linea != null) {
				String[] elemPopulation = linea.split(";");
				populationCSV.add(elemPopulation);
				linea = br.readLine();
			}
			fr.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return populationCSV;
	}

	/**
	 * Obtiene y separa la primera línea de un archivo CSV para generar los campos
	 * de la tabla.
	 *
	 * @param archivoCSV Archivo CSV del que se extraerá la cabecera.
	 * @return linea.split(";") Un array con los nombres de las columnas, o null si
	 *         el archivo está vacío.
	 * @throws IOException Si ocurre un error al leer el archivo.
	 */
	public String[] obtenereCabeceraCSV(File archivoCSV) throws IOException {
		try (FileReader fr = new FileReader(archivoCSV); BufferedReader br = new BufferedReader(fr)) {

			String linea = br.readLine();
			if (linea != null) {
				return linea.split(";");
			}
			return null;
		}
	}

	/**
	 * Crea la tabla "population" dinámicamente en la base de datos. Lee la cabecera
	 * del CSV para definir las columnas de la tabla.
	 *
	 * @param populationCSV Un array con los nombres de las columnas extraídas de la
	 *                      cabecera del CSV.
	 * @throws SQLException Si ocurre algún error durante la ejecución de las
	 *                      consultas SQL.
	 */
	public void crearTablaPopulation(String[] populationCSV) throws SQLException {
		Connection conexion = iniciarConexion();

		Statement stmt = conexion.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS population");

		String consultaSQL = "CREATE TABLE population (";

		for (int i = 0; i < populationCSV.length; i++) {
			consultaSQL += populationCSV[i] + " VARCHAR(30)";
			if (i < populationCSV.length - 1) {
				consultaSQL += ", ";
			}
		}
		consultaSQL += ");";

		PreparedStatement pstmt = conexion.prepareStatement(consultaSQL);
		pstmt.executeUpdate(); // SOLO CREA LA TABLA, NO INSERTA NINGÚN VALOR.
	}

	/**
	 * Genera archivos XML individuales para cada registro de la lista 'population'
	 * y los guarda en una carpeta llamada "XML". que si esta no está creada la
	 * creará y mostrará un mensaje de aviso..
	 *
	 * @param population Una lista de arrays donde cada array representa una fila.
	 * @param cabecera   Un array que contiene los nombres de las columnas de la
	 *                   cabecera.
	 */
	public void generarArchivosXML(List<String[]> population, String[] cabecera) {

		try {
			File carpetaXML = new File("XML");

			if (!carpetaXML.exists()) {
				carpetaXML.mkdir();
				JOptionPane.showMessageDialog(null, "Carpeta 'XML' creada correctamente.", "INFO",
						JOptionPane.INFORMATION_MESSAGE);
			}

			for (String[] fila : population) {
				String nombreXML = fila[0] + ".xml";
				File archivoXML = new File(carpetaXML, nombreXML);

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document document = dBuilder.newDocument();

				Element raiz = document.createElement("COUNTRY");
				document.appendChild(raiz);

				for (int i = 0; i < cabecera.length; i++) {
					Element elemento = document.createElement(cabecera[i]);
					elemento.appendChild(document.createTextNode(fila[i]));
					raiz.appendChild(elemento);
				}

				TransformerFactory tranFactory = TransformerFactory.newInstance();
				Transformer aTransformer = tranFactory.newTransformer();

				aTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(archivoXML);
				aTransformer.transform(source, result);
			}
			System.out.println("Archivos XML generados en la carpeta 'XML'.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserta los datos en la tabla "population" desde los archivos XML almacenados
	 * en la carpeta "XML". Si la tabla ya contiene datos, se eliminan para evitar
	 * duplicidades.
	 */
	public void insertarDesdeArchivosXML() {
		try {
			File carpetaXML = new File("XML");
			File[] archivosXML = carpetaXML.listFiles();

			String country, population, density, area, fertility, age, urban, share;

			Connection conexion = iniciarConexion();

			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM population");
			rs.next();
			int numFilas = rs.getInt(1);
			rs.close();

			if (numFilas > 1) {
				JOptionPane.showMessageDialog(null,
						"Para evitar duplicidades se eliminan los archivos insertados anteriormente.");

				PreparedStatement pstmtTruncate = conexion.prepareStatement("TRUNCATE TABLE population");
				pstmtTruncate.executeUpdate();
				pstmtTruncate.close();
			}

			for (File archivo : archivosXML) {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = dbFactory.newDocumentBuilder();
				Document documento = builder.parse(archivo);

				Element raiz = documento.getDocumentElement();

				country = raiz.getElementsByTagName("country").item(0).getTextContent();
				population = raiz.getElementsByTagName("population").item(0).getTextContent();
				density = raiz.getElementsByTagName("density").item(0).getTextContent();
				area = raiz.getElementsByTagName("area").item(0).getTextContent();
				fertility = raiz.getElementsByTagName("fertility").item(0).getTextContent();
				age = raiz.getElementsByTagName("age").item(0).getTextContent();
				urban = raiz.getElementsByTagName("urban").item(0).getTextContent();
				share = raiz.getElementsByTagName("share").item(0).getTextContent();

				PreparedStatement pstmtInsert = conexion.prepareStatement(
						"INSERT INTO population (country, population, density, area, fertility, age, urban, share)"
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

				pstmtInsert.setString(1, country);
				pstmtInsert.setString(2, population);
				pstmtInsert.setString(3, density);
				pstmtInsert.setString(4, area);
				pstmtInsert.setString(5, fertility);
				pstmtInsert.setString(6, age);
				pstmtInsert.setString(7, urban);
				pstmtInsert.setString(8, share);

				int resultadoInsertado = pstmtInsert.executeUpdate();

				if (resultadoInsertado > 0) {
					System.out.println("Datos insertados desde archivo: " + archivo.getName());
				}
				pstmtInsert.close();
			}
			JOptionPane.showMessageDialog(null, "Archivos XML insertados en la tabla 'populattion' correctamente.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al insertar Archivos en la tabla.", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Ejecuta una consulta personalizada, tiene en cuenta el tipo de usuario.
	 * 
	 * @param consulta Consulta SQL introducida por el usuario.
	 * @param tipoUser Tipo de usuario ("client" o "admin").
	 * @return Lista con las filas resultantes de la consulta, donde la primera fila
	 *         es la cabecera.
	 * @throws SQLException Si ocurre algún error en la ejecución de la consulta.
	 */
	public List<String[]> ejecutaConsultaPersonalizada(String consulta, String tipoUser) throws SQLException {
		List<String[]> query = new ArrayList<String[]>();
		Connection conexion = iniciarConexion();

		if (tipoUser.equals("client")) {
			String consultaNormalizada = consulta.toLowerCase();

			if (!consultaNormalizada.startsWith("select") || !consultaNormalizada.contains("from population")) {
				JOptionPane.showMessageDialog(null,
						"Los usuarios de tipo 'cliente' solo pueden acceder a la tabla population y hacer consultas 'SELECT'",
						"ERROR", JOptionPane.ERROR_MESSAGE);
				return query;
			}
		}
		try {
			Statement stmtPresonalizado = conexion.createStatement();

			ResultSet rs = stmtPresonalizado.executeQuery(consulta);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnas = rsmd.getColumnCount();

			String[] cabecera = new String[columnas];
			for (int i = 1; i <= columnas; i++) {
				cabecera[i - 1] = rsmd.getColumnLabel(i);
			}
			query.add(cabecera);

			while (rs.next()) {
				String[] fila = new String[columnas];
				for (int i = 1; i <= columnas; i++) {
					fila[i - 1] = rs.getString(i);
				}
				query.add(fila);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;

	}

	/**
	 * Exporta los datos en formato CSV a un archivo. Si la carpeta "CSV_Exportados"
	 * no existe, la crea automáticamente.
	 *
	 * @param cabecera Es un array de Strings que representa los nombres de las
	 *                 columnas (cabecera del CSV).
	 * @param datos    Una lista de arrays de Strings, donde cada array representa
	 *                 una fila.
	 * @param archivo  Es el archivo de destino donde se exportará el contenido.
	 * @return true Si la exportación fue exitosa, false si ocurrió algún error.
	 */
	public boolean exportarArchivoCSV(String[] cabecera, List<String[]> filas, File archivo) {
		try {
			File carpetaCSV = new File("CSV_Exportados");

			if (!carpetaCSV.exists()) {
				carpetaCSV.mkdir();
				JOptionPane.showMessageDialog(null, "Carpeta 'CSV_Exportados' creada correctamente.", "INFO",
						JOptionPane.INFORMATION_MESSAGE);
			}

			FileWriter fw = new FileWriter(archivo);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(String.join(";", cabecera));
			bw.newLine();

			for (String[] fila : filas) {
				bw.write(String.join(";", fila));
				bw.newLine();
			}
			bw.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
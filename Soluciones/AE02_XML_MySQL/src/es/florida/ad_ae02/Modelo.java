package es.florida.ad_ae02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Modelo {

	String ficheroDatos = "population.csv";
	String directorioXML = "xml";
	String url = "jdbc:mysql://localhost:3306/population";
	Connection conexion = null;
	boolean usuarioAutorizado = false;

	/**
	 * Método que realiza la conexión a la base de datos a partir de la información
	 * del fichero XML de configuración.
	 * 
	 * @return Devuelve un booleano a true indicando si la conexión se ha realizado
	 *         correctamente o false en caso contrario.
	 */
	public boolean conexion(String user, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection(url, user, password);
			usuarioAutorizado = true;
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}

	}

	/**
	 * Método para realizar el login de un usuario en la aplicación y poder realizar
	 * operaciones sobre la base de datos.
	 * 
	 * @param usuario  String con el nombre del usuario.
	 * @param charPass Array de caracteres con el password.
	 * @return Devuelve un booleano a true si se ha accedido correctamente o a false
	 *         en caso contrario.
	 */
	public boolean login(String user, String password) {
		return conexion(user, getMD5(password));
	}

	public void register(String user, String password) {
		if (!usuarioAutorizado) {
			return;
		}
		try {
			String sqlRegister = "INSERT INTO users (login, password, type) VALUES (?, ?, ?)";
			PreparedStatement pstmt = conexion.prepareStatement(sqlRegister);
			pstmt.setString(1, user);
			pstmt.setString(2, getMD5(password));
			pstmt.setString(3, "client");
			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				JOptionPane.showMessageDialog(null, "New user " + user + " registered", "INFO",
						JOptionPane.INFORMATION_MESSAGE);
			}
			pstmt.close();
			String createUserSQL = "CREATE USER '" + user + "' IDENTIFIED BY '" + getMD5(password) + "';";
			String grantPermissionsSQL = "GRANT SELECT ON population.population TO '" + user + "';";
			Statement stmt = conexion.createStatement();
			stmt.executeUpdate(createUserSQL);
			stmt.executeUpdate(grantPermissionsSQL);
			stmt.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Método para cerrar la sesión del usuario activo en la aplicación (no cierra
	 * la conexión con la BDD).
	 */
	public void logout() {
		if (!usuarioAutorizado) {
			JOptionPane.showMessageDialog(null, "User not logged in", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			conexion.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public String getMD5(String password) {
		StringBuilder passwordMD5 = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(password.getBytes());
			for (byte b : messageDigest) {
				passwordMD5.append(String.format("%02x", b));
			}
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return passwordMD5.toString();
	}

	public String importarDatos() {
		if (!usuarioAutorizado) {
			JOptionPane.showMessageDialog(null, "User not logged in", "ERROR", JOptionPane.ERROR_MESSAGE);
			return "";
		}
		String textXML = "";
		try {
			File directorio = new File(directorioXML);
			directorio.mkdirs();
			FileReader fr = new FileReader(new File(ficheroDatos));
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine(); // Cabecera (crear tabla)
			boolean creada = crearTabla(linea);
			if (!creada) {
				br.close();
				fr.close();
				return "";
			}
			while ((linea = br.readLine()) != null) {
				String[] elementos = linea.split(";");
				createXML(elementos[0], elementos[1], elementos[2], elementos[3], elementos[4], elementos[5],
						elementos[6], elementos[7]);
			}
			br.close();
			fr.close();
			FiltroExtension filtro = new FiltroExtension(".xml");
			String[] listaXML = directorio.list(filtro);
			int contadorInsertados = 0;
			for (int i = 0; i < listaXML.length; i++) {
				fr = new FileReader(new File(directorioXML + File.separator + listaXML[i]));
				br = new BufferedReader(fr);
				while ((linea = br.readLine()) != null) {
					textXML += linea + "\n";
				}
				textXML += "------------------------------------------\n";
				contadorInsertados += insertarBDD(new File(directorioXML + File.separator + listaXML[i]));
			}
			JOptionPane.showMessageDialog(null,
					"Inserted " + contadorInsertados + " of " + listaXML.length + " registers.", "INFO",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return textXML;
	}

	public void createXML(String country, String population, String density, String area, String fertility, String age,
			String urban, String share) {
		if (!usuarioAutorizado) {
			JOptionPane.showMessageDialog(null, "User not logged in", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder build = dFact.newDocumentBuilder();
			Document doc = build.newDocument();

			Element raiz = doc.createElement("dataUnit");
			doc.appendChild(raiz);

			Element countryNode = doc.createElement("country");
			countryNode.appendChild(doc.createTextNode(country));
			raiz.appendChild(countryNode);

			Element populationNode = doc.createElement("population");
			populationNode.appendChild(doc.createTextNode(population));
			raiz.appendChild(populationNode);

			Element densityNode = doc.createElement("density");
			densityNode.appendChild(doc.createTextNode(density));
			raiz.appendChild(densityNode);

			Element areaNode = doc.createElement("area");
			areaNode.appendChild(doc.createTextNode(area));
			raiz.appendChild(areaNode);

			Element fertilityNode = doc.createElement("fertility");
			fertilityNode.appendChild(doc.createTextNode(fertility));
			raiz.appendChild(fertilityNode);

			Element ageNode = doc.createElement("age");
			ageNode.appendChild(doc.createTextNode(age));
			raiz.appendChild(ageNode);

			Element urbanNode = doc.createElement("urban");
			urbanNode.appendChild(doc.createTextNode(urban));
			raiz.appendChild(urbanNode);

			Element shareNode = doc.createElement("share");
			shareNode.appendChild(doc.createTextNode(share));
			raiz.appendChild(shareNode);

			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			try {
				FileWriter fw = new FileWriter(directorioXML + File.separator + country + ".xml");
				StreamResult result = new StreamResult(fw);
				aTransformer.transform(source, result);
				fw.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		} catch (TransformerException | ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean crearTabla(String cabecera) {
		boolean creada = false;
		if (!usuarioAutorizado) {
			JOptionPane.showMessageDialog(null, "User not logged in", "ERROR", JOptionPane.ERROR_MESSAGE);
			return creada;
		}
		String campos[] = cabecera.split(";");
		String dropTableSQL = "DROP TABLE IF EXISTS population;";
		String createTableSQL = "CREATE TABLE population (" + campos[0] + " VARCHAR(30), " + campos[1]
				+ "  VARCHAR(30), " + "" + campos[2] + "  VARCHAR(30), " + campos[3] + "  VARCHAR(30), " + campos[4]
				+ "  VARCHAR(30), " + campos[5] + "  VARCHAR(30), " + campos[6] + "  VARCHAR(30), " + campos[7]
				+ "  VARCHAR(30));";
		try {
			Statement stmt = conexion.createStatement();
			stmt.executeUpdate(dropTableSQL);
			stmt.executeUpdate(createTableSQL);
			creada = true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return creada;
	}

	public int insertarBDD(File f) {
		if (!usuarioAutorizado) {
			JOptionPane.showMessageDialog(null, "User not logged in", "ERROR", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
		int rowsInserted = 0;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(f);
			NodeList nodeList = document.getElementsByTagName("dataUnit");
			Node node = nodeList.item(0);
			Element eElement = (Element) node;
			String country = eElement.getElementsByTagName("country").item(0).getTextContent();
			String population = eElement.getElementsByTagName("population").item(0).getTextContent();
			String density = eElement.getElementsByTagName("density").item(0).getTextContent();
			String area = eElement.getElementsByTagName("area").item(0).getTextContent();
			String fertility = eElement.getElementsByTagName("fertility").item(0).getTextContent();
			String age = eElement.getElementsByTagName("age").item(0).getTextContent();
			String urban = eElement.getElementsByTagName("urban").item(0).getTextContent();
			String share = eElement.getElementsByTagName("share").item(0).getTextContent();
			String sqlRegister = "INSERT INTO population (country,population,density,area,fertility,age,urban,share) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt = conexion.prepareStatement(sqlRegister);
			pstmt.setString(1, country);
			pstmt.setString(2, population);
			pstmt.setString(3, density);
			pstmt.setString(4, area);
			pstmt.setString(5, fertility);
			pstmt.setString(6, age);
			pstmt.setString(7, urban);
			pstmt.setString(8, share);
			rowsInserted = pstmt.executeUpdate();
			pstmt.close();
		} catch (ParserConfigurationException | SAXException | IOException | SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return rowsInserted;
	}

	public String consultaSQL(String consultaSQL) {
		if (!usuarioAutorizado) {
			JOptionPane.showMessageDialog(null, "User not logged in", "ERROR", JOptionPane.ERROR_MESSAGE);
			return "";
		}
		String resultado = "<table><tr><th>";
		try {
			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery(consultaSQL);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                resultado += columnName + "</th><th>";
            }
			resultado += metaData.getColumnName(columnCount) + "</th></tr>";
			while (rs.next()) {
				resultado += "<tr><td>";
				for (int i = 1; i < columnCount; i++) {
	                resultado += rs.getString(i) + "</td><td>";
	            }
				resultado += rs.getString(columnCount) + "</td></tr>";
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		resultado += "</table>";
		return resultado;
	}
	
	public void exportarConsulta(String consultaSQL) {
		if (!usuarioAutorizado) {
			JOptionPane.showMessageDialog(null, "User not logged in", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String resultado = "";
		try {
			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery(consultaSQL);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                resultado += columnName + ";";
            }
			resultado += metaData.getColumnName(columnCount) + "\n";
			while (rs.next()) {
				for (int i = 1; i < columnCount; i++) {
	                resultado += rs.getString(i) + ";";
	            }
				resultado += rs.getString(columnCount) + "\n";
			}
			JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setDialogTitle("Save as csv file...");
	        int userSelection = fileChooser.showSaveDialog(null);
	        if (userSelection == JFileChooser.APPROVE_OPTION) {
	            File f = fileChooser.getSelectedFile();
	            if (!f.getAbsolutePath().endsWith(".csv")) {
	                f = new File(f.getAbsolutePath() + ".csv");
	            }
	            try {
	            	FileWriter fw = new FileWriter(f);
	                fw.write(resultado);
	                fw.close();
	                JOptionPane.showMessageDialog(null, "File saved!", "INFO", JOptionPane.INFORMATION_MESSAGE);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

}

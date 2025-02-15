package es.florida.acceso_a_datos;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import static com.mongodb.client.model.Filters.*;

/**
 * Clase Modelo que gestiona la interacción con la base de datos de MongoDB.
 * Esta clase incluye métodos para registrar usuarios, validar credenciales,
 * manejar las cartas y guardar los resultados de las partidas en las
 * colecciones pertinentes dentro de la base de datos.
 */
public class Modelo {

	private static MongoClient mongoClient;
	private static MongoDatabase dataBase;
	public static boolean compruebaConexion = false;

	/**
	 * Establece la conexión con la base de datos de MongoDB utilizando los detalles
	 * de configuración que se encuentran en un archivo JSON llamado
	 * "conexion.json". Si ya existe una conexión activa, la reutilizará.
	 */
	public static void conectarConDataBase() {
		try (BufferedReader br = new BufferedReader(new FileReader("conexion.json"))) {
			System.out.println("Entra en conectarConDataBase");
			if (mongoClient == null) {
				StringBuilder contenidoJson = new StringBuilder();
				String linea;
				while ((linea = br.readLine()) != null) {
					contenidoJson.append(linea);
				}

				JSONObject obj = new JSONObject(contenidoJson.toString());

				String ip = obj.getString("ip");
				int puerto = Integer.parseInt(obj.getString("puerto"));
				String db = obj.getString("db");

				// No aparecen errores.
				Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
				mongoLogger.setLevel(Level.SEVERE);// e.g. or Log.Warning, etc...

				mongoClient = new MongoClient(ip, puerto);
				dataBase = mongoClient.getDatabase(db);
				compruebaConexion = true;
				System.out.println("Conexión exitosa a la base de datos: " + db);
			} else {
				compruebaConexion = true;
				System.out.println("Conexión ya establecida.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cierra la conexión con la base de datos MongoDB si esta estuviera activa.
	 * 
	 * Este método verifica si hay una conexión activa con MongoDB mediante el
	 * cliente, si existe, cierra la conexión y libera los recursos asignados. Si no
	 * hay una conexión activa, se informa por un mensaje en la consola.
	 */
	public static void cerrarConexion() {
		try {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
				dataBase = null;
				System.out.println("Conexión cerrada con casino de MongoDB");
			} else {
				System.out.println("No hay conexión activa para cerrar.");
			}
		} catch (Exception e) {
			System.err.println("Error al cerrar la conexión: " + e.getMessage());
		}
	}

	/**
	 * Registra un nuevo usuario en la base de datos con un nombre de usuario y una
	 * contraseña encriptada.
	 *
	 * @param user     El nombre del usuario que se desea registrar.
	 * @param password La contraseña del usuario que se almacenará encriptada.
	 * 
	 * @return true Si el usuario se registra exitosamente.
	 * @return false Si el usuario ya existe o si ocurre un error durante el
	 *         proceso.
	 */
	public boolean registrarUsuario(String user, String password) {
		try {
			if (dataBase == null) {
				conectarConDataBase();
			}
			MongoCollection<Document> usersCollection = dataBase.getCollection("users");

			if (usersCollection.find(eq("user", user)).first() != null) {
				JOptionPane.showMessageDialog(null, "Ese usuario ya existe.");
				return false;
			}
			String hashedPassword = hashPassword(password);
			Document nuevoUser = new Document("user", user).append("pass", hashedPassword);

			usersCollection.insertOne(nuevoUser);
			cerrarConexion();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Valida si un usuario y contraseña coinciden con uno ya registrado en la base
	 * de datos.
	 * 
	 * Este método genera una consulta a la colección "users" en la base de datos
	 * MongoDB para verificar si existe un documento que coincida con el nombre de
	 * usuario y la contraseña proporcionados. La contraseña se compara después de
	 * aplicarle un hash.
	 * 
	 * @param user     El nombre del usuario a validar.
	 * @param password La contraseña del usuario a validar.
	 * 
	 * @return true Si el usuario y la contraseña son válidos;
	 * @return false En caso de que no lo sea válido.
	 */
	public boolean validarUsuario(String user, String password) {
		try {
			String hashedPasword = hashPassword(password);

			Bson query = and(eq("user", user), eq("pass", hashedPasword));

			MongoCursor<Document> cursor = dataBase.getCollection("users").find(query).iterator();
			boolean usuarioValido = cursor.hasNext();
			return usuarioValido;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Genera un hash SHA-256 a partir de una contraseña.
	 * 
	 * @param password Contraseña que se va a hashear.
	 * 
	 * @return Hash de la contraseña.
	 */
	public String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedHash = digest.digest(password.getBytes());

			StringBuilder hexString = new StringBuilder();
			for (byte b : encodedHash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append("0");
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error al generar el hash SHA-256", e);
		}
	}

	/**
	 * Extrae los IDs de las cartas almacenadas en la base de datos para una de las
	 * dos barajas disponibles ("ES" o "FR").
	 *
	 * @param baraja El tipo de baraja a utilizar: "ES" para la baraja española o
	 *               "FR" para la baraja francesa.
	 * 
	 * @return idCartas Una lista de strings que son los IDs únicos de las cartas en
	 *         la colección elegida. Si ocurre un error durante la consulta, se
	 *         devuelve una lista vacía.
	 */
	public List<String> extraerIDsCartasDeDB(String baraja) {
		List<String> idCartas = new ArrayList<>();
		try {
			String nombreBaraja;
			if (baraja.equals("ES")) {
				nombreBaraja = "cards_es";
			} else {
				nombreBaraja = "cards_fr";
			}

			MongoCollection<Document> coleccion = dataBase.getCollection(nombreBaraja);

			MongoCursor<Document> cursor = coleccion.find().iterator();
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				idCartas.add(doc.getObjectId("_id").toString());
			}
		} catch (Exception e) {
			System.out.println("Error al extraer las cartas de la base de datos.");
		}
		return idCartas;
	}

	/**
	 * Recoje un documento el cual será una carta de la base de datos utilizando su
	 * ID.
	 *
	 * @param baraja  El tipo de baraja que se elije desde la vista: "ES" para la
	 *                baraja española o "FR" para la baraja francesa.
	 * @param idCarta El identificador único (ID) de la carta que se desea obtener.
	 * 
	 * @return carta Devuelve un objeto que contiene toda la información de la
	 *         carta.
	 * @return null Si ocurre un error o si no se encuentra la carta con el ID
	 *         indicado.
	 */
	public Document obtenerCartaPorID(String baraja, String idCarta) {
		try {
			MongoCollection<Document> coleccion;
			String nombreBaraja;
			if (baraja.equals("ES")) {
				nombreBaraja = "cards_es";
			} else {
				nombreBaraja = "cards_fr";
			}

			coleccion = dataBase.getCollection(nombreBaraja);

			Document carta = coleccion.find(eq("_id", new ObjectId(idCarta))).first();
			return carta;
		} catch (Exception e) {
			System.out.println("Error al obtener la carta por el id.");
			return null;
		}
	}

	/**
	 * Decodifica una imagen codificada en Base64 sacada de la carta.
	 * 
	 * @param imageBase64 Imagen codificada en formato Base64.
	 * @return Imagen decodificada como BufferedImage.
	 */
	public ImageIcon obtenerImagen(Document carta) {
		try {
			String base64 = carta.getString("base64");
			byte[] btDataFile = Base64.decodeBase64(base64);

			BufferedImage imagen = ImageIO.read(new ByteArrayInputStream(btDataFile));
			if (imagen != null) {
				Image scaledImagen = imagen.getScaledInstance(290, 437, java.awt.Image.SCALE_SMOOTH);
				return new ImageIcon(scaledImagen);
			} else {
				System.err.println("La imagen no se pudo decodificar.");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Codifica una imagen ubicada en una ruta específica en una cadena Base64.
	 * 
	 * Este método lee el contenido de un archivo de imagen desde el sistema de
	 * archivos, lo convierte a un arreglo de bytes y lo codifica en formato Base64
	 * para que pueda almacenarse en la base de datos.
	 * 
	 * @param rutaImagenes La ruta absoluta del archivo de imagen a codificar.
	 * 
	 * @return encodedStrng Una cadena que representa la imagen codificada en
	 *         Base64.
	 * @return null Si ocurre un error durante la lectura o codificación.
	 */
	public static String codificarImagenesBase64(String rutaImagenes) {
		try {
			byte[] fileContent = Files.readAllBytes(Paths.get(rutaImagenes));
			String encodedStrng = Base64.encodeBase64String(fileContent);
			return encodedStrng;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Carga las dos barajas en la base de datos.
	 * 
	 * Este método inicia la conexión con la base de datos, elimina las cartas de
	 * las colecciones "cards_es" y "cards_fr" si ya existiesen para evitar
	 * duplicidades y al terminar vuelve a cerrar la conexión.
	 * 
	 * @param rutaEspañola La ruta del directorio que contiene las imágenes de la
	 *                     baraja española.
	 * @param rutaFrancesa La ruta del directorio que contiene las imágenes de la
	 *                     baraja francesa.
	 */
	public static void cargarBaraja(String rutaEspañola, String rutaFrancesa) {

		try {
			System.out.println("DEPURACION >>> Iniciando carga de las barajas...");
			if (dataBase == null) {
				conectarConDataBase();
			}

			eliminarCartasSiExisten("cards_es");
			eliminarCartasSiExisten("cards_fr");

			System.out.println("Procesando baraja española...");
			procesarBaraja(rutaEspañola, "cards_es");

			System.out.println("Procesando baraja francesa...");
			procesarBaraja(rutaFrancesa, "cards_fr");

			cerrarConexion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Elimina todas las cartas de una colección específica en la base de datos.
	 * 
	 * Este método verifica si existen documentos en la colección especificada. Si
	 * existen, los elimina. Si no, informa que la colección está vacía.
	 * 
	 * @param nombreColeccion El nombre de la colección de la base de datos desde la
	 *                        cual se eliminarán las cartas.
	 */
	public static void eliminarCartasSiExisten(String nombreColeccion) {
		MongoCollection<Document> coleccion = dataBase.getCollection(nombreColeccion);

		long comprobarColec = coleccion.countDocuments();

		if (comprobarColec > 0) {
			coleccion.deleteMany(new Document()); // Como todos los elementos son documentos los eliminará.
			System.out.println(
					"DEPURACION >>> Se eliminaron " + comprobarColec + " cartas de la colección: " + nombreColeccion);
		} else {
			System.out.println("DEPURACION >>> No hay cartas en la colección: " + nombreColeccion);
		}
	}

	/**
	 * Procesa una baraja de cartas leyendo las imágenes de una ruta específica,
	 * codificándolas en formato Base64 y almacenándolas en la colección de la base
	 * de datos.
	 * 
	 * @param ruta   Ruta del directorio donde se encuentran las imágenes de las
	 *               cartas.
	 * @param baraja Nombre de la baraja que se procesará "cards_es" para baraja
	 *               española y "cards_fr" para baraja francesa.
	 */
	public static void procesarBaraja(String ruta, String baraja) {
		try {

			System.out.println("Procesando la baraja: " + baraja);

			MongoCollection<Document> coleccion = null;
			if (baraja.equals("cards_es")) {
				coleccion = dataBase.getCollection("cards_es");
			} else if (baraja.equals("cards_fr")) {
				coleccion = dataBase.getCollection("cards_fr");
			}

			List<Document> cartas = new ArrayList<>();

			File carpeta = new File(ruta);
			File[] archivos = carpeta.listFiles();

			if (archivos != null) {
				for (File carta : archivos) {
					System.out.println("Procesando carta: " + carta.getName());
					String imagenBase64 = codificarImagenesBase64(carta.getAbsolutePath());

					String cartaSinFormato = carta.getName().replace(".jpg", "").replace(".png", "");

					String[] partes = cartaSinFormato.split("_");

					String suit = partes[0];
					int number = Integer.parseInt(partes[1]);

					int points = 0;

					if (baraja.equals("cards_es")) {
						if (number >= 1 && number <= 7) {
							points = number;
						} else if (number == 10 || number == 11 || number == 12) {
							points = 10;
						}
					}

					if (baraja.equals("cards_fr")) {
						if (number >= 2 && number <= 10) {
							points = number;
						} else if (number == 1) {
							points = number;
						} else if (number == 11 || number == 12 || number == 13) {
							points = 10;
						}
					}

					Document cartaDoc = new Document();
					cartaDoc.append("suit", suit).append("points", points).append("base64", imagenBase64);

					cartas.add(cartaDoc);
					System.out.println("DEPURACION >>> Carta cargada: " + carta.getName());
				}
			}
			if (!cartas.isEmpty()) {
				System.out.println("Insertando " + cartas.size() + " cartas en la base de datos...");
				coleccion.insertMany(cartas);
				System.out.println("Todas las cartas han sido cargadas en la base de datos.");
			}

		} catch (Exception e) {
			System.err.println("Error al procesar la baraja: " + e.getMessage());
		}
	}

	/**
	 * Guarda el resultado de una partida en la base de datos en la colección
	 * "scores". Este método guarda la información del usuario, tipo de baraja
	 * "suit", puntos obtenidos y el timestamp de la partida en la colección
	 * "scores" de Mla base de datos "casino".
	 * 
	 * @param user      El nombre del usuario que ha jugado la partida.
	 * @param suit      El tipo de baraja que se utiliza ("ES" o "FR").
	 * @param points    La puntuación obtenida en la partida.
	 * @param timeStamp El timestamp que representa la fecha y hora de la partida.
	 * 
	 * @return true Si se guarda el resultado correctamente.
	 * @return false Si ocurre un error.
	 */
	public boolean guardarResultadoEnDB(String user, String suit, int points, String timeStamp) {
		try {
			if (dataBase == null) {
				conectarConDataBase();
			}

			MongoCollection<Document> coleccion = dataBase.getCollection("scores");

			Document resultado = new Document();
			resultado.append("user", user).append("suit", suit).append("points", points).append("timestamp", timeStamp);

			coleccion.insertOne(resultado);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Obtiene la lista de resultados de partidas almacenados en la base de datos,
	 * ordenados de mayor a menor puntuación.
	 *
	 * @return hallOfFame Una lista de documentos que serán los resultados de las
	 *         partidas. Cada documento tiene los campos "user", "suit", "points" y
	 *         "timestamp". Si ocurre un error durante la consulta, se devuelve una
	 *         lista vacía.
	 */
	public List<Document> obtenerHallOfFame() {
		List<Document> hallOfFame = new ArrayList<>();

		try {
			if (dataBase == null) {
				conectarConDataBase();
			}

			MongoCollection<Document> coleccion = dataBase.getCollection("scores");

			hallOfFame = coleccion.find().sort(Sorts.descending("points")).into(new ArrayList<>());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hallOfFame;
	}
}
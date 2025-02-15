package es.florida.ae03.modelo;

import static com.mongodb.client.model.Filters.eq;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

/**
 * Clase Modelo
 */
public class Modelo {

	private String strFicheroJSON_local = "config_local.json";
	private String strFicheroJSON_remote = "config_remote.json";
	private File rutasCartasEs = new File("./img/cards_es");
	private File rutasCartasFr = new File("./img/cards_fr");
	private boolean usuarioAutorizado = false;
	private MongoClient mongoClient;
	private MongoCollection<Document> collectionScores, collectionUsers, collectionCardsFr, collectionCardsEs,
			collectionChosen;
	private ArrayList<Carta> baraja = new ArrayList<Carta>();

	/**
	 * Método que realiza la conexión a la base de datos a partir de la información
	 * del fichero JSON de configuración.
	 * 
	 * @return Devuelve un booleano a true indicando si la conexión se ha realizado
	 *         correctamente o false en caso contrario.
	 */
	public boolean conexion(String servidor) {

		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.

		boolean resultado = false;
		File ficheroJSON = new File(strFicheroJSON_local);
		if (servidor.equals("remote")) {
			ficheroJSON = new File(strFicheroJSON_remote);
		}
		try {
			FileReader fr = new FileReader(ficheroJSON);
			BufferedReader br = new BufferedReader(fr);
			String strJSON = "";
			String linea = "";
			while ((linea = br.readLine()) != null)
				strJSON = strJSON + linea;
			br.close();
			JSONObject obj = new JSONObject(strJSON);
			String ip = obj.getString("ip");
			int port = obj.getInt("port");
			String strDatabase = obj.getString("database");
			String strCollectionScores = obj.getString("collection_scores");
			String strCollectionUsers = obj.getString("collection_users");
			String strCollectionCardsFr = obj.getString("collection_cards_fr");
			String strCollectionCardsEs = obj.getString("collection_cards_es");
			MongoClientURI uri;
			if (servidor.equals("local")) {
				uri = new MongoClientURI("mongodb://" + ip + ":" + port);
			} else {
				uri = new MongoClientURI(ip);
			}
			mongoClient = new MongoClient(uri);
			MongoDatabase database = mongoClient.getDatabase(strDatabase);
			collectionScores = database.getCollection(strCollectionScores);
			collectionUsers = database.getCollection(strCollectionUsers);
			collectionCardsFr = database.getCollection(strCollectionCardsFr);
			collectionCardsEs = database.getCollection(strCollectionCardsEs);
			resultado = true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return resultado;
	}

	public boolean registro(String usuario, char[] charPass) {
		boolean respuesta = false;
		try {
			String strPass = new String(charPass);
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-256");
			byte[] result = md.digest(strPass.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				sb.append(String.format("%02x", b));
			}
			String strPassSHA256 = sb.toString();
			Bson filtro = Filters.and(Filters.eq("user", usuario));
			MongoCursor<Document> cursor = collectionUsers.find(filtro).iterator();
			if (cursor.hasNext()) {
				JOptionPane.showMessageDialog(null, "User already exists", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else {
				Document doc = new Document();
				doc.append("user", usuario);
				doc.append("pass", strPassSHA256);
				collectionUsers.insertOne(doc);
				JOptionPane.showMessageDialog(null, "User registered successfully", "New user",
						JOptionPane.INFORMATION_MESSAGE);
				respuesta = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return respuesta;
	}

	public boolean login(String usuario, char[] charPass) {
		usuarioAutorizado = false;
		try {
			String strPass = new String(charPass);
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-256");
			byte[] result = md.digest(strPass.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				sb.append(String.format("%02x", b));
			}
			String strPassSHA256 = sb.toString();
			Bson filtro = Filters.and(Filters.eq("user", usuario), Filters.eq("pass", strPassSHA256));
			MongoCursor<Document> cursor = collectionUsers.find(filtro).iterator();
			if (cursor.hasNext()) {
				usuarioAutorizado = true;
				JOptionPane.showMessageDialog(null, "Login successful", "INFO", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Login error: incorrect user or password", "INFO",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return usuarioAutorizado;
	}

	public boolean guardarResultado(String user, String suit, int points, String timestamp) {
		boolean insertado = false;
		try {
			Document doc = new Document();
			doc.append("user", user);
			doc.append("suit", suit);
			doc.append("points", points);
			doc.append("timestamp", timestamp);
			collectionScores.insertOne(doc);
			insertado = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return insertado;
	}

	public ArrayList<String> obtenerResultados() {
		ArrayList<String> resultados = new ArrayList<String>();
		MongoCursor<Document> cursor = collectionScores.find().sort(new Document("points", -1)).iterator();
		while (cursor.hasNext()) {
			Document docActual = cursor.next();
			JSONObject obj = new JSONObject(docActual.toJson());
			resultados.add(obj.getString("user") + " " + obj.getInt("points") + " points (" + obj.getString("suit")
					+ ", " + obj.getString("timestamp") + ")");
		}
		return resultados;
	}

	public boolean cargarCartas() {
		boolean cargadas = false;
		try {
			// cards_es
			collectionCardsEs.drop();
			File[] arrayCardsEs = rutasCartasEs.listFiles();
			for (int i = 0; i < arrayCardsEs.length; i++) {
				String fileName = arrayCardsEs[i].getName();
				String suit = fileName.split("_")[0];
				int number = Integer.parseInt(fileName.substring(fileName.length() - 6, fileName.length() - 4));
				int points = number;
				if (number > 7) {
					points = 10;
				}
				byte[] fileContent = Files.readAllBytes(arrayCardsEs[i].toPath());
				String base64 = Base64.encodeBase64String(fileContent);
				Document doc = new Document();
				doc.append("suit", suit);
				doc.append("points", points);
				doc.append("base64", base64);
				collectionCardsEs.insertOne(doc);
			}
			// cards_fr
			collectionCardsFr.drop();
			File[] arrayCardsFr = rutasCartasFr.listFiles();
			for (int i = 0; i < arrayCardsFr.length; i++) {
				String fileName = arrayCardsFr[i].getName();
				String suit = fileName.split("_")[0];
				int number = Integer.parseInt(fileName.substring(fileName.length() - 6, fileName.length() - 4));
				int points = number;
				if (number > 10) {
					points = 10;
				}
				byte[] fileContent = Files.readAllBytes(arrayCardsFr[i].toPath());
				String base64 = Base64.encodeBase64String(fileContent);
				Document doc = new Document();
				doc.append("suit", suit);
				doc.append("points", points);
				doc.append("base64", base64);
				collectionCardsFr.insertOne(doc);
			}
			cargadas = true;
			JOptionPane.showMessageDialog(null, "Images have been 'mongoed' successfully", "INFO",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return cargadas;
	}

	public void barajar(String suit) {
		baraja = new ArrayList<Carta>();
		if (suit.equals("ES")) {
			collectionChosen = collectionCardsEs;
		} else {
			collectionChosen = collectionCardsFr;
		}
		MongoCursor<Document> cursor = collectionChosen.find().iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			Carta c = new Carta(obj.getString("suit"), obj.getInt("points"), obj.getString("base64"));
			baraja.add(c);
		}
		Collections.shuffle(baraja);
	}
	
	public ArrayList<Carta> getBaraja() {
		return baraja;
	}

}

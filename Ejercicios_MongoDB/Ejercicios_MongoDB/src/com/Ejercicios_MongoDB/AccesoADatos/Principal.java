package com.Ejercicios_MongoDB.AccesoADatos;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class Principal {

	static String arrayTitulos[] = { "Bohemian Rhapsody", "Billie Jean", "Shape of You", "Blinding Lights", "Rolling in the Deep", "Smells Like Teen Spirit", "Uptown Funk", "Someone Like You" };
	static String arrayArtistas[] = { "Queen", "Michael Jackson", "Ed Sheeran", "The Weeknd", "Adele", "Nirvana", "Mark Ronson ft. Bruno Mars", "Adele" };
	static int arrayAños[] = { 1975, 1982, 2017, 2019, 2010, 1991, 2014, 2011 };
	static String arrayFormatos[] = {"MP3", "MP3", "WAV", "OGG", "RAW", "MP3", "MP3", "WAV"};

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("localhost", 27017);	//Se crea el cliente.
		MongoDatabase dataBase = mongoClient.getDatabase("Ejercicios_MongoDB_Canciones");	//Se crea la conexión a la base de datos.
		MongoCollection<Document> coleccion = dataBase.getCollection("canciones");	//Se crea la colección(tabla en SQL) de las canciones.
		
		//Insertar un documento en la colección.
		System.out.println("Insertar un documento...\n");
		Document doc = new Document();
		doc.append("titulo", arrayTitulos[0]);
		doc.append("artista", arrayArtistas[0]);
		doc.append("año", arrayAños[0]);
		doc.append("formato", arrayFormatos[0]);
		coleccion.insertOne(doc);	//Insertará una única canción a la colección, solo introducirá la primera.
		
		//Insertar varios documentos a la colección
		System.out.println("Insertar varios documentos a la colección...");
		ArrayList<Document> listaDocs = new ArrayList<Document>();
		for (int i = 0; i < arrayTitulos.length; i++) {
			doc = new Document();
			doc.append("titulo", arrayTitulos[i]);
			doc.append("artista", arrayArtistas[i]);
			doc.append("año", arrayAños[i]);
			doc.append("formato", arrayFormatos[i]);
			listaDocs.add(doc);
		}
		coleccion.insertMany(listaDocs);
		
		//Obtener tamaño de la colección
		System.out.println("\n=======================");
		System.out.println("Tamaño de la colección: " + coleccion.countDocuments());	//Conteo exacto de los documentos.(coleccion.estimatedDocumentCount(); conteo estimado, para cuando hay muchos archivos).
		System.out.println("=======================\n");
		
		
		//Obtener todos los elementos de la coleción
		System.out.println("\n=======================");
		System.out.println("Elementos de la colección: ");
		System.out.println("=======================\n");
		MongoCursor<Document> cursor = coleccion.find().iterator();	//find() es el filtro, si no se añade nada buscará todos los elementos.
		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
		System.out.println("=======================\n");
		 
		//Queries específicos.
		System.out.println("\nDocumentos con año igual a 2017: ");
		Bson query = eq("año", 2017);
		cursor = coleccion.find(query).iterator();
		while (cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
		
		System.out.println("\nDocumentos con artista igual a Queen: ");
		query = eq("artista", "Queen");
		cursor = coleccion.find(query).iterator();
		while (cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
		
		System.out.println("\nDocumentos con año posterior al 2000: ");
		query = gte("año", 2000);
		cursor = coleccion.find(query).iterator();
		while (cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}

		System.out.println("\nTítulos con año posterior al 2000: ");
		query = gte("año", 2000);
		cursor = coleccion.find(query).iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			System.out.println(obj.getString("titulo"));
		}
		
		
		//Actualizar un elemento.
		System.out.println("\n=======================");
		System.out.println("Elementos de la colección: ");
		System.out.println("=======================\n");
		cursor = coleccion.find().iterator();
		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
		
		System.out.println("\nActualizar solo 1 documento cambiando WAV a OGG");
		coleccion.updateOne(eq("formato", "WAV"), new Document("$set", new Document("formato", "OGG")));	//Pasando el $set es necesario para actualizar el documento.
		
		
		//Actualizar varios elementos.
		System.out.println("\n=======================");
		System.out.println("Elementos de la colección: ");
		System.out.println("=======================\n");
		cursor = coleccion.find().iterator();
		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
		
		System.out.println("\nActualizar todos los documentos cambiando MP3 a RAW");
		coleccion.updateMany(eq("formato", "MP3"), new Document("$set", new Document("formato", "RAW")));
		
		//Borrar elementos.
		
		System.out.println("\n=======================");
		System.out.println("Elementos de la colección: ");
		System.out.println("=======================\n");
		cursor = coleccion.find().iterator();
		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
		System.out.println("Tamaño de la colección: " + coleccion.countDocuments());
		System.out.println("Borrar elemento con formato OGG");
		coleccion.deleteOne(eq("formato", "OGG"));
		System.out.println("Tamaño de la colección: " + coleccion.countDocuments());
		System.out.println("Borrar todos los elementos con formato MP3");
		coleccion.deleteMany(eq("formato", "MP3"));
		System.out.println("Tamaño de la colección: " + coleccion.countDocuments());
		
//		System.out.println("Borrar toda la colección");
//		coleccion.drop();
//		System.out.println("Tamaño de la colección: " + coleccion.countDocuments());
		
		mongoClient.close();
	}
}

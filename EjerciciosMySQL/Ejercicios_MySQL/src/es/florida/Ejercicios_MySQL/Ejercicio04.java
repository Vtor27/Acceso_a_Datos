package es.florida.Ejercicios_MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ejercicio04 {

public static void recuperarDB() throws ClassNotFoundException {
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	try {
		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas","root",""); //Se crea la conexión a la BD.
		System.out.println("Conexión realizada correctamente.");	
		
		Statement stmt = conexion.createStatement(); //Se crea una variable Statement para poder hacer peticiones a la BD.
		
		ResultSet resultSelect = stmt.executeQuery("SELECT * FROM peliculas");	//Se realiza la petición pero esta no devuelve los valores visualmente bien por si sola.
			
		System.out.format("%3s%30s%20s%15s%20s\n", "id", "Título", "Director", "Puntuación", "Género");	//Con esto se "Formatea" la salida para que muentre una tabla
		System.out.format("%3s%30s%20s%15s%20s\n", "==", "======", "========", "==========", "======");
		
		while(resultSelect.next()) { //Mientras que en el resultado de result.Select tenga columnas que leer mostrará el contenido de la tabla formateado como queremos.
			System.out.format("%3s%35s%25s%15s%20s\n", resultSelect.getString(1), resultSelect.getString(2), resultSelect.getString(3), resultSelect.getString(4), resultSelect.getString(5));
		}
		resultSelect.close();	//Se cierran las conexiones.
		conexion.close();
	}catch(SQLException e) {
		System.err.println("ERROR en la conexión.");
		e.printStackTrace();
	}
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println("Para comprobar que efectivamente se han recuperado todos los datos, implementar el\r\n"
				+ "código para recorrer el resultado de la consulta y mostrar por pantalla las entradas de la\r\n"
				+ "tabla. Puedes utilizar concatenación de strings para que el formato de salida sea más\r\n"
				+ "comprensible.");
		
		recuperarDB();

	}

}

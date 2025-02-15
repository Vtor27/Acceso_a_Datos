package es.florida.Ejercicios_MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Ejercicio02 {

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println("Realiza un programa que importe la librería necesaria para poder realizar una conexión a\r\n"
				+ "una base de datos MySQL, realice la conexión a la base de datos anterior y muestre un\r\n"
				+ "mensaje si se ha hecho o no con éxito.");
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas","root",""); 
			System.out.println("Conexión realizada correctamente.");
			conexion.close();
		}catch(SQLException e) {
			System.err.println("ERROR en la conexión.");
			e.printStackTrace();
		}
	}

}

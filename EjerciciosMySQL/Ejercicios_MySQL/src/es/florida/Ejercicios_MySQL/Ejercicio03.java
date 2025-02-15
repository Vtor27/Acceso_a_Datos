package es.florida.Ejercicios_MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ejercicio03 {

	public static void main(String[] args) throws ClassNotFoundException  {
		System.out.println("Amplía el programa anterior para que implemente el código necesario para interrogar a la\r\n"
				+ "base de datos con una sentencia SQL. Implementa una sentencia que permita recuperar\r\n"
				+ "todo el contenido de una tabla de la base de datos.");

		Class.forName("com.mysql.cj.jdbc.Driver");
		
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas","root",""); 
			System.out.println("Conexión realizada correctamente.");
			
			Statement stmt = conexion.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM peliculas");
			result.close();
			conexion.close();
		}catch(SQLException e) {
			System.err.println("ERROR en la conexión.");
			e.printStackTrace();
		}
		
	}

}

package es.florida.Ejercicios_MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Ejercicio07 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		System.out.println("Implementa un método que permita, dado un id de la tabla, borrar la entrada.");
		
		Ejercicio04.recuperarDB();

		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas","root",""); 		
		
		System.out.println("Introduce el id de la pelicula para borarla.");
		Scanner scanner = new Scanner(System.in);
		String respuesta = scanner.nextLine();
		
		PreparedStatement psDelete = conexion.prepareStatement("DELETE FROM peliculas WHERE id = " + respuesta);
	
		int resulDelete = 0;
		resulDelete = psDelete.executeUpdate();
		
		if(resulDelete > 0) {

			System.out.println("Pelicula borrada en la DB");
		}else {
			System.err.println("ERROR en la actualiacion de los datos de la pelicula con id: " + respuesta);
		}
		psDelete.close();
		scanner.close();
		conexion.close();
	}
}

package es.florida.Ejercicios_MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Ejercicio06 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		System.out.println("Implementa un método que permita, dado un id de la tabla, actualizar alguno de sus campos\r\n"
				+ "(el nuevo valor del campo se introducirá por teclado).");
		
		Ejercicio04.recuperarDB();
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas","root",""); 		
		
		System.out.println("Introduce el id de la pelicula que queires acturlizar los datos.");
		Scanner scanner = new Scanner(System.in);
		String respuesta = scanner.nextLine();
		
		System.out.println("Título: ");
		String titulo = scanner.nextLine();
		
		System.out.println("Director: ");
		String director = scanner.nextLine();
		
		System.out.println("Puntuación: ");
		String puntuacion = scanner.nextLine();
		
		System.out.println("Género: ");
		String genero = scanner.nextLine();
			//El prepared statement se podria hacer igual que en el ejercicio05, esta es otra forma de hacerlo.(Mejor la del 05)
		PreparedStatement psUpdate = conexion.prepareStatement("UPDATE peliculas SET titulo = '" + titulo + "', director = '" + director + "',"
				+ " puntuacion = '" + puntuacion + "', genero = '" + genero + "' WHERE id = " + respuesta);
	
		int resultadoActualizar = 0;
		resultadoActualizar = psUpdate.executeUpdate();
		
		if(resultadoActualizar > 0) {
			Pelicula peliculaActualizada = new Pelicula(titulo, director, puntuacion, genero);
			System.out.println("Pelicula actualizada en la DB: " + peliculaActualizada.toString());
		}else {
			System.err.println("ERROR en la actualiacion de los datos de la pelicula con id: " + respuesta);
		}
		scanner.close();
		conexion.close();
	}

}

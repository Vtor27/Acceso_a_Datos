package es.florida.Ejercicios_MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Ejercicio08 {

	public static void deletePelicula() throws ClassNotFoundException, SQLException {
		
		Ejercicio04.recuperarDB();
		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas", "root", "");

		System.out.println("Introduce el id de la pelicula para borarla.");
		Scanner scanner = new Scanner(System.in);
		String respuesta = scanner.nextLine();

		PreparedStatement psDelete = conexion.prepareStatement("DELETE FROM peliculas WHERE id = " + respuesta);
		int resulDelete = 0;

		System.out.println("Estas seguro de que deseas eliminar esta pelicula de la DB? (s/n)");
		String confirmacionDelete = scanner.nextLine();

		if (confirmacionDelete.equals("s")) {
			resulDelete = psDelete.executeUpdate();
		}

		if (resulDelete > 0) {

			System.out.println("Pelicula borrada en la DB");
		} else {
			System.err.println("ERROR en la actualiacion de los datos de la pelicula con id: " + respuesta);
		}
		psDelete.close();
		scanner.close();
		conexion.close();
	}

	public static void updatePelicula() throws SQLException, ClassNotFoundException {
		
		Ejercicio04.recuperarDB();
		
		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas", "root", "");

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

		PreparedStatement psUpdate = conexion
				.prepareStatement("UPDATE peliculas SET titulo = '" + titulo + "', director = '" + director + "',"
						+ " puntuacion = '" + puntuacion + "', genero = '" + genero + "' WHERE id = " + respuesta);

		int resultadoActualizar = 0;

		System.out.println("Estas seguro de que deseas actualizar esta pelicula de la DB? (s/n)");
		String confirmacionUpdate = scanner.nextLine();

		if (confirmacionUpdate.equals("s")) {
			resultadoActualizar = psUpdate.executeUpdate();

			if (resultadoActualizar > 0) {
				Pelicula peliculaActualizada = new Pelicula(titulo, director, puntuacion, genero);
				System.out.println("Pelicula actualizada en la DB: " + peliculaActualizada.toString());
			} else {
				System.err.println("ERROR en la actualiacion de los datos de la pelicula con id: " + respuesta);
			}
			scanner.close();
			conexion.close();
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		System.out.println(
				"Modifica los dos ejercicios anteriores para que pidan una confirmación del usuario antes de\r\n"
						+ "pasar a realizar la acción.");

		System.out.println(
				"Selecciona que quieres hacer: \n 1. Eliminar pelicuala de la DB.(del) \n 2. Modificar los datos de alguna pelicula.(mod)");
		Scanner scanner = new Scanner(System.in);
		String eleccion = scanner.nextLine();

		switch (eleccion) {
		case "1":
			deletePelicula();
			break;

		case "2":
			updatePelicula();
			break;

		default:
			System.out.println("Método inexistente o no disponible.");
			break;
		}
		scanner.close();

	}
}

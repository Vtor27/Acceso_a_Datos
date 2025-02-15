package es.florida.Ejercicios_MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Ejercicio05 {

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println("Implementa un método que permita leer por teclado los atributos para una nueva entrada\r\n"
				+ "en la tabla (puedes utilizar lo que ya has desarrollado en el bloque anterior), cree la consulta\r\n"
				+ "adecuada de inserción e introduzca los nuevos datos en la base de datos. Comprueba\r\n"
				+ "después que la inserción se ha realizado correctamente.");
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas","root",""); 
			System.out.println("Conexión realizada correctamente.");	
			
			PreparedStatement stmtInsertar = conexion.prepareStatement("INSERT INTO peliculas(titulo, director, puntuacion, genero) VALUES (?,?,?,?)"); 

			System.out.format("Quieres introducir una pelicula nueva en la BD? (s/n)");	
			Scanner scanner = new Scanner(System.in);
			String respuesta = scanner.nextLine();
			
			while(respuesta.equals("s")) {
				System.out.println("Título: "); 
				String titulo = scanner.nextLine();
				
				System.out.println("Director: "); 
				String director = scanner.nextLine();
				
				System.out.println("Puntuación: "); 
				String puntuacion = scanner.nextLine();
				
				System.out.println("Género: "); 
				String genero = scanner.nextLine();
				
				stmtInsertar.setString(1, titulo);
				stmtInsertar.setString(2, director);
				stmtInsertar.setString(3, puntuacion);
				stmtInsertar.setString(4, genero);
				
				
				int resultadoInsertar = stmtInsertar.executeUpdate();
				Pelicula nuevaPelicula = new Pelicula(titulo, director, puntuacion, genero);
				
				if(resultadoInsertar > 0) {
					System.out.println("Pelicula guardada correctamente en la BD: " + nuevaPelicula.toString());
				}else {
					System.err.println("Error en la inserción.");
				}
				System.out.println("Desea añadir una nueva pelicula? (s/n)");
				respuesta = scanner.nextLine();
			}
			scanner.close();
			conexion.close();
		}catch(SQLException e) {
			System.err.println("ERROR en la conexión.");
			e.printStackTrace();
		}

	}

}

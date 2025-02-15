package es.florida.hibernate;

public class Pelicula {

	private int id;
	private String titulo;
	private String director;
	private double puntuacion;
	private String genero;
	
	public Pelicula() {
	}

	public Pelicula(int id, String titulo, String director, double puntuacion, String genero) {
		this.id = id;
		this.titulo = titulo;
		this.director = director;
		this.puntuacion = puntuacion;
		this.genero = genero;
	}
	
	//Es más comodo porque así la base de datos asigna automaticamente el id sin necesidad de manejarlos(Lo gestiona la DB).
	public Pelicula( String titulo, String director, double puntuacion, String genero) {
		this.titulo = titulo;
		this.director = director;
		this.puntuacion = puntuacion;
		this.genero = genero;
	}
	
	public int getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getDirector() {
		return director;
	}
	public double getPuntuacion() {
		return puntuacion;
	}
	public String getGenero() {
		return genero;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
	
	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}
	
	public void setGenero(String genero) {
		this.genero = genero;
	}
	
	public String toString() {
		return("Objeto pelicula -> Id: " + getId() + ", Titulo: " + getTitulo() + ", Director: " + getDirector() + ", Puntuacion: " + getPuntuacion() + ", Genero: " + getGenero()); 
	}
}
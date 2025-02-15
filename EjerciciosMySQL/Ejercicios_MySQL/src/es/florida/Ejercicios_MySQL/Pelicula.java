package es.florida.Ejercicios_MySQL;

public class Pelicula {
	String titulo, director, puntuacion, genero;
	
	Pelicula(){}
	
	Pelicula(String titulo, String director, String puntuacion, String genero){
		this.titulo = titulo;
		this.director = director;
		this.puntuacion = puntuacion;
		this.genero = genero;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDirector() {
		return director;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getPuntuacion() {
		return puntuacion;
	}
	
	public void setPuntuacion(String puntuacion) {
		this.puntuacion = puntuacion;
	}
	
	public String getGenero() {
		return genero;
	}
	
	public void setGenero(String genero) {
		this.genero = genero;
	}
	
	public String toString() {
		String infoCompleta = "Objeto pelicula -> Título: " + titulo + "Autor: " + "Director: " + director + "Puntuación: " + puntuacion + "Género: " + genero;
		return infoCompleta;
	}
	
	
}

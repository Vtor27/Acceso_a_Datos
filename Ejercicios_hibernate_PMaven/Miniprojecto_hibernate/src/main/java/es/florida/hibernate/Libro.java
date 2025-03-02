package es.florida.hibernate;

public class Libro {
	private int id;
	private String titulo;
	private String autor;
	private int año_nac_autor;
	private int año_publicacion;
	private String editorial;
	private int numero_paginas;
	
	public Libro() {}
	
	public Libro(int id, String titulo, String autor, int año_nac_autor, int año_publicacion, String editorial, int numero_paginas) {
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.año_nac_autor = año_nac_autor;
		this.año_publicacion = año_publicacion;
		this.editorial = editorial;
		this.numero_paginas = numero_paginas;
	}
	
	public Libro(String titulo, String autor, int año_nac_autor, int año_publicacion, String editorial, int numero_paginas) {
		this.titulo = titulo;
		this.autor = autor;
		this.año_nac_autor = año_nac_autor;
		this.año_publicacion = año_publicacion;
		this.editorial = editorial;
		this.numero_paginas = numero_paginas;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public String getAutor() {
		return autor;
	}
	
	public int getAño_nac_autor() {
		return año_nac_autor;
	}
	
	public int getAño_publicacion() {
		return año_publicacion;
	}
	
	public String getEditorial() {
		return editorial;
	}
	
	public int getNumero_paginas() {
		return numero_paginas;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	public void setAño_nac_autor(int año_nac_autor) {
		this.año_nac_autor = año_nac_autor;
	}
	
	public void setAño_publicacion(int año_publicacion) {
		this.año_publicacion = año_publicacion;
	}
	
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}
	
	public void setNumero_paginas(int numero_paginas) {
		this.numero_paginas = numero_paginas;
	}
	
	public String toString() {
		return("Id: " + getId() + " Título: " + getTitulo() + "\n");
	}
	
	public String detailsBooK() {
		return("Libro seleccionado ->\n  Id: " + getId() + "\n  Titulo: " + getTitulo() + "\n  Autor: " + getAutor() + "\n  Año de nacimiento del autor: " + getAño_nac_autor() + "\n  Año de publicacion: " + getAño_publicacion() + "\n  Editorial: " + getEditorial() + "\n  Número de páginas: " + getNumero_paginas()); 
	}
}

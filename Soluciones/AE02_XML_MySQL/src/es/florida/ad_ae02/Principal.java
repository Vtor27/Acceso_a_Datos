package es.florida.ad_ae02;

public class Principal {

	public static void main(String[] args) {

		Vista vista = new Vista();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(modelo,vista);

	}

}

package es.florida.acceso_a_datos;

/**
 * Clase principal que inicializa y ejecuta la aplicación del Blackjack. Crea
 * las vistas, el modelo y el controlador, estableciendo la conexión entre
 * ellos.
 */
public class Main {

	/**
	 * Método principal de la aplicación.
	 * 
	 * @param args Argumentos de la línea de comandos (no utilizados en esta
	 *             aplicación).
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Vista vista = new Vista();
		VistaLogin vistaLogin = new VistaLogin();
		VistaHallOfFame vistaHallOfFame = new VistaHallOfFame();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(modelo, vista, vistaLogin, vistaHallOfFame);
	}
}
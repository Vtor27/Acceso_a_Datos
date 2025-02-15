package es.florida.AE02_ADD_XML_MySQL;

/**
 * Clase principal que inicializa la aplicación.
 * Configura las vistas, el modelo y el controlador para lanzar la interfaz gráfica.
 */
public class Main {

	/**
     * Método principal de la aplicación.
     * Encargado de inicializar el modelo, las vistas y el controlador.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
	public static void main(String[] args) {
		Vista vista = new Vista();
		VistaLogin vistaLogin = new VistaLogin();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(modelo, vista, vistaLogin);
	}
}
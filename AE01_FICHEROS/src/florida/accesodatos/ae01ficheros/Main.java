package florida.accesodatos.ae01ficheros;

/**
 * Clase principal desde donde se inicializa toda la aplicación, instanciando la clase vista, modelo y
 * controlador, utilizando para esta ultima, la vista y el modelo como constructores.
 */
public class Main {

	public static void main(String[] args)  {
		Vista vista = new Vista();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(modelo, vista);
	}
}

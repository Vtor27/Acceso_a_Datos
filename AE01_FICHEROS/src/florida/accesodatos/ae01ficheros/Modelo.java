package florida.accesodatos.ae01ficheros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Clase utilizada para manejar la lógica de la aplicación y poder comunicarse
 * con la clase Controlador.
 * 
 */
public class Modelo {
	
	/**
	 * Método que servirá para formatear la fecha, la hora y el tamaño para
	 * utilizarse en el nombre de los ficheros.
	 * 
	 * @param archivo Es el objeto File del cual se calculara su tamaño y se
	 *                mostrará la fecha y hora.
	 * 
	 * @return Devuelve una cadena con el tamaño en kylobytes con la fecha y hora
	 *         formateada.
	 */
	public static String formatEspacioFecha(File archivo) {
		DateTimeFormatter dateFormatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter dateFormatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");

		String localDateString = LocalDate.now().format(dateFormatterDate);
		String localTimeString = LocalTime.now().format(dateFormatterTime);
		String dateTimeString = localDateString + " " + localTimeString;

		return " (" + (archivo.length() / 1024) + " KB - " + dateTimeString + ")";
	}

	/**
	 * Método que servirá para mostrar los subdirectorios y ficheros/archivos dentro
	 * del directorio que elijamos con una estructura de arbol.
	 * 
	 * @param directorio  Es la dirección que se le pasa del directorio que se
	 *                    quiere explorar.
	 * @param indentacion Es una cadena vacia la cual se actualiza para poder ir
	 *                    añadiendo los espacios y asi tenga una apariencia de
	 *                    estructura en arbol.
	 * 
	 * @return estructuraDirectorio Devuelve la estructura del directorio
	 *         seleccionado debidamente indentado.
	 */
	public String mostrarDirectorio(File directorio, String indentacion) {
		String estructuraDirectorio = "";

		File[] archivos = directorio.listFiles();

		if (archivos != null) {
			for (File archivo : archivos) {
				if (archivo.isDirectory()) {

					estructuraDirectorio += indentacion + "|---- " + archivo.getName() + "\n";
					estructuraDirectorio += mostrarDirectorio(archivo, indentacion + "|    ");
				}
			}
			for (File archivo : archivos) {
				if (!archivo.isDirectory()) {
					estructuraDirectorio += indentacion + "|---- " + archivo.getName() + formatEspacioFecha(archivo)
							+ "\n";
				}
			}
		}
		return estructuraDirectorio;
	}

	/**
	 * Método que servirá para que la cadena que le pasemos para buscar las
	 * coincidencias en los ficheros ignore las mayusculas o los acentos.
	 * 
	 * @param cadena            Es la cadena que se quiere comprobar las
	 *                          coincidencias.
	 * @param ignorarAcentos    Elimina todas los acentos de la cadena si se indica.
	 * @param ignorarMayusculas Convierte toda la cadena a minúsculas si se indica.
	 * 
	 * @return cadena Devuelve la cadena ignorando tildes, ignorando mayusculas o
	 *         ignorando ambas.
	 */
	public String preprocesarCadena(String cadena, boolean ignorarAcentos, boolean ignorarMayusculas) {
		if (ignorarMayusculas) {
			cadena = cadena.toLowerCase();
		}
		if (ignorarAcentos) {
			cadena = eliminarAcentos(cadena);
		}
		return cadena;
	}

	/**
	 * Método que se utiliza para elimina los acentos.
	 * 
	 * @param cadena Es la cadena que se quieren eliminar los acentos.
	 * 
	 * @return cadena Devuelve la cadena sin acentos.
	 */
	public String eliminarAcentos(String cadena) {
		String normalizer = Normalizer.normalize(cadena, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(normalizer).replaceAll("");
	}

	/**
	 * Método que se utiliza para contar el número de coincidencias en un fichero.
	 * 
	 * @param linea  Es la línea que se está leyendo.
	 * @param cadena Cadena que se compara con las líneas que se van pasando al
	 *               método.
	 * 
	 * @return coincidencias Devuelve el número de coincidencias.
	 */
	public static int contarCoincidencias(String linea, String cadena) {
		if (cadena.isEmpty()) {
			return 0;
		}

		int coincidencias = 0;

		int i = linea.indexOf(cadena); // Indica la posición donde estará la cadena que coincida para asi poder seguir
										// buscando coincidencias desde donde se ha quedado.

		while (i != -1) {
			coincidencias++;

			i = linea.indexOf(cadena, i + cadena.length()); // Despues de sumar continua desde donde se había quedado.
		}
		return coincidencias;
	}

	/**
	 * Método que servirá para mostrar los subdirectorios y ficheros en forma de
	 * arbol y revise los ficheros buscando coincidencias con la cadena que se le
	 * pasa.
	 * 
	 * @param directorio        Es la dirección que se le pasa del directorio que se
	 *                          quiere explorar.
	 * @param indentacion       Es una cadena vacia la cual se actualiza para poder
	 *                          ir añadiendo los espacios y asi tenga una apariencia
	 *                          de estructura en arbol.
	 * @param cadena            Es la cadena que se quiere comprobar las
	 *                          coincidencias.
	 * @param ignorarAcentos    Elimina todas los acentos de la cadena si se indica.
	 * @param ignorarMayusculas Convierte toda la cadena a minúsculas si se indica.
	 * 
	 * @return resultado Devuelve la estructura contando las coincidencias al lado
	 *         del nombre del fichero.
	 * 
	 * @throws IOException Se utiliza para manejas los posibles errores que puedan
	 *                     aparecer.
	 */
	public String buscarCoincidencias(File directorio, String indentacion, String cadena, boolean ignorarAcentos,
			boolean ignorarMayusculas) throws IOException {
		String estructuraDirectorio = "";

		File[] ficheros = directorio.listFiles();

		if (ficheros != null) {
			for (File fichero : ficheros) {
				if (fichero.isDirectory()) {
					estructuraDirectorio += indentacion + "|---- " + fichero.getName() + '\n';
					estructuraDirectorio += buscarCoincidencias(fichero, indentacion + "|    ", cadena, ignorarAcentos,
							ignorarMayusculas);
				}
			}

			for (File fichero : ficheros) {
				if (!fichero.isDirectory()) {
					int coincidencias = buscarEnFichero(fichero, cadena, ignorarAcentos, ignorarMayusculas);
					estructuraDirectorio += indentacion + "|-- " + fichero.getName() + " (" + coincidencias
							+ " coincidencias) \n";
				}

			}
		}
		return estructuraDirectorio;

	}

	/**
	 * Busca una cadena que se le pasa como parámetro pudiendo ignorar mayúsculas y
	 * acentos si se le indica y lee el archivo buscando coincidencias en el con la
	 * cadena que se le proporciona.
	 *
	 * @param fichero           Es el archivo donde se realiza la busqueda de
	 *                          coincidencias.
	 * @param cadena            Es la cadena que se quiere comprobar las
	 *                          coincidencias.
	 * @param ignorarAcentos    Si se indica se ignorarán los acentos durante la
	 *                          búsqueda.
	 * @param ignorarMayusculas Si se indica se ignorarán las mayusculas durante la
	 *                          búsqueda.
	 * 
	 * @return Devuelve el número de coincidencias.
	 * 
	 * @throws IOException Se utiliza para manejas los posibles errores que puedan
	 *                     aparecer.
	 */
	public int buscarEnFichero(File fichero, String cadena, boolean ignorarAcentos, boolean ignorarMayusculas)
			throws IOException {
		int coincidencias = 0;

		// Preprocesar la cadena de búsqueda según las opciones
		String cadenaBuscar = preprocesarCadena(cadena, ignorarAcentos, ignorarMayusculas);

		try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String lineaProcesada = preprocesarCadena(linea, ignorarAcentos, ignorarMayusculas);
				coincidencias += contarCoincidencias(lineaProcesada, cadenaBuscar);
			}
		} catch (IOException e) {
			System.out.println("Error al procesar el archivo: " + fichero.getName());
			e.printStackTrace();
		}

		return coincidencias;
	}

	/**
	 * Reemplaza una cadena dentro de un archivo y crea un nuevo archivo con el
	 * prefijo `MOD_`.
	 * 
	 * @param fichero          El archivo donde se realiza el reemplazo.
	 * @param cadenaABuscar    La cadena que se busca en el archivo.
	 * @param cadenaReemplazar La cadena que reemplaza a la buscada.
	 * @return Devuelve el nombre del archivo modificado, si no hay ningun archivo
	 *         para modificar devuelve null.
	 * @throws IOException Se utiliza para manejas los posibles errores que puedan
	 *                     aparecer.
	 */
	public String reemplazarCadena(File fichero, String cadenaABuscar, String cadenaReemplazar) throws IOException {

		if (fichero == null || !fichero.exists() || fichero.isDirectory() || !fichero.canWrite()) {
			return "El archivo especificado no es válido o no se puede escribir.";
		}

		int coincidencias = 0;
		ArrayList<String> contenidoModificado = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				if (linea.contains(cadenaABuscar)) {
					coincidencias += contarCoincidencias(linea, cadenaABuscar);
					linea = linea.replace(cadenaABuscar, cadenaReemplazar);
				}
				contenidoModificado.add(linea);
			}
		} catch (IOException e) {
			return ("Error al procesar el archivo: " + fichero.getName() + ": " + e.getMessage());
		}

		if (coincidencias > 0) {
			File ficheroModificado = new File(fichero.getParent(), "MOD_" + fichero.getName());

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroModificado))) {
				for (String lineaModificada : contenidoModificado) {
					bw.write(lineaModificada);
					bw.newLine();
				}
			} catch (IOException e) {
				return ("Error al crear el archivo modificado: " + ficheroModificado.getName() + ": " + e.getMessage());
			}
			return "Archivo modificado creado: " + ficheroModificado.getName();
		} else {
			return null;
		}
	}

	/**
	 * Método que buscará todos los archivos dentro de un directorio (contando
	 * subdirectorios) y por cada archivo ".txt" que encuentre llamará al método
	 * reemplazarCadena.
	 * 
	 * @param directorio       Es la dirección que se le pasa del directorio que se
	 *                         quiere explorar.
	 * @param cadenaABuscar    Es la cadena que se quiere comprobar las
	 *                         coincidencias.
	 * @param cadenaReemplazar La cadena que reemplazará a la cadena buscada.
	 * 
	 * @return resultadosModificados Devuelve una ArrayList de los archivos que han
	 *         sido modificados.
	 * 
	 * @throws IOException Se utiliza para manejas los posibles errores que puedan
	 *                     aparecer.
	 */
	public ArrayList<String> buscaFicherosAReemplazar(File directorio, String cadenaABuscar,
			String cadenaReemplazar) throws IOException {
		ArrayList<String> resultadosModificados = new ArrayList<>();

		File[] archivos = directorio.listFiles();

		if (archivos == null || archivos.length == 0) {
			return resultadosModificados;
		}

		for (File archivo : archivos) {
			if (archivo.isDirectory()) {
				resultadosModificados.addAll(buscaFicherosAReemplazar(archivo, cadenaABuscar, cadenaReemplazar));
			} else if (archivo.isFile() && archivo.getName().endsWith(".txt")) {
				String resultado = reemplazarCadena(archivo, cadenaABuscar, cadenaReemplazar);
				if (resultado != null) {
					resultadosModificados.add(resultado);
				}

			}

		}
		return resultadosModificados;
	}

}

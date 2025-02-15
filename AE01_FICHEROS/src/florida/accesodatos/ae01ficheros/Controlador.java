package florida.accesodatos.ae01ficheros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Clase utilizada para manejar la interacción entre la clase vista y la clase
 * modelo.
 * 
 */
public class Controlador {
	private Modelo modelo;
	private Vista vista;

	public Controlador(Modelo modelo, Vista vista) {
		this.modelo = modelo;
		this.vista = vista;
		control();
	}

	/**
	 * Se encarga de controlar los "ActioListener" que serán los eventos de los
	 * botones que estan en la interfaz visual.
	 * 
	 * -El botón "Examinar" llama al método "controlExaminarDir()" que permite
	 * visualizar la estructura del directorio seleccionado. 
	 * -El botón "Buscar"llama al método "controlBuscarCoincidencia()" que se encarga de buscar
	 * cuantos ficheros contienen la palabra indicada. 
	 * -El botón "Reemplazar" llama al método "controlReemplazarCadena()" que se encarga de buscar palabras que
	 * coincidan en los ficheros y reemplazarlas por otra que le indiquemos
	 */
	public void control() {
		vista.getExaminar().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controlExaminarDir();
			}
		});

		vista.getBuscar().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controlBuscarCoincidencia();

			}
		});

		vista.getReemplazar().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controlReemplazarCadena();
			}
		});
	}

	/**
	 * Permite al usuario seleccionar un archivo o directorio utilizando un JFileChooser.
	 * 
	 * Con JFileChooser la ruta la indicas mediante una interfaz de explorador de archivos, una vez 
	 * seleccionada una ruta, esta aparecerá en un campo de texto.
	 */
	public void controlExaminarDir() {
		JFileChooser jfc = new JFileChooser();
		//Página donde está la información de JFileChooser: https://docs.oracle.com/javase/8/docs/api/javax/swing/JFileChooser.html
		
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int resultado = jfc.showOpenDialog(null);

		try {
			if (resultado == JFileChooser.APPROVE_OPTION) {
				File rutaDirectorio = jfc.getSelectedFile();
				vista.getTextPane_RutaDirectorio().setText(rutaDirectorio.getAbsolutePath());
				if (rutaDirectorio.isFile()) {
					try {
						String contenido = new String(Files.readAllBytes(rutaDirectorio.toPath()));
						vista.getTextArea().setText(contenido);
					} catch (IOException e) {
						e.printStackTrace();
						vista.getTextArea().setText("Error al leer el archivo.");
					}
				} else {
					String estructuraDirectorio = modelo.mostrarDirectorio(rutaDirectorio, "");
					vista.getTextArea().setText(estructuraDirectorio);
				}

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(jfc, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Una vez elegida la ruta de los directorios, busca la cantidad de coincidencias que habrán con la palabra 
	 * elegida para buscar, se pueden ignorar mayúsculas y acentos.
	 * 
	 * Muestra mensajes de error si no se rellena algún campo o si el directorio no fuese válido. 
	 */
	public void controlBuscarCoincidencia() {
		String rutaDirectorio = vista.getTextPane_RutaDirectorio().getText();
		String stringBuscar = vista.getTextFieldBuscar().getText();

		boolean ignorarAcentos = vista.getRdbtnIgnoraAcentos().isSelected();	//Si está seleccionado devuelve TRUE.
		boolean ignorarMayusculas = vista.getRdbtnIgnoraMayusculas().isSelected();

		if (stringBuscar.isEmpty()) {
			JOptionPane.showMessageDialog(null,
					"Para buscar alguna coincidencia en el directorio tienes que rellenar el campo de busqueda.");
			return;
		}

		File directorio = new File(rutaDirectorio);
		if (directorio.exists() && directorio.isDirectory()) {
			try {
				String resultadoBusqueda = modelo.buscarCoincidencias(directorio, "", stringBuscar, ignorarAcentos,
						ignorarMayusculas);
				vista.getTextArea().setText(resultadoBusqueda);
				JOptionPane.showMessageDialog(null, "Búsqueda de coincidencias completada.");
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al buscar en los archivos.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "El directorio seleccionado no es válido.");
		}

	}

	/**
	 * Verifica que los campos esten rellenados si no no buscará nada.
	 * 
	 * Busca las coincidencias en los fichero para crear un nuevo archivo con el nombre modificado y las coincidencias 
	 * sustituidas por la cadena escrita en "Reemplazar".
	 *
	 * @throws IOException Muestra un error si hay algún problema al leer o modificar el fichero.
	 */
	public void controlReemplazarCadena() {
		String rutaArchivoODirectorio = vista.getTextPane_RutaDirectorio().getText();
		String cadenaBuscar = vista.getTextFieldBuscar().getText();
		String cadenaReemplazar = vista.getTextFieldReemplazar().getText();

		if (rutaArchivoODirectorio.isEmpty() || cadenaBuscar.isEmpty() || cadenaReemplazar.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos antes de reemplazar.",
					"Campos vacíos", JOptionPane.WARNING_MESSAGE);
			return;
		}

		File archivoODirectorio = new File(rutaArchivoODirectorio);

		if (archivoODirectorio.isDirectory()) {
			try {
				ArrayList<String> resultados = modelo.buscaFicherosAReemplazar(archivoODirectorio, cadenaBuscar,
						cadenaReemplazar);

				for (String resultado : resultados) {
					JOptionPane.showMessageDialog(null, resultado, "Archivo Modificado",
							JOptionPane.INFORMATION_MESSAGE);
				}

				JOptionPane.showMessageDialog(null, "REEMPLAZO COMPLETADO.");

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error al intentar modificar los archivos en el directorio.",
						"Error de modificación", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} else if (archivoODirectorio.isFile()) {
			try {
				String resultado = modelo.reemplazarCadena(archivoODirectorio, cadenaBuscar, cadenaReemplazar);
				if (resultado != null) {
					JOptionPane.showMessageDialog(null, resultado, "Archivo Modificado",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "No se encontraron coincidencias en el archivo.",
							"Sin coincidencias", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error al intentar modificar el archivo.", "Error de modificación",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "El archivo o directorio seleccionado no es válido.",
					"Archivo/Directorio no válido", JOptionPane.WARNING_MESSAGE);
		}
	}
}
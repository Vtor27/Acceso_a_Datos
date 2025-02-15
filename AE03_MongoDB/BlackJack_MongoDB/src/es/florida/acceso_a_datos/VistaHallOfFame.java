package es.florida.acceso_a_datos;

import javax.swing.JFrame;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.Rectangle;

/**
 * Clase `VistaHallOfFame` que representa la ventana de las clasificaciones.
 * 
 * Esta clase crea una ventana que contiene un área de texto (`TextArea`) dentro
 * de un contenedor con barra de desplazamiento (`ScrollPane`). La ventana
 * muestra los resultados de las partidas guardadas en la base de datos.
 */

public class VistaHallOfFame {

	public JFrame frame;
	private TextArea textArea;

	/**
	 * Constructor de la clase `VistaHallOfFame`.
	 * 
	 * Inicializa los componentes de la vista y configura su diseño.
	 */
	public VistaHallOfFame() {
		initialize();
	}

	/**
	 * Inicializa los componentes de la ventana y configura sus propiedades.
	 * 
	 * Este método crea la ventana, establece sus dimensiones, añade un contenedor
	 * con barra de desplazamiento (`ScrollPane`) y un área de texto para mostrar la
	 * información.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 259, 388);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setBounds(new Rectangle(0, 0, 259, 388));
		scrollPane.setBounds(0, 0, 259, 388);
		frame.getContentPane().add(scrollPane);

		textArea = new TextArea();
		scrollPane.add(textArea);
		textArea.setBounds(0, 0, 200, 388);
		textArea.setEditable(false);
	}

	public TextArea getTextArea() {
		return textArea;
	}
}